package com.brunix.quieromi.tapa.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.brunix.quieromi.data.entity.Tapa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dolo on 10/21/16.
 */

public class TapaPagerAdapter extends FragmentStatePagerAdapter {

    private List<Tapa> mTapas = new ArrayList<>();

    public TapaPagerAdapter(FragmentManager fm, List<Tapa> mTapas) {
        super(fm);
        this.mTapas = mTapas;
    }

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
        if(mTapas == null) {
            mTapas = new ArrayList<>();
        } else {
            mTapas.clear();
        }
        mTapas.addAll(tapas);
        notifyDataSetChanged();
    }


}
