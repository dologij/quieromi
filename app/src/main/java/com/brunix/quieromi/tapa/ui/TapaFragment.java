package com.brunix.quieromi.tapa.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brunix.quieromi.R;
import com.brunix.quieromi.application.MyApplication;
import com.brunix.quieromi.data.entity.Tapa;
import com.brunix.quieromi.tapa.presenter.TapaPresenterImpl;
import com.brunix.quieromi.tapa.view.TapaView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaFragment extends Fragment implements TapaView, OnMapReadyCallback {

    private final static String TAG = TapaFragment.class.getSimpleName();

    private static final String ARG_TAPA_ID = "tapa_id";

    private Tapa tapa;

    private String tapaId;

    @BindView(R.id.tapa_id)
    EditText idField;

    @BindView(R.id.tapa_name)
    EditText nameField;

    @BindView(R.id.tapa_price)
    EditText priceField;

    @BindView(R.id.tapa_mapview)
    MapView tapaMapView;


    private TapaPresenterImpl presenter;

    private Unbinder unbinder;
    private GoogleMap googleMap;


    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onTapaUpdated(Tapa tapa);
    }

    public static TapaFragment newInstance(String tapaId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAPA_ID, tapaId);

        TapaFragment fragment = new TapaFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tapaId = (String) getArguments().getSerializable(ARG_TAPA_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tapa, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        // Inject components
        MyApplication.get(getActivity()).getApplicationComponent().inject(this);

        initPresenter();
//        initAdapter();
        initUI(savedInstanceState);

//        Tapa tapa = new Tapa();
//        tapa.setId("TAPAID");
//        tapa.setName("TAPANAME");
//        refreshData(tapa);
    }

    private void initPresenter() {
        presenter = new TapaPresenterImpl(MyApplication.get(getActivity()).getDatabaseHelper()); //, MyApplication.get(getActivity()).getAuthenticationHelper());
        presenter.setView(this);
    }

    private void initUI(@Nullable Bundle savedInstanceState) {
//        idField.setText(tapaId);

        // Gets the MapView from the XML layout and creates it
        tapaMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        tapaMapView.getMapAsync(this);

        presenter.requestTapaFromNetwork(tapaId);
    }

    @Override
    public void refreshData(Tapa tapa) {
        this.tapa = tapa;
        refreshUI();
    }

    private void refreshUI() {
        if (tapa != null && googleMap != null && isAdded()) {

            idField.setText(tapa.getId());
            nameField.setText(tapa.getName());
            Double price = tapa.getPrice();
            Resources resources = this.getResources();
            String priceStr = String.format(resources.getString(R.string.price), String.valueOf(price));
            priceField.setText(priceStr);

            double longitud = tapa.getLongitude();
            double latitud = tapa.getLatitude();

            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(true);


            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            MapsInitializer.initialize(this.getActivity());

            String snippet = priceStr;

            drawMarker(longitud, latitud, tapa.getName(), snippet);
        }
    }

    private void drawMarker(double longitude, double latitude, String name, String snippet) {
        if (googleMap != null) {
            googleMap.clear();
            LatLng gps = new LatLng(latitude, longitude);

            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(gps);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            // Updates the location and zoom of the MapView
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
/*
            CameraUpdate position = CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(gps, 15));
            googleMap.animateCamera(position);
*/
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 15));
            googleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title(name)
                    .snippet(snippet))
                    .showInfoWindow();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        if (tapaMapView != null) {
            tapaMapView.onDestroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tapaMapView != null) {
            tapaMapView.onResume();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (tapaMapView != null) {
            tapaMapView.onLowMemory();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tapaMapView != null) {
            tapaMapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tapaMapView != null) {
            tapaMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "--> onMapReady");
        this.googleMap = googleMap;
        refreshData(tapa);
    }
}
