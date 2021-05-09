package dev.mrz3t4.literatureclub.UI;

import java.util.ArrayList;

public class GetSimilarity {

    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0; /* both strings are zero length */
        }
    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }


    public double getSimilarityBetweenWords(String word, String toCompare){
        return similarity(word,toCompare);
    }

    public boolean itsSame(String word, String toCompare){
        return similarity(word, toCompare) == 1;
    }
    public boolean isMostSmilar(String word, String toCompare){
        return similarity(word, toCompare) > 0.6;
    }
    public boolean isProbablySimilar(String word, String toCompare){
        return similarity(word, toCompare) < 0.6;
    }

    public boolean isDateSimilar(String date, String toCompare){
        System.out.println("Porcentaje similar date --> " + similarity(date, toCompare));
        return similarity(date, toCompare) >= 0.8;
    }

    public boolean startWithSameWord(String word, String toCompare){
        String word1 = word.substring(0,1);
        String word2 = toCompare.substring(0,1);
        System.out.println("word 1 startsWith: "+word1);
        System.out.println("word 2 startsWith: "+word2);
        return word1.equalsIgnoreCase(word2);
    }

    public boolean endWithSameWord(String word, String toCompare){
        String word1 = word.substring(word.length() - 2);
        String word2 = toCompare.substring(toCompare.length() - 2);
        System.out.println("word 1 endsWith: "+word1);
        System.out.println("word 2 endsWith: "+word2);
        return word1.equalsIgnoreCase(word2);
    }

    public boolean isSimilar(String s, String t, String d, String d2) {

        //        System.out.println(String.format(
//                "%.3f es la similitud entre \"%s\" and \"%s\"", similarity(s, t), s, t));


        if (similarity(s, t) == 1.0 || similarity(s, t) > 0.85) {
            System.out.println("--------------------es similar");
            System.out.println("Similitud entre: " + s + " y " + t + " es de: " + similarity(s, t));
            System.out.println("Año from database: " + d2);
            System.out.println("Año from monoschinos: " + d);

            return true;
        } else {
            if (similarity(s, t) > 0.350 && d.equalsIgnoreCase(d2)) {
                String s1 = s.substring(0, 1);
                String s2 = t.substring(0, 1);

                if (s1.equalsIgnoreCase(s2)) {
                    System.out.println("--------------------es similar");
                    System.out.println("Similitud entre: " + s + " y " + t + " es de: " + similarity(s, t));
                    System.out.println("Año from database: " + d2);
                    System.out.println("Año from monoschinos: " + d);
                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;
    }
}
