package com.brunix.quieromi.tapalist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brunix.quieromi.R;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.base.BaseFragment;
import com.brunix.quieromi.data.Dao;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapa.ui.TapaPagerActivity;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenter;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenterImpl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by dolo on 9/20/16.
 */

public class TapaListFragment extends BaseFragment<TapaListPresenterImpl> implements TapaAdapter.TapaViewHolder.OpenTapaListener{

    private final static String TAG = TapaListFragment.class.getSimpleName();

    @Inject
    Picasso picasso;

    @Inject
    Dao dao;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.tapas_recycler_view)
    RecyclerView mRecyclerView;

//    @BindView(R.id.tapas_swipe)
//    SwipeRefreshLayout mSwipe;

    private TapaAdapter mAdapter;
//    public ArrayList<Tapa> mTapas = new ArrayList<>();

    private TapaListPresenterImpl presenter;

//    private Unbinder unbinder;


    public static TapaListFragment newInstance() {
        TapaListFragment fragment = new TapaListFragment();
        return fragment;
    }


    @Override
    public void open(Tapa tapa) {
//        Toast.makeText(getActivity(), tapa.getId() + "," + tapa.getName() + " clicked...", Toast.LENGTH_SHORT).show();
        Intent intent = TapaPagerActivity.newIntent(getActivity(),tapa.getId());
        startActivity(intent);
    }

    /**
     * Required interface for hosting activities.
     */
//    public interface Callbacks {
//        void onTapaSelected(Tapa tapa);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tapa_list, container, false);

    }

    // Code moved from onCreateView(). As per the onViewCreated()'s comment:
    // "Any view setup should happen here. E.g., view lookups, attaching listeners."
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        unbinder = ButterKnife.bind(this, view);

        // Inject components
        MyApplication.get(getActivity()).getApplicationComponent().inject(this);

        super.onViewCreated(view, savedInstanceState);

        //initDatabase();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

//        List<Tapa> temp = new ArrayList<>();
//        temp.add(new Tapa("nombre2", new Double(2), "url", null, new Double(3), new Double(4)));

        if(mAdapter == null) {
            mAdapter = new TapaAdapter(new ArrayList<Tapa>(), picasso);
            mAdapter.setOpenTapaListener(this);
            mRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.setTapas(temp);
//            mAdapter.notifyDataSetChanged();
        }


//        mSwipe.setOnRefreshListener(this::refreshList);

        refreshList();

    }

    private void refreshList() {

        showRefresh(true);

        RxFirebaseDatabase.observeSingleValueEvent(dao.getTapasOrderByNameQuery(), //DataSnapshotMapper.listOf(Tapa.class))
                new Func1<DataSnapshot, List<Tapa>>() {
                    @Override
                    public List<Tapa> call(DataSnapshot dataSnapshot) {
                        List<Tapa> tapas = new ArrayList<Tapa>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Tapa tapa = child.getValue(Tapa.class);
                            if (tapa == null) {
                                Log.d(TAG, "Object not a Tapa");
                            } else {
                                tapa.setId(child.getKey());
                                Log.d(TAG, "Tapa retrieved:" + tapa.getId());
                                tapas.add(tapa);
                            }
                        }

                        return tapas;
                    }
                })
                .subscribe(new Observer<List<Tapa>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "--- RxFirebaseDatabase.observeSingleValueEvent COMPLETED");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        showRefresh(false);
                    }

                    @Override
                    public void onNext(List<Tapa> tapas) {
                        showRefresh(false);
                        mAdapter.updateTapas(tapas);
                    }
                });

    }

    private void showRefresh(boolean show) {
//        mSwipe.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        mRecyclerView.setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();

        //mTapasReference.removeEventListener(mTapaValueEventListener);
        mAdapter.cleanup();
    }

    @Override public void onDestroy() {
        super.onDestroy();
//        MyApplication.instance.mustDie(this);
    }


    private void initDatabase() {
        dao = new Dao(FirebaseDatabase.getInstance().getReference());

    }




    @Override
    protected TapaListPresenterImpl getPresenter() {
        if (presenter == null) {
            presenter = new TapaListPresenterImpl();//(RemoteServiceImpl.getInstance());
        }
        return presenter;
    }

    @NonNull
    @Override
    protected View getFallbackView() {
        return fab;
    }



}
