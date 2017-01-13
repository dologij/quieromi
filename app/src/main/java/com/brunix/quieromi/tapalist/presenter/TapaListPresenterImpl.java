package com.brunix.quieromi.tapalist.presenter;

import android.util.Log;

import com.brunix.quieromi.Utils;
import com.brunix.quieromi.data.DatabaseHelper;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapalist.view.TapaListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by dolo on 12/27/16.
 */

//public class TapaListPresenterImpl extends BasePresenterOld<TapaListPresenter.View> implements TapaListPresenter {
public class TapaListPresenterImpl implements TapaListPresenter {

    private final static String TAG = TapaListPresenterImpl.class.getSimpleName();

    private TapaListView tapaListView;

    private final DatabaseHelper databaseHelper;

    public TapaListPresenterImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

/*
    @Override
    protected View getDummyView() {
        return new TapaListPresenter.View() {
            @Override
            public void showMessage(@StringRes int messageResourceId, BaseViewOld.MessageCallback callback) {
                    Log.d(TAG, "showMessage: not attached");
            }
        };
    }
*/

    @Override
    public void setView(TapaListView view) {
        this.tapaListView = view;
    }

    @Override
    public void requestTapasFromNetwork(int viewId) {
        databaseHelper.receiveTapasFromFirebase(viewId, provideTapaCallback());
    }

    @Override
    public void removeTapaCallbacks() {
//        databaseHelper.removeTapaCallbacks(provideTapaCallback());
    }

    @Override
    public void sendTapaToNetwork(Tapa tapa) {
/*
        if (!StringUtils.stringsAreNullOrEmpty(messageToSend)) {
            tapaListView.clearMessageBox();
            //databaseHelper.sendTapaToFirebase(authenticationHelper.getCurrentUserDisplayName(), messageToSend, lastMessageAuthor);
            databaseHelper.sendTapaToFirebase(tapa);
        } else {
            tapaListView.showErrorMessage();
        }
*/
    }

    @Override
    public void onDestroy() {
        databaseHelper.removeListeners(tapaListView.getViewId());
    }


    protected ChildEventListener provideTapaCallback() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded");
                Tapa tapa = dataSnapshot.getValue(Tapa.class);
                if (tapa != null) {
                    tapa.setId(dataSnapshot.getKey());
                    tapaListView.sendTapaToAdapter(tapa);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged");
                Tapa tapa = dataSnapshot.getValue(Tapa.class);
                if (tapa != null) {
                    tapa.setId(dataSnapshot.getKey());
                    tapaListView.sendTapaToAdapter(tapa);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved");
                Tapa tapa = dataSnapshot.getValue(Tapa.class);
                if (tapa != null) {
                    tapa.setId(dataSnapshot.getKey());
                    tapaListView.removeTapaFromAdapter(tapa.getId());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
                if (databaseError != null) {
                    Utils.logError(databaseError.toException());
                }
            }
        };
    }

}
