package dev.mrz3t4.literatureclub.ViewPager;

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

import dev.mrz3t4.literatureclub.Jsoup.GetBroadcast;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.RecyclerView.BroadcastAdapter;

import static dev.mrz3t4.literatureclub.Utils.Constants.BROADCAST_URL;

public class BroadcastFragment extends Fragment {

    private ArrayList<Broadcast> broadcastArrayList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_broadcast, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_broadcast);
        progressBar = view.findViewById(R.id.broadcast_progressbar);


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,
                new IntentFilter("Simulcast"));

        GetBroadcast getBroadcast = new GetBroadcast(getActivity());
        getBroadcast.connect(BROADCAST_URL, 0);

        return view;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("BroadcastReceiver", "Simulcast Received!");
            broadcastArrayList = intent.getParcelableArrayListExtra("arraylist");
            if (!broadcastArrayList.isEmpty()){
                progressBar.setVisibility(View.GONE);
                setRecyclerView(broadcastArrayList);
            } else {
                Log.e("BroadcastReceiver" ,"Arraylist is empty, size = " + broadcastArrayList.size());
            }

        }
    };

    public void setRecyclerView(ArrayList<Broadcast> broadcastArrayList){

        BroadcastAdapter broadcastAdapter = new BroadcastAdapter(broadcastArrayList, getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(broadcastAdapter);
        broadcastAdapter.notifyDataSetChanged();

    }


}