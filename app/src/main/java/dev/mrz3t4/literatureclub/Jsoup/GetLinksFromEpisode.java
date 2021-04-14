package dev.mrz3t4.literatureclub.Jsoup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mrz3t4.literatureclub.R;
import dev.mrz3t4.literatureclub.Utils.GenericContext;
import dev.mrz3t4.literatureclub.Utils.NotificationsBuilder;
import dev.mrz3t4.literatureclub.Utils.WebViewBuilder;
import dev.mrz3t4.literatureclub.Utils.xGetterVideo;

import static dev.mrz3t4.literatureclub.Utils.Constants.MODE_EPISODE;

public class GetLinksFromEpisode {

    private String first_server, details_url, final_link;
    private ArrayList<String> other_servers;

    public void getLinks(String variable_url, String title, Context context, int mode) {

        if (mode == 2) // From Season or Explore
        {
                Intent intent2 = new Intent("Information");
                intent2.putExtra("url", variable_url);
                intent2.putExtra("title", title);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }

        // <-- From Broadcast -->
        else {

            new Thread(() -> {

                try {
                    Document document = Jsoup.connect(variable_url).userAgent("Mozilla").get();
                    Elements serverClass = document.select("div[class=row justify-content-center]");

                    // Get first stream server & others

                    first_server = serverClass.select("iframe[class=embed-responsive-item]").attr("src");
                    other_servers = extractLinks(serverClass.text(), first_server, 0);


                    for (int pos = 0; pos < other_servers.size(); pos++) {
                        System.out.println("Stream Server " + pos + " " + other_servers.get(pos));
                    }

                    // Get anime details url

                    Elements detailsClass = document.select("div[class=mt-1 mb-4]");
                    details_url = detailsClass.select("a[class=btnWeb green Current]").attr("href");

                } catch (IOException e) { e.printStackTrace(); }

                // <-- On Post Execute -->

                ((Activity) context).runOnUiThread(() -> {

                    switch (mode) {

                        case 1: // MODE_EPISODE
                            createDialog(other_servers, context);
                            break;
                        case 0: // MODE_INFORMATION
                            Intent intent = new Intent("Information");
                            intent.putExtra("url", details_url);
                            intent.putExtra("title", title);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                            break;

                    }

                });
            }).start();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createDialog(ArrayList<String> servers_array, Context context) {

        ArrayList<String> name_servers = new ArrayList<>();

        String server;

        System.out.println(servers_array.size());

        for (int pos = 0; pos < servers_array.size(); pos++){
            System.out.println("pos: " + pos + " server: " + servers_array.get(pos));
            if (servers_array.get(pos).contains("fembed")){
                server = "Fembed (Nativo)";
            } else if (servers_array.get(pos).contains("ok.ru")){
                server = "Okru (Nativo)";
            } else if (servers_array.get(pos).contains("sendvid")){
                server = "Sendvid (Nativo)";
            } else if (servers_array.get(pos).contains("mp4upload")){
                server = "Mp4Upload";
            } else if (servers_array.get(pos).contains("uqload")){
                server = "Mp4Upload";
            } else if (servers_array.get(pos).contains("clip")){
                server = "ClipWatching";
            } else if (servers_array.get(pos).contains("streamtape")){
                server = "Streamtape";
            } else if (servers_array.get(pos).contains("videobin")){
                server = "Videobin";
            } else if (servers_array.get(pos).contains("mediafire")){
                server = "Mediafire";
            } else {
                server = "Otro";
            }

            name_servers.add(server);
        }

        CharSequence[] servers = name_servers.toArray(new CharSequence[name_servers.size()]);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MyTitle_ThemeOverlay_MaterialComponents_MaterialAlertDialog);

            builder.setTitle("Elige un servidor")
                    .setBackground(context.getResources().getDrawable(R.drawable.corner_dialog, context.getTheme()))
                    .setSingleChoiceItems(servers, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final_link = servers_array.get(which);
                        }
                    })
                    .setPositiveButton("Nativo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (final_link == null){
                                final_link = servers_array.get(0);
                            }
                            System.out.println("Stream server selected is: " + final_link);

                            NotificationsBuilder notificationsBuilder = new NotificationsBuilder();
                            notificationsBuilder.createToast("Preparando streaming...", Toast.LENGTH_SHORT);

                            xGetterVideo video = new xGetterVideo(final_link);
                            video.getXGetterUrl();


                        }
                    })
                    .setNegativeButton("Web", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (final_link == null){
                                final_link = servers_array.get(0);
                            }
                            System.out.println("Stream server selected is: " + final_link);
                            WebViewBuilder webViewBuilder = new WebViewBuilder();
                            webViewBuilder.webView(final_link, context);

                        }
                    })
                    .setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .show();

    }

    public ArrayList<String> extractLinks(String text, String firstServer, int mode) {
        ArrayList<String> linksArrayList = new ArrayList<>();

        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        if (firstServer!=null && !firstServer.contains("monoschinos2")) {
            first_server = formatLink(firstServer);
            linksArrayList.add(first_server);
        }

        while(m.find())
        {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            { urlStr = urlStr.substring(1, urlStr.length() - 1); }

            if (mode == 0) {
                String urlFinal = formatLink(urlStr);

                if (!urlFinal.contains("monoschinos2")){
                    linksArrayList.add(urlFinal);
                }
            } else {
                if (!urlStr.contains("myanimelist")){
                    String url_semifinal = urlStr.substring(1, urlStr.indexOf(")"));
                    linksArrayList.add(url_semifinal);
                }
            }

        }

        return linksArrayList;
    }

    private String formatLink(String urlStr) {
        String rawUrl = urlStr.replace("%3A", ":").replace("%2F", "/").replace(".html", "");
        return rawUrl.substring(rawUrl.lastIndexOf("http"), rawUrl.lastIndexOf("&"));
    }

}
