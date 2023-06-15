package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity  implements LocationListener {

    TextView locationtxtlat;
    TextView locationtxtlong;
    TextView speed;
    TextView time;
    TextView timeprevious;
    TextView speedprevious;
    LocationManager locationManager;
    Location previousLocation;
    SQLiteDatabase sqLiteDatabase;

    FirebaseFirestore firestore;

    double latitude; // Your latitude
    double longitude; // Your longitude

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Intent that started this activity and the user id
        Intent intent = getIntent();
        String userId = intent.getStringExtra("USER_ID");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // UID
            String uid = user.getUid();
            // Email
            String email = user.getEmail();
            showMessage("Hello",email);
//            showMessage("Start GPS","Press this to start the app");

        }


        //        create or open database
        sqLiteDatabase = openOrCreateDatabase("DB3.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "LOCATION" + "(" +
                "latitude" + " TEXT PRIMARY KEY," +
                "longtitude" + " TEXT)");
        sqLiteDatabase.execSQL("INSERT OR IGNORE INTO LOCATION VALUES('0','0')");

//        firestone test
//        firestore = FirebaseFirestore.getInstance();
//        Map<String,Object> user = new HashMap<>();
//        user.put("first","easy");
//        user.put("last","hard");
//        user.put("description","good student");
//        firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
//            }
//        });

        locationtxtlat = findViewById(R.id.textView);
        locationtxtlong = findViewById(R.id.textView8);
        speed = findViewById(R.id.textView2);
        time = findViewById(R.id.textView3);
        timeprevious = findViewById(R.id.textView4);
        speedprevious = findViewById(R.id.textView6);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    public void gps(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void maps(View view){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("locationsBrakes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Double> latitudeList = new ArrayList<>();
                            ArrayList<Double> longitudeList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double latitude = Double.parseDouble(document.getString("latitude"));
                                double longitude = Double.parseDouble(document.getString("longitude"));
                                latitudeList.add(latitude);
                                longitudeList.add(longitude);
                            }

                            double[] latitudeArray = new double[latitudeList.size()];
                            double[] longitudeArray = new double[longitudeList.size()];

                            for (int i = 0; i < latitudeList.size(); i++) {
                                latitudeArray[i] = latitudeList.get(i);
                                longitudeArray[i] = longitudeList.get(i);
                            }

                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("LATITUDE_ARRAY_EXTRA", latitudeArray);
                            intent.putExtra("LONGITUDE_ARRAY_EXTRA", longitudeArray);

                            startActivity(intent);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void maps2(View view){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("locationsAcceleration")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Double> latitudeList = new ArrayList<>();
                            ArrayList<Double> longitudeList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double latitude = Double.parseDouble(document.getString("latitude"));
                                double longitude = Double.parseDouble(document.getString("longitude"));
                                latitudeList.add(latitude);
                                longitudeList.add(longitude);
                            }

                            double[] latitudeArray = new double[latitudeList.size()];
                            double[] longitudeArray = new double[longitudeList.size()];

                            for (int i = 0; i < latitudeList.size(); i++) {
                                latitudeArray[i] = latitudeList.get(i);
                                longitudeArray[i] = longitudeList.get(i);
                            }

                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("LATITUDE_ARRAY_EXTRA", latitudeArray);
                            intent.putExtra("LONGITUDE_ARRAY_EXTRA", longitudeArray);

                            startActivity(intent);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void maps3(View view){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("locationsMaxSpeed")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Double> latitudeList = new ArrayList<>();
                            ArrayList<Double> longitudeList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double latitude = Double.parseDouble(document.getString("latitude"));
                                double longitude = Double.parseDouble(document.getString("longitude"));
                                latitudeList.add(latitude);
                                longitudeList.add(longitude);
                            }

                            double[] latitudeArray = new double[latitudeList.size()];
                            double[] longitudeArray = new double[longitudeList.size()];

                            for (int i = 0; i < latitudeList.size(); i++) {
                                latitudeArray[i] = latitudeList.get(i);
                                longitudeArray[i] = longitudeList.get(i);
                            }

                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("LATITUDE_ARRAY_EXTRA", latitudeArray);
                            intent.putExtra("LONGITUDE_ARRAY_EXTRA", longitudeArray);

                            startActivity(intent);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void showMessage(String title, String msg){
        new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msg)
                .show();
    }

    public void select(View view){
//        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM LOCATION",null);
//        StringBuilder stringBuilder = new StringBuilder();
//        while(cursor.moveToNext()){
//            stringBuilder.append("lat: ").append(cursor.getString(0)).append("\n");
//            stringBuilder.append("long: ").append(cursor.getString(1)).append("\n\n");
//        }
//        showMessage("Locations", stringBuilder.toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("locationsBrakes")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Map<String, Object> location = document.getData();
                            String latitude = document.getString("latitude");
                            String longitude = document.getString("longitude");
                            stringBuilder.append("lat: ").append(latitude).append("\n");
                            stringBuilder.append("long: ").append(longitude).append("\n\n");
                            // Display the location data
//                            Log.d("Firestore", "Location: " + location)
                        }
                        showMessage("Locations of Brakes", stringBuilder.toString());

                    } else {
//                        Log.d("Firestore", "Error getting documents: ", task.getException());
                            showMessage("error","task wasn't successfull");
                    }
                });
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

