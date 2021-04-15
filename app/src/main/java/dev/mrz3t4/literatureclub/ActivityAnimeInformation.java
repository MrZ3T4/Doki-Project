package dev.mrz3t4.literatureclub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import dev.mrz3t4.literatureclub.Jsoup.GetAnime;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Episode;
import dev.mrz3t4.literatureclub.RecyclerView.GenderAdapter;
import dev.mrz3t4.literatureclub.Retrofit.InterfaceMAL;
import dev.mrz3t4.literatureclub.Retrofit.MalID;
import dev.mrz3t4.literatureclub.Retrofit.ResultID;
import dev.mrz3t4.literatureclub.UI.AnimeEpisodesFragment;
import dev.mrz3t4.literatureclub.UI.AnimeInformationFragment;
import dev.mrz3t4.literatureclub.UI.AnimeViewPager;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.Utils.PicassoBlurImage;
import dev.mrz3t4.literatureclub.ViewPager.InformationFragment;
import dev.mrz3t4.literatureclub.ViewPager.StaffFragment;
import dev.mrz3t4.literatureclub.ViewPager.ThemesFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;
import static dev.mrz3t4.literatureclub.Utils.Constants.episodes;
import static dev.mrz3t4.literatureclub.Utils.Constants.information;
import static dev.mrz3t4.literatureclub.Utils.Constants.season;
import static dev.mrz3t4.literatureclub.Utils.Constants.staff;
import static dev.mrz3t4.literatureclub.Utils.Constants.themes;

public class ActivityAnimeInformation extends AppCompatActivity {

    private String sstitle, stitle, sdescription, sdate, stype, ssdata, scategory, sgender, sstatus, scover, URI;
    private ArrayList<String> sgenders = new ArrayList<>();
    private ArrayList<Episode> episodesArrayList = new ArrayList<>();

    private TextView title, date, category, status;
    private ImageView cover, blurCover, toolbar_back, categoryImgView;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private RecyclerView recyclerViewGenders;

    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;

    private String titulo;
    private String poster;
    private String tipo;
    private String estado;
    private String estado_finalizado;
    private String sinopsis;
    private String emitido;
    private String BASE_URL;
    private String TITULO;
    private String ID;
    private String SCORE;
    private String RATED;
    private String EPISODES;
    private String TITULO_ORIGINAL;
    private String COVER;
    private String URL_MAL;

    private FloatingActionButton fab;
    private AppBarLayout appBarLayout;

    Boolean isReadyRetrofit = false;

