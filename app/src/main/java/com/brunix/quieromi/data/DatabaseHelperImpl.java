package com.brunix.quieromi.data;

import com.brunix.quieromi.DatabaseRefMap;
import com.brunix.quieromi.data.entity.Tapa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dolo on 1/3/17.
 */

public class DatabaseHelperImpl implements DatabaseHelper {

    private final FirebaseDatabase firebaseDatabase;
    private Query mMessageQuery;
    private HashMap<Integer, DatabaseRefMap> listenerMap = new HashMap<>();

    public DatabaseHelperImpl(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void receiveTapasFromFirebase(int viewId, ChildEventListener listener) {
        final DatabaseReference reference = firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE);
        addListenerToMap(new Integer(viewId), reference, listener);
        reference.orderByValue().limitToLast(FirebaseConstants.RETRIEVED_TAPAS_LIMIT).addChildEventListener(listener);
    }

    private void addListenerToMap(Integer objectId, DatabaseReference reference, ChildEventListener listener) {
        boolean isObjectIdInMap = false;
        DatabaseRefMap refMap = null;
        for (Map.Entry<Integer, DatabaseRefMap> entry : listenerMap.entrySet()) {
            if (entry.getKey().equals(objectId)) {
                isObjectIdInMap = true;
                refMap = entry.getValue();
                if (refMap == null) {
                    refMap = new DatabaseRefMap();
                }
                refMap.addElement(reference, listener);
            }
        }
        if (!isObjectIdInMap) {
            refMap = new DatabaseRefMap();
            refMap.addElement(reference, listener);
        }
        listenerMap.put(objectId, refMap);
    }

    @Override
    public void sendTapaToFirebase(Tapa tapa) {
        //ChatMessage message = new ChatMessage(messageToSend, username, lastMessageAuthor);
        firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE).push().setValue(tapa);
    }

    // TODO: call this when checking if the user is logged in (on presenter.checkIfUserIsLoggedIn())
    @Override
    public void removeTapaCallbacks(ChildEventListener listener) {
        firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE).removeEventListener(listener);
    }

    @Override
    public void removeListeners(Integer objectId) {
        removeListeners(objectId, firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE));
    }

    public void removeListeners(Integer objectId, DatabaseReference reference) {
        for(Iterator<Map.Entry<Integer, DatabaseRefMap>> it = listenerMap.entrySet().iterator(); it.hasNext();){
            Map.Entry<Integer, DatabaseRefMap> entry = it.next();
            Integer objectKey = entry.getKey();
            if (objectKey.intValue() == objectId.intValue()) {
                DatabaseRefMap refMap = entry.getValue();
                refMap.getListenerMap();
                for(Iterator<Map.Entry<DatabaseReference, List<ChildEventListener>>> refIt = refMap.getListenerMap().entrySet().iterator(); refIt.hasNext();){
                    Map.Entry<DatabaseReference, List<ChildEventListener>> refEntry = refIt.next();
                    DatabaseReference ref = refEntry.getKey();
                    if (ref.equals(reference)) {
                        for (ChildEventListener listener : refEntry.getValue()) {
                            ref.removeEventListener(listener);
                        }
                        refIt.remove();
                    }
                }
            }
        }
    }

    @Override
    public void receiveTapaFromFirebase(String key, ValueEventListener listener) {
        firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE).orderByKey().equalTo(key).addListenerForSingleValueEvent(listener);
    }

}
