package m2dl.towerphone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.util.Random;

public class ShakeService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private boolean timeRunning = false;
    private boolean shake = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 11) {
            if (!timeRunning) {
                new CountDownTimer(10000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        Game.shake.setText("Shake available in: " + millisUntilFinished / 1000);
                        if (!shake) {
                            Game.minions.setVisibility(View.INVISIBLE);
                            Game.monster1.setVisibility(View.INVISIBLE);
                            Game.monster2.setVisibility(View.INVISIBLE);
                            Game.score = Game.score + 3;
                            Game.scoreLabel.setText("SCORE: " + Game.score);
                            shake = true;
                        }
                        timeRunning = true;
                    }

                    public void onFinish() {
                        Game.shake.setText("SHAKE TO KILL THEM ALL!");
                        timeRunning = false;
                        shake = false;
                    }
                }.start();
            }
        }
    }
}
