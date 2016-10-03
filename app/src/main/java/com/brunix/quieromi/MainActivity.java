package com.brunix.quieromi;

import android.support.v4.app.Fragment;
import com.brunix.quieromi.ui.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance();
    }
}
