package com.brunix.quieromi;

/**
 * Created by dolo on 1/12/17.
 */

public class ViewIdManager {

    public static int getViewId(Object obj) {
        return obj.hashCode();
    }

}
