package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Retrofit.Character;
import dev.mrz3t4.literatureclub.Retrofit.Seiyuu;
import dev.mrz3t4.literatureclub.Utils.WebViewBuilder;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

import static android.view.View.GONE;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.MyViewHolder> {

    private ArrayList<Seiyuu> seiyuus;
    private List<Character> character;
    private Context ctx;

    public StaffAdapter(ArrayList<Character> staff, ArrayList<Seiyuu> seiyuus, Context ctx) {
        this.character = staff;
        this.seiyuus = seiyuus;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_staff, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.mStaff.setOnClickListener(view -> {
            WebViewBuilder webViewBuilder = new WebViewBuilder();
            webViewBuilder.webView(character.get(position).getUrl(), ctx);
        });




   /*     if (character.get(position).getRole().equalsIgnoreCase("Main")) {
            holder.mRol.setText("Principal");
        } else if (character.get(position).getRole().equalsIgnoreCase("Supporting")){
            holder.mRol.setText("Secundario");
        }
*/
       holder.mNombre.setText(character.get(position).getName());
       holder.mNombre.setSelected(true);

        if (character.get(position).getImageUrl() != null) {

            if (character.get(position).getImageUrl().contains("questionmark")) {
                holder.mImagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(ctx).load("https://www.dranneede.nl/assets/images/trainers/geenfoto.png").into(holder.mImagen);
            } else {
                Glide.with(ctx).load(character.get(position).getImageUrl()).into(holder.mImagen);
            }

        }

        if (seiyuus.get(position).getSeiyuu() != null){
            holder.mNombreSeiyuu.setSelected(true);
            holder.mNombreSeiyuu.setText(seiyuus.get(position).getSeiyuu());
            holder.mImagenSeiyuu.setOnClickListener(view -> {

                WebViewBuilder webViewBuilder = new WebViewBuilder();
                webViewBuilder.webView(seiyuus.get(position).getSeiyuuUrl(), ctx);

            });
        } else {
            holder.mNombreSeiyuu.setVisibility(GONE);
        }

        if (seiyuus.get(position).getSeiyuuImagen() != null) {
            if (seiyuus.get(position).getSeiyuuImagen().contains("questionmark")){
                holder.mImagenSeiyuu.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(ctx).load("https://www.dranneede.nl/assets/images/trainers/geenfoto.png").into(holder.mImagenSeiyuu);
            } else {
                Glide.with(ctx).load(seiyuus.get(position).getSeiyuuImagen()).into(holder.mImagenSeiyuu);
            }

        } else {
            holder.mImagenSeiyuu.setVisibility(GONE);
        }


    }

    @Override
    public int getItemCount() {
        return character.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mNombre, mRol, mNombreSeiyuu, mNacionalidad;
        ImageView mImagen;
        ShapeableImageView mImagenSeiyuu;
        CardView mStaff;


        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            mStaff = itemView.findViewById(R.id.staff_cardview);
            mImagen = itemView.findViewById(R.id.staff_image);
            mNombre = itemView.findViewById(R.id.staff_nombre);
          //  mRol = itemView.findViewById(R.id.staff_desc);
            mImagenSeiyuu = itemView.findViewById(R.id.staff_s_image);
            mNombreSeiyuu = itemView.findViewById(R.id.staff_s_nombre);
          //  mNacionalidad = itemView.findViewById(R.id.staff_s_nacionalidad);

        }
    }
}
