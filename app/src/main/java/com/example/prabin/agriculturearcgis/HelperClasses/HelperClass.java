package com.example.prabin.agriculturearcgis.HelperClasses;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prabin on 6/17/2018.
 */

public class HelperClass {

    public static String toSentenceCase(String original) {//hello

        String firstChar = original.substring(0, 1);//h
        String remChar = original.substring(1, original.length());//ello

        firstChar = firstChar.toUpperCase();//H
        String modified = firstChar.concat(remChar);//Hello

        return modified;
    }

    //https://stackoverflow.com/a/13913206
    public static HashMap<String, Long> sortByComparator(HashMap<String, Long> unsortHashMap) {

        final boolean order = true;   //true for ASCENDING
        //false for DESCENDING

        List<HashMap.Entry<String, Long>> list = new LinkedList<>(unsortHashMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>() {
            public int compare(HashMap.Entry<String, Long> o1,
                               HashMap.Entry<String, Long> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Long> sortedHashMap = new LinkedHashMap<String, Long>();
        for (HashMap.Entry<String, Long> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }

        return sortedHashMap;
    }

    public static long maxValueHashMap(HashMap<String, Long> dataHashMap) {

        final boolean order = true;   //true for ASCENDING
        //false for DESCENDING
        long max = 0;
        for (Map.Entry<String, Long> entry : dataHashMap.entrySet()) {
            long value = entry.getValue();
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static long getNearestValueInThousand(long value) {


        //27846 should return 27000
        //7846 should return 7000
        //746 should return 700
        //46 should return 40

        String v = value + "";
        int length = v.length();

        switch (length) {
            case 5:
                v = v.substring(0,2).concat("000");
                break;
            case 4:
                v = v.substring(0,1).concat("000");
                break;
            case 3:
                v = v.substring(0,1).concat("00");
                break;
            case 2:
                v = v.substring(0,1).concat("0");
                break;
            default:
                break;
        }

        return Long.parseLong(v);
    }

    public static boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }

}