    Intent intent = new Intent("data");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime_information_test);

        Bundle b = getIntent().getExtras();
        if (!b.isEmpty()){
            URI = b.getString("url");
            TITULO = b.getString("title");
        }

        System.out.println(TITULO + "AAAAAAAAAAAAAAAA");

        GetAnime getAnime = new GetAnime(ActivityAnimeInformation.this, null, null);
        getAnime.getThemes(TITULO);


        fab = findViewById(R.id.fab_fav);

        progressBar = findViewById(R.id.information_progressBar);
        coordinatorLayout = findViewById(R.id.information_coordinatorLayout);

        appBarLayout = findViewById(R.id.informacion_appbarlayout);

        title = findViewById(R.id.txtView_title);
        date = findViewById(R.id.txtView_date);
        status = findViewById(R.id.txtView_status);
        category = findViewById(R.id.txtView_category);

        recyclerViewGenders = findViewById(R.id.recyclerview_gender);

        categoryImgView = findViewById(R.id.imgView_category);
        toolbar_back = findViewById(R.id.toolbar_back);
        cover = findViewById(R.id.imgView_cover);
        blurCover = findViewById(R.id.imgBlur_cover);

        tabLayout = findViewById(R.id.information_tabLayout);
        viewPager = findViewById(R.id.information_viewPager);

        viewPager.requestDisallowInterceptTouchEvent(true);

        AppBar();
        setTabs();

        getData();

        setupFAB();

        toolbar_back.setOnClickListener(v -> {
            Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vi.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vi.vibrate(20);
            }
            finish();
        } );

    }

    public void nestedScrollViewPagerOn(){
      viewPager.setUserInputEnabled(true);
    }
    public void nestedScrollViewPagerOff(){
        viewPager.setUserInputEnabled(false);
    }

    private void setTabs() {
        AnimeViewPager animeViewPager = new AnimeViewPager();

        InformationFragment animeInformationFragment = new InformationFragment();
        AnimeEpisodesFragment animeEpisodesFragment = new AnimeEpisodesFragment();
        StaffFragment staffFragment = new StaffFragment();
        ThemesFragment themesFragment = new ThemesFragment();

        animeViewPager.setupViewPager(
                viewPager,
                animeInformationFragment,
                animeEpisodesFragment,
                staffFragment,
                themesFragment,
                getSupportFragmentManager() ,
                getLifecycle());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText(information);
                            break;
                        case 1:
                            tab.setText(episodes);
                            break;
                        case 2:
                            tab.setText(staff);
                            break;
                        case 3:
                            tab.setText(themes);
                            break;
                    }
                }).attach();




    }


    private void AppBar(){

        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                fab.show();
            } else {
                fab.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void getData() {

            new Thread(() -> {
                try {
                    Document document = Jsoup.connect(URI).userAgent("Mozilla").get();

                    sstitle = document.select("h1[class=Title]").text();
                    stitle = sstitle.replace("Sub Español", "");
                    sdescription = document.select("div[class=Description]").text();
                    sstatus = document.select("div[class=Type d-inline-block mr-2]").text();
                    scover = document.select("div[class=col-12 col-sm-3]").select("img").attr("src");

                    ssdata = document.select("div[class=after-title mb-2]").text();
                    scategory = ssdata.substring(ssdata.indexOf("| ")).substring(2);
                    sdate = ssdata.substring(ssdata.indexOf(" ")).substring(1,11).replaceAll("-", "/");

                    Elements episodes = document.select("div[class=SerieCaps]").select("a");

                    for (Element e: episodes){

                        episodesArrayList.add(new Episode(e.text().replace(stitle, "Episodio "), e.attr("href")));
                    }


                    Elements gender = document.select("div[class=generos]").select("a");

                    for (Element e: gender){

                        sgender = e.text();
                        sgenders.add(sgender);

                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(()->{
                    // OnPostExecute stuff here

                    Intent episodes = new Intent("Episodes");
                    episodes.putParcelableArrayListExtra("arraylist", episodesArrayList);
                    episodes.putExtra("description", sdescription);
                    episodes.putExtra("title", stitle);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(episodes);

                    date.setText(sdate);
                    category.setText(scategory);

                    if (scategory != null){
                        if (scategory.equalsIgnoreCase("Anime")){
                            categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_tv));
                        } else if (scategory.equalsIgnoreCase("Pelicula")){
                            categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_movie));
                        } else {
                            categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_other));
                        }

                    }

                    GenderAdapter genderAdapter = new GenderAdapter(sgenders, this);
                    recyclerViewGenders.setAdapter(genderAdapter);


                    COVER = scover;

                    Picasso.get().load(scover).into(blurCover);
                           /* .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            blurCover.setImageBitmap(PicassoBlurImage.fastblur(bitmap, 1f, 5));
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            System.out.println(e.getMessage());
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });*/
                    Picasso.get().load(scover).into(cover);
                    title.setText(stitle);
                    intent.putExtra("sinopsis",sdescription);
                    intent.putExtra("titulo", stitle);

                    if (TITULO == null){
                    getMAL(stitle);
                    } else {
                        getMAL(TITULO);
                    }


                    status.setText(sstatus);


                    progressBar.setVisibility(View.GONE);
                    coordinatorLayout.animate().alpha(1f).setDuration(300).start();

                });
            }).start();

    }

    private void setupFAB(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationsBuilder notificationsBuilder = new NotificationsBuilder();
                notificationsBuilder.createToast("Proximamente ;p", Toast.LENGTH_SHORT);
            }

        });
    }

    private void retrofitIsReady(){

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        isReadyRetrofit = true; }

    private void getMAL(String titulo){

        String JIKANURL = "https://api.jikan.moe/v3/";

            System.out.println("JIKAN: " + titulo);

        String ANIME_URL = JIKANURL.concat("search/anime?q=")
                .concat(titulo.replaceAll(" ", "%20")).concat("&limit=1");

        System.out.println("MAL_ID: "+ ANIME_URL);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JIKANURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceMAL interfaceMAL = retrofit.create(InterfaceMAL.class);

        interfaceMAL.getID(ANIME_URL).enqueue(new Callback<MalID>() {
            @Override
            public void onResponse(Call<MalID> call, Response<MalID> response) {


                if (response.isSuccessful()){

                    System.out.println("MAL_ID RESPONSE: " + response.code());

                    List<ResultID> malIDList = response.body().getResults();

                    for (ResultID result : malIDList){

                        int id = result.getMalId();
                        int episodes = result.getEpisodes();
                        double score = result.getScore();

                        SCORE = String.valueOf(score);
                        ID = String.valueOf(id);
                        RATED = result.getRated();
                        EPISODES = String.valueOf(episodes);
                        URL_MAL = result.getUrl();


                        intent.putExtra("fecha", SCORE);

                        retrofitIsReady();

                    }


                    intent.putExtra("ID", ID);
                    intent.putExtra("RATED", RATED);
                    intent.putExtra("EPISODES", EPISODES);
                    // intent.putExtra("ORIGINAL", TITULO_ORIGINAL);
                    intent.putExtra("MAL", URL_MAL);


                }

            }
            @Override
            public void onFailure(Call<MalID> call, Throwable t) { }
        });

    }



}
