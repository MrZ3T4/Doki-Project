package dev.mrz3t4.literatureclub.UI;

public class GetSimilarity {

    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
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

    public boolean isSimilar(String s, String t, String d, String d2) {
//        System.out.println(String.format(
//                "%.3f es la similitud entre \"%s\" and \"%s\"", similarity(s, t), s, t));


        if (similarity(s, t) > 0.340 && d.contains(d2)){
            String s1 = s.substring(0, 1);
            String s2 = t.substring(0,1);

            if (s1.equalsIgnoreCase(s2)){
                System.out.println("s1: " + s1 + " s2: " + s2);
                System.out.println("--------------------es similar");
                System.out.println("Similitud entre: " + s + " y " + t + " es de: " + similarity(s,t));
                System.out.println("Año from database: " + d2);
                System.out.println("Año from monoschinos: " + d);
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }



    }

    public double max(double a, int b) {
        return Math.max(a, b);
    }

}
