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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetDataFromEpisode;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> implements RecyclerViewFastScroller.OnPopupTextUpdate {

    private ArrayList<Anime> animeArrayList;
    private Context context;

    public AnimeAdapter(ArrayList<Anime> animeArrayList, Context context) {
        this.animeArrayList = animeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_anime, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.ViewHolder holder, int position) {
        holder.bindData(animeArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return animeArrayList.size();
    }

    @NotNull
    @Override
    public CharSequence onChange(int pos) {
        return animeArrayList.get(pos).getTitle().substring(0,2);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        MaterialTextView titleTextView, urlTextView, dateTextView, typeTextView;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.anime_cardview);
            coverImageView = itemView.findViewById(R.id.anime_cover);
            titleTextView = itemView.findViewById(R.id.anime_title);
            typeTextView = itemView.findViewById(R.id.anime_type);

        }

        void bindData(final Anime anime){
            titleTextView.setText(anime.getTitle());
            typeTextView.setText(anime.getType());

            Picasso.get()
                    .load(anime.getImg())
                    .tag(PicassoOnScrollListener.TAG)
                    .into(coverImageView);

            cardView.setOnClickListener(v -> {

                GetDataFromEpisode getDataFromEpisode = new GetDataFromEpisode();
                getDataFromEpisode.getLinks(anime.url, context, 2);

            });


        }
    }
}
