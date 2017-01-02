package com.brunix.quieromi.tapa.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brunix.quieromi.R;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.Dao;
import com.brunix.quieromi.data.entity.Tapa;
import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.functions.Func1;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaPagerActivity extends AppCompatActivity {

    private final static String TAG = TapaPagerActivity.class.getSimpleName();

    private static final String EXTRA_TAPA_ID =
            "com.brunix.quieromi.ui.tapa_id";

    @BindView(R.id.activity_tapa_pager_view_pager)
    ViewPager mViewPager;

    private List<Tapa> mTapas;

    private TapaPagerAdapter mAdapter;

    @Inject
    Dao dao;

    public static Intent newIntent(Context packageContext, String tapaId) {
        Intent intent = new Intent(packageContext, TapaPagerActivity.class);
        intent.putExtra(EXTRA_TAPA_ID, tapaId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapa_pager);
        ButterKnife.bind(this);

        // Inject components
        MyApplication.get(this).getApplicationComponent().inject(this);

        final String tapaId = (String) getIntent()
                .getSerializableExtra(EXTRA_TAPA_ID);

        Log.d(TAG, "---> Need tapaId:" + tapaId);

        //mViewPager = (ViewPager) findViewById(R.id.activity_tapa_pager_view_pager);


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
                                tapas.add(tapa);
                            }
                        }

                        return tapas;
                    }
                })
                .subscribe(new Observer<List<Tapa>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "---> RxFirebaseDatabase.observeSingleValueEvent COMPLETED");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
//                        showRefresh(false);
                    }

                    @Override
                    public void onNext(List<Tapa> tapas) {
                        Log.d(TAG, "---> RxFirebaseDatabase.observeSingleValueEvent NEXT");
                        mTapas = tapas;
//                        mAdapter.updateTapas(tapas);
                        for (int i = 0; i < mTapas.size(); i++) {
                            if (mTapas.get(i).getId().equals(tapaId)) {
                                mViewPager.setCurrentItem(i);
                                mAdapter.updateTapas(mTapas);
                                break;
                            }
                        }
                    }
                });

//        mTapas = dao.getTapas(); //new ArrayList<Tapa>(DummyData.getDummyTapasAsHashMap().values());//CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdapter = new TapaPagerAdapter(fragmentManager, mTapas);
        mViewPager.setAdapter(mAdapter);
/*
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Tapa tapa = mTapas.get(position);
                return TapaFragment.newInstance(tapa.getId());
            }

            @Override
            public int getCount() {
                return (mTapas == null ? 0 : mTapas.size());
            }

            public void updateTapas(List<Tapa> tapas) {
                mTapas.clear();
                mTapas.addAll(tapas);
                notifyDataSetChanged();
            }

        });
*/
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Tapa tapa = mTapas.get(position);
                if (tapa.getName() != null) {
                    setTitle(tapa.getName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

//        for (int i = 0; i < mTapas.size(); i++) {
//            if (mTapas.get(i).getId().equals(tapaId)) {
//                mViewPager.setCurrentItem(i);
//                break;
//            }
//        }
    }

}
