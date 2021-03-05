package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.R;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {

    private ArrayList<Season> seasonArrayList;
    private LayoutInflater inflater;
    private Context context;

    public SeasonAdapter(ArrayList<Season> seasonArrayList, Context context) {
        this.seasonArrayList = seasonArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SeasonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_season, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonAdapter.ViewHolder holder, int position) {
        holder.bindData(seasonArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return seasonArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        MaterialTextView titleTextView, urlTextView, dateTextView;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.season_cardview);
            coverImageView = itemView.findViewById(R.id.season_cover);
            titleTextView = itemView.findViewById(R.id.season_title);

        }

        void bindData(final Season season){
            titleTextView.setText(season.getTitle());
            Glide.with(itemView).load(season.getImg()).into(coverImageView);

            cardView.setOnClickListener(v -> Toast.makeText(context, season.getUrl(), Toast.LENGTH_SHORT).show());


        }
    }
}
