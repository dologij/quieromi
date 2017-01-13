package com.brunix.quieromi.tapalist.view;

import com.brunix.quieromi.base.BaseView;
import com.brunix.quieromi.data.entity.Tapa;

/**
 * Created by dolo on 1/4/17.
 */

public interface TapaListView extends BaseView {

    void sendNewTapaToAdapter(Tapa tapa);

    void showErrorMessage();

    void removeCallbacksFromFirebase();

}
