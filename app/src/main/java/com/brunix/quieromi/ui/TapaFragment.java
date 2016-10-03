package com.brunix.quieromi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.brunix.quieromi.model.Tapa;

import java.util.UUID;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaFragment extends Fragment {

    private static final String ARG_TAPA_ID = "tapa_id";

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
}
