package com.example.anna.ses_1b_group2.utils;

public class StringManipulation {

    public static String expendUsername(String username){
        return username.replace(".", " ");
    }
    public static String condenseUsername(String username){
        return username.replace(" ", ".");
    }
}
