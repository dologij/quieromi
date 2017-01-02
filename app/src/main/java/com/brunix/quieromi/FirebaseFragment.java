package com.brunix.quieromi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brunix.quieromi.data.entity.Bar;
import com.brunix.quieromi.data.entity.DummyData;
import com.brunix.quieromi.data.entity.Tapa;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/14/16.
 */
public class FirebaseFragment extends Fragment {

    private final static String TAG = FirebaseFragment.class.getSimpleName();

    public static final String BARS_CHILD = "bars";
    public static final String TAPAS_CHILD = "tapas";
    public static final String TAPA_LIST_CHILD = "tapa_list";
    public static final String BAR_LIST_CHILD = "bar_list";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mTapasReference;
    private DatabaseReference mBarsReference;

    private ValueEventListener mTapaValueEventListener;
    private ValueEventListener mBarValueEventListener;
    private ChildEventListener mTapaChildEventListener;

    @BindView(R.id.loadData_button)
    Button mLoadDataButton;

    @BindView(R.id.addItem_button)
    Button mAddItemButton;

    @BindView(R.id.addSingleItem_button)
    Button mAddSingleItemButton;

    @BindView(R.id.updateItem_button)
    Button mUpdateItemButton;

    @BindView(R.id.findItem_button)
    Button mFindItemButton;

    @BindView(R.id.deleteItem_button)
    Button mDeleteItemButton;

    private Unbinder unbinder;

    public static FirebaseFragment newInstance() {
        FirebaseFragment fragment = new FirebaseFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_firebase, container, false);
        unbinder = ButterKnife.bind(this, view);

        initDatabase();

        return view;
    }

    private void initDatabase() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTapasReference = FirebaseDatabase.getInstance().getReference(TAPAS_CHILD);
        mBarsReference = FirebaseDatabase.getInstance().getReference(BARS_CHILD);

        mTapaValueEventListener = mTapasReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--> [Tapa] addValueEventListener.onDataChange()");

                extractAndLogTapaFromMap(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"--> [Tapa] addValueEventListener.onCancelled():" + databaseError);
            }
        });

        mBarValueEventListener = mBarsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--> [Bar] addValueEventListener.onDataChange()");

                extractAndLogBarFromMap(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"--> [Bar] addValueEventListener.onCancelled():" + databaseError);
            }
        });

        mTapaChildEventListener = mTapasReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"--> addChildEventListener.onChildAdded():" + s);

                extractAndLogTapa(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"--> addChildEventListener.onChildChanged():" + s);

                extractAndLogTapa(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--> addChildEventListener.onChildRemoved()");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG,"--> addChildEventListener.onChildMoved():" + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"--> addChildEventListener.onCancelled():");
            }
        });
/*
        mTapasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--> addListenerForSingleValueEvent.onDataChange()");

                extractAndLog(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"--> addListenerForSingleValueEvent.onCancelled()");
            }
        });
*/
    }

    private void extractAndLogTapaFromMap(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            extractAndLogTapa(child);
        }
    }

    private void extractAndLogTapa(DataSnapshot dataSnapshot) {
        Tapa tapa = dataSnapshot.getValue(Tapa.class);
        if(tapa == null) {
            Log.d(TAG, "Object not a Tapa");
        } else {
            tapa.setId(dataSnapshot.getKey());
            Log.d(TAG, "Tapa changed:" + tapa.toString());
        }
    }

    private void extractAndLogBarFromMap(DataSnapshot dataSnapshot) {
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            extractAndLogBar(child);
        }
    }

    private void extractAndLogBar(DataSnapshot dataSnapshot) {
        Bar bar = dataSnapshot.getValue(Bar.class);
        if(bar == null) {
            Log.d(TAG, "Object not a Tapa");
        } else {
            bar.setId(dataSnapshot.getKey());
            Log.d(TAG, "Bar changed:" + bar.toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTapasReference.removeEventListener(mTapaValueEventListener);
        mTapasReference.removeEventListener(mBarValueEventListener);
        mTapasReference.removeEventListener(mTapaChildEventListener);
    }

    @OnClick(R.id.loadData_button)
    public void loadDummyData(Button button) {
        addItem(4);
        //loadDummyDataHashMap(mDatabaseReference);
    }

    @OnClick(R.id.addItem_button)
    public void addItem(Button button) {
        addItem(1);
    }

    @OnClick(R.id.addSingleItem_button)
    public void loadSingleItemData(Button button) {
        //loadDummyDataHashMap(friendsDatabaseReference);
    }

    @OnClick(R.id.updateItem_button)
    public void updateItem(Button button) {
        //loadDummyDataHashMap(friendsDatabaseReference);
    }

    @OnClick(R.id.findItem_button)
    public void findItem(Button button) {
        Query query = mTapasReference.orderByChild("name");//.equalTo("Peter Pan");
        query.addChildEventListener(mTapaChildEventListener);

    }

    @OnClick(R.id.deleteItem_button)
    public void deleteItem(Button button) {
        //loadDummyDataHashMap(friendsDatabaseReference);
    }

    private void loadDummyDataHashMap(DatabaseReference ref) {
        ref.setValue(DummyData.getDummyTapasAsHashMap());
    }

    private void addItem(int numItems) {
        for (int i = 0; i < numItems; i++) {
            DatabaseReference newTapaRef = mTapasReference.push();
            String newTapaKey = newTapaRef.getKey();

            DatabaseReference newBarRef = mBarsReference.push();
            String newBarKey = newBarRef.getKey();

            final Bar bar = DummyData.getDummyBarAsHashMap();
            Map<String, Object> barValues = bar.toMap();
            final Tapa tapa = DummyData.getDummyTapaAsHashMap();
            Map<String, Object> tapaValues = tapa.toMap();

            Map updatedBarData = new HashMap();
            updatedBarData.put(TAPAS_CHILD+"/" + newTapaKey, tapaValues);
            updatedBarData.put(BARS_CHILD+"/" + newBarKey, barValues);
            updatedBarData.put(BAR_LIST_CHILD +"/" + newBarKey + "/" + newTapaKey, true);

            mDatabaseReference.updateChildren(updatedBarData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null) {
                        Log.d(TAG, "--> Error Response: " + databaseError.toString());
                    } else {
                        Log.d(TAG, "--> databaseReference: " + (databaseReference != null ? databaseReference.getKey() : "NULL"));
                        if(databaseReference != null) {
//                        tapa.setUid(databaseReference.getKey());
                            Log.d(TAG, "--> tapa: " + tapa.toString());
                        }

                    }
                }
            });

        }
    }
}
