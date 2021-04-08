package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.Utils.GenericContext;
import dev.mrz3t4.literatureclub.Utils.JsonUtils;
import dev.mrz3t4.literatureclub.Utils.NotificationBuilder;
import dev.mrz3t4.literatureclub.Utils.Sort;

import static dev.mrz3t4.literatureclub.Utils.Constants.BASE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.EMISION_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;

public class GetAnime {

    private int count = 1;

    private boolean isValid;

    private String MODE_URL;

    private ArrayList<Anime> animeArrayList = new ArrayList<>();

    private Activity activity;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private NotificationBuilder notificationBuilder = new NotificationBuilder();


    public GetAnime(Activity activity, RecyclerView recyclerView, ProgressBar progressBar) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;

    }

    public void getDirectory(int mode) {

        if (isDirectory(mode)){
            MODE_URL = BASE_URL;
        } else {
            MODE_URL = EMISION_URL;
        }
        if (count!=1) { MODE_URL = MODE_URL + PAGE_URI + count; }

        if (isDirectory(mode) ){
            notificationBuilder.createNotification("Actualizando Directorio...", "Pagina ", count);
        }


        new Thread(() -> {

            try {
                System.out.println(MODE_URL);

                Document document = Jsoup.connect(MODE_URL).userAgent("Mozilla").get();
                Elements doc = document.select("article[class=col-6 col-sm-4 col-lg-2 mb-5]");

                if (!doc.text().isEmpty()) {

                    for (Element body : doc) {

                        Anime anime = new Anime();

                        String title = body.select( "h3").text();
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

                } else {
                    isValid = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(()-> {
                if (isValid) {
                    count++;
                    if (isDirectory(mode)){
                        getDirectory(1); // For Directory
                    } else {
                        getDirectory(0); // For Season
                    }

                } else {

                    System.out.println("Done.");

                    if (isDirectory(mode)){
                        JsonUtils jsonUtils = new JsonUtils();
                        jsonUtils.createJsonDirectory(animeArrayList);

                        notificationBuilder.cancelNotification(1);
                        Toast.makeText(activity, "¡Directorio actualizado!", Toast.LENGTH_SHORT).show();
                    }

                    Sort sort = new Sort();
                    sort.getArrayListByTitle(animeArrayList);

                    setRecyclerView(animeArrayList, activity);


                }
            });

        }).start();

    }

    public void getDirectoryFromJson(){

        File directory = new File(GenericContext.getContext().getFilesDir(), "directory.json");

        try {

            FileInputStream fileInputStream = new FileInputStream(directory);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String animesJson = bufferedReader.readLine();

            JSONArray jsonDirectory = new JSONArray(animesJson);

            for (int pos = 0; pos <= jsonDirectory.length(); pos++) {

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

        setRecyclerView(animeArrayList, activity);
    }

    public void reloadDirectory(){

        File animeDirectory = new File(activity.getFilesDir(), "directory.json");

        if (animeDirectory.exists()){

            Toast.makeText(activity, "Actualizando...", Toast.LENGTH_SHORT).show();

            count = 1;

            getDirectory(1);

            animeDirectory.delete();
            animeArrayList.clear();
        } else {
            Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {

                //deprecated in API 26
                v.vibrate(100);
            }
            Toast.makeText(activity, "No se puede actualizar el directorio mientras se está creando.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDirectory(int mode){
        if (mode == 1){ return true; }
        else { return false; }
    }

    private void setRecyclerView(ArrayList<Anime> animeArrayList, Activity activity) {

        AnimeAdapter animeAdapter = new AnimeAdapter(animeArrayList, activity);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(animeAdapter);
        animeAdapter.notifyDataSetChanged();


    }

}
