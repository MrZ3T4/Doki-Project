package dev.mrz3t4.literatureclub.Utils;

import java.util.ArrayList;
import java.util.Collections;

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

}
