package com.brunix.quieromi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.brunix.quieromi.base.SingleFragmentActivity;

/**
 * Created by dolo on 9/14/16.
 */
public class FirebaseActivity extends SingleFragmentActivity {

    private final static String TAG = FirebaseActivity.class.getSimpleName();


    @Override
    protected Fragment createFragment() {
        return FirebaseFragment.newInstance();
    }

    public static Intent newFirebaseIntent(Context packageContext) {
        return new Intent(packageContext, FirebaseActivity.class);
    }

}