package com.brunix.quieromi.application;

import com.brunix.quieromi.MainFragment;
import com.brunix.quieromi.tapa.ui.TapaFragment;
import com.brunix.quieromi.tapalist.ui.TapaListFragment;
import com.brunix.quieromi.tapa.ui.TapaPagerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dolo on 10/6/16.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    //void inject(BaseFragmentOld<TapaListPresenterImpl> fragment);

    void inject(TapaListFragment tapaListFragment);

    void inject(TapaPagerActivity tapaPagerActivity);

    void inject(MainFragment mainFragment);

    void inject(TapaFragment tapaFragment);

//    void inject(RemoteServiceImpl remoteService);

//    Retrofit retrofit();
}
