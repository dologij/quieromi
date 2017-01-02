package com.brunix.quieromi.data;

import com.brunix.quieromi.data.entity.Tapa;

import java.util.Map;

import rx.Observable;

/**
 * Created by dolo on 12/30/16.
 */
public interface TapasRepo {
    Observable<Tapa> getTapa (final String tapaId);

    Observable<Map<String, Tapa>> getAllTapas ();
}
