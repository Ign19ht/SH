package com.example.alevtina.sh;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class MyService extends Service implements SensorEventListener, StepListener{

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static int numSteps;
//    private static final int NOTIFY_ID = 101;
    final String SAVED_TEXT = "saved_text";

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        load();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(MyService.this , accel, SensorManager.SENSOR_DELAY_FASTEST);
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
//                .setContentTitle("Напоминание")
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
        ed.commit();
    }
}

