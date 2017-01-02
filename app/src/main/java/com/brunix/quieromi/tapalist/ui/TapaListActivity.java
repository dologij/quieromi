package com.brunix.quieromi.tapalist.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.base.SingleFragmentActivity;
import com.brunix.quieromi.tapa.ui.TapaFragment;

/**
 * Created by dolo on 9/20/16.
 */

public class TapaListActivity extends SingleFragmentActivity
        implements TapaFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return TapaListFragment.newInstance();
    }

    public static Intent newTapaListIntent(Context packageContext) {
        return new Intent(packageContext, TapaListActivity.class);
    }

//    @Override
//    public void onTapaSelected(Tapa tapa) {
//        Log.d("TapaListActivity", "---> Show selected tapa.");
//        Intent intent = TapaPagerActivity.newIntent(this, tapa.getId());
//        startActivity(intent);
//    }

    @Override
    public void onTapaUpdated(Tapa tapa) {

    }


}
