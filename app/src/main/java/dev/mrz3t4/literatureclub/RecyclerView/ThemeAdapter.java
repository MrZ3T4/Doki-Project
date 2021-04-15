package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetLinks;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.xGetterVideo;

import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_EPISODE;


public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.MyViewHolder> {

    private ArrayList<Theme> themeArrayList;

    private Context ctx;

    public ThemeAdapter(ArrayList<Theme> themeArrayList, Context ctx) {
        this.themeArrayList = themeArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_theme, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.title.setText(themeArrayList.get(position).getTitle());
        holder.type.setText(themeArrayList.get(position).getType());

        holder.cardView.setOnClickListener(v -> {

            xGetterVideo video = new xGetterVideo(themeArrayList.get(position).getUrl());
            video.startPlay();

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
        return themeArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        TextView title, type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =  itemView.findViewById(R.id.theme_cardview);
            title = itemView.findViewById(R.id.title_theme_textview);
            type = itemView.findViewById(R.id.type_theme_textview);
        }
    }
}
