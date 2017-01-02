package com.brunix.quieromi.data;

import com.brunix.quieromi.data.entity.Tapa;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dolo on 12/30/16.
 */
public interface FirebaseApi {
        @PUT("/tapas/{id}.json")
        Call<Tapa> createTapa(
                // title configured as identifier for bars
                @Path("title") String title,
                @Body Tapa tapa);

        @GET("/tapas/{id}.json")
        Call<Tapa> getTapaById(@Path("id") String id); // could be used for fetching details or checking if item already exists

        @GET("/tapas/{id}.json")
        Observable<Tapa> getTapaByIdRx(@Path("id") String id); // could be used for fetching details or checking if item already exists

        @GET("/tapas/.json")
        Call<Map<String, Tapa>> getTapas(); // note that we'll receive a Map here from firebase with key being the identifier

        @GET("/tapas/.json")
        Observable<Map<String, Tapa>> getTapasRx(); // note that we'll receive a Map here from firebase with key being the identifier

        @DELETE("/tapas/{id}.json")
        Call<Tapa> deleteTapa(@Path("title") String title);
}
