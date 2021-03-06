package dev.mrz3t4.literatureclub.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.Utils.SpaceItemDecoration;

import static dev.mrz3t4.literatureclub.Utils.Constants.BASE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;

public class ExploreFragment extends Fragment {

    private String FINAL_URL;
    private boolean isValid;
    private int count = 1;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<Anime> animeArrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);


        recyclerView = view.findViewById(R.id.recyclerview_anime);
        progressBar = view.findViewById(R.id.anime_progressbar);
        getDirectory();


        return view;
    }

    private void getDirectory() {

        if (count==1){
            FINAL_URL = BASE_URL;
        } else {
            FINAL_URL = BASE_URL + PAGE_URI + count;
        }

        new Thread(() -> {

            try {


                System.out.println(FINAL_URL);

                Document document = Jsoup.connect(FINAL_URL).userAgent("Mozilla").get();
                Elements doc = document.select("article[class=col-6 col-sm-4 col-lg-2 mb-5]");
                System.out.println("test: " + doc.text());

                if (!doc.text().isEmpty()) {

                    for (Element body : doc) {

                        Anime anime = new Anime();

                        String title = body.select( "h3").text();
                        String date2 = body.select("span[class=fecha]").text();
                        String img2 = body.select("img[class=img-fluid]").attr("src");
                        String url2 = body.select("a").attr("href");

                        String type="";

                        if (body.text().contains("Anime")){
                            type = "Anime";
                            anime.setType(type);
                        } else if (body.text().contains("Pelicula")){
                            type = "Pelicula";
                            anime.setType(type);
                        } else if (body.text().contains("Ova")) {
                            type = "OVA";
                            anime.setType(type);
                        } else if (body.text().contains("Donghua")){
                            type = "Donghua";
                            anime.setType(type);
                        } else if (body.text().contains("Corto")) {
                            type = "Corto";
                            anime.setType(type);
                        } else if (body.text().contains("Especial")){
                            type = "Especial";
                            anime.setType(type);
                        } else if (body.text().contains("Sin Censura")) {
                            type = "Sin Censura";
                            anime.setType(type);
                        } else if (body.text().contains("Ona")) {
                            type = "ONA";
                            anime.setType(type);
                        }


                        anime.setTitle(title);
                        anime.setImg(img2);
                        anime.setDate(date2);
                        anime.setUrl(url2);
                        animeArrayList.add(anime);

                        System.out.println(title);
                        System.out.println(img2);
                        System.out.println(date2);
                        System.out.println("XD: "+ type);
                        System.out.println(url2);

                    }

                    isValid = true;
                } else {
                    isValid = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            Objects.requireNonNull(getActivity()).runOnUiThread(()-> {
                if (isValid) {
                    count++;
                    getDirectory();
                } else {
                    System.out.println("Finish...");

                    progressBar.setVisibility(View.GONE);

                    AnimeAdapter exploreAdapter = new AnimeAdapter(animeArrayList, getContext());

                    recyclerView.setItemViewCacheSize(30);
                    recyclerView.setDrawingCacheEnabled(true);
                    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    recyclerView.setHasFixedSize(true);

                    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                    recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));


                    recyclerView.setAdapter(exploreAdapter);

                }
            });

        }).start();



    }

}