//        / calculate the time elapsed since the location was obtained
//
        long timeElapsed = System.currentTimeMillis() - location.getTime();
        // display the current location data
        locationtxtlat.setText(location.getLatitude()+"");
        locationtxtlong.setText(location.getLongitude()+"");
        speed.setText(location.getSpeed()* 3.6f+"");
        time.setText(location.getTime()+"");
        // update the previous location and display its time
        if (previousLocation != null) {
            long timeDifference = location.getTime() - previousLocation.getTime();
            double timeDifferenceInSeconds = timeDifference / 1000.0;
            timeprevious.setText(previousLocation.getTime()+"");
            speedprevious.setText(previousLocation.getSpeed()* 3.6f+"");

//            time diff
            TextView timeDifferenceText = findViewById(R.id.textView5);
            timeDifferenceText.setText(String.format("%.2f sec", timeDifferenceInSeconds));

//            speed diff
            float speedDifference = location.getSpeed() - previousLocation.getSpeed();
            float speedDifferenceKilometersPerHour = speedDifference * 3.6f;
            TextView speedDifferenceText = findViewById(R.id.textView7);
            speedDifferenceText.setText(String.format("%.2f km/h", speedDifferenceKilometersPerHour));


//          max speed limit
            float speed_per_hour = location.getSpeed() * 3.6f;
            if(speed_per_hour>90){
                firestore = FirebaseFirestore.getInstance();
                Map<String,Object> locationF = new HashMap<>();
                locationF.put("latitude",String.valueOf(location.getLatitude()));
                locationF.put("longitude",String.valueOf(location.getLongitude()));
                locationF.put("timestamp",String.valueOf(location.getTime()));
                locationF.put("speed",String.valueOf(location.getTime()));
                firestore.collection("locationsMaxSpeed").add(locationF).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "location Speed Limit Break", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });
            }

//            losing 10 km per sec is considered a brake
            if(speedDifferenceKilometersPerHour<-10){
                System.out.println("Brake");
                System.out.println(previousLocation.getLatitude()+","+previousLocation.getLongitude());
//                sqLiteDatabase.execSQL("INSERT OR IGNORE INTO LOCATION VALUES(?,?)",new String[] {String.valueOf(previousLocation.getLatitude()), String.valueOf(previousLocation.getLongitude())});
//                Toast.makeText(this, "location Added", Toast.LENGTH_SHORT).show();


//                adding brakes
                firestore = FirebaseFirestore.getInstance();
                Map<String,Object> locationF = new HashMap<>();
                locationF.put("latitude",String.valueOf(previousLocation.getLatitude()));
                locationF.put("longitude",String.valueOf(previousLocation.getLongitude()));
                locationF.put("timestamp",String.valueOf(previousLocation.getTime()));
                firestore.collection("locationsBrakes").add(locationF).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "location of Brake Added", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });


            };
//            adding 10 km per sec is considered a acceleration
            if(speedDifferenceKilometersPerHour> 10){
                System.out.println("Acceleration");
                System.out.println(previousLocation.getLatitude()+","+previousLocation.getLongitude());
//                sqLiteDatabase.execSQL("INSERT OR IGNORE INTO LOCATION VALUES(?,?)",new String[] {String.valueOf(previousLocation.getLatitude()), String.valueOf(previousLocation.getLongitude())});
//                Toast.makeText(this, "location Added", Toast.LENGTH_SHORT).show();


//                adding acceleration
                firestore = FirebaseFirestore.getInstance();
                Map<String,Object> locationF = new HashMap<>();
                locationF.put("latitude",String.valueOf(previousLocation.getLatitude()));
                locationF.put("longitude",String.valueOf(previousLocation.getLongitude()));
                locationF.put("timestamp",String.valueOf(previousLocation.getTime()));
                firestore.collection("locationsAcceleration").add(locationF).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "location of Acceleration Added", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                });


            };
        }

//        assign the location to previous location
        previousLocation = location;
    }
}