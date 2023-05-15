package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //        create or open database
        sqLiteDatabase = openOrCreateDatabase("DB2.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "LOCATION" + "(" +
                "latitude" + " TEXT PRIMARY KEY," +
                "longtitude" + " TEXT)");
        sqLiteDatabase.execSQL("INSERT OR IGNORE INTO LOCATION VALUES('0','0')");

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
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

//        / calculate the time elapsed since the location was obtained
//        long timeElapsed = System.currentTimeMillis() - location.getTime();
////        dv/dt and save location
//        locationtxt.setText(location.getLatitude()+","+location.getLongitude());
//        speed.setText(location.getSpeed()+"");
//        time.setText(location.getTime()+"");
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

//            losing 5 km per sec is considered a brake
            if(speedDifferenceKilometersPerHour<-1){
                System.out.println("Brake");
                System.out.println(previousLocation.getLatitude()+","+previousLocation.getLongitude());
                sqLiteDatabase.execSQL("INSERT OR IGNORE INTO LOCATION VALUES(?,?)",new String[] {String.valueOf(previousLocation.getLatitude()), String.valueOf(previousLocation.getLongitude())});
                Toast.makeText(this, "location Added", Toast.LENGTH_SHORT).show();

            };

        }

//        assign the location to previous location
        previousLocation = location;
    }
}