package dev.mrz3t4.literatureclub.Anime.SeasonAndExplore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Anime.AnimeWebScrap;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.JsonTools;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;

import static dev.mrz3t4.literatureclub.Utils.Constants.EXPLORE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_EXPLORE;

public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;
    private ProgressBar progressBar;


    private AnimeWebScrap explore;
    private JsonTools JSONTools = new JsonTools();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        textView = view.findViewById(R.id.anime_textView);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        explore = new AnimeWebScrap(getActivity());

        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(reload, new IntentFilter("Reload"));
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(receiver, new IntentFilter("Explore"));

        if (JSONTools.jsonExists()){
            System.out.println("exist");
            JSONTools.getDirectoryFromJson(getActivity());
        } else {
            explore.connect(EXPLORE_URL, MODE_EXPLORE);
        }

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("ExploreReceiver", "Explore Received!");

            ArrayList<Anime> animeArrayList = intent.getParcelableArrayListExtra("arraylist");
                try {
                    setRecyclerView(animeArrayList);
                    progressBar.setVisibility(View.GONE);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
        }
    };

    private void setRecyclerView(ArrayList<Anime> animeArrayList) {
        AnimeAdapter animeAdapter = new AnimeAdapter(animeArrayList, getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(animeAdapter);
    }


    private BroadcastReceiver reload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean reload = intent.getBooleanExtra("RELOAD", false);
            if (reload){ explore.reload(); }

        }
    };

}