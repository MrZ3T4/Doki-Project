package dev.mrz3t4.literatureclub.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import dev.mrz3t4.literatureclub.ActivityAnimeInformation;
import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.StaffAdapter;
import dev.mrz3t4.literatureclub.Retrofit.Character;
import dev.mrz3t4.literatureclub.Retrofit.InterfaceStaff;
import dev.mrz3t4.literatureclub.Retrofit.Personas;
import dev.mrz3t4.literatureclub.Retrofit.Seiyuu;
import dev.mrz3t4.literatureclub.Retrofit.VoiceActor;
import dev.mrz3t4.literatureclub.Utils.WebViewBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

import static android.view.View.GONE;

public class InformationFragment extends Fragment {

    @BindView(R.id.sinopsis)
    ExpandableTextView tvSinopsis;

    @BindView(R.id.titulo_completo) TextView tvTituloCompleto;
    @BindView(R.id.episodios_anime) TextView tvEpisodios;
    @BindView(R.id.episodios_title) TextView tvEpisodiosTitle;
    @BindView(R.id.clasificacion_title) TextView tvClasificacionTitle;
    @BindView(R.id.clasificacion_anime) TextView tvClasificacion;
    @BindView(R.id.emitido_anime) TextView tvEmitido;
    @BindView(R.id.id_anime) TextView tvID;
    @BindView(R.id.mal_anime) TextView tvMAL;
    @BindView(R.id.progressbar_details) ProgressBar progressBar;
  //  @BindView(R.id.detalles_expandablelayout) ExpandableLayout expandableLayout;
    @BindView(R.id.detalles_linearlayout) LinearLayout linearLayout;
    @BindView(R.id.detalles_cardview)
    CardView cardView;
    @BindView(R.id.show_hide_more_btn)
    MaterialButton btnShowHide;

    private String sinopsis;
    private String tituloCompleto;
    private String fecha;
    private String mal_id;
    private String BASE_URL = "https://api.jikan.moe/v3/";
    private String ANIME_URL;
    private String RATED;
    private String EPISODES;
    private String ORIGINAL;
    private String date;
    private String URL_MAL;

    @BindView(R.id.layout_details) LinearLayout linearLayout_details;

    private Boolean isExpanded = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detalles_anime_informacion_fm, container, false);

        ButterKnife.bind(this, view);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(setData,
                new IntentFilter("data"));

        onClicks();

        return view;
    }


    public BroadcastReceiver setData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            sinopsis = intent.getStringExtra("sinopsis");
            tituloCompleto = intent.getStringExtra("titulo");
            fecha = intent.getStringExtra("fecha");
            mal_id = intent.getStringExtra("ID");
            RATED = intent.getStringExtra("RATED");
            EPISODES = intent.getStringExtra("EPISODES");
            date = intent.getStringExtra("date");

//            ORIGINAL = intent.getStringExtra("ORIGINAL").replace(" ", "");
            URL_MAL = intent.getStringExtra("MAL");



            if (RATED == null) {
            tvClasificacion.setVisibility(GONE);
            tvClasificacionTitle.setVisibility(GONE);
            } else {
            tvClasificacion.setText(RATED);
            }

            if (EPISODES != null) {
                tvEpisodios.setText(EPISODES);
            } else {
                tvEpisodios.setVisibility(GONE);
                tvEpisodiosTitle.setVisibility(GONE);
            }


            if (sinopsis != null && sinopsis.length() > 260) {
                btnShowHide.setVisibility(View.VISIBLE);
            } else {
                btnShowHide.setVisibility(GONE);
            }

            tvSinopsis.setText(sinopsis);
            tvTituloCompleto.setText(tituloCompleto);
            tvEmitido.setText(fecha);
            tvID.setText(mal_id);

            String text = "<a href='" + URL_MAL + "'>" + tituloCompleto + "</a>";
            tvMAL.setText(Html.fromHtml(text));
            tvMAL.setOnClickListener(v -> {

                WebViewBuilder webViewBuilder = new WebViewBuilder();
                webViewBuilder.webView(URL_MAL, getActivity());
            });



            linearLayout_details.animate().alpha(1f).setDuration(300).start();
            progressBar.setVisibility(GONE);

        }


    };



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void onClicks() {

        btnShowHide.setOnClickListener(view -> {

            if (tvSinopsis.isExpanded()){
            tvSinopsis.collapse();
            btnShowHide.setText("Mostrar más");
            } else {
                tvSinopsis.expand();
                btnShowHide.setText("Mostrar menos");
            }


            });

    }




}
