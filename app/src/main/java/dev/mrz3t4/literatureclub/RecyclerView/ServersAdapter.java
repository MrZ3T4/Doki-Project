package dev.mrz3t4.literatureclub.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Anime.Server;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.xGetterVideo;


public class ServersAdapter extends RecyclerView.Adapter<ServersAdapter.MyViewHolder> {

    private ArrayList<Server> themeArrayList;

    private int lastposition = 0;

    private Context ctx;

    public ServersAdapter(ArrayList<Server> themeArrayList, Context ctx) {
        this.themeArrayList = themeArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recycleerview_options_servers, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = ctx.getTheme();
        theme.resolveAttribute(R.attr.color, typedValue, true);
        @ColorInt int color = typedValue.data;

        holder.title.setText(themeArrayList.get(position).getTitle());

        if (!themeArrayList.get(position).getTitle().contains("Nativo")){
            holder.download.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> {

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

        MaterialCardView cardView, download;
        TextView title, type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =  itemView.findViewById(R.id.server_cardview);
            download =  itemView.findViewById(R.id.server_cardview_download);
            title = itemView.findViewById(R.id.server_name);
            //type = itemView.findViewById(R.id.type_theme_textview);
        }
    }
}
