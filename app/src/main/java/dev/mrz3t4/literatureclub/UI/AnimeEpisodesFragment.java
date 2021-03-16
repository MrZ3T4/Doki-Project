package dev.mrz3t4.literatureclub.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Episode;
import dev.mrz3t4.literatureclub.RecyclerView.EpisodeAdapter;

public class AnimeEpisodesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;

    private ArrayList<Episode> episodeArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_episodes, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_episodes);
        textView = view.findViewById(R.id.txtView_noEpisodes);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(episodesReceiver,
                new IntentFilter("Episodes"));


        return view;
    }

    BroadcastReceiver episodesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            episodeArrayList = intent.getParcelableArrayListExtra("arraylist");

            if (episodeArrayList.isEmpty()){
                recyclerView.setVisibility(View.GONE);
            } else {

                textView.setVisibility(View.GONE);
                EpisodeAdapter episodeAdapter = new EpisodeAdapter(episodeArrayList, getContext());
                recyclerView.setAdapter(episodeAdapter);
            }

        }
    };
}