package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetEpisodeServer;
import dev.mrz3t4.literatureclub.R;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;


public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {

    private ArrayList<Episode> episodeArrayList;

    private Context ctx;

    public EpisodeAdapter(ArrayList<Episode> episodeArrayList, Context ctx) {
        this.episodeArrayList = episodeArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_episodes, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.title.setText(episodeArrayList.get(position).getTitle());

        holder.cardView.setOnClickListener(v -> {
            System.out.println("UUUUUUUU: "+episodeArrayList.get(position).getLink());

            GetEpisodeServer getEpisodeServer = new GetEpisodeServer(ctx, episodeArrayList.get(position).getLink());
            getEpisodeServer.getServers();



            /*Toast.makeText(ctx, "Preparando streaming...", Toast.LENGTH_SHORT).show();
            Intent streaming = new Intent("streaming");
            streaming.putExtra("URL", URL);
            streaming.putExtra("TITULO", TITULO);
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(streaming);
*/
        });
    }

    @Override
    public int getItemCount() {
        return episodeArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        TextView title;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =  itemView.findViewById(R.id.cardView_episodes);
            title = itemView.findViewById(R.id.txtView_episode);
        }
    }
}
