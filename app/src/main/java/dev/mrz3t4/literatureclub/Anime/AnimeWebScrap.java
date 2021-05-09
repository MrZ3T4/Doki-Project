package dev.mrz3t4.literatureclub.Anime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Anime.Broadcast.Broadcast;
import dev.mrz3t4.literatureclub.Anime.SeasonAndExplore.Anime;
import dev.mrz3t4.literatureclub.Utils.Constants;
import dev.mrz3t4.literatureclub.Utils.Format;
import dev.mrz3t4.literatureclub.Utils.JsonTools;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;

import static dev.mrz3t4.literatureclub.Utils.Constants.EXPLORE_URL;
import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_BROADCAST;
import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_EXPLORE;
import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_SEASON;
import static dev.mrz3t4.literatureclub.Utils.Constants.PAGE_URI;
import static dev.mrz3t4.literatureclub.Utils.Constants.SEASON_URL;
import static dev.mrz3t4.literatureclub.Utils.JsonTools.directory;

public class AnimeWebScrap {

    private final Context context;
    private final NotificationsBuilder builder = new NotificationsBuilder();

    public AnimeWebScrap(Context context) {
        this.context = context;
    }

    public void connect (String url, int mode){

        Log.d("DynamicURL", url);

        new Thread(()->{

            try {
                Document document = Jsoup.connect(url).userAgent("Mozilla").maxBodySize(0).get();

                switch (mode){
                    case MODE_BROADCAST:
                        broadcast(document);
                        break;
                    case MODE_SEASON:
                        anime(document, true);
                        break;
                    case MODE_EXPLORE:
                        anime(document, false);
                        break;

                }

            } catch (IOException e) {
                e.printStackTrace();
                builder.createToast("Error al intentar establecer conexión...", Toast.LENGTH_SHORT);
            }

        }).start();

    }

    private void broadcast (Document document){

            try {
                Elements doc = document.select("article[class=col-6 col-sm-4 col-md-3]");

                ArrayList<Broadcast> broadcastArrayList = new ArrayList<>();

                for (Element body:doc){

                    String title = body.select("h2").text();
                    String episode = Constants.episode + " " + body.select("span[class=episode]").text();
                    String img =(body.select("img[class=img-fluid]").attr("src"));
                    String url = body.select("a").attr("href");
                    String type = body.select("span[class=vista2]").text();

                    Log.v("Broadcast: ", title);

                    broadcastArrayList.add(new Broadcast(title, episode,img,url,type));
                }

                Intent intent = new Intent("Simulcast");
                intent.putParcelableArrayListExtra("arraylist", broadcastArrayList);
                sendBroadcast(intent);

            } catch (Exception e){
                Log.e("BROADCAST", "<---- Check Exception---->");
                e.printStackTrace();
            }


    }

    private int page_number = 1;
    private boolean page_empty;
    private JsonTools jsonTools = new JsonTools();

    private final ArrayList<Anime> animeArrayList = new ArrayList<>();

    @SuppressLint("InlinedApi")
    private void anime (Document document, boolean fromSeason){


        if (!fromSeason) {
            String notification_page = "Pagina " + page_number;

                builder.createNotification("Actualizando Directorio...",
                        notification_page,
                        NotificationManager.IMPORTANCE_LOW,
                    "DIRECTORY",
                    "RELOAD",
                    1);
        }

        new Thread(() -> { // Background

            try {

                Elements elements = document.select("article[class=col-6 col-sm-4 col-lg-2 mb-5]");

                if (!elements.text().isEmpty()) {

                    for (Element body : elements) {

                        Anime anime = new Anime();

                        String title = body.select( "h3").text();
                        String date = body.select("span[class=fecha]").text();
                        String img = body.select("img[class=img-fluid]").attr("src");
                        String url = body.select("a").attr("href");

                        String type = Format.getType(body.text());

                        anime.setTitle(title);
                        anime.setImg(img);
                        anime.setDate(date);
                        anime.setUrl(url);
                        anime.setType(type);
                        animeArrayList.add(anime);

                    }
                    page_empty = false;

                } else { page_empty = true; }

            } catch (Exception e) { e.printStackTrace(); }

            ((Activity)context).runOnUiThread(()-> { // Do Next...

                String dynamic_url;

                // Reload
                if (!page_empty) {
                    page_number++;
                    if (fromSeason){
                        dynamic_url = SEASON_URL + PAGE_URI + page_number;
                        connect(dynamic_url, MODE_SEASON);
                    }
                    else {
                        dynamic_url = EXPLORE_URL + PAGE_URI + page_number;
                        connect(dynamic_url, MODE_EXPLORE);
                    }

                } else { // Done.

                    Intent intent;

                    if (fromSeason){
                        intent = new Intent("Season");
                        intent.putParcelableArrayListExtra("arraylist",animeArrayList);
                    } else {
                        jsonTools.createJSONFileDirectory(animeArrayList);

                        builder.createToast("Directorio actualizado", Toast.LENGTH_SHORT);
                        builder.cancelNotification(1);

                        intent = new Intent("Explore");
                        intent.putParcelableArrayListExtra("arraylist",animeArrayList);
                    }

                    Format.sortAlphabetical(animeArrayList);
                    sendBroadcast(intent);

                }
            });

        }).start();

    }

    private void sendBroadcast (Intent intent){
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    public void reload(){
        if (jsonTools.jsonExists()){

            builder.createToast("Actualizando directorio...", Toast.LENGTH_SHORT);
            page_number = 1;

            connect(EXPLORE_URL, MODE_EXPLORE);
            directory.delete();
            animeArrayList.clear();
        } else {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            else v.vibrate(100);

            builder.createToast("No se puede actualizar el directorio mientras se está obteniendo.", Toast.LENGTH_SHORT);
        }
    }

}
