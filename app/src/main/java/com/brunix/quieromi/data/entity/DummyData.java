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
    static double[] prices = {34.56, 43.21, 19.65, 58.90,32.91,82.76,15.03};
    static double[] longitudes = {12.3456, 65.4321, 19.8765, 56.7890, 37.5291, 98.3276, 18.2503};
    static double[] latitudes = {-12.3456, -65.4321, -19.8765, -56.7890, -37.5291, -98.3276, -18.2503};
    static String imageUrl = "http://www.amys.com/images/uploads/special_diets/healthy_living/vegetarian_plate.jpg";

    public static HashMap<String, Bar> getDummyBarsAsHashMap() {
        HashMap<String, Bar> barsHashMap = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            Bar bar = new Bar();
            bar.setName(names[i]);
            bar.setLongitude(longitudes[i]);
            bar.setLatitude(latitudes[i]);

            barsHashMap.put(String.valueOf(i), bar);
        }
        return barsHashMap;
    }

    public static HashMap<String, Tapa> getDummyTapasAsHashMap() {
        HashMap<String, Tapa> tapasHashMap = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            Tapa tapa = new Tapa();
            tapa.setId(uuids[i]);
            tapa.setName(names[i]);
            tapa.setPrice(prices[i]);
            tapa.setLongitude(longitudes[i]);
            tapa.setLatitude(latitudes[i]);
            tapa.setImageUrl(imageUrl);

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
