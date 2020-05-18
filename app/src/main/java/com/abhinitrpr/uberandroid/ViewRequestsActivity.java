package com.abhinitrpr.uberandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ViewRequestsActivity extends AppCompatActivity {

    ListView requestListView;
    ArrayList<String> requests = new ArrayList<String>();
    ArrayList<Double> requestLatitude = new ArrayList<Double>();
    ArrayList<Double> requestLongitude = new ArrayList<Double>();
    ArrayList<String> usernames = new ArrayList<String>();
    HashMap<String, Double> driverLocation = new HashMap<String, Double>();
    ArrayAdapter arrayAdapter;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int index, count = 0;

    ArrayList<String> userIds = new ArrayList<>();
    DatabaseReference User = FirebaseDatabase.getInstance().getReference().child("users");



    public void updateListView(Location location1) {


        if (location1 != null) {
           requests.clear();


            //final GeoLocation driverGeoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
            final double driverLatitude = location1.getLatitude();
            final double driverLongitude = location1.getLongitude();
//        GeoQuery geoQueryNearByUser=null;
//
//        GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//
//
//
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        };
//
//
//
//        if(geoQueryNearByUser == null){
//            geoQueryNearByUser = geoFire.queryAtLocation(driverGeoLocation, 50);
//
//            geoQueryNearByUser.addGeoQueryEventListener(geoQueryEventListener);
//        }
//        else {
//            geoQueryNearByUser.setCenter(driverGeoLocation);
//        }


            // Log.i("driver location", String.valueOf(geoLocation));


//        User.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                long childrenCount = dataSnapshot.getChildrenCount();
//
//                Log.i("Childern Count", String.valueOf(childrenCount));
//                for (DataSnapshot ds: dataSnapshot.getChildren()) {
//                    String userId = ds.getKey();
//                    Log.i("Userid", userId);
//                    index++;
//                    Log.i("For loops", String.valueOf(index));
//
//                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
//
//                    geoFire = new GeoFire(currentUser.child("request"));
//                    geoQuery = geoFire.queryAtLocation(driverGeoLocation, 50);
//                    //Log.i("GeoQuery", String.valueOf(count));
//                    if (geoQuery != null) {
//                        //Log.i("Geoquery2", String.valueOf(count));
//                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                            @Override
//                            public void onKeyEntered(String key, GeoLocation location) {
//                                count++;
//                                if (location != null) {
//                                    float[] results = new float[1];
//
//                                    Log.i("user location & key", String.valueOf(location) + "key:" + key);
//                                    Log.i("For loops key entered", String.valueOf(count));
//
//                                    Location.distanceBetween(driverGeoLocation.latitude, driverGeoLocation.longitude, location.latitude, location.longitude, results);
//                                    double distance = (double) Math.round(results[0] * 10) / 1000;
//                                    //Log.i("distance", String.format("%.2d",distance) + "kms");
//                                    requests.add(String.valueOf(distance) + " kms");
//                                    arrayAdapter.notifyDataSetChanged();
//                                }
//
//                            }
//
//                            @Override
//                            public void onKeyExited(String key) {
//
//
//                            }
//
//                            @Override
//                            public void onKeyMoved(String key, GeoLocation location) {
//
//                            }
//
//                            @Override
//                            public void onGeoQueryReady() {
//
//                            }
//
//                            @Override
//                            public void onGeoQueryError(DatabaseError error) {
//
//                            }
//                        });
//
//
//                    }
//                }
//                    //geoQuery.removeAllListeners();
//
//
//
//
//                }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


//
//       User.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    userIds.add(dataSnapshot.getKey());
//               for (DataSnapshot ds: dataSnapshot.getChildren()){
//                    String userId = ds.getKey();
//                    Log.i("Userid", userId);
//                    userIds.add(userId);
//
//                   index++;
//                   Log.i("For loops", String.valueOf(index));
//
////                    DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
////
////                    geoFire = new GeoFire(currentUser.child("request"));
////                  geoQuery = geoFire.queryAtLocation(driverGeoLocation, 50);
////
////                    if (geoQuery != null) {
////                        Log.i("Query for loop", String.valueOf(index));
////
////
////
////                    }
//                }
//
//           }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        Log.i("userID arrayList", String.valueOf(userIds));


//        geoFire.getLocation("location", new LocationCallback() {
//            @Override
//            public void onLocationResult(String key, GeoLocation location) {
//                if (location != null) {
//                    Log.i("message", String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
//                } else {
//                    Log.i("message", String.format("There is no location for key %s in GeoFire", key));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.err.println("There was an error getting the GeoFire location: " + databaseError);
//            }
//        });


//
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while( iterator.hasNext()){
//                    String uid = ((DataSnapshot) iterator.next()).getKey();
//                    requests.add(uid);
//                    arrayAdapter.notifyDataSetChanged();
//
//                }
////                for(DataSnapshot ds : dataSnapshot.getChildren()) {
////                    String uid = ds.getKey();
////
////                    Log.i("userID arrayList", String.valueOf(requests));
////
////
////
////
////
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        User.addListenerForSingleValueEvent(eventListener);
//
//        User.removeEventListener(eventListener);


            User.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String id = dataSnapshot.getKey();

                    Log.i("child added loop", String.valueOf(index));


                    final DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("users").child(id);

                    currentUser.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange( @NonNull DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.hasChild("driverUsername")){
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    if (ds.getKey().equals("location") && dataSnapshot.child("riderOrDriver").getValue().equals("rider")) {
                                        double userLatitude = (double) ds.child("latitude").getValue();
                                        double userLongitude = (double) ds.child("longitude").getValue();
                                        usernames.add(String.valueOf(dataSnapshot.getKey()));
                                        Log.i("username", String.valueOf(dataSnapshot.getKey()));
                                        //Log.i("lat and long", "lat: "+String.valueOf(lat)+ "long: "+ String.valueOf(lon));
                                        float[] results = new float[1];
                                        Location.distanceBetween(driverLatitude, driverLongitude, userLatitude, userLongitude, results);
                                        double distance = (double) Math.round(results[0] * 1) / 100;
                                        //Log.i("distance", String.format("%.2d",distance) + "kms");
                                        requests.add(String.valueOf(distance) + " kms");
                                        arrayAdapter.notifyDataSetChanged();
                                        Log.i("Request size-for", String.valueOf(requests.size()));
                                        requestLatitude.add(userLatitude);
                                        requestLongitude.add(userLongitude);
                                    }


                                }
                        }
                            arrayAdapter.notifyDataSetChanged();
                            Log.i("Request size", String.valueOf(requests.size()));

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            //requests.subList(2, 4).clear();
            //Log.i("request size-last", String.valueOf(requests.size()));
            // arrayAdapter.notifyDataSetChanged();


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);


                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);

                }

            }


        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        setTitle("Nearby Requests");

        requestListView = findViewById(R.id.requestListView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requests);
        requests.clear();
        requests.add("Getting nearby requests...");
        requestListView.setAdapter(arrayAdapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(ViewRequestsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (requestLatitude.size() > i && requestLongitude.size() > i && usernames.size() > i && lastKnownLocation != null) {

                        Intent intent = new Intent(getApplicationContext(), DriverLocationActivity.class);

                        intent.putExtra("requestLatitude", requestLatitude.get(i));
                        intent.putExtra("requestLongitude", requestLongitude.get(i));
                        intent.putExtra("driverLatitude", lastKnownLocation.getLatitude());
                        intent.putExtra("driverLongitude", lastKnownLocation.getLongitude());
                        intent.putExtra("username", usernames.get(i));

                        startActivity(intent);


                    }

                }

            }
        });


        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                     updateListView(location);

                driverLocation.put("latitude", location.getLatitude());
                driverLocation.put("longitude", location.getLongitude());
                User.child(mAuth.getCurrentUser().getUid()).child("location").setValue(driverLocation);

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

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,  100, locationListener);



                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//                if (lastKnownLocation != null) {
//
//                    updateListView(lastKnownLocation);
//
//                }

            }
        }


    }
}


//if (dataSnapshot.hasChild("location")) {
//
//        if (dataSnapshot.child("location").hasChildren()) {
//
//        double latitude = (double) dataSnapshot.child("location").child("latitude").getValue();
//        double longitude = (double) dataSnapshot.child("location").child("longitude").getValue();
//
//        float[] results = new float[1];
//        Location.distanceBetween(driverLatitude, driverLongitude, latitude, longitude, results);
//        double distance = (double) Math.round(results[0] * 10) / 1000;
//        //Log.i("distance", String.format("%.2d",distance) + "kms");
//        requests.add(String.valueOf(distance) + " kms");
//        arrayAdapter.notifyDataSetChanged();
//        }

       // }