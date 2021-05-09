package dev.mrz3t4.literatureclub.Anime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Jsoup.GetLinks;
import dev.mrz3t4.literatureclub.RecyclerView.Theme;
import dev.mrz3t4.literatureclub.UI.GetSimilarity;
import dev.mrz3t4.literatureclub.Utils.Format;

import static dev.mrz3t4.literatureclub.Utils.Constants.OPENDINGS_BASE;
import static dev.mrz3t4.literatureclub.Utils.Constants.OPENDINGS_URL;

public class OpendingsWebScrap {

    private String final_url, final_title;

    private Context context;

    private ArrayList<Theme> opendingsArrayList = new ArrayList<>();

    public OpendingsWebScrap(Context context) {
        this.context = context;
    }

    public void get (String title, String id, String date){

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(OPENDINGS_URL).userAgent("Mozilla").maxBodySize(0).get();
                Elements elements = document.select("a");

                String date_from_anime = date.substring(0, 4);

                for (Element e: elements){

                    String title_from_database = e.text()
                            .replaceAll("\\(.*\\)", "")
                            .replaceFirst(".$", "");
                    String date_from_database = e.text()
                            .replaceAll("[()]", "")
                            .replaceAll("\\D+","")
                            .replace(" ", "");

                    GetSimilarity getSimilarity = new GetSimilarity();
                    boolean isSimilar = getSimilarity.isSimilar(title, title_from_database, date_from_anime, date_from_database);

                    if (isSimilar){
                        final_url = OPENDINGS_BASE.concat(e.attr("href"));
                        final_title = "[" + title_from_database;
                        System.out.println(final_url + " ----> " + final_url);
                        System.out.println("MyAnimeListID: " + id);
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            ((Activity)context).runOnUiThread(()-> { // Do Next...
                if (final_url != null){
                    opendings(final_url, final_title);
                   // getVideos(final_url, final_title);
                } else {
                    sendBroadcast();
                    // hacer algo
                 //   setRecyclerView(null, null);
                }
            });

        }).start();
    }

    private void opendings(String url, String title) {

        System.out.println("REDIT SEARCH --> " + title);

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(url).userAgent("Mozilla").maxBodySize(0).get();

                String doc = document.text();
                String text = doc.substring(doc.lastIndexOf(title));
                String block = text.substring(0, text.indexOf("#"));

                ArrayList<String> titlesArraylist = Format.extractNames(block);
                ArrayList<String> linksArraylist = Format.extractLinks(block);
                ArrayList<String> typesArraylist = Format.extractTypes(block);

                System.out.println(titlesArraylist.size() + " --- " + typesArraylist.size() + " --- " + linksArraylist.size());

                try {
                    for (int position = 0; position < titlesArraylist.size(); position++){

                        Theme theme = new Theme();

                        String titlex = titlesArraylist.get(position);
                        String typex = typesArraylist.get(position)
                                .replace("OP", "Opening ")
                                .replace("ED", "Ending ");
                        String urlx = linksArraylist.get(position);

                        theme.setTitle(titlex);
                        theme.setType(typex);
                        theme.setUrl(urlx);
                        System.out.println("title --> " +  titlex);
                        System.out.println("type --> " +  typex);
                        System.out.println("url --> " +  urlx);
                        opendingsArrayList.add(theme);


                    }


                } catch (Exception e){
                    e.printStackTrace();
                }



            } catch (IOException e) { e.printStackTrace(); }

            ((Activity)context).runOnUiThread(()-> { // Do Next...

                sendBroadcast();

            });

        }).start();



    }

    private void sendBroadcast() {
        Intent intent = new Intent("Opendings");
        intent.putParcelableArrayListExtra("arraylist", opendingsArrayList);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void getVideos(String url, String title) {

        new Thread(() -> { // Background

            try {

                Document document = Jsoup.connect(url).userAgent("Mozilla").maxBodySize(0).get();

                String doc = document.text();

                String text = doc.substring(doc.indexOf(title));
                String block = text.substring(0, text.indexOf("#"));


                GetLinks getLinks = new GetLinks();


                ArrayList<String> typesArrayList = getLinks.extractTypesFromTheme(block);
                ArrayList<String> arraylist = getLinks.extractLinks(block, null, 1);
                ArrayList<String> titlesArrayList = getLinks.extractNamesFromTheme(block);

                ArrayList<String> finalTitlesArrayList = new ArrayList<>();

                String lastText;

                for (int pos = 0; pos < titlesArrayList.size(); pos++){

                    if (pos == 0){
                        lastText = "Laweaqueseaxd";
                    } else {
                        lastText = titlesArrayList.get(pos-1);
                    }

                    String posText = titlesArrayList.get(pos);
                    if (!lastText.equalsIgnoreCase(posText)){
                        finalTitlesArrayList.add(posText);
                        Log.d("TITLE", "Current Text --> " + posText + " Last Text --> " + lastText);
                    }



                }

                addOpendings(arraylist, finalTitlesArrayList);

            } catch (IOException e) { e.printStackTrace(); }

            ((Activity)context).runOnUiThread(()-> { // Do Next...


                Intent intent = new Intent("Opendings");
                intent.putParcelableArrayListExtra("arraylist", opendingsArrayList);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            });

        }).start();


    }

    private void addOpendings(ArrayList<String> arraylist, ArrayList<String> titlesArrayList) {

        for (int i = 0; i < titlesArrayList.size(); i++){

            Theme theme = new Theme();


            String title = titlesArrayList.get(i);
            String url_theme = arraylist.get(i);

            String version = url_theme.substring(url_theme.length() -7).replace(".webm", "");

          //  theme.setTitle(title);
            System.out.println(title + " ===> "+ url_theme);

            /*String type_theme, num;

            if (url_theme.contains("OP")){
                theme.setUrl(url_theme);
                if (url_theme.contains("OP1.webm")){
                    type_theme = "Opening 1";
                } else {
                    num = version.replaceAll("[a-zA-Z]", "");
                    type_theme = "Opening " + num;
                }
            } else {
                theme.setUrl(url_theme);
                if (url_theme.contains("ED1.webm")){
                    type_theme = "Ending 1";
                } else {

                    num = version.replaceAll("[a-zA-Z]", "");
                    type_theme = "Ending " + num;
                    System.out.println(type_theme);

                }
            }

            String opending_type = titlesArrayList.get(i).substring(0,4);
            String opending_number = opending_type.replaceAll("[a-zA-Z\\s]*", "");
            System.out.println("OPENDING NUMBER " + opending_number);

            if (!arraylist.get(i).contains(opending_number)){
                titlesArrayList.remove(i);
                System.out.println("Nope --> " + titlesArrayList.get(i));
                //opendingsArrayList.clear();

            } else {
                theme.setUrl(arraylist.get(i));
                theme.setTitle(titlesArrayList.get(i).substring(3));
                theme.setType(type_theme);
                opendingsArrayList.add(theme);
                System.out.println("---OK---");
            }


*/
        }

    }

}
