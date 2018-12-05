package com.example.cynthia.kasa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends AppCompatActivity {
    private final  String TAG = "PlaceAutoComplete";
    private static LatLng home;
    private String[] currentLocationArray;
    private double longitude;
    private double latitude;
    private LatLng currentLocation;
    private Button button;




    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    /**
     * Checks to see whether the user said yes.
     * If the user said yes, then we use the location manager and listener to give us updates on the location.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
        }
    }


    /** The location manager manages all location tracking.
     * The location listener updates us on when a user says yes or no to things and when the device moves.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });



        // This is the location manager. It gets the user's location.
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // This is the location listener.
        LocationListener locationListener = new LocationListener() {
            @Override
            // Regular updates from the gps on changes; ex: how much the device has moved.
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                currentLocationArray = location.toString().trim().split(",");
                try {
                    latitude = Double.parseDouble(currentLocationArray[0]);
                } catch (NumberFormatException ex){
                    return;
                }
                try {
                    longitude = Double.parseDouble(currentLocationArray[1]);
                } catch (NumberFormatException ex) {
                    return;
                }
                currentLocation = new LatLng(longitude, latitude);
            }

            @Override
            // Checks whether or not te user has given permission and if the app is working.
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /** This asks the user for permission.
         * If the permission is not granted, we ask for permission.
         * @param requestCode keeps track of requests
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                home = place.getLatLng();
                if (!(home.equals(currentLocation))) {
                    System.out.println("It worked!!");
                }
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG,"An error occurred: " + status);

            }
        });
    }


    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

}
