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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class Activity2 extends AppCompatActivity {

    private Button editButton;
    private static String[] currentLocationArray;
    private static String[][] temp = new String[2][2];



    /*** */
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        // This is the location manager. It gets the user's location.
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // This is the location listener.
        locationListener = new LocationListener() {
            @Override
            // Regular updates from the gps on changes; ex: how much the device has moved.
            public void onLocationChanged(Location location) {
                Log.i("Location", location.toString());
                currentLocationArray = location.toString().trim().split(",");
                temp[0] = currentLocationArray[0].split("\\[")[1].trim().split("\\.");
                temp[1] = currentLocationArray[1].split("]")[0].trim().split("\\.");
                System.out.println("current location array: " + currentLocationArray[0] + currentLocationArray[1]);
                currentLocationArray[0] = currentLocationArray[0].split(" ")[1].split("\\.")[0].trim() + "." + temp[0][1].trim().charAt(0) + temp[0][1].trim().charAt(1) + temp[0][1].trim().charAt(2) + temp[0][1].trim().charAt(3);
                currentLocationArray[1] = currentLocationArray[1].split("\\.")[0].trim() + "." + temp[1][1].trim().charAt(0) + temp[1][1].trim().charAt(1) + temp[1][1].trim().charAt(2) + temp[1][1].trim().charAt(3);
                System.out.println("current location array: " + currentLocationArray[0] + " " + currentLocationArray[1]);
                System.out.println("home: " + MainActivity.getHome2()[0] + " " + MainActivity.getHome2()[1]);
                if (Arrays.equals(currentLocationArray, MainActivity.getHome2())) {
                    sendSMS(MainActivity.getNumber(), "Your friend has reached their destination.");
                    System.out.println("SMS Sent");
                    MainActivity.home2[0] = "0";
                    MainActivity.home2[1] = "0";
                }
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
        }


    }
    public void openMainActivity() {
        Intent intent2 = new Intent(this, MainActivity.class);
        startActivity(intent2);
    }
    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("smsto:1" + phoneNumber, null, message, null, null);
    }
}
