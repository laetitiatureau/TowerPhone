package m2dl.towerphone;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;

public class Game extends AppCompatActivity {

    //private CanvasView customCanvas;

    public static TextView scoreLabel;
    private TextView startLabel;

    public static ImageView minions;
    public static ImageView monster1;
    public static ImageView monster2;
    private ImageView tour;
    private ProgressBar hpBar;

    private int frameHeight, frameWidth;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;
    public static int score = 0;
    private int monster1X, monster1Y, monster2X, monster2Y, minionsX, minionsY;

    public static boolean start_flg = false;
    private boolean action_flg = false;
    private int curHP = 100;

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

        hpBar = findViewById(R.id.progressBar4);
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        tour = findViewById(R.id.tower);
        monster1 = findViewById(R.id.monstre1);


        monster1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                monster1.setVisibility(View.INVISIBLE);
                score += 10;
                return false;
            }
        });
        monster2 = findViewById(R.id.monstre2);
        monster2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                monster2.setVisibility(View.INVISIBLE);
                score+=20;
                return false;
            }
        });
        minions = findViewById(R.id.minions);
        minions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                minions.setVisibility(View.INVISIBLE);
                score+=30;
                return false;
            }
        });

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        monster1.setX(-150);
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
        checkPosition();
        //Left
        minionsX -= 7;
        if (minionsX < 0){
                minionsX = screenWidth;
                minionsY = (int) Math.floor(Math.random() * (frameHeight - minions.getHeight()));
        }
        if ((tour.getX() - 5 <= minionsX && minionsX <= tour.getX() + 5)  && (minionsY > tour.getY() - 2  ||  minionsY < tour.getY() + 2 )) {
            curHP = curHP - 10;
            hpBar.setProgress(curHP);
            minionsX = -100;
        }
        minions.setX(minionsX);
        minions.setY(minionsY);
        if (minions.getX() >= screenWidth) minions.setVisibility(View.VISIBLE);

        //Down
        monster1Y += 7;
        if (monster1Y > screenHeight) {
            monster1X = (int) Math.floor(Math.random() * (screenWidth - monster1.getWidth()));
            monster1Y = -100;
        }
        monster1.setX(monster1X);
        monster1.setY(monster1Y);
        if (monster1.getY() < 0) monster1.setVisibility(View.VISIBLE);

        //Up
        monster2Y -= 7;
        if (monster2X + monster2Y < 0){
            monster2X = (int) Math.floor(Math.random() * (screenWidth - monster2.getWidth()));
            monster2Y = screenHeight + 100;
        }
        monster2.setX(monster2X);
        monster2.setY(monster2Y);
        if (monster2.getY() < 0) monster2.setVisibility(View.VISIBLE);
        scoreLabel.setText("Score : "+ score);
    }

    private void checkPosition() {

        if(curHP == 0){
            timer.cancel();
            timer = null;
            Intent intent = new Intent(getApplicationContext(), HighScore.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        if (!start_flg){
            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            frameWidth = frame.getWidth();

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
