package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mrz3t4.literatureclub.Utils.GenericContext;

public class GetLinksFromEpisode {


    private ArrayList<String> links;
    private String firstOptFinal;
    private String animeInfo;

    private String finalURI;

    public void getLinks(String url, String title, Context context, int mode) {


        if (mode == 2){
                Intent intent2 = new Intent("Information");
                intent2.putExtra("url", url);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        } else {

            new Thread(() -> {
                try {
                    Document document = Jsoup.connect(url).userAgent("Mozilla").get();
                    Elements doc = document.select("div[class=TPlayer mt-3 mb-3]");

                    animeInfo = formatInfo(url);

                    firstOptFinal = doc.select("iframe[class=embed-responsive-item]").attr("src");
                    links = pullLinks(doc.text(), firstOptFinal);

                    System.out.println(animeInfo);


                    for (int i = 0; i < links.size(); i++) {
                        System.out.println("URL: " + links.get(i));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Activity) context).runOnUiThread(() -> {
                    // OnPostExecute stuff here


                    switch (mode) {

                        case 1:
                            finalURI = links.get(0);
                            break;
                        case 0:
                            Intent intent = new Intent("Information");
                            intent.putExtra("url", animeInfo);
                            intent.putExtra("title", title);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }

                });
            }).start();
        }
    }

    public ArrayList<String> pullLinks(String text, String firstURL) {
        ArrayList<String> links = new ArrayList<>();

        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        if (firstURL!=null) {
            firstOptFinal = formatLink(firstURL);
            links.add(firstOptFinal);
        }

        while(m.find())
        {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }

            String urlFinal = formatLink(urlStr);
            links.add(urlFinal);
        }

        return links;
    }

    private String formatLink(String urlStr) {
        String rawUrl = urlStr.replace("%3A", ":").replace("%2F", "/");
        return rawUrl.substring(rawUrl.lastIndexOf("http"), rawUrl.lastIndexOf("&"));
    }

    private String formatInfo(String urlStr){
        return urlStr.replaceFirst("ver", "anime")
                .substring(0, urlStr.lastIndexOf("-"))
                .replace("episod", "sub-espanol");
    }

}
