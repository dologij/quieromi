package com.brunix.quieromi.data;

import android.net.Uri;

import com.brunix.quieromi.data.entity.Tapa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dolo on 1/3/17.
 */

public interface DatabaseHelper {

    void receiveTapasFromFirebase(int viewId, ChildEventListener listener);
//    void receiveTapasFromFirebase(ValueEventListener listener);

    void sendTapaToFirebase(Tapa tapa, Uri imageUri);

    void removeTapaCallbacks(ChildEventListener listener);

//    public void receiveTapaFromFirebase(String key, ChildEventListener listener);
    public void receiveTapaFromFirebase(String key, ValueEventListener listener);

    void removeListeners(Integer objectId);
}
