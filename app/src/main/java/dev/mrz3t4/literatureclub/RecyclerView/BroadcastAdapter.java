package dev.mrz3t4.literatureclub.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetLinksFromEpisode;
import dev.mrz3t4.literatureclub.R;

import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_EPISODE;

public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.ViewHolder> {

    private ArrayList<Broadcast> broadcastArrayList;
    private final Context context;

    public BroadcastAdapter(ArrayList<Broadcast> broadcastArrayList, Context context) {
        this.broadcastArrayList = broadcastArrayList;

        this.context = context;
    }

    @NonNull
    @Override
    public BroadcastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_broadcast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastAdapter.ViewHolder holder, int position) {
        holder.bindData(broadcastArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return broadcastArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        MaterialTextView titleTextView, episodeTextView, typeTextView;
        MaterialCardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.broadcast_cardview);
            coverImageView = itemView.findViewById(R.id.broadcast_cover);
            titleTextView = itemView.findViewById(R.id.broadcast_title);
            episodeTextView = itemView.findViewById(R.id.broadcast_episode);
            typeTextView = itemView.findViewById(R.id.broadcast_type);
        }

        void bindData(final Broadcast broadcast){
            titleTextView.setText(broadcast.getTitle());
            episodeTextView.setText(broadcast.getEpisode());
            typeTextView.setText(broadcast.getType());
            Glide.with(itemView).load(broadcast.getImg()).into(coverImageView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("Title (From Broadcast Adapter): "+broadcast.title);

                    LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = li.inflate(R.layout.bottomsheet_broadcast, null);

                    BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setContentView(view);
                    dialog.show();

                    GetLinksFromEpisode getVideoURL = new GetLinksFromEpisode();

                    MaterialCardView stream, information;

                    stream = view.findViewById(R.id.bottomsheet_cardview_play);

                    information = view.findViewById(R.id.bottomsheet_cardview_information);

                    information.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(broadcast.url);
                            getVideoURL.getLinks(broadcast.url, broadcast.title, context, 0);
                            dialog.dismiss();
                        }
                    });

                    stream.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        @Override
                        public void onClick(View v) {

                            getVideoURL.getLinks(broadcast.url, broadcast.title, context, MODE_EPISODE);
                            dialog.dismiss();

                        }
                    });

                    ShapeableImageView cover = view.findViewById(R.id.bottomsheet_cover);
                    MaterialTextView title = view.findViewById(R.id.bottomsheet_title);
                    MaterialTextView episode = view.findViewById(R.id.bottomsheet_episode);

                    Glide.with(context).load(broadcast.getImg()).into(cover);
                    title.setText(broadcast.getTitle());
                    episode.setText(broadcast.getEpisode());


                }
            });


        }
    }
}
