package com.brunix.quieromi.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alterego.advancedandroidlogger.implementations.DetailedAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.brunix.quieromi.BuildConfig;
import com.brunix.quieromi.data.DatabaseHelper;
import com.brunix.quieromi.data.DatabaseHelperImpl;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by dolo on 9/19/16.
 */
public class MyApplication extends MultiDexApplication {

//    public static DetailedAndroidLogger L;

    private ApplicationComponent applicationComponent;

    // TODO: Inject dependency
    private DatabaseHelper databaseHelper;
//    private AuthenticationHelper authenticationHelper;

    protected void createApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
                .builder().applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(this))
                .build();
    }

    /**
     * Static method that can be used by any class that isn't a Context, but
     * does have access to Context object, in order to get the Application object.
     * @param context The current context that the caller has access to.
     * @return The instance of {@link MyApplication}
     */
    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            createApplicationComponent();
        }
        return applicationComponent;
    }

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

//        L = new DetailedAndroidLogger("QUIEROMI", IAndroidLogger.LoggingLevel.DEBUG);

        //AndroidDevMetrics: Use it only in debug builds
        if (BuildConfig.DEBUG_MODE) {
            AndroidDevMetrics.initWith(this);
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        instance = this;
        refWatcher = LeakCanary.install(this);

        if (!FirebaseApp.getApps(this).isEmpty()) {
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            // TODO: this code goes inside the correspondant module (when providing dependencies)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // dangerous, maybe this should not be called
            instance = this;
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            databaseHelper = new DatabaseHelperImpl(firebaseDatabase);
//            authenticationHelper = new AuthenticationHelperImpl(firebaseAuth);
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

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

//    public AuthenticationHelper getAuthenticationHelper() {
//        return authenticationHelper;
//    }

}
