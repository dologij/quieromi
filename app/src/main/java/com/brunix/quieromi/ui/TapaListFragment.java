package com.brunix.quieromi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunix.quieromi.R;
import com.brunix.quieromi.model.Tapa;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/20/16.
 */

public class TapaListFragment extends Fragment {

    private final static String TAG = TapaListFragment.class.getSimpleName();

    @BindView(R.id.tapas_recycler_view)
    RecyclerView mRecyclerView;

    //private TapaListAdapter mAdapter;
    public ArrayList<Tapa> mTapas = new ArrayList<>();

    private Unbinder unbinder;
    private Callbacks mCallbacks;


    public static TapaListFragment newInstance() {
        TapaListFragment fragment = new TapaListFragment();
        return fragment;
    }

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTapaSelected(Tapa tapa);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tapa_list, container, false);
        unbinder = ButterKnife.bind(this, v);

        initDatabase();

        setUpFirebaseAdapter();

        // TODO: Move code to onViewCreated()? As per the onViewCreated()'s comment:
        // "Any view setup should happen here. E.g., view lookups, attaching listeners."
/*
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getTapas();
*/
        //updateUI();

        //mAdapter = new TapaListAdapter(new ArrayList<Tapa>(DummyData.getDummyTapasAsHashMap().values()));
        //mRecyclerView.setAdapter(mAdapter);

        return v;
    }

/*
    public void updateUI() {

        if (mAdapter == null) {
            mAdapter = new TapaListAdapter(mTapas);

//        mAdapter.setOnItemClickListener(new ItemsCallback<ViewModel>() {
//            @Override
//            public void onItemClick(View view, ViewModel viewModel) {
//                mAdapter.remove(viewModel);
//            }
//        });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTapas(mTapas);
            mAdapter.notifyDataSetChanged();
        }

        //updateSubtitle();
    }
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        //mTapasReference.removeEventListener(mTapaValueEventListener);
        mFirebaseAdapter.cleanup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity){
            Log.d(TAG, "--> onAttach(Contex): context IS an Activity");
            activity = (Activity) context;

            if(activity instanceof Callbacks) {
                mCallbacks = (Callbacks) activity;
            }

        } else {
            Log.d(TAG, "--> onAttach(Contex): context IS NOT an Activity");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }



    public static final String TAPAS_CHILD = "tapas";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mTapasReference;
    private DatabaseReference mBarsReference;

    private ValueEventListener mTapaValueEventListener;

    private void initDatabase() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTapasReference = FirebaseDatabase.getInstance().getReference(TAPAS_CHILD);
        //mBarsReference = FirebaseDatabase.getInstance().getReference(BARS_CHILD);
    }
/*
    public void getTapas() {
        Query query = mTapasReference.orderByChild("name");//.equalTo("Peter Pan");

        mTapaValueEventListener = mTapasReference.addValueEventListener(new ValueEventListener() {
        //mTapasReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--> [Tapa] addValueEventListener.onDataChange()");


                mTapas = new ArrayList<Tapa>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Tapa tapa = child.getValue(Tapa.class);
                    if(tapa == null) {
                        Log.d(TAG, "Object not a Tapa");
                    } else {
                        tapa.setId(child.getKey());
                        mTapas.add(tapa);
                    }
                }
                updateUI();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"--> [Tapa] addValueEventListener.onCancelled():" + databaseError);
            }
        });

        //query.addChildEventListener(mTapaChildEventListener);
        query.addValueEventListener(mTapaValueEventListener);

    }
*/

    static class TapaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final int MAX_WIDTH = 200;
        private static final int MAX_HEIGHT = 200;

        @BindView(R.id.tapaImageView)
        ImageView mTapaImageView;
        @BindView(R.id.tapaNameTextView)
        TextView mNameTextView;
        @BindView(R.id.categoryTextView)
        TextView mCategoryTextView;
        @BindView(R.id.ratingTextView)
        TextView mRatingTextView;

        //View mView;
        private Context mContext;

        public TapaViewHolder(View itemView) {
            super(itemView);
            //mView = itemView;
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        public void bindTapa(Tapa tapa) {
/*
            ImageView mTapaImageView = (ImageView) mView.findViewById(R.id.tapaImageView);
            TextView mNameTextView = (TextView) mView.findViewById(R.id.tapaNameTextView);
            TextView mCategoryTextView = (TextView) mView.findViewById(R.id.categoryTextView);
            TextView mRatingTextView = (TextView) mView.findViewById(R.id.ratingTextView);
*/
            // TODO Usar como singleton
            Picasso.with(mContext)
                    .load(tapa.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mTapaImageView);

            mNameTextView.setText(tapa.getName());
            mCategoryTextView.setText("Category of the tapa");
            mRatingTextView.setText("Rating: " + tapa.getLongitude() + "/5");
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, v.getTag() + " clicked", Toast.LENGTH_SHORT).show();
/*
            final ArrayList<Tapa> tapas = new ArrayList<>();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(TAPAS_CHILD);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        tapas.add(snapshot.getValue(Tapa.class));
                    }

                    int itemPosition = getLayoutPosition();

                    Intent intent = new Intent(mContext, TapaDetailActivity.class);
                    intent.putExtra("position", itemPosition + "");
                    intent.putExtra("restaurants", Parcels.wrap(tapas));

                    mContext.startActivity(intent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
*/
        }
    }

    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Tapa, TapaViewHolder>
                (Tapa.class, R.layout.tapa_list_item, TapaViewHolder.class,
                        mTapasReference.orderByChild("name")) {

            @Override
            protected void populateViewHolder(TapaViewHolder viewHolder,
                                              Tapa model, int position) {
                viewHolder.bindTapa(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }


/*
    private class TapaListAdapter extends RecyclerView.Adapter<TapaViewHolder> {

        private List<Tapa> mTapas = new ArrayList<>();

        public TapaListAdapter(List<Tapa> tapas) {
            mTapas = tapas;
        }

        @Override
        public TapaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.tapa_list_item, parent, false);
            TapaViewHolder viewHolder = new TapaViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(TapaViewHolder holder, int position) {
            holder.bindTapa(mTapas.get(position));
        }

        @Override
        public int getItemCount() {
            return mTapas.size();
        }

        public void setTapas(List<Tapa> tapas) {
            mTapas = tapas;
        }

    }
*/
}
