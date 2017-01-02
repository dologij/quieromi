package com.brunix.quieromi.application;

import com.brunix.quieromi.MainFragment;
import com.brunix.quieromi.base.BaseFragment;
import com.brunix.quieromi.base.BasePresenter;
import com.brunix.quieromi.data.RemoteServiceImpl;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenter;
import com.brunix.quieromi.tapalist.presenter.TapaListPresenterImpl;
import com.brunix.quieromi.tapalist.ui.TapaListFragment;
import com.brunix.quieromi.tapa.ui.TapaPagerActivity;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by dolo on 10/6/16.
 */

@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    //void inject(BaseFragment<TapaListPresenterImpl> fragment);

    void inject(TapaListFragment tapaListFragment);

    void inject(TapaPagerActivity tapaPagerActivity);

    void inject(MainFragment mainFragment);

//    void inject(RemoteServiceImpl remoteService);

//    Retrofit retrofit();
}
