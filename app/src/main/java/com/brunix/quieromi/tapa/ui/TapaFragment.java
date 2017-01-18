package com.brunix.quieromi.tapa.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.brunix.quieromi.PictureUtils;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dolo on 9/21/16.
 */

public class TapaFragment extends Fragment implements TapaView, OnMapReadyCallback {

    private final static String TAG = TapaFragment.class.getSimpleName();

    private static final String ARG_TAPA_ID = "tapa_id";
    private static final int REQUEST_PHOTO= 2;

    private Tapa tapa;

    private String tapaId;

    @BindView(R.id.tapa_id)
    EditText idField;

    @BindView(R.id.tapa_photo)
    ImageView photoField;

    @BindView(R.id.camera_btn)
    ImageButton photoButton;

    @BindView(R.id.tapa_name)
    EditText nameField;

    @BindView(R.id.tapa_price)
    EditText priceField;

    @BindView(R.id.tapa_mapview)
    MapView tapaMapView;

    @BindView(R.id.edit_btn)
    FloatingActionButton fab;

    private File photoFile;
    Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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

    }

    private void initPresenter() {
        presenter = new TapaPresenterImpl(MyApplication.get(getActivity()).getDatabaseHelper()); //, MyApplication.get(getActivity()).getAuthenticationHelper());
        presenter.setView(this);
    }

    private void initUI(@Nullable Bundle savedInstanceState) {

        // Gets the MapView from the XML layout and creates it
        tapaMapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        tapaMapView.getMapAsync(this);

        presenter.requestTapaFromNetwork(tapaId);
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoField.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.get(getActivity()).getScaledBitmap(
                    photoFile.getPath(), getActivity());
            photoField.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    @Override
    public void refreshData(Tapa tapa) {
        this.tapa = tapa;
        refreshUI();
    }


    Uri uri;


    private void refreshUI() {
        if (tapa != null && googleMap != null && isAdded()) {

            idField.setText(tapa.getId());
            nameField.setText(tapa.getName());
            Double price = tapa.getPrice();
            Resources resources = this.getResources();
            String priceStr = String.format(resources.getString(R.string.price), String.valueOf(price));
            priceField.setText(priceStr);
            photoFile = PictureUtils.get(getActivity()).getPhotoFile(tapa.getPhotoFilename());

            boolean canTakePhoto = photoFile != null &&
                    captureImage.resolveActivity(getActivity().getPackageManager()) != null;
            photoButton.setEnabled(canTakePhoto);

            if (canTakePhoto) {

                 // For targetSdkVersion >= 24, file:// schema Uris are not allowed in Intents. If that
                 // is the case, a FileProvider must be used to get a content:// schema Uri.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // content: Uri
                    uri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", photoFile);
                    //uri = FileProvider.getUriForFile(getActivity(), Build.ID + ".provider", photoFile);
                } else {
                    // file: Uri
                    uri = Uri.fromFile(photoFile);
                }
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            updatePhotoView();

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
        PictureUtils.get(getActivity()).clear();
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

    @OnClick(R.id.camera_btn)
    public void onCameraBtnClicked(ImageButton button) {
        startActivityForResult(captureImage, REQUEST_PHOTO);
    }

    @OnClick(R.id.edit_btn)
    public void onEditBtnClicked(ImageButton button) {
        refreshTapaFromUI();
        presenter.saveTapaOnNetwork(tapa);
    }

    private void refreshTapaFromUI() {
        tapa.setName(nameField.getText().toString());

        //tapa.setPrice(Double.valueOf(priceField.getText().toString()));
        tapa.setPrice(new Double("12.34"));

        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://quieromi-10873.appspot.com");
        //StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

        StorageReference riversRef = storageRef.child("images/"+tapa.getPhotoFilename());
        UploadTask uploadTask = riversRef.putFile(uri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }

}
