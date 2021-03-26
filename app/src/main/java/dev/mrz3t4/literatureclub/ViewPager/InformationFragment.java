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
import dev.mrz3t4.literatureclub.Utils.ExpandableCardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

import static android.view.View.GONE;

public class InformationFragment extends Fragment {


    @BindView(R.id.recyclerview_staff)
    RecyclerView recyclerViewStaff;

    @BindView(R.id.sinopsis)
    ExpandableTextView tvSinopsis;

    @BindView(R.id.titulo_completo) TextView tvTituloCompleto;
    @BindView(R.id.text_Actores) TextView tvActores;
    @BindView(R.id.episodios_anime) TextView tvEpisodios;
    @BindView(R.id.episodios_title) TextView tvEpisodiosTitle;
    @BindView(R.id.clasificacion_title) TextView tvClasificacionTitle;
    @BindView(R.id.clasificacion_anime) TextView tvClasificacion;
    @BindView(R.id.emitido_anime) TextView tvEmitido;
    @BindView(R.id.id_anime) TextView tvID;
    @BindView(R.id.mal_anime) TextView tvMAL;
    @BindView(R.id.progressbar_MAL) ProgressBar progressBar;
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
    private String ALTERNATIVO;
    private String URL_MAL;

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

        recyclerViewStaff.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewStaff.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            int lastX = 0;
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isScrollingRight = e.getX() < lastX;
                        if ((isScrollingRight && ((LinearLayoutManager) recyclerViewStaff.getLayoutManager()).findLastCompletelyVisibleItemPosition() == recyclerViewStaff.getAdapter().getItemCount() - 1) ||
                                (!isScrollingRight && ((LinearLayoutManager) recyclerViewStaff.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)) {
                            ((ActivityAnimeInformation) getActivity()).nestedScrollViewPagerOn();
                        } else {
                            ((ActivityAnimeInformation) getActivity()).nestedScrollViewPagerOff();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = 0;
                        ((ActivityAnimeInformation) getActivity()).nestedScrollViewPagerOn();
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

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
            ALTERNATIVO = intent.getStringExtra("SINONIMOS");
//            ORIGINAL = intent.getStringExtra("ORIGINAL").replace(" ", "");
            URL_MAL = intent.getStringExtra("MAL");



            if (RATED == null) {
            tvClasificacion.setVisibility(GONE);
            tvClasificacionTitle.setVisibility(GONE);
            } else {
            tvClasificacion.setText(RATED); }

            if (EPISODES.contains("0")) {
                tvEpisodios.setVisibility(GONE);
                tvEpisodiosTitle.setVisibility(GONE);
            } else {
                tvEpisodios.setText(EPISODES);}


            String text = "<a href='" + URL_MAL + "'>" + tituloCompleto + "</a>";
            tvMAL.setText(Html.fromHtml(text));
            tvMAL.setOnClickListener(view -> {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(getActivity().getResources().getColor(R.color.black))
                        .setShowTitle(true)
                        .build();
                CustomTabsHelper.addKeepAliveExtra(getActivity(), customTabsIntent.intent);
                CustomTabsHelper.openCustomTab(getActivity(), customTabsIntent,
                        Uri.parse(URL_MAL),
                        new WebViewFallback());});

            if (sinopsis.length() > 260){
                btnShowHide.setVisibility(View.VISIBLE);
            } else {
                btnShowHide.setVisibility(GONE);
            }

            tvSinopsis.setText(sinopsis);
            tvTituloCompleto.setText(tituloCompleto);
            tvEmitido.setText(fecha);
            tvID.setText(mal_id);
            getMALStaff(mal_id);

        }
    };


    private void getMALStaff(String mal_id) {

        ANIME_URL = BASE_URL.concat("anime/").concat(mal_id).concat("/characters_staff");

        System.out.println("MAL_STAFF: " + ANIME_URL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceStaff interfaceStaff = retrofit.create(InterfaceStaff.class);

        interfaceStaff.getPersonas(ANIME_URL).enqueue(new Callback<Personas>() {
            @Override
            public void onResponse(Call<Personas> call, Response<Personas> response) {

                if (response.isSuccessful()){

                    System.out.println("MAL_STAFF RESPONSE: " + response.code());

                    if (response.body().getCharacters().isEmpty()){
                        tvActores.setVisibility(GONE);
                        recyclerViewStaff.setVisibility(GONE);
                    } else {
                    tvActores.setVisibility(View.VISIBLE);
                    recyclerViewStaff.setVisibility(View.VISIBLE);
                    }

                    List<Character> charactersList = response.body().getCharacters();

                    ArrayList<Seiyuu> seiyuuArrayList = new ArrayList<>();

                        for (Character c : charactersList){

                            Seiyuu seiyuu = new Seiyuu();

                            List<VoiceActor> voiceActors = c.getVoiceActors();

                            String voice, imagen, url;

                            if (voiceActors.size() == 0){
                                voice = null;
                                imagen = null;

                                seiyuu.setSeiyuu(voice);
                                seiyuu.setSeiyuuImagen(imagen);
                                seiyuuArrayList.add(seiyuu);


                            } else {

                                voice = voiceActors.get(0).getName();
                                imagen = voiceActors.get(0).getImageUrl();
                                url = voiceActors.get(0).getUrl();

                                seiyuu.setSeiyuu(voice);
                                seiyuu.setSeiyuuImagen(imagen);
                                seiyuu.setSeiyuuUrl(url);
                                seiyuuArrayList.add(seiyuu);

                            }

                        }

                        progressBar.setVisibility(GONE);
                        StaffAdapter staffAdapter = new StaffAdapter((ArrayList<Character>) charactersList, seiyuuArrayList, getActivity());
                        recyclerViewStaff.setAdapter(staffAdapter);


                }

            }

            @Override
            public void onFailure(Call<Personas> call, Throwable t) {
            }
        });


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
