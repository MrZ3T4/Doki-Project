package dev.mrz3t4.literatureclub.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterLinks {

    public ArrayList<String> pullLinks(String text, String firstURL)
    {
        ArrayList<String> links = new ArrayList<String>();

        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        String regex = "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while(m.find())
        {
            String urlStr = m.group();

            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }

            String urlFinal = formatLink(urlStr);
            String firstOptFinal = formatLink(firstURL);
            links.add(firstOptFinal);
            links.add(urlFinal);
        }

        return links;
    }

    private String formatLink(String urlStr) {

        String rawUrl = urlStr.replace("%3A", ":").replace("%2F", "/");

        return rawUrl.substring(rawUrl.lastIndexOf("http"), rawUrl.lastIndexOf("&"));

    }


}
