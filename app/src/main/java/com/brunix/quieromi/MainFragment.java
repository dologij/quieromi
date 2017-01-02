package com.brunix.quieromi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.RemoteServiceImpl;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapalist.ui.TapaListActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dolo on 9/14/16.
 */
public class MainFragment extends Fragment {

    private final static String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.map_button)
    Button mMapButton;

    @BindView(R.id.firebase_button)
    Button mFirebaseButton;

    @BindView(R.id.quieromi_button)
    Button mQuieroMiButton;

    private Unbinder unbinder;

    @Inject
    RemoteServiceImpl remoteService;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Inject components
        MyApplication.get(getActivity()).getApplicationComponent().inject(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.map_button)
    public void showMap(Button button) {
//        Intent i = MapActivity.newMapIntent(getActivity());
//        startActivity(i);

//        RemoteServiceImpl.RemoteService remoteService = RemoteServiceImpl.getInstance();

        remoteService.getTapa("-KSHBPpDYSzHtjsAUMmv")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Tapa>() {
                    @Override
                    public void call(Tapa tapa) {
//                        final Tapa task = response.body();
                        if (tapa != null) {
                            Log.d(TAG, "Rx-onResponse: tapa found with name: " + tapa.getName());
                        } else {
                            Log.d(TAG, "Rx-onResponse: no tapa found");
                        }

                    }
                });
/*
        Call<Tapa> call = remoteService.getTapa("-KSHBPpDYSzHtjsAUMmv");//"-KSGfRGjLqZ-FFMaIM8S");
        call.enqueue(new Callback<Tapa>() {
            @Override
            public void onResponse(final Call<Tapa> call, final Response<Tapa> response) {
                    final Tapa task = response.body();
                    if (task != null) {
                        Log.d(TAG, "01-onResponse: tapa found with name: " + task.getName());
                    } else {
                        Log.d(TAG, "onResponse: no tapa found");
                    }

            }

            @Override
            public void onFailure(Call<Tapa> call, Throwable t) {
                Log.e(TAG, "onResume: failed to find task", t);
            }
        });
*/
/*
        Call<Map<String, Tapa>> call = remoteService.getAllTapas();
        call.enqueue(new Callback<Map<String, Tapa>>() {
            @Override
            public void onResponse(
                    final Call<Map<String, Tapa>> call,
                    final Response<Map<String, Tapa>> response) {
                final Map<String, Tapa> tasks = response.body();
                if (tasks != null && !tasks.isEmpty()) {
//                    getView().showLoadedItems(tasks);
                    Log.d(TAG, "onResponse: tasks found as map with size: " + tasks.size());
                } else {
                    Log.d(TAG, "onResponse: no tasks found");
                }
            }

            @Override
            public void onFailure(
                    final Call<Map<String, Tapa>> call,
                    final Throwable t) {
//                getView().showErrorLoading();
                Log.e(TAG, "onResume: failed to find task", t);
            }
        });
*/


    }

    @OnClick(R.id.firebase_button)
    public void addFirebaseRecord(Button button) {
        Intent i = FirebaseActivity.newFirebaseIntent(getActivity());
        startActivity(i);
    }

    @OnClick(R.id.quieromi_button)
    public void goToApp(Button button) {
        Intent i = TapaListActivity.newTapaListIntent(getActivity());
        startActivity(i);
    }
}
