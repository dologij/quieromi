package com.brunix.quieromi.ui;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dolo on 9/19/16.
 */
public class MyApplication extends Application {

    // TODO: añadir leakcanary

    @Override
    public void onCreate() {
        super.onCreate();
        if (!FirebaseApp.getApps(this).isEmpty()) {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }
}
