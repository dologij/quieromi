package com.brunix.quieromi;

import java.util.Random;

/**
 * Created by dolo on 9/14/16.
 */
public class Utils {

    public static int randInt(Random rand, int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
