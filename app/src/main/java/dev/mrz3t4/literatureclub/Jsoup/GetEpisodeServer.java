package dev.mrz3t4.literatureclub.Jsoup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.Utils.xGetterVideo;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class GetEpisodeServer {

    private final Context context;
    private final String EPISODE_URL;

    private String firstLink, finalLink;
    private ArrayList<String> test = new ArrayList<>();


    public GetEpisodeServer(Context context, String EPISODE_URL) {
        this.context = context;
        this.EPISODE_URL = EPISODE_URL;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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

                test = getLinksFromEpisode.pullLinks(filter, firstLink);
                for (int i = 0; i <= test.size(); i++){
                    System.out.println(test.get(i));
                }




            } catch (Exception e) {
                e.printStackTrace();
            }
            ((Activity) context).runOnUiThread(() -> {
                // OnPostExecute stuff here


                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

                CharSequence[] servers = test.toArray(new CharSequence[test.size()]);

                builder.setTitle("ELIGE UN SERVIDOR")
                        .setBackground(context.getResources().getDrawable(R.drawable.corner_dialog, context.getTheme()))
                        .setSingleChoiceItems(servers, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              System.out.println(which);
                                finalLink = test.get(which);
                            }
                        })
                        .setPositiveButton("Nativo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //webView(test.get(which));
                                if (finalLink == null){
                                    finalLink = test.get(0);
                                }
                                System.out.println(finalLink);
                                xGetterVideo video = new xGetterVideo(finalLink);
                                video.getXGetterUrl();


                            }
                        })
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton("Web", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (finalLink == null){
                                    finalLink = test.get(0);
                                }
                                System.out.println(finalLink);
                                webView(finalLink);

                            }
                        })
                        .show();

            });
        }).start();

    }

    private void webView(String url){

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(context.getResources().getColor(R.color.black))
                .setShowTitle(true)
                .build();
        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);
        CustomTabsHelper.openCustomTab( context, customTabsIntent,
                Uri.parse(url),
                new WebViewFallback());
    }







}
