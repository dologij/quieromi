package com.brunix.quieromi.tapalist.presenter;

import android.support.annotation.StringRes;
import android.util.Log;

import com.brunix.quieromi.base.BasePresenter;

/**
 * Created by dolo on 12/27/16.
 */

public class TapaListPresenterImpl extends BasePresenter<TapaListPresenter.View> implements TapaListPresenter {

    private final static String TAG = TapaListPresenterImpl.class.getSimpleName();

    public TapaListPresenterImpl() {
    }

    @Override
    protected View getDummyView() {
        return new TapaListPresenter.View() {
            @Override
            public void showMessage(@StringRes int messageResourceId, MessageCallback callback) {
                    Log.d(TAG, "showMessage: not attached");
            }
        };
    }
}
