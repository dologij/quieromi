package com.brunix.quieromi.data;

import com.brunix.quieromi.data.entity.Tapa;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by dolo on 12/19/16.
 */

public class RemoteServiceImpl implements TapasRepo {

    private final FirebaseApi firebaseApi;

    private static Retrofit retrofit;

    //    = new Retrofit.Builder()
//            .baseUrl(BuildConfig.REMOTE_SERVICE_URL)
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // in case we want to return Observable<T> in any method of the interface
//            .addConverterFactory(GsonConverterFactory.create()) // use gson converter
//            .build();

//    private static FirebaseApi service = null;

//    @Inject
    public RemoteServiceImpl(Retrofit retrofit) {
        firebaseApi = retrofit.create(FirebaseApi.class);
    }

//    public static FirebaseApi getInstance() {
//        if (service == null) {
//            service = retrofit.create(FirebaseApi.class);
//        }
//        return service;
//    }

    @Inject
    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    @Override
    public Observable<Tapa> getTapa(String tapaId) {
        return firebaseApi.getTapaByIdRx(tapaId);

    }

    @Override
    public Observable<List<Tapa>> getAllTapas() {
        return firebaseApi.getTapasRx()
                .flatMap(new Func1<Map<String, Tapa>, Observable<Map.Entry<String, Tapa>>>() {
                    @Override
                    public Observable<Map.Entry<String, Tapa>> call(Map<String, Tapa> stringTapaMap) {
                        return Observable.from(stringTapaMap.entrySet());
                    }
                })
                .map(new Func1<Map.Entry<String, Tapa>, Tapa>() {
                    @Override
                    public Tapa call(Map.Entry<String, Tapa> stringTapaEntry) {
                        Tapa tapa = stringTapaEntry.getValue();
                        tapa.setId(stringTapaEntry.getKey());
                        return tapa;
                    }
                }).toList();
    }

}
