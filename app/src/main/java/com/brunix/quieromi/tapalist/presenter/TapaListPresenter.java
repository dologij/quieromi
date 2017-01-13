package com.brunix.quieromi.tapalist.presenter;

import com.brunix.quieromi.base.Presenter;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapalist.view.TapaListView;

/**
 * Created by dolo on 12/27/16.
 */
public interface TapaListPresenter extends Presenter<TapaListView> {

/*
    interface View extends BaseViewOld {
    }
*/
    void requestTapasFromNetwork(int viewId);

    void removeTapaCallbacks();

    void sendTapaToNetwork(Tapa tapa);

    void onDestroy();

}
