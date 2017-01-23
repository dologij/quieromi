package com.brunix.quieromi.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.brunix.quieromi.DatabaseRefMap;
import com.brunix.quieromi.data.entity.Tapa;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dolo on 1/3/17.
 */

public class DatabaseHelperImpl implements DatabaseHelper {

    private final FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    private Query mMessageQuery;
    private HashMap<Integer, DatabaseRefMap> listenerMap = new HashMap<>();

    public DatabaseHelperImpl(FirebaseDatabase firebaseDatabase, FirebaseStorage firebaseStorage) {
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseStorage = firebaseStorage;
    }

    @Override
    public void receiveTapasFromFirebase(int viewId, ChildEventListener listener) {
        final DatabaseReference reference = firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE);
        addListenerToMap(new Integer(viewId), reference, listener);
        reference.orderByChild("name").limitToLast(FirebaseConstants.RETRIEVED_TAPAS_LIMIT).addChildEventListener(listener);
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
    public void sendTapaToFirebase(final Tapa tapa, final Uri imageUri) {
        final DatabaseReference ref = firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE + "/" + tapa.getId());
                ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // The tapa already exists, it must be updated
                        if (dataSnapshot.exists()) {
                            ref.updateChildren(tapa.toMap());
                        // The tapa does not exist, it must be created
                        } else {
                            String newTapaKey = firebaseDatabase.getReference(FirebaseConstants.TAPAS_REFERENCE).push().getKey();
                            Map tapaData = new HashMap();
                            tapaData.put(FirebaseConstants.TAPAS_REFERENCE + "/" + newTapaKey, tapa.toMap());
                        }

                        sendImageToFirebase(ref, tapa, imageUri);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void sendImageToFirebase (final DatabaseReference ref, final Tapa tapa, final Uri imageUri) {
        // Create a storage reference from our app
        StorageReference storageRef = firebaseStorage.getReference();

        StorageReference imagesRef = storageRef.child(FirebaseConstants.TAPAS_REFERENCE + "/" + tapa.getPhotoFilename());
        UploadTask uploadTask = imagesRef.putFile(imageUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                tapa.setImageUrl(downloadUrl.toString());
                ref.updateChildren(tapa.toMap());
            }
        });
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
