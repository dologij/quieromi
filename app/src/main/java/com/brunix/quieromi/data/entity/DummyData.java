package com.brunix.quieromi.data.entity;

import com.brunix.quieromi.Utils;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by dolo on 9/19/16.
 */
public class DummyData {

    static String[] uuids = {UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(),
            UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()};
    static String[] names = {"Jack Jones", "Mike Malone", "Peter Pan"
            , "Rodney Roper", "Harry Horne", "Casey Clarke", "Bernie Bunting"};
    static double[] prices = {34.56, 43.21, 19.65, 58.90, 32.91, 82.76, 15.03};
    static double[] longitudes = {-80.1055376, 2.1817251, -6.9730373, 14.1197316, -17.9145562, -7.1655582, -5.664467};
    static double[] latitudes = {26.2754584, 41.4234447, 38.878818, 57.4966613, 28.659471, 38.8799428, 40.9706544};
    static String[] imageUrl = {"http://www.aroming.kz/cms/uploads/images/aromatizatori.png", "http://icons.iconarchive.com/icons/aha-soft/desktop-buffet/256/Salad-icon.png"
            , "http://icons.iconarchive.com/icons/aha-soft/desktop-buffet/256/Steak-icon.png", "http://icons.iconarchive.com/icons/aha-soft/desktop-buffet/256/Piece-of-cake-icon.png"};

    public static HashMap<String, Bar> getDummyBarsAsHashMap() {
        HashMap<String, Bar> barsHashMap = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            String suffix = String.valueOf(System.currentTimeMillis());
            Bar bar = new Bar();
            bar.setName(names[i] + " " + suffix.substring(suffix.length()-5, suffix.length()-1));
            bar.setLongitude(longitudes[i]);
            bar.setLatitude(latitudes[i]);

            barsHashMap.put(String.valueOf(i), bar);
        }
        return barsHashMap;
    }

    public static HashMap<String, Tapa> getDummyTapasAsHashMap() {
        HashMap<String, Tapa> tapasHashMap = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            String suffix = String.valueOf(System.currentTimeMillis());
            Tapa tapa = new Tapa();
            tapa.setId(uuids[i]);
            tapa.setName(names[i] + " " + suffix.substring(suffix.length()-5, suffix.length()-1));
            tapa.setPrice(prices[i]);
            tapa.setLongitude(longitudes[i]);
            tapa.setLatitude(latitudes[i]);
            tapa.setImageUrl(imageUrl[i%4]);

            tapasHashMap.put(String.valueOf(i), tapa);
        }
        return tapasHashMap;
    }

    public static Tapa getDummyTapaAsHashMap() {
        Random r = new Random();
        int i = Utils.randInt(r, 0, 6);
        return getDummyTapasAsHashMap().get(Integer.toString(i));
    }

    public static Bar getDummyBarAsHashMap() {
        Random r = new Random();
        int i = Utils.randInt(r, 0, 6);
        return getDummyBarsAsHashMap().get(Integer.toString(i));
    }
}
