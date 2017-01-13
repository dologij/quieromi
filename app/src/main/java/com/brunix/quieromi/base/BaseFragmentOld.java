package com.brunix.quieromi.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dolo on 12/27/16.
 */

public abstract class BaseFragmentOld<P extends BasePresenterOld> extends Fragment implements BaseViewOld {

    private final static String TAG = BaseFragmentOld.class.getSimpleName();

    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(getLayoutResId());
//        ButterKnife.bind(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        // Inject components in every child class. You can only inject here the dependencies
        // inside this base class.
        //MyApplication.get(getActivity()).getApplicationComponent().inject(this);
    }

    // TODO instance state saving and recovery would go here

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().attach(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().detach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

//        mAdapter.cleanup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        if (context instanceof Activity){
            Log.d(TAG, "--> onAttach(Contex): context IS an Activity");
            activity = (Activity) context;

//            if(activity instanceof Callbacks) {
//                mCallbacks = (Callbacks) activity;
//            }

        } else {
            Log.d(TAG, "--> onAttach(Contex): context IS NOT an Activity");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mCallbacks = null;
    }

    /**
     * implement to specify layout view
     *
     * @return
     */
//    protected abstract
//    @LayoutRes
//    int getLayoutResId();

    /**
     * implement to specify presenter instance
     *
     * @return
     */
    protected abstract P getPresenter();

    /**
     * Should always return a non null view that can be used as a fallback when current focus view is not set
     *
     * @return
     */
    protected abstract
    @NonNull
    View getFallbackView();

    /**
     * helper to always return a valid view even if non in focus
     */
    protected
    @NonNull
    View getCurrentView() {
        return getActivity().getCurrentFocus() != null ? getActivity().getCurrentFocus() : getFallbackView();
    }

    @Override
    public void showMessage(
            @StringRes final int messageResourceId,
            final MessageCallback callback) {

        Snackbar.make(getCurrentView(), messageResourceId, Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(
                    final Snackbar snackbar,
                    final int event) {
                super.onDismissed(snackbar, event);
                if (callback != null) {
                    callback.done();
                }
            }

            @Override
            public void onShown(final Snackbar snackbar) {
                super.onShown(snackbar);
            }
        }).show();
    }
}
