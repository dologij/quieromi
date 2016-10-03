package com.brunix.quieromi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.brunix.quieromi.ui.TapaListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.map_button)
    public void showMap(Button button) {
        Intent i = MapActivity.newMapIntent(getActivity());
        startActivity(i);
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
