package com.brunix.quieromi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.brunix.quieromi.R;
import com.brunix.quieromi.model.DummyData;
import com.brunix.quieromi.model.Tapa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaPagerActivity extends AppCompatActivity {

    private static final String EXTRA_TAPA_ID =
            "com.brunix.quieromi.ui.tapa_id";

    @BindView(R.id.activity_tapa_pager_view_pager)
    ViewPager mViewPager;
    private List<Tapa> mTapas;

    public static Intent newIntent(Context packageContext, UUID tapaId) {
        Intent intent = new Intent(packageContext, TapaPagerActivity.class);
        intent.putExtra(EXTRA_TAPA_ID, tapaId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapa_pager);
        ButterKnife.bind(this);

        UUID tapaId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_TAPA_ID);

        //mViewPager = (ViewPager) findViewById(R.id.activity_tapa_pager_view_pager);

        mTapas = new ArrayList<Tapa>(DummyData.getDummyTapasAsHashMap().values());//CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Tapa tapa = mTapas.get(position);
                return TapaFragment.newInstance(tapa.getId());
            }

            @Override
            public int getCount() {
                return mTapas.size();
            }
        });

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

        for (int i = 0; i < mTapas.size(); i++) {
            if (mTapas.get(i).getId().equals(tapaId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
