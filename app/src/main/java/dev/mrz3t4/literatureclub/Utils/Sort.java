package dev.mrz3t4.literatureclub.Utils;

import java.util.ArrayList;
import java.util.Collections;

import dev.mrz3t4.literatureclub.RecyclerView.Anime;

public class Sort {

    public void getArrayListByTitle(ArrayList<Anime> directoryArrayList){
        Collections
                .sort(directoryArrayList,
                        (o1, o2) -> o1.getTitle()
                                .compareTo(o2.getTitle()));
    }



}
