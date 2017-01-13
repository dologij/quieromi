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

    private List<Tapa> tapas = new ArrayList<>();

    public TapaPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TapaPagerAdapter(FragmentManager fm, List<Tapa> tapas) {
        super(fm);
        this.tapas = tapas;
    }

    @Override
    public Fragment getItem(int position) {
        Tapa tapa = tapas.get(position);
        return TapaFragment.newInstance(tapa.getId());
    }

    @Override
    public int getCount() {
        return (tapas == null ? 0 : tapas.size());
    }

    public void clearTapas() {
        if (tapas != null) {
            tapas.clear();
            notifyDataSetChanged();
        }
    }

    public void addNewTapa(Tapa tapa) {
        if(tapas == null) {
            tapas = new ArrayList<>();
        } else {
            tapas.add(tapa);
            notifyDataSetChanged();
        }
    }

    public void updateTapas(List<Tapa> pTapas) {
        if(tapas == null) {
            tapas = new ArrayList<>();
        } else {
            tapas.clear();
        }
//        tapas.clear();
        if (pTapas != null) {
            tapas.addAll(pTapas);
        }
        notifyDataSetChanged();
    }

    public int getPositionFromId(String id) {
        int pos = -1;
        for (Tapa tapa : tapas) {
            if (tapa.getId().equals(id)) {
                pos = tapas.indexOf(tapa);
            }
        }
        return pos;
    }

}
