package edu.pitt.redwhistle;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ShakeService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private static NetworkActivity net;
    private long previousShakeTime = 0;
    private FusedLocationProviderClient fusedLocationClient;

    public ShakeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private FusedLocationProviderClient getLocationProviderClientInstance() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
        return fusedLocationClient;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    NetworkActivity getNetInstance() {
        if (net == null) {
            net = new NetworkActivity(this);
        }
        return net;
    }

    // Detects shake gestures
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        // Once shake is detected, it sends an HTTP request to notify the web server
        if (mAccel > 12) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - previousShakeTime < 800) {
                return;
            }
            previousShakeTime = currentTime;

            fusedLocationClient = getLocationProviderClientInstance();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                net = getNetInstance();
                                System.out.println("Shake Shake Shake");
                                String url ="http://10.0.0.240:8080/api/emergency/patient/"
                                        + HelloActivity.prop.getProperty("userToken")
                                        + "?latitude=" + latitude + "&longitude=" + longitude;

                                System.out.println("My URL ---->" + url);
                                net.sendRequest(url);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
