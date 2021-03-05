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
import java.util.Objects;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Season;
import dev.mrz3t4.literatureclub.RecyclerView.SeasonAdapter;
import dev.mrz3t4.literatureclub.Utils.Sort;
import dev.mrz3t4.literatureclub.Utils.SpaceItemDecoration;

public class SeasonFragment extends Fragment {

    private ArrayList<Season> seasonArrayList = new ArrayList<>();;
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
        view = inflater.inflate(R.layout.viewpager_season, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_season);
        progressBar = view.findViewById(R.id.season_progressbar);

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

                            String title = body.select("h3").text();
                            String date = body.select("span[class=fecha]").text();
                            String img = body.select("img[class=img-fluid]").attr("src");
                            String url = body.select("a").attr("href");

                            seasonArrayList.add(new Season(title, img, url, date));
                        }
                        isValid = true;
                    } else { isValid = false; }

            } catch (IOException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(getActivity()).runOnUiThread(()->{
                // OnPostExecute stuff here

                if (isValid){
                    uri = "https://monoschinos2.com/emision?page=" + count++;
                    run2();
                } else {
                    SeasonAdapter seasonAdapter = new SeasonAdapter(seasonArrayList, getContext());

                    progressBar.setVisibility(View.GONE);
                    sort.getArrayListByTitle(seasonArrayList);

                    recyclerView.setItemViewCacheSize(30);
                    recyclerView.setDrawingCacheEnabled(true);
                    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    recyclerView.setHasFixedSize(true);

                    int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                    recyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));


                    recyclerView.setAdapter(seasonAdapter);
                }
            });
        }).start();

    }
}