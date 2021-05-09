package dev.mrz3t4.literatureclub.Utils;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.Anime.SeasonAndExplore.Anime;


public class JsonTools {


    public static File directory = new File(GenericContext.getContext().getFilesDir(), "directory.json");

    public void createJSONFileDirectory(ArrayList<Anime> animeArrayList) {

        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < animeArrayList.size(); i++) {
            jsonArray.put(animeArrayList.get(i).getJsonObject());
        }
        String jsonDirectory = jsonArray.toString();

        try {

            FileWriter writer = new FileWriter(directory);
            writer.append(jsonDirectory);
            writer.flush();
            writer.close();

            directory.mkdirs();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean jsonExists() {
        return directory.exists();
    }

    public void getDirectoryFromJson(Context context){

        ArrayList<Anime> animeArrayList = new ArrayList<>();

        File directory = new File(GenericContext.getContext().getFilesDir(), "directory.json");

        try {

            FileInputStream fileInputStream = new FileInputStream(directory);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String animesJson = bufferedReader.readLine();

            JSONArray jsonDirectory = new JSONArray(animesJson);

            for (int pos = 0; pos < jsonDirectory.length(); pos++) {

                Anime anime = new Anime();

                JSONObject jsonObject = jsonDirectory.getJSONObject(pos);

                String title = jsonObject.getString("Title");
                String img = jsonObject.getString("Cover");
                String date = jsonObject.getString("Date");
                String type = jsonObject.getString("Type");
                String url = jsonObject.getString("Link");

                anime.setTitle(title);
                anime.setImg(img);
                anime.setType(type);
                anime.setDate(date);
                anime.setUrl(url);

                animeArrayList.add(anime);

            }

        } catch (Exception e){ e.printStackTrace(); }

        Intent intent = new Intent("Explore");
        intent.putParcelableArrayListExtra("arraylist", animeArrayList);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }


}
