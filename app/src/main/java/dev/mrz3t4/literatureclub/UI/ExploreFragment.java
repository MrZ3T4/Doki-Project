package dev.mrz3t4.literatureclub.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetAnime;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.Utils.JsonUtils;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;
import dev.mrz3t4.literatureclub.Utils.Sort;

import static dev.mrz3t4.literatureclub.Utils.Constants.BASE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;
import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private ProgressBar progressBar;


    private GetAnime getAnime;
    private JsonUtils jsonUtils = new JsonUtils();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        textView = view.findViewById(R.id.anime_textView);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        getAnime = new GetAnime(getActivity(), recyclerView, progressBar);

        if (jsonUtils.jsonExists()){
            getAnime.getDirectoryFromJson();
        } else {
            getAnime.getDirectory(1);
        }

        IntentFilter filter = new IntentFilter("FORCE_RELOAD");
        getActivity().registerReceiver(receiver, filter);

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean reload = intent.getBooleanExtra("RELOAD", false);
            if (reload){ getAnime.reloadDirectory(); }

        }
    };

}