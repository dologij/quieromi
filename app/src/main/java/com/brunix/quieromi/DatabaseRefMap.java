package com.brunix.quieromi;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dolo on 1/12/17.
 */

public class DatabaseRefMap {

    private HashMap<DatabaseReference, List<ChildEventListener>> listenerMap = new HashMap<>();

    public HashMap<DatabaseReference, List<ChildEventListener>> getListenerMap() {
        return listenerMap;
    }

    public void setListenerMap(HashMap<DatabaseReference, List<ChildEventListener>> listenerMap) {
        this.listenerMap = listenerMap;
    }

    public void addElement(DatabaseReference ref, ChildEventListener listener) {
        List<ChildEventListener> listenersList = null;
        boolean isRefInMap = false;
        for (Map.Entry<DatabaseReference, List<ChildEventListener>> entry : listenerMap.entrySet()) {
            if (entry.getKey().equals(ref)) {
                isRefInMap = true;
                listenersList = entry.getValue();
                if (listenersList == null) {
                    listenersList = new ArrayList<>();
                }
                listenersList.add(listener);
            }
        }
        if (!isRefInMap) {
            listenersList = new ArrayList<>();
            listenersList.add(listener);
        }
        listenerMap.put(ref, listenersList);

    }
}
