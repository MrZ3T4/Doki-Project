package dev.mrz3t4.literatureclub.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dev.mrz3t4.literatureclub.R;

import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;
import static dev.mrz3t4.literatureclub.Utils.Constants.calendar;
import static dev.mrz3t4.literatureclub.Utils.Constants.season;

public class RecentsFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private AnimeViewPager animeViewPager = new AnimeViewPager();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recents, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);

        animeViewPager.setupViewPager(viewPager, getChildFragmentManager(), getLifecycle());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText(broadcast);
                            break;
                        case 1:
                            tab.setText(season);
                            break;
                    }
                }).attach();


        return view;
    }
}