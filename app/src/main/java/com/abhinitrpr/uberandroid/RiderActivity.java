package com.abhinitrpr.uberandroid;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class RiderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    Button callUberButton;
    Handler handler = new Handler();
    Boolean requestActive = false;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference User = FirebaseDatabase.getInstance().getReference().child("users");
    HashMap<String, Double> userLocation = new HashMap<String, Double>();
    Boolean driverActive = false;
    TextView infoTextView;

    public  void checkForUpdates(){

        User.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("driverUsername")){
                    driverActive = true;

                    String driverId = (String) dataSnapshot.child("driverUsername").getValue();

                    User.child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("location")){

                                double driverLatitude = (double) dataSnapshot.child("location").child("latitude").getValue();
                                double driverLongitude = (double) dataSnapshot.child("location").child("longitude").getValue();

                                if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(RiderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                    if (lastKnownLocation != null) {

                                         double userLatitude = lastKnownLocation.getLatitude();
                                        double userLongitude = lastKnownLocation.getLongitude();

                                        float[] results = new float[1];
                                        Location.distanceBetween(driverLatitude, driverLongitude, userLatitude, userLongitude, results);
                                        double distance = (double) Math.round(results[0] * 1) / 100;

                                        if(distance < 0.1){

                                            infoTextView.setText("Your driver is here!");

                                            User.child(mAuth.getCurrentUser().getUid()).child("location").removeValue();

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    infoTextView.setText("");
                                                    callUberButton.setVisibility(View.VISIBLE);
                                                    callUberButton.setText("Call An Uber");
                                                    requestActive = false;
                                                    driverActive = false;

                                                }
                                            }, 5000);

                                        }else {

                                            infoTextView.setText("Your driver is " + String.valueOf(distance) + " kms away!");
                                            LatLng driverLocationLatLng = new LatLng(driverLatitude, driverLongitude);

                                            LatLng requestLocationLatLng = new LatLng(userLatitude, userLongitude);

                                            ArrayList<Marker> markers = new ArrayList<>();

                                            mMap.clear();

                                            markers.add(mMap.addMarker(new MarkerOptions().position(driverLocationLatLng).title("Driver Location")));
                                            markers.add(mMap.addMarker(new MarkerOptions().position(requestLocationLatLng).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

                                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                            for (Marker marker : markers) {
                                                builder.include(marker.getPosition());
                                            }
                                            LatLngBounds bounds = builder.build();


                                            int padding = 60; // offset from edges of the map in pixels
                                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                                            mMap.animateCamera(cu);


                                            callUberButton.setVisibility(View.INVISIBLE);

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    checkForUpdates();

                                                }
                                            }, 2000);

                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void logout(View view){
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void callUber(View view) {
        Log.i("Info", "Call Uber");
        if (mAuth.getCurrentUser() != null) {
            if (requestActive) {

                User.child(mAuth.getCurrentUser().getUid()).child("location").removeValue();
                requestActive = false;
                callUberButton.setText("Call an Uber");


            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (lastKnownLocation != null) {
                        if (mAuth.getCurrentUser() != null) {
                            userLocation.put("latitude", lastKnownLocation.getLatitude());
                            userLocation.put("longitude", lastKnownLocation.getLongitude());
                            User.child(mAuth.getCurrentUser().getUid()).child("location").setValue(userLocation);
                            Toast.makeText(this, "Location Saved", Toast.LENGTH_SHORT).show();
                            callUberButton.setText("CANCEL UBER");
                            requestActive = true;

                            checkForUpdates();

//                            GeoFire geoFire = new GeoFire(User.child(mAuth.getCurrentUser().getUid()).child("request"));
//                            geoFire.setLocation("location", new GeoLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), new GeoFire.CompletionListener() {
//                                @Override
//                                public void onComplete(String key, DatabaseError error) {
//                                    if (error != null) {
//                                        Log.i("Error", "There was an error saving the location to GeoFire: " + error);
//                                    } else {
//                                        Log.i("Sucess", "Location saved on server successfully!");
//
//                                        callUberButton.setText("CANCEL UBER");
//                                        requestActive = true;
//                                    }
//                                }
//
//
//                            });

                        }
                    }else {
                            Toast.makeText(this, "Couldn't find location. Please try again later", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateLocation(lastKnownLocation);



                }

            }
        }
    }

    public void updateLocation(Location location){

        if(!driverActive) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        callUberButton = findViewById(R.id.callUberButton);
        infoTextView = findViewById(R.id.infoTextView);


        if (mAuth.getCurrentUser() != null) {
            User.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("location")) {
                        requestActive = true;
                        callUberButton.setText("Cancel Uber");
                        checkForUpdates();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                updateLocation(lastKnownLocation);

            }
        }


    }
}
