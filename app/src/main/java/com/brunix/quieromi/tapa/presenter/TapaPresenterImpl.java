package com.brunix.quieromi.tapa.presenter;

import android.util.Log;

import com.brunix.quieromi.Utils;
import com.brunix.quieromi.data.DatabaseHelper;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapa.view.TapaView;
import com.brunix.quieromi.tapalist.view.TapaListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dolo on 1/9/17.
 */

public class TapaPresenterImpl implements TapaPresenter {

    private final static String TAG = TapaPresenterImpl.class.getSimpleName();

    private TapaView tapaView;

    private final DatabaseHelper databaseHelper;

    public TapaPresenterImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void requestTapaFromNetwork(String key) {
        databaseHelper.receiveTapaFromFirebase(key, provideTapaCallbackValue());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setView(TapaView view) {
        tapaView = view;
    }

    protected ValueEventListener provideTapaCallbackValue() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot tapaSnapshot : dataSnapshot.getChildren()) {
                        Tapa tapa = tapaSnapshot.getValue(Tapa.class);
                        if (tapa != null) {
                            tapa.setId(tapaSnapshot.getKey());
                            tapaView.refreshData(tapa);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    Utils.logError(databaseError.toException());
                }
            }
        };
    }

}
