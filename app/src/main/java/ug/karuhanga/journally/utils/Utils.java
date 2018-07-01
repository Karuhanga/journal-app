package ug.karuhanga.journally.utils;

import android.util.Log;

import com.google.gson.Gson;

public class Utils {
    public static void log(Object object) {
        Log.d("Journal.ly", new Gson().toJson(object));
    }
}
