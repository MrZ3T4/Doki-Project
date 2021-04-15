package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dev.mrz3t4.literatureclub.Jsoup.GetLinks;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> implements RecyclerViewFastScroller.OnPopupTextUpdate, Filterable {

    private ArrayList<Anime> animeArrayList;
    private ArrayList<Anime> fullArrayList;
    private Context context;

    public AnimeAdapter(ArrayList<Anime> animeArrayList, Context context) {
        this.animeArrayList = animeArrayList;
        this.context = context;
        fullArrayList = new ArrayList<>(animeArrayList);
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

    @Override
    public Filter getFilter() {
        return directorioFiltrado;
    }

    private Filter directorioFiltrado = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Anime> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(fullArrayList);
            } else {

                String filtro = charSequence.toString().toLowerCase().trim();
                for (Anime anime : fullArrayList) {

                    if (anime.getTitle().toLowerCase().contains(filtro)) {
                        filterList.add(anime);
                    }

                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            animeArrayList.clear();
            animeArrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

    };

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

                GetLinks getDataFromEpisode = new GetLinks();
                getDataFromEpisode.getLinks(anime.getUrl(), anime.getTitle(), context, 2);

            });


        }
    }
}
