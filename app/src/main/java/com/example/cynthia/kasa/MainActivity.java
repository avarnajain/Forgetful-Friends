package com.example.cynthia.kasa;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private final  String TAG = "MainActivity";
    private static double[] home = new double[2];
    private static String[] currentLocationArray;
    private final int REQUEST_CODE=99;
    private String[][] temp;
    private String[] temp2 = new String[2];
    public static String[] home2 = new String[2];



    public static String[] getHome2() {
        return home2;
    }

    public static String[] getCurrentLocationArray() {
        return currentLocationArray;
    }

    private String name;
    private String id;
    public static String number;

    public static String getNumber() {
        return number;
    }

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 5, locationListener);
        }
    }


    /** The location manager manages all location tracking.
     * The location listener updates us on when a user says yes or no to things and when the device moves.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //this is creating a link for the button on main activity to open the second activity
        Button nextActivity = findViewById(R.id.nextActivityButton);
        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });

        //this is creating a link to open phone contacts
        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intentContact, 1);

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
                System.out.println("original: " + location.toString());
                currentLocationArray = location.toString().trim().split(",");
                System.out.println("current location array: " + currentLocationArray[0] + currentLocationArray[1]);
                currentLocationArray[0] = currentLocationArray[0].split(" ")[1].split("\\.")[0];
                currentLocationArray[1] = currentLocationArray[1].split("\\.")[0];
                System.out.println("current location array: " + currentLocationArray[0] + currentLocationArray[1]);

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 5, locationListener);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 2);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 3);
            }
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                home[0] = place.getLatLng().latitude;
                home[1] = place.getLatLng().longitude;
                temp = new String[2][2];
                home2 = Arrays.toString(home).trim().split(",");
                temp[0] = home2[0].split("\\[")[1].trim().split("\\.");
                temp[1] = home2[1].split("]")[0].trim().split("\\.");
                home2[0] = home2[0].split("\\[")[1].trim().split("\\.")[0].trim() + "." + temp[0][1].trim().charAt(0) + temp[0][1].trim().charAt(1) + temp[0][1].trim().charAt(2) + temp[0][1].trim().charAt(3);
                home2[1] = home2[1].split("]")[0].trim().split("\\.")[0].trim() + "." + temp[1][1].trim().charAt(0) + temp[1][1].trim().charAt(1) + temp[1][1].trim().charAt(2) + temp[1][1].trim().charAt(3);
                System.out.println("home 2: " + home2[0] + home2[1]);
                if ((home2.equals(currentLocationArray))) {
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


    //for button Im on my way! button
    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        if (home2 != null) {
            startActivity(intent);
        }
    }
    // to remove characters other than digits from phone number
    public static String stripNonDigits(final CharSequence input) {
        final StringBuilder sb = new StringBuilder(input.length());
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }
    //for contact button
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case 1:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                        Uri contactData = data.getData();
                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        if (c.getCount() > 0) {
                            if (c.moveToNext()) {
                                name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                                id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                                TextView displayName = findViewById(R.id.contactName);
                                displayName.setText(name);
                                if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                                    while (phones.moveToNext()) {
                                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        number = stripNonDigits(phoneNumber);
                                    }
                                    phones.close();
                                }
                            }
                        }

                        c.close();
                    }
                }
                break;
        }
    }
}
