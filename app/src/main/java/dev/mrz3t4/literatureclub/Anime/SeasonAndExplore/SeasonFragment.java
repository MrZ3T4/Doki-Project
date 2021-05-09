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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Anime.AnimeWebScrap;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;

import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_SEASON;
import static dev.mrz3t4.literatureclub.Utils.Constants.SEASON_URL;

public class SeasonFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    NotificationsBuilder builder = new NotificationsBuilder();

    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("Season"));

        AnimeWebScrap season = new AnimeWebScrap(getActivity());
        season.connect(SEASON_URL, MODE_SEASON);


        //GetAnime getAnime = new GetAnime(getActivity(), recyclerView, progressBar);

        //getAnime.getDirectoryFromWeb(0);


        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("SeasonReceiver", "Season Received!");

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


}