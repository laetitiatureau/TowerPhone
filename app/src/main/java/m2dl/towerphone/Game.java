package m2dl.towerphone;

import android.content.Intent;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;

public class Game extends AppCompatActivity {

    //private CanvasView customCanvas;

    private TextView scoreLabel;
    private TextView startLabel;

    private ImageView minions;
    private ImageView monster1;
    private ImageView monster2;

    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;
    private int score = 0;
    private int monster1X, monster1Y, monster2X, monster2Y, minionsX, minionsY;

    private boolean start_flg = false;
    private boolean action_flg = false;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    public static TextView shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        monster1 = findViewById(R.id.monstre1);
        monster2 = findViewById(R.id.monstre2);
        minions = findViewById(R.id.minions);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        monster1.setX(-150f);
        monster1.setY(-150);

        monster2.setX(-150);
        monster2.setY(-150);

        minions.setX(-150);
        minions.setY(-150);

        scoreLabel.setText("SCORE: 0");

        shake = findViewById(R.id.timer);
        Intent intent = new Intent(this, ShakeService.class);
        startService(intent);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
            }
        });
    }

    private void changePosition(){

        /*minionsX += 20;
        if (minionsX < 0){
            minionsX = screenHeight + 10;
            minionsY = (int) Math.floor(Math.random() * (screenHeight - minions.getHeight()));
        }
        minions.setX(minionsX);
        minions.setY(minionsY);

        //Down
        monster1X += 10;
        if (monster1.getX() > screenWidth){
            monster1X = screenWidth - 10;
            monster1Y =  (int) Math.floor(Math.random() * (screenHeight - monster1.getHeight()));
        }
        monster1.setX(monster1Y);
        monster1.setY(monster1X);*/

        minionsX -=12;
        if (minionsX < 0){
            minionsX = screenWidth + 20 ;
            minionsY = (int) Math.floor(Math.random() * (frameHeight - minions.getHeight()));
                minionsX = screenWidth + 20 ;
                minionsY = (int) Math.floor(Math.random() * (frameHeight - minions.getHeight()));
        }
        minions.setX(minionsX);
        minions.setY(minionsY);

        monster1X -=12;
        if (monster1X < 0){
            monster1X = screenWidth + 20 ;
            monster1Y = (int) Math.floor(Math.random() * (frameHeight - monster1.getHeight()));
        }
        monster1.setX(monster1Y);
        monster1.setY(monster1X);

        scoreLabel.setText("Score : "+ score);
    }

    /*public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }*/

    public boolean onTouchEvent(MotionEvent event){
        if (!start_flg){
            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePosition();
                        }
                    });
                }
            }, 0, 20);
        }else{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
