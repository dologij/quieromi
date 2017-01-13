package com.brunix.quieromi;

import com.brunix.quieromi.application.MyApplication;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Locale;
import java.util.Random;

/**
 * Created by dolo on 9/14/16.
 */
public class Utils {

    public static int randInt(Random rand, int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static void logError(Throwable throwable) {
        if(!FirebaseApp.getApps(MyApplication.instance).isEmpty()){
            FirebaseCrash.report(throwable);
            if (BuildConfig.DEBUG) {
                throwable.printStackTrace();
            }
        }
    }

    public static boolean stringsAreNullOrEmpty(String... strings) {
        for (String current : strings) {
            if (current == null || current.isEmpty() || current.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static String generateRandomUsername() {
        Random random = new Random();
        return String.format(Locale.getDefault(), MyApplication.instance.getString(R.string.random_username_generator_string_template), random.nextInt(5001));
    }

}
