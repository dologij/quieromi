package com.brunix.quieromi.data;

import android.util.Log;

import com.brunix.quieromi.data.entity.Tapa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dolo on 10/4/16.
 */

public class Dao {

    private final static String TAG = Dao.class.getSimpleName();

    public static final String TAPAS_CHILD = "tapas";

    DatabaseReference ref = null;

//    private HashMap<String,Tapa> tapas;
    private List<Tapa> tapas;

    public Dao(DatabaseReference ref){
        this.ref = ref;
        final ValueEventListener tapaDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<HashMap<String,Tapa>> t = new GenericTypeIndicator<HashMap<String,Tapa>>() {};
//                tapas = dataSnapshot.getValue(t);

                tapas = new ArrayList<Tapa>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tapa tapa = child.getValue(Tapa.class);
                    if(tapa == null) {
                        Log.d(TAG, "Object not a Tapa");
                    } else {
                        tapa.setId(child.getKey());
                        tapas.add(tapa);
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.child(TAPAS_CHILD).addValueEventListener(tapaDataListener);
    }

    DatabaseReference addTapa(Tapa tapa){
        DatabaseReference objRef=ref.child(TAPAS_CHILD).push();
        objRef.setValue(tapa);
        return objRef;
    }

    public List<Tapa> getTapas() {
//        List list = new ArrayList<>(tapas.values());
//        return list;

        return tapas;
    }

    public Query getTapasOrderByNameQuery() {
        return ref.child(TAPAS_CHILD).orderByChild("name");
    }
}
