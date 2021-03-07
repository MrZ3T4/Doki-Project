package dev.mrz3t4.literatureclub.Utils;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class PicassoOnScrollListener extends RecyclerView.OnScrollListener {
    public static final Object TAG = new Object();
    private static final int SETTLING_DELAY = 500;

    private static Picasso sPicasso = null;
    private Runnable mSettlingResumeRunnable = null;

    public PicassoOnScrollListener(Context context) {
        if(this.sPicasso == null) {
            this.sPicasso = Picasso.get();
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
        if(scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerView.removeCallbacks(mSettlingResumeRunnable);
            sPicasso.resumeTag(TAG);

        } else if(scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
            mSettlingResumeRunnable = new Runnable() {
                @Override
                public void run() {
                    sPicasso.resumeTag(TAG);
                }
            };

            recyclerView.postDelayed(mSettlingResumeRunnable, SETTLING_DELAY);

        } else {
            sPicasso.pauseTag(TAG);
        }
    }}

