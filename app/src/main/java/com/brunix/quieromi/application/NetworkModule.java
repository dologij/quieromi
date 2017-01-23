package com.brunix.quieromi.application;

import android.content.Context;

import com.brunix.quieromi.BuildConfig;
import com.brunix.quieromi.data.RemoteServiceImpl;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dolo on 12/30/16.
 */

@Module
public class NetworkModule {

    private Context applicationContext;

    public NetworkModule(Context context) {
        this.applicationContext = context;
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGson() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory provideRxCallAdapter() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public Picasso providePicasso() {
        Picasso.Builder builder = new Picasso.Builder(applicationContext);
        builder.downloader(new OkHttpDownloader(applicationContext, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

        return Picasso.with(applicationContext);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.REMOTE_SERVICE_URL)
                .addCallAdapterFactory(rxJavaCallAdapterFactory) // in case we want to return Observable<T> in any method of the interface
                .addConverterFactory(gsonConverterFactory) // use gson converter
                .build();
    }

    @Provides
    @Singleton
    public RemoteServiceImpl provideRemoteService(Retrofit retrofit) {
        return new RemoteServiceImpl(retrofit);
    }
}