package dev.mrz3t4.literatureclub.Utils;

import android.os.Environment;

import dev.mrz3t4.literatureclub.R;

public class Constants {

    public static String broadcast = GenericContext.getContext().getResources().getString(R.string.broadcast);
    public static String calendar = GenericContext.getContext().getResources().getString(R.string.calendar);
    public static String season = GenericContext.getContext().getResources().getString(R.string.season);
    public static String information = GenericContext.getContext().getResources().getString(R.string.anime_data);
    public static String episodes = GenericContext.getContext().getResources().getString(R.string.episodes);
    public static String staff = GenericContext.getContext().getResources().getString(R.string.staff);
    public static String themes = GenericContext.getContext().getResources().getString(R.string.themes);

    public static String recents = GenericContext.getContext().getResources().getString(R.string.recents_toolbar);
    public static String collections = GenericContext.getContext().getResources().getString(R.string.collections_toolbar);
    public static String explore = GenericContext.getContext().getResources().getString(R.string.explore_toolbar);
    public static String settings = GenericContext.getContext().getResources().getString(R.string.settings_toolbar);

    public static String episode = GenericContext.getContext().getResources().getString(R.string.episode);

    public static Integer MODE_EPISODE = 1;


    // Broadcast
    public static final int MODE_BROADCAST = 65468432;
    public static final String BROADCAST_URL = "https://monoschinos2.com/";
    //Season
    public static final int MODE_SEASON = 35468321;
    public static String SEASON_URL = "https://monoschinos2.com/emision";
    //Explore
    public static final int MODE_EXPLORE = 13221384;
    public static String EXPLORE_URL = "https://monoschinos2.com/animes";
    //Complements
    public static String PAGE_URI = "?page=";
}
