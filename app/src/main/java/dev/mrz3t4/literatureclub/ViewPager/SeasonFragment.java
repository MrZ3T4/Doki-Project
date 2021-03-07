package dev.mrz3t4.literatureclub.ViewPager;

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

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;
import dev.mrz3t4.literatureclub.Utils.Sort;

public class SeasonFragment extends Fragment {

    private ArrayList<Anime> animeArrayList = new ArrayList<>();;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    View view;

    String uri = "https://monoschinos2.com/emision";;
    boolean isValid;
    int count=2;
    private Sort sort = new Sort();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        run2();

        return view;
    }

    private void run2() {

        new Thread(() -> {
            try {
                    Document document = Jsoup.connect(uri).userAgent("Mozilla").get();
                    Elements doc = document.select("article[class=col-6 col-sm-4 col-lg-2 mb-5]");

                    if (!doc.text().isEmpty()) {

                        for (Element body : doc) {

                            Anime anime = new Anime();

                            String title = body.select("h3").text();
                            String date = body.select("span[class=fecha]").text();
                            String img = body.select("img[class=img-fluid]").attr("src");
                            String url = body.select("a").attr("href");

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
                            anime.setImg(img);
                            anime.setDate(date);
                            anime.setUrl(url);

                            animeArrayList.add(anime);
                        }
                        isValid = true;
                    } else { isValid = false; }

            } catch (IOException e) {
                e.printStackTrace();
            }
            requireActivity().runOnUiThread(()->{
                // OnPostExecute stuff here

                if (isValid){
                    uri = "https://monoschinos2.com/emision?page=" + count++;
                    run2();
                } else {
                    AnimeAdapter seasonAdapter = new AnimeAdapter(animeArrayList, getContext());

                    progressBar.setVisibility(View.GONE);
                    sort.getArrayListByTitle(animeArrayList);

                    recyclerView.setItemViewCacheSize(30);
                    recyclerView.setDrawingCacheEnabled(true);
                    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    recyclerView.setHasFixedSize(true);

                    recyclerView.setAdapter(seasonAdapter);
                }
            });
        }).start();

    }
}