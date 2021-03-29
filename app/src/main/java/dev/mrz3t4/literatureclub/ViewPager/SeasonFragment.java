package dev.mrz3t4.literatureclub.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetAnime;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;
import dev.mrz3t4.literatureclub.Utils.Sort;

public class SeasonFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        GetAnime getAnime = new GetAnime(getActivity(), recyclerView, progressBar);

        getAnime.getDirectory(0);

        return view;
    }


}