package dev.mrz3t4.literatureclub.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import dev.mrz3t4.literatureclub.R;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {

    private ArrayList<String> sgenders;
    private Context ctx;

    public GenderAdapter(ArrayList<String> sgenders, Context ctx) {
        this.sgenders = sgenders;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_chip_genders, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.gender.setText(sgenders.get(position));
    }

    @Override
    public int getItemCount() {
         return sgenders.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private Chip gender;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gender = itemView.findViewById(R.id.chip_gender);
        }
    }
}
