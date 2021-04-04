package com.example.bettertrialbook.experiment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.models.ExperimentInfo;
import com.example.bettertrialbook.models.Geolocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class GeolocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    Button cancelButton, selectButton;
    Marker marker = null;

    // Boolean isTrialOwner;
    Geolocation geolocation;
    Boolean allGeoLocations;
    ArrayList<Geolocation> geoLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        // Fetch extra info
        // isTrialOwner = getIntent().getBooleanExtra("IsTrialOwner", true);
        geolocation = getIntent().getParcelableExtra("geolocation");
        allGeoLocations = getIntent().getBooleanExtra("allLocations", false);  // if true, display all the trial locations
        Log.d("Geolocation", String.valueOf(allGeoLocations));
        geoLocations = getIntent().getParcelableArrayListExtra("geoLocations");

        // Button setup
        cancelButton = findViewById(R.id.mapCancel_button);
        selectButton = findViewById(R.id.mapSelect_button);
        
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        
        if (allGeoLocations) {
            // change button layout so users can't add locations
            cancelButton.setText("Back");
            selectButton.setVisibility(View.INVISIBLE);
        }

//        if (isTrialOwner) {
//            selectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
//        }
//        else {
//            selectButton.setAlpha(.5f);
//        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Geolocation", "Granted1");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                getLocation();
            }
        }
        else {
            Log.d("Geolocation", "Denied1");
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE_ASK_PERMISSIONS) {
            return;
        }

        boolean granted = false;

        for (int i = 0; i < grantResults.length; i++) {
            Log.d("Geolocation", permissions[i]);
            Log.d("Geolocation", String.valueOf(grantResults[i]));
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i])) {
                granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                break;
            }
        }

        if (granted) {
            // Enable the my location layer if the permission has been granted.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("Geolocation", "Granted2");
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                    getLocation();
                }
            } else {
                Log.d("Geolocation", "Denied2");
                // Permission to access the location is missing. Show rationale and request permission
                // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            Log.d("Geolocation", "Permission Denied.");
        }
    }

    public void getLocation() {
        if (!allGeoLocations) {
            // selecting a location
            if (geolocation.getLocation() == null) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    // set the marker to the current location
                                    geolocation.setLocation(location);
                                    sendLocation(geolocation);
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Your Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                                } else {
                                    Log.d("Geolocation", "null location");
                                }
                            }
                        });
            } else {
                // otherwise use the current geolocation
                sendLocation(geolocation);
                LatLng currentLocation = new LatLng(geolocation.getLocation().getLatitude(), geolocation.getLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    if (marker != null) {
                        marker.remove();
                    }

                    if (point != null) {
                        // add marker to tapped location
                        marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker in Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        Location newLocation = new Location("");
                        newLocation.setLatitude(point.latitude);
                        newLocation.setLongitude(point.longitude);
                        geolocation.setLocation(newLocation);
                        sendLocation(geolocation);
                        Log.d("Geolocation", String.valueOf(geolocation.getLocation().getLatitude()));
                        Log.d("Geolocation", String.valueOf(geolocation.getLocation().getLongitude()));
                        Log.d("Geolocation", "selected location");
                    } else {
                        Log.d("Geolocation", "null selected location");
                    }
                }
            });
        } else {
            // show all locations
            Log.d("Geolocation", "size: " + geoLocations.size());
            for (int i = 0; i < geoLocations.size(); i++) {
                Log.d("Geolocation", String.valueOf(i));
                if (geoLocations.get(i).getLocation() != null) {
                    Log.d("Geolocation", geoLocations.get(i).getLocation().getLatitude() + " " + geoLocations.get(i).getLocation().getLongitude());
                    // get the lat and lon of each geolocation passed to the map activity
                    LatLng currentLocation = new LatLng(geoLocations.get(i).getLocation().getLatitude(), geoLocations.get(i).getLocation().getLongitude());
                    if (currentLocation != null) {
                        // place a marker on each location
                        marker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                        Log.d("Geolocation", "selected location");
                    } else {
                        Log.d("Geolocation", "null selected location2");
                    }
                } else {
                    Log.d("Geolocation", "null selected location1");
                }
            }

        }

//        if (isTrialOwner) {
//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng point) {
//                    if (marker != null) {
//                        marker.remove();
//                    }
//
//                    if (point != null) {
//                        marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker in Selected Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//                        Location newLocation = new Location("");
//                        newLocation.setLatitude(point.latitude);
//                        newLocation.setLongitude(point.longitude);
//                        geolocation.setLocation(newLocation);
//                        sendLocation(geolocation);
//                        Log.d("Geolocation", String.valueOf(geolocation.getLocation().getLatitude()));
//                        Log.d("Geolocation", String.valueOf(geolocation.getLocation().getLongitude()));
//                        Log.d("Geolocation", "selected location");
//                    } else {
//                        Log.d("Geolocation", "null selected location");
//                    }
//                }
//            });
//        }
    }

    /*
    Communicates with the add trial fragments, sending the selected geolocation to the fragment
     */
    public void sendLocation(Geolocation geolocation) {
        Log.d("geolocation", "sent location");
        Intent intent = new Intent();
        intent.putExtra("geolocation", geolocation);

        setResult(Activity.RESULT_OK, intent);
    }
}