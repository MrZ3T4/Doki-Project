package dev.mrz3t4.literatureclub.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Theme;
import dev.mrz3t4.literatureclub.RecyclerView.ThemeAdapter;

public class ThemesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_themes, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_themes);
        progressBar = view.findViewById(R.id.progressbar_themes);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                new IntentFilter("Opendings"));

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ArrayList<Theme> themeArrayList = intent.getParcelableArrayListExtra("arraylist");

            System.out.println("THEMS RECEIVED --> " + themeArrayList.size());

            if (themeArrayList != null) {
                setRecyclerView(themeArrayList);
            } else {progressBar.setVisibility(View.GONE);}
        }
    };

    private void setRecyclerView(ArrayList<Theme> themeArrayList) {
        progressBar.setVisibility(View.GONE);

        recyclerView.animate().alpha(1f).setDuration(300).start();
        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);


            ThemeAdapter themeAdapter = new ThemeAdapter(themeArrayList, getContext());
            recyclerView.setAdapter(themeAdapter);
    }

}