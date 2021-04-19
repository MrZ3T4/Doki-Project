package dev.mrz3t4.literatureclub.Jsoup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

import dev.mrz3t4.literatureclub.RecyclerView.Anime;
import dev.mrz3t4.literatureclub.RecyclerView.AnimeAdapter;
import dev.mrz3t4.literatureclub.RecyclerView.Theme;
import dev.mrz3t4.literatureclub.RecyclerView.ThemeAdapter;
import dev.mrz3t4.literatureclub.UI.GetSimilarity;
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
    private final ArrayList<Theme> openingsEndingsArrayList = new ArrayList<>();

    private final Activity activity;
    private final RecyclerView recyclerView;
    private final ProgressBar progressBar;

    private String final_url, final_title;

    private boolean isSearchable;

    private final NotificationsBuilder notificationsBuilder = new NotificationsBuilder();
    private final JsonTools jsonTools = new JsonTools();
    private final Sort sort = new Sort();


    public GetAnime(Activity activity, RecyclerView recyclerView, ProgressBar progressBar) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;

    }


    @SuppressLint("InlinedApi")
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
                    setRecyclerView(animeArrayList, null);

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

        setRecyclerView(animeArrayList, null);

    }

    public void getThemes(String title, String id, String date){

        String date_from_anime = date.substring(0, 4);

        System.out.println("anime date: " + date_from_anime);

        String mal = "https://myanimelist.net/anime/" + id + "/";

        System.out.println("CSMSMSMSMSMSMSMSMSMSMSM: "  + title);
        String base = "https://old.reddit.com";
        String diccionary = "/r/AnimeThemes/wiki/anime_index/";

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(base.concat(diccionary)).userAgent("Mozilla").get();
                Elements elements = document.select("a");

                for (Element e: elements){

                    String name = e.text().replaceAll("\\(.*\\)", "");
                    String text = name.replaceFirst(".$", "");

                    String title_from_database = e.text().replaceAll("[()]", "");
                    String date_from_database = title_from_database.replaceAll("\\D+","").replace(" ", "");

                    GetSimilarity getSimilarity = new GetSimilarity();
                    boolean isSimilar = getSimilarity.isSimilar(title, text, date_from_anime, date_from_database);

                    if (isSimilar){
                        final_title = text;
                        final_url = base.concat(e.attr("href"));
                        System.out.println( final_title + " AQUI ESTAAAAA---- " + final_url);
                        System.out.println(id);
                    }






               /*     if (isSearchable) {
                        name = e.text().replaceAll("\\(.*\\)", "");
                        text = name.replaceFirst(".$", "");
                    } else {
                        text = e.text();
                    }

                    if (text.equalsIgnoreCase(title)){

                        final_url = base.concat(e.attr("href"));
                        System.out.println("TESTEOOOO: " + final_url);
                    }
*/
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(()-> { // Do Next...
                if (final_url != null){
                getVideos(final_url, final_title, id);

                } else {
                    // hacer algo
                    setRecyclerView(null, null);
                }
            });

        }).start();


    }

    private void getVideos(String url, String title, String id) {


        String test = "[" + title + "]";

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(url).userAgent("Mozilla").get();

                String doc = document.text();

                String str = doc.substring(doc.indexOf(test));
                String str2 = str.substring(0, str.indexOf("#"));


                GetLinks getLinks = new GetLinks();

                ArrayList<String> arraylist = getLinks.extractLinks(str2, null, 1);
                ArrayList<String> titlesArrayList = getLinks.extractNamesFromTheme(str2);


                for (int i = 0; i < titlesArrayList.size(); i++){

                    Theme theme = new Theme();

                    String url_theme = arraylist.get(i);
                    String title_theme = titlesArrayList.get(i);

                    String version = url_theme.substring(url_theme.length() -7).replace(".webm", "");
                    String if_version = url_theme.substring(url_theme.length() -6).replace(".webm", "");


                    theme.setTitle(title_theme);
                    theme.setUrl(url_theme);
                        //System.out.println(title_theme + " ===> "+ url_theme);

                    String type_theme;

                    if (url_theme.contains("OP")){
                        if (url_theme.contains("OP1.webm")){
                            type_theme = "Opening";
                        } else {
                            if (version.contains("v")){
                                type_theme = "Opening v" + if_version;

                            } else {
                                String num = version.replaceAll("[a-zA-Z]", "");
                                type_theme = "Opening " + num;
                            }

                            System.out.println(if_version);
                        }
                    } else {

                        if (url_theme.contains("ED1.webm")){
                            type_theme = "Ending";
                        } else {

                            if (version.contains("v")){
                                type_theme = "Ending v" + if_version;

                            }
                            else {
                                String num = version.replaceAll("[a-zA-Z]", "");
                                type_theme = "Ending " + num;
                            }

                        }
                    }
                    theme.setType(type_theme);

                    openingsEndingsArrayList.add(theme);

                }

            } catch (IOException e) { e.printStackTrace(); }

            activity.runOnUiThread(()-> { // Do Next...

                setRecyclerView(null, openingsEndingsArrayList);



            });

        }).start();


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

    private void setRecyclerView(ArrayList<Anime> animeArrayList, ArrayList<Theme> themeArrayList) {


        if (progressBar != null) { progressBar.setVisibility(View.GONE); }

        if (themeArrayList == null && animeArrayList != null) {
            recyclerView.setItemViewCacheSize(30);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setHasFixedSize(true);
            AnimeAdapter animeAdapter = new AnimeAdapter(animeArrayList, activity);
            recyclerView.setAdapter(animeAdapter);
            animeAdapter.notifyDataSetChanged();
        } else if (animeArrayList == null && themeArrayList != null){
            Intent episodes = new Intent("theme");
            episodes.putParcelableArrayListExtra("arraylist", themeArrayList);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(episodes);
        } else {
            Intent episodes = new Intent("theme");
            episodes.putParcelableArrayListExtra("arraylist", null);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(episodes);
        }


    }

}
