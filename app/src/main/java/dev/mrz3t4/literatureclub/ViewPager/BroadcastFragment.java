package dev.mrz3t4.literatureclub.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.RecyclerView.BroadcastAdapter;
import dev.mrz3t4.literatureclub.Utils.Constants;
import dev.mrz3t4.literatureclub.Utils.GenericContext;
import dev.mrz3t4.literatureclub.Utils.Sort;

import static dev.mrz3t4.literatureclub.Utils.Constants.BROADCAST_URL;

public class BroadcastFragment extends Fragment {

    private ArrayList<Broadcast> broadcastArrayList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.viewpager_broadcast, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_broadcast);
        progressBar = view.findViewById(R.id.broadcast_progressbar);

        getBroadcast();

        return view;
    }

    public void getBroadcast() {

        new Thread(() -> {
            try {
                Document document = Jsoup.connect(BROADCAST_URL).userAgent("Mozilla").get();

                Elements doc = document.select("article[class=col-6 col-sm-4 col-md-3]");

                broadcastArrayList = new ArrayList<>();
                for (Element body:doc){

                    String title = body.select("h2").text();
                    String episode = Constants.episode + " " + body.select("span[class=episode]").text();
                    String img =(body.select("img[class=img-fluid]").attr("src"));
                    String url = body.select("a").attr("href");
                    String type = body.select("span[class=vista2]").text();

                    System.out.println(title);

                    broadcastArrayList.add(new Broadcast(title, episode,img,url,type));
                }

            } catch (IOException e) { e.printStackTrace(); }

            getActivity().runOnUiThread(()->{
                // OnPostExecute stuff here

                progressBar.setVisibility(View.GONE);
                BroadcastAdapter broadcastAdapter = new BroadcastAdapter(broadcastArrayList, getActivity());

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(30);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setAdapter(broadcastAdapter);
                broadcastAdapter.notifyDataSetChanged();

            });
        }).start();

    }
}