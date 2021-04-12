package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
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
import dev.mrz3t4.literatureclub.Utils.JsonTools;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.Utils.Sort;

import static dev.mrz3t4.literatureclub.Utils.Constants.BASE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.EMISION_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;

public class GetAnime {


    private int PAGE_NUMBER = 1;

    private boolean itsNotEmpty;

    private String VARIABLE_URL;

    private final ArrayList<Anime> animeArrayList = new ArrayList<>();

    private final Activity activity;
    private final Context context;
    private final RecyclerView recyclerView;
    private final ProgressBar progressBar;

    private final NotificationsBuilder notificationsBuilder = new NotificationsBuilder();
    private final JsonTools jsonTools = new JsonTools();
    private final Sort sort = new Sort();


    public GetAnime(Activity activity, RecyclerView recyclerView, ProgressBar progressBar) {
        this.activity = activity;
        this.context = activity;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;

    }

    public void getDirectoryFromWeb(int mode) {

        if (isDirectory(mode)) { VARIABLE_URL = BASE_URL; }
        else { VARIABLE_URL = EMISION_URL; }

        if (PAGE_NUMBER!=1) { VARIABLE_URL = VARIABLE_URL + PAGE_URI + PAGE_NUMBER; }

        if (isDirectory(mode) ){

            String notification_text = "Pagina " + PAGE_NUMBER;
            notificationsBuilder.createNotification(
                    "Actualizando Directorio...",
                    notification_text,
                    NotificationManager.IMPORTANCE_LOW,
                    "DIRECTORY",
                    "RELOAD",
                    1);
        }

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(VARIABLE_URL).userAgent("Mozilla").get();
                Elements elements = document.select("article[class=col-6 col-sm-4 col-lg-2 mb-5]");

                if (!elements.text().isEmpty()) {

                    for (Element body : elements) {

                        Anime anime = new Anime();

                        String title = body.select( "h3").text();
                        String date = body.select("span[class=fecha]").text();
                        String img = body.select("img[class=img-fluid]").attr("src");
                        String url = body.select("a").attr("href");

                        String type = getType(body.text());

                        anime.setTitle(title);
                        anime.setImg(img);
                        anime.setDate(date);
                        anime.setUrl(url);
                        anime.setType(type);
                        animeArrayList.add(anime);

                    }
                    itsNotEmpty = true;

                } else { itsNotEmpty = false; }

            } catch (IOException e) { e.printStackTrace(); }

            activity.runOnUiThread(()-> { // Do Next...

                if (itsNotEmpty) { // Reload

                    PAGE_NUMBER++;
                    if (isDirectory(mode)){ getDirectoryFromWeb(1); } // Explore
                    else { getDirectoryFromWeb(0); } // Season

                } else { // Done.

                    if (isDirectory(mode)){

                        jsonTools.createJSONFileDirectory(animeArrayList);
                        notificationsBuilder.createToast("Directorio actualizado", Toast.LENGTH_SHORT);
                        notificationsBuilder.cancelNotification(1);

                    }

                    sort.getArrayListByTitle(animeArrayList);
                    setRecyclerView(animeArrayList);

                }
            });

        }).start();

    }

    private String getType(String type){

        String RESULT;

        if (type.contains("Anime")){ RESULT = "Anime"; }
        else if (type.contains("Pelicula")){ RESULT = "Pelicula"; }
        else if (type.contains("Ova")) { RESULT = "OVA"; }
        else if (type.contains("Donghua")){ RESULT = "Donghua"; }
        else if (type.contains("Corto")) { RESULT = "Corto"; }
        else if (type.contains("Especial")){ RESULT = "Especial"; }
        else if (type.contains("Sin Censura")) { RESULT = "Sin Censura"; }
        else if (type.contains("Ona")) { RESULT = "ONA"; }
        else { RESULT = "Desconocido"; }

        return RESULT;

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

            }

        } catch (Exception e){ e.printStackTrace(); }

        setRecyclerView(animeArrayList);

    }

    public void reloadDirectory(){

        File directory = new File(activity.getFilesDir(), "directory.json");

        if (directory.exists()){

            notificationsBuilder.createToast("Actualizando...", Toast.LENGTH_SHORT);
            PAGE_NUMBER = 1;

            getDirectoryFromWeb(1);

            directory.delete();
            animeArrayList.clear();
        } else {
            Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else { v.vibrate(100); }

            notificationsBuilder.createToast("No se puede actualizar el directorio mientras se está obteniendo.", Toast.LENGTH_SHORT);
        }
    }

    private boolean isDirectory(int mode){ return mode == 1; }

    private void setRecyclerView(ArrayList<Anime> animeArrayList) {

        AnimeAdapter animeAdapter = new AnimeAdapter(animeArrayList, activity);

        if (progressBar != null) { progressBar.setVisibility(View.GONE); }

        recyclerView.setItemViewCacheSize(30);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(animeAdapter);
        animeAdapter.notifyDataSetChanged();


    }

}
