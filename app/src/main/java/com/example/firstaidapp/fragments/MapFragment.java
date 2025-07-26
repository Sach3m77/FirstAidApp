package com.example.firstaidapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.firstaidapp.MainActivity;
import com.example.firstaidapp.helpers.CustomClusterRenderer;
import com.example.firstaidapp.adapters.CustomInfoWindowAdapter;
import com.example.firstaidapp.models.MyItem;
import com.example.firstaidapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPoint;

import org.json.JSONException;

import java.io.IOException;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, ClusterManager.OnClusterClickListener<MyItem> {
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private static final float MAX_ZOOM_LEVEL = 18.0f;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private ImageButton mGpsButton;
    private LocationManager mLocationManager;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;
    private SharedPreferences mSharedPreferences;
    private static final String PREF_LAST_KNOWN_LATITUDE = "last_known_latitude";
    private static final String PREF_LAST_KNOWN_LONGITUDE = "last_known_longitude";
    double mLastKnownLatitude;
    double mLastKnownLongitude;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    checkLocationPermissionAndEnableLocation();
                } else {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(requireContext(), "Odmówiono uprawnienia", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGpsButton = view.findViewById(R.id.gps_button);
        mLocationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mSharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE);

        mLastKnownLatitude = mSharedPreferences.getFloat(PREF_LAST_KNOWN_LATITUDE, 0.0f);
        mLastKnownLongitude = mSharedPreferences.getFloat(PREF_LAST_KNOWN_LONGITUDE, 0.0f);

        if (isGooglePlayServicesAvailable()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        } else {
            Toast.makeText(requireContext(), "Usługi Google Play Service są niedostępne na tym urządzeniu", Toast.LENGTH_SHORT).show();
        }

        mGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermissionAndEnableLocation();
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() != null) {
                    mGpsButton.setImageResource(R.drawable.ic_crosshairs_gps);
                    LatLng lastLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    saveLastKnownLocation(lastLocation);
                    zoomToLocation(lastLocation);
                }
            }
        };

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        checkLocationPermissionAndGetLastKnowLocation();
        getGeoJsonLayer();
        removeLocationRequestListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onCameraIdle() {
        if (mMap.getCameraPosition().zoom <= MAX_ZOOM_LEVEL) {
            mClusterManager.cluster();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(requireContext());
        return result == ConnectionResult.SUCCESS;
    }

    private void checkLocationPermissionAndEnableLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (!isGpsOn()) {
                showGpsOnSettins();
            }
            mFusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), mLocationCallback, Looper.getMainLooper());

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void checkLocationPermissionAndGetLastKnowLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {

                mGpsButton.setImageResource(R.drawable.ic_crosshairs_gps);
                zoomToLocation(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));

            } else if (mLastKnownLatitude != 0.0 && mLastKnownLongitude != 0.0) {

                mGpsButton.setImageResource(R.drawable.ic_crosshairs_question);
                LatLng lastKnownLocation = new LatLng(mLastKnownLatitude, mLastKnownLongitude);
                zoomToLocation(lastKnownLocation);

            }
        }

    }

    private void        removeLocationRequestListener() {
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                }
            }
        });
    }
    private boolean isGpsOn() {
        return (mLocationManager != null && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    private LocationRequest getLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(50);
        locationRequest.setSmallestDisplacement(5);

        return locationRequest;
    }

    private void showGpsOnSettins() {
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();
        locationSettingsRequestBuilder.addLocationRequest(getLocationRequest());
        locationSettingsRequestBuilder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(locationSettingsRequestBuilder.build());

        task.addOnSuccessListener(requireActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

            }
        });

        task.addOnFailureListener(requireActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(requireActivity(), REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void zoomToLocation(LatLng latLng) {
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAX_ZOOM_LEVEL));
    }

    private void getGeoJsonLayer() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.aed_poland, requireContext());

            mClusterManager = new ClusterManager<>(requireContext(), mMap);
            CustomClusterRenderer<MyItem> customClusterRenderer = new CustomClusterRenderer<>(requireContext(), mMap, mClusterManager);
            mClusterManager.setRenderer(customClusterRenderer);
            mClusterManager.setAnimation(false);
            mMap.setOnCameraIdleListener(this);

            mClusterManager.setOnClusterClickListener(this);

            CustomInfoWindowAdapter infoWindowAdapter = new CustomInfoWindowAdapter(LayoutInflater.from(requireContext()));
            infoWindowAdapter.mapGeoJsonLayer(layer);

            addItemsToCluster(layer);
            mClusterManager.getMarkerCollection().setInfoWindowAdapter(infoWindowAdapter);
            mMap.setInfoWindowAdapter(infoWindowAdapter);

            mClusterManager.cluster();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cluster.getPosition(), mMap.getCameraPosition().zoom + 1));
        return true;
    }

    private void addItemsToCluster(GeoJsonLayer layer) {
        for (GeoJsonFeature feature : layer.getFeatures()) {
            GeoJsonPoint point = (GeoJsonPoint) feature.getGeometry();
            MyItem offsetItem = new MyItem(point.getCoordinates().latitude,
                    point.getCoordinates().longitude,
                    feature.getProperty("osm_id"),
                    feature.getProperty("defibrillator:location"));
            mClusterManager.addItem(offsetItem);
        }
    }

    private void saveLastKnownLocation(LatLng location) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(PREF_LAST_KNOWN_LATITUDE, (float) location.latitude);
        editor.putFloat(PREF_LAST_KNOWN_LONGITUDE, (float) location.longitude);
        editor.apply();
    }

}
