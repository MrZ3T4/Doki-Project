package dev.mrz3t4.literatureclub.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import dev.mrz3t4.literatureclub.Jsoup.GetAnime;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.JsonTools;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private ProgressBar progressBar;


    private GetAnime getAnime;
    private JsonTools JSONTools = new JsonTools();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        textView = view.findViewById(R.id.anime_textView);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        getAnime = new GetAnime(getActivity(), recyclerView, progressBar);

        if (JSONTools.jsonExists()){
            getAnime.getDirectoryFromJson();
        } else {
            getAnime.getDirectoryFromWeb(1);
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