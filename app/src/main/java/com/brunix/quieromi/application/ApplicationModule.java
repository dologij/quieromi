package com.brunix.quieromi.application;

import android.content.Context;

import com.brunix.quieromi.data.Dao;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dolo on 10/6/16.
 */

@Module
public class ApplicationModule {

    private Context applicationContext;

    public ApplicationModule(Context context) {
        this.applicationContext = context;
    }

    @Provides
    @Singleton
    public Dao provideDao() { return new Dao(FirebaseDatabase.getInstance().getReference()); }

}
