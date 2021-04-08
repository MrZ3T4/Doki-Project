package dev.mrz3t4.literatureclub.Utils;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dev.mrz3t4.literatureclub.RecyclerView.Anime;


public class JsonTools {


    File directory = new File(GenericContext.getContext().getFilesDir(), "directory.json");

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

        if (directory.exists()){
            return true;
        } else {
            return false;
        }

    }


}
