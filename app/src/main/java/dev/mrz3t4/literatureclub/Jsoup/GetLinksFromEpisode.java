package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mrz3t4.literatureclub.Utils.GenericContext;

public class GetLinksFromEpisode {


    private ArrayList<String> links;
    private String FIRST_SERVER;
    private String DETAILS_URL;

    private String finalURI;

    public void getLinks(String VARIABLE_URL, String TITLE, Context context, int mode) {

        if (mode == 2) // From Season or Explore
        {
                Intent intent2 = new Intent("Information");
                intent2.putExtra("url", VARIABLE_URL);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }
        else { // From Broadcast
            new Thread(() -> {

                try {
                    Document document = Jsoup.connect(VARIABLE_URL).userAgent("Mozilla").get();
                    Elements doc = document.select("div[class=TPlayer mt-3 mb-3]");

                    //DETAILS_URL = getDetailsLink(VARIABLE_URL);

                    FIRST_SERVER = doc.select("iframe[class=embed-responsive-item]").attr("src");
                    links = pullLinks(doc.text(), FIRST_SERVER);

                    Elements d = document.select("div[class=mt-1 mb-4]");

                    DETAILS_URL = d.select("a[class=btnWeb green Current]").attr("href");


                    for (int i = 0; i < links.size(); i++) {
                        System.out.println("URL: " + links.get(i));
                    }

                } catch (IOException e) { e.printStackTrace(); }

                ((Activity) context).runOnUiThread(() -> {// OnPostExecute stuff here

                    switch (mode) {

                        case 1: // Stream episode
                            finalURI = links.get(0);
                            break;
                        case 0: // Go to information
                            Intent intent = new Intent("Information");
                            intent.putExtra("url", DETAILS_URL);
                            intent.putExtra("title", TITLE);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            break;

                    }

                });
            }).start();
        }
    }


    // Get Links
    public ArrayList<String> pullLinks(String text, String firstURL) {
        ArrayList<String> linksArrayList = new ArrayList<>();

        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        if (firstURL!=null && !firstURL.contains("monoschinos2")) {
            FIRST_SERVER = formatLink(firstURL);
            linksArrayList.add(FIRST_SERVER);
        }

        while(m.find())
        {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            { urlStr = urlStr.substring(1, urlStr.length() - 1); }

            String urlFinal = formatLink(urlStr);

            if (!urlFinal.contains("monoschinos2")){
                linksArrayList.add(urlFinal);
            }

        }

        return linksArrayList;
    }

    private String formatLink(String urlStr) {
        String rawUrl = urlStr.replace("%3A", ":").replace("%2F", "/").replace(".html", "");
        return rawUrl.substring(rawUrl.lastIndexOf("http"), rawUrl.lastIndexOf("&"));
    }

    private String getDetailsLink(String urlStr){
        return urlStr.replaceFirst("ver", "anime")
                .substring(0, urlStr.lastIndexOf("-"))
                .replace("episod", "sub-espanol");
    }

}
