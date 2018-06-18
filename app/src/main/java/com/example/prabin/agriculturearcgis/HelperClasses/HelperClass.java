package com.example.prabin.agriculturearcgis.HelperClasses;

/**
 * Created by Prabin on 6/17/2018.
 */

public class HelperClass {

    public static String toSentenceCase(String original) {//hello

        String firstChar = original.substring(0,1);//h
        String remChar = original.substring(1, original.length());//ello

        firstChar = firstChar.toUpperCase();//H
        String modified = firstChar.concat(remChar);//Hello

        return modified;
    }


}
