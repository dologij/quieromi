package com.brunix.quieromi.tapa.presenter;

import com.brunix.quieromi.base.Presenter;
import com.brunix.quieromi.tapa.view.TapaView;

/**
 * Created by dolo on 1/9/17.
 */

public interface TapaPresenter extends Presenter<TapaView> {

    void requestTapaFromNetwork(String key);

    void onDestroy();
}
