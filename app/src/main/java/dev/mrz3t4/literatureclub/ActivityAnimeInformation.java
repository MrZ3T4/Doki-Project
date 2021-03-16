package dev.mrz3t4.literatureclub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.List;

import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Episode;
import dev.mrz3t4.literatureclub.RecyclerView.GenderAdapter;
import dev.mrz3t4.literatureclub.UI.AnimeEpisodesFragment;
import dev.mrz3t4.literatureclub.UI.AnimeInformationFragment;
import dev.mrz3t4.literatureclub.UI.AnimeViewPager;
import dev.mrz3t4.literatureclub.Utils.PicassoBlurImage;

import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;
import static dev.mrz3t4.literatureclub.Utils.Constants.episodes;
import static dev.mrz3t4.literatureclub.Utils.Constants.information;
import static dev.mrz3t4.literatureclub.Utils.Constants.season;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anime_information_test);

        Bundle b = getIntent().getExtras();
        if (!b.isEmpty()){
            URI = b.getString("url");
        }

        progressBar = findViewById(R.id.information_progressBar);
        coordinatorLayout = findViewById(R.id.information_coordinatorLayout);

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
        setTabs();

        getData();

        toolbar_back.setOnClickListener(v -> finish());

    }

    private void setTabs() {
        AnimeViewPager animeViewPager = new AnimeViewPager();

        AnimeInformationFragment animeInformationFragment = new AnimeInformationFragment();
        AnimeEpisodesFragment animeEpisodesFragment = new AnimeEpisodesFragment();

        animeViewPager.setupViewPager(viewPager, animeInformationFragment, animeEpisodesFragment, getSupportFragmentManager() , getLifecycle());

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position){
                        case 0:
                            tab.setText(information);
                            break;
                        case 1:
                            tab.setText(episodes);
                            break;
                    }
                }).attach();




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

                    progressBar.setVisibility(View.GONE);
                    coordinatorLayout.animate().alpha(1f).setDuration(300).start();

                    date.setText(sdate);
                    category.setText(scategory);
                    if (scategory.equalsIgnoreCase("Anime")){
                        categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_tv));
                    } else if (scategory.equalsIgnoreCase("Pelicula")){
                        categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_movie));
                    } else {
                        categoryImgView.setImageDrawable(getDrawable(R.drawable.ic_other));
                    }

                    GenderAdapter genderAdapter = new GenderAdapter(sgenders, this);
                    recyclerViewGenders.setAdapter(genderAdapter);

                    Picasso.get().load(scover).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            blurCover.setImageBitmap(PicassoBlurImage.fastblur(bitmap, 1f, 5));
                        }
                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                    Picasso.get().load(scover).into(cover);
                    title.setText(stitle);
//                    description.setText(sdescription);
                    status.setText(sstatus);
                });
            }).start();

    }
}
