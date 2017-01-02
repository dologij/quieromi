package com.brunix.quieromi.tapa.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brunix.quieromi.R;
import com.brunix.quieromi.data.entity.Tapa;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaFragment extends Fragment {

    private static final String ARG_TAPA_ID = "tapa_id";

    private Tapa mTapa;

    private String tapaId;
    private EditText mTitleField;

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
        View v = inflater.inflate(R.layout.fragment_tapa, container, false);

        mTitleField = (EditText) v.findViewById(R.id.tapa_id);
        mTitleField.setText(tapaId);

        return v;
    }

}
