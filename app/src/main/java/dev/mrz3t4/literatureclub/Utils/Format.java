package dev.mrz3t4.literatureclub.Utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.mrz3t4.literatureclub.Anime.SeasonAndExplore.Anime;

public class Format {

    public static String getType(String type){

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

    public static void sortAlphabetical(ArrayList<Anime> directoryArrayList){
        Collections
                .sort(directoryArrayList,
                        (o1, o2) -> o1.getTitle()
                                .compareTo(o2.getTitle()));
    }

    public static String servers(ArrayList<String> serversArrayList, int pos){

        String server;

        if (serversArrayList.get(pos).contains("fembed")){
            server = "Fembed (Nativo)";
        } else if (serversArrayList.get(pos).contains("ok.ru")){
            server = "Okru (Nativo)";
        } else if (serversArrayList.get(pos).contains("sendvid")){
            server = "Sendvid (Nativo)";
        } else if (serversArrayList.get(pos).contains("mp4upload")){
            server = "Mp4Upload (Web)";
        } else if (serversArrayList.get(pos).contains("uqload")){
            server = "Uqload (Web)";
        } else if (serversArrayList.get(pos).contains("clip")){
            server = "ClipWatching (Web)";
        } else if (serversArrayList.get(pos).contains("streamtape")){
            server = "Streamtape (Web)";
        } else if (serversArrayList.get(pos).contains("videobin")){
            server = "Videobin (Web)";
        } else if (serversArrayList.get(pos).contains("mediafire")){
            server = "Mediafire (Nativo)";
        } else {
            server = "Servidor (Web)";
        }

        return server;
    }

    public static ArrayList<String> extractNames(String text){

        ArrayList<String> namesArrayList = new ArrayList<>();
        Pattern pa = Pattern.compile("\"([^\"]*)\"");
        Matcher ma = pa.matcher(text);

        while (ma.find()) {
            namesArrayList.add(ma.group(1));
            System.out.println("Title --> " + ma.group(1));
        }

        return namesArrayList;
    }

    public static ArrayList<String> extractLinks(String text) {
        ArrayList<String> linksArrayList = new ArrayList<>();

        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while(m.find())
        {
            String urlStr = m.group();

                if (!urlStr.contains("myanimelist")){

                    if (!urlStr.contains("1080") && !urlStr.contains("Lyrics")){

                    String url = urlStr.substring(1, urlStr.indexOf(")"));
                    System.out.println("Opending url --> " + url);
                    linksArrayList.add(url);
                    }
                }

                }

        return linksArrayList;
    }

    public static ArrayList<String> extractTypes(String text){
        ArrayList<String> typesArrayList = new ArrayList<>();

        System.out.println(text);
        String line = text.substring(text.indexOf("Theme"));


        Pattern p = Pattern.compile("([C-F[N-Q]]{2}[\\d]*)\\s(V?[\\d]*)");
        Matcher m = p.matcher(line);



        try {
            while (m.find()){
                System.out.println("Type --> " + m.group());
                typesArrayList.add(m.group());
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return typesArrayList;

    }
    private static String formatLink(String urlStr) {
        String rawUrl = urlStr.replace("%3A", ":").replace("%2F", "/").replace(".html", "");
        return rawUrl.substring(rawUrl.lastIndexOf("http"), rawUrl.lastIndexOf("&"));
    }

}
