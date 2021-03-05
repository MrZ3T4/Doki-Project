package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mrz3t4.literatureclub.Utils.GenericContext;

public class GetDataFromEpisode {


    private ArrayList<String> links;
    private String firstOptFinal;
    private String animeInfo;

    public void getLinks(String url, Context context, int mode) {

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
            ((Activity)context).runOnUiThread(()->{
                // OnPostExecute stuff here

                if (mode==1) {
                    Toast.makeText(GenericContext.getContext(), links.get(0), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GenericContext.getContext(), animeInfo, Toast.LENGTH_SHORT).show();
                }

            });
        }).start();
    }

    public ArrayList<String> pullLinks(String text, String firstURL) {
        ArrayList<String> links = new ArrayList<>();

        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        firstOptFinal = formatLink(firstURL);
        links.add(firstOptFinal);

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
        return urlStr.replace("ver", "anime")
                .substring(0, urlStr.lastIndexOf("-"))
                .replace("episod", "sub-espanol");
    }

}
