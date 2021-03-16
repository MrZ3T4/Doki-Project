package dev.mrz3t4.literatureclub.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.Broadcast;
import dev.mrz3t4.literatureclub.Utils.PicassoOnScrollListener;
import dev.mrz3t4.literatureclub.Utils.Sort;

import static dev.mrz3t4.literatureclub.Utils.Constants.BASE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;
import static dev.mrz3t4.literatureclub.Utils.Constants.broadcast;

public class ExploreFragment extends Fragment {

    private String FINAL_URL;
    private boolean isValid;
    private int count = 1;

    private RecyclerView recyclerView;
    private TextView textView;
    private ProgressBar progressBar;

    private boolean exists;

    private ArrayList<Anime> animeArrayList = new ArrayList<>();

    private String animesJson;
    String path = Environment.getExternalStorageDirectory() + "/Android/data/dev.mrz3t4.literatureclub/files";
    File animeDirectory = new File(path, "directory.json");

    AnimeAdapter exploreAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager_anime, container, false);



        textView = view.findViewById(R.id.anime_textView);

        recyclerView = view.findViewById(R.id.recyclerview_anime);
        recyclerView.addOnScrollListener(new PicassoOnScrollListener(getContext()));
        progressBar = view.findViewById(R.id.anime_progressbar);

        if (exists = jsonExists()){
            System.out.println("existe");
            getDirectoryFromJson();
        } else {
            getDirectory();
        }

        IntentFilter filter = new IntentFilter("FORCE_RELOAD");
        getActivity().registerReceiver(receiver, filter);

        return view;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean reload = intent.getBooleanExtra("RELOAD", false);

            if (reload){
                if (animeDirectory.exists()){

                    Toast.makeText(getContext(), "Actualizando directorio, espere un momento.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);

                    count = 1;

                    getDirectory();

                    animeDirectory.delete();
                    animeArrayList.clear();
                    exploreAdapter.notifyDataSetChanged();
                } else {
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(100);
                    }
                    Toast.makeText(getContext(), "No se puede actualizar el directorio mientras se está creando.", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };

    private void getDirectoryFromJson() {

        try {
            FileInputStream fileInputStream = new FileInputStream(animeDirectory);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            animesJson = bufferedReader.readLine();

            JSONArray jsonDirectory = new JSONArray(animesJson);

            for (int pos = 0; pos <= jsonDirectory.length(); pos++){

                Anime anime = new Anime();

                JSONObject jsonObject = jsonDirectory.getJSONObject(pos);

                String title = jsonObject.getString("Title");
                String img = jsonObject.getString("Cover");
                String date = jsonObject.getString("Date");
                String type = jsonObject.getString("Type");
                String url = jsonObject.getString("Link");


                anime.setTitle(title);
                anime.setImg(img);
                anime.setType(type);
                anime.setDate(date);
                anime.setUrl(url);

                animeArrayList.add(anime);
                System.out.println(animeArrayList.get(pos).getTitle());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        setRecyclerView(animeArrayList);


    }

    private void setRecyclerView(ArrayList<Anime> arrayList) {

        progressBar.setVisibility(View.GONE);

        exploreAdapter = new AnimeAdapter(arrayList, getContext());

        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(exploreAdapter);
    }

    private boolean jsonExists() {

        if (animeDirectory.exists()){
            return true;
        }
        return false;
    }

    private void getDirectory() {

        if (count==1){
            FINAL_URL = BASE_URL;
        } else {
            FINAL_URL = BASE_URL + PAGE_URI + count;
        }

        textView.setVisibility(View.VISIBLE);
        textView.setText("Creando directorio... Pagina " + count);

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

            requireActivity().runOnUiThread(()-> {
                if (isValid) {
                    count++;
                    getDirectory();
                } else {

                    System.out.println("Finish...");

                    textView.setVisibility(View.GONE);

                    Sort sort = new Sort();
                    sort.getArrayListByTitle(animeArrayList);

                    createJsonFile();
                    setRecyclerView(animeArrayList);

                }
            });

        }).start();



    }

    private void createJsonFile() {

        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < animeArrayList.size(); i++) {
            jsonArray.put(animeArrayList.get(i).getJsonObject());
        }
        String jsonDirectory = jsonArray.toString();

        try {
            FileWriter writer = new FileWriter(animeDirectory);
            writer.append(jsonDirectory);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

}