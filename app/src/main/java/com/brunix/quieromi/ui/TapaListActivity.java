package com.brunix.quieromi.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.brunix.quieromi.R;
import com.brunix.quieromi.model.Tapa;

import java.util.List;

/**
 * Created by dolo on 9/20/16.
 */

public class TapaListActivity extends SingleFragmentActivity
        implements TapaListFragment.Callbacks, TapaFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return TapaListFragment.newInstance();
    }

    public static Intent newTapaListIntent(Context packageContext) {
        return new Intent(packageContext, TapaListActivity.class);
    }

    @Override
    public void onTapaSelected(Tapa tapa) {

    }

    @Override
    public void onTapaUpdated(Tapa tapa) {

    }


}
