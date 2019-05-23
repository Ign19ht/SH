package com.example.alevtina.sh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class MyService extends Service implements SensorEventListener, StepListener{

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static int numSteps;
    private static final int NOTIFY_ID = 101;
    final static String SAVED_TEXT = "saved_text";
    final static String SAVED_DATE = "saved_date";

    public MyService() {
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        load();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(MyService.this , accel, SensorManager.SENSOR_DELAY_FASTEST);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        try{
            HomeFragment.SetStats();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

//        Toast.makeText(this, Integer.toString(numSteps), Toast.LENGTH_SHORT).show();
        save();
//        Intent notificationIntent = new Intent();
//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                0, notificationIntent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentIntent(contentIntent)
//                // обязательные настройки
//                .setSmallIcon(R.drawable.ic_refresh_black_24dp)
//                .setContentTitle("Гиги За Шаги")
//                .setContentText(Integer.toString(numSteps)); // Текст уведомления
//                // необязательные настройки
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    void load() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        numSteps = sharedPreferences.getInt(SAVED_TEXT, 0);
    }

    void save() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putInt(SAVED_TEXT, numSteps);
        ed.putString(SAVED_DATE, SupportClass.GetDate());
        ed.commit();
    }
}

