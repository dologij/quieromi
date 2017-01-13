package com.brunix.quieromi.tapa.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brunix.quieromi.R;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapa.presenter.TapaPresenterImpl;
import com.brunix.quieromi.tapa.view.TapaView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaFragment extends Fragment implements TapaView {

    private static final String ARG_TAPA_ID = "tapa_id";

    private Tapa mTapa;

    private String tapaId;

    @BindView(R.id.tapa_id)
    EditText titleField;

    private TapaPresenterImpl presenter;

    private Unbinder unbinder;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTapaUpdated(Tapa tapa);
    }

    public static TapaFragment newInstance(String tapaId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAPA_ID, tapaId);

        TapaFragment fragment = new TapaFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tapaId = (String) getArguments().getSerializable(ARG_TAPA_ID);
//        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
//        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tapa, container, false);

        //titleField = (EditText) v.findViewById(R.id.tapa_id);
        //titleField.setText(tapaId);

        //return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        // Inject components
        MyApplication.get(getActivity()).getApplicationComponent().inject(this);

        initPresenter();
//        initAdapter();
        initUI();

        Tapa tapa = new Tapa();
        tapa.setId("TAPAID");
        tapa.setName("TAPANAME");
        refreshData(tapa);
    }

    private void initPresenter() {
        presenter = new TapaPresenterImpl(MyApplication.get(getActivity()).getDatabaseHelper()); //, MyApplication.get(getActivity()).getAuthenticationHelper());
        presenter.setView(this);
    }

    private void initUI() {
        titleField.setText(tapaId);
        presenter.requestTapaFromNetwork(tapaId);
    }

    @Override
    public void refreshData(Tapa tapa) {
        titleField.setText(tapa.getId() + "--" + tapa.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

}
