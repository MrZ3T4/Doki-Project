package dev.mrz3t4.literatureclub;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.potyvideo.library.AndExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reproductor extends AppCompatActivity {

    String url;

    @BindView(R.id.andExoPlayerView)
    AndExoPlayerView andExoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();

        if (b != null){
            url = b.getString("VIDEO");
            andExoPlayerView.setSource(url);
            System.out.println(url);
        }

        andExoPlayerView.setShowFullScreen(false);
        andExoPlayerView.setExoPlayerCallBack(() -> {
            Toast.makeText(Reproductor.this, "Video no disponible", Toast.LENGTH_LONG).show();
                finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    andExoPlayerView.stopPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        andExoPlayerView.pausePlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    andExoPlayerView.stopPlayer();
    finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
