package com.brunix.quieromi.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by dolo on 9/19/16.
 */
public class MyApplication extends MultiDexApplication {

    // LeakCanary
    private RefWatcher refWatcher;
    public static MyApplication instance;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        instance = this;
        refWatcher = LeakCanary.install(this);

        if (!FirebaseApp.getApps(this).isEmpty()) {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

    }

    /**
     * Method to watch for memory leaks with LeakCanary.
     *
     * Usually, we would watch for leaks with:
     *   RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
     *   refWatcher.watch(suspect);
     * in the Activity/Fragment. But with this method, we only need to do:
     *   MyApplication.instance.mustDie(suspect);
     *
     * @param object
     */
    public void mustDie(Object object) {
        if (refWatcher != null)
            refWatcher.watch(object);
    }
}
