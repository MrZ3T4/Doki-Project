package dev.mrz3t4.literatureclub.Jsoup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import dev.mrz3t4.literatureclub.R;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class GetEpisodeServer {

    private Context context;
    private String EPISODE_URL;

    private String firstLink;

    public GetEpisodeServer(Context context, String EPISODE_URL) {
        this.context = context;
        this.EPISODE_URL = EPISODE_URL;
    }

    public void getServers(){

        new Thread(() -> {
            try {

                Document document = Jsoup.connect(EPISODE_URL).userAgent("Mozilla").get();

                Elements element = document.select("div[class=row justify-content-center]");

                firstLink  = element.select("iframe[class=embed-responsive-item]").attr("src");

                String filter = element.text()
                        .replaceAll("&lt;", "<")
                        .replaceAll("&gt;", ">")
                        .replaceAll("&quot;", "\"");

                GetLinksFromEpisode getLinksFromEpisode = new GetLinksFromEpisode();

                ArrayList<String> test = getLinksFromEpisode.pullLinks(filter, firstLink);
                for (int i = 0; i <= test.size(); i++){
                    System.out.println(test.get(i));
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            ((Activity) context).runOnUiThread(() -> {
                // OnPostExecute stuff here

                Random randomGenerator = new Random();

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(context.getResources().getColor(R.color.black))
                        .setShowTitle(true)
                        .build();
                CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);
                CustomTabsHelper.openCustomTab( context, customTabsIntent,
                        Uri.parse(firstLink),
                        new WebViewFallback());
            });
        }).start();



    }







}
