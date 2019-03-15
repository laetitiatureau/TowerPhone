package m2dl.towerphone;

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

public class Game extends AppCompatActivity {

    //private CanvasView customCanvas;

    private TextView scoreLabel;
    private TextView startLabel;

    private ImageView minions;
    private ImageView monster1;
    private ImageView monster2;
    private ImageView tour;

    private ProgressBar hpBar;
    private int curHP = 100;
    private int frameHeight, frameWidth;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;
    private int score = 0;
    private int monster1X, monster1Y, monster2X, monster2Y, minionsX, minionsY;

    private boolean start_flg = false;
    private boolean action_flg = false;

    private Handler handler = new Handler();
    private Timer timer = new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        hpBar = findViewById(R.id.progressBar4);
        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        tour = findViewById(R.id.tower);
        monster1 = findViewById(R.id.monstre1);
        monster1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                monster1.setVisibility(View.INVISIBLE);
                score++;
                return false;
            }
        });
        monster2 = findViewById(R.id.monstre2);
        monster2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                monster2.setVisibility(View.INVISIBLE);
                score++;
                return false;
            }
        });
        minions = findViewById(R.id.minions);
        minions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                minions.setVisibility(View.INVISIBLE);
                score++;
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
    }

    private void changePosition(){
        //Left
        minionsX -= 7;
        if (minionsX < 0){
                minionsX = screenWidth;
                minionsY = (int) Math.floor(Math.random() * (screenHeight - minions.getHeight()));
        }
        if(minions.getVisibility() == View.VISIBLE) {
            if ((tour.getX() -1 < minionsX && minionsX <= tour.getX() + 1) && (minionsY > tour.getY() - 1 || minionsY < tour.getY() + 1)) {
                curHP = curHP - 10;
                hpBar.setProgress(curHP);
                minionsX = -100;
            }
        }
        minions.setX(minionsX);
        minions.setY(minionsY);

        //Down
        monster1Y += 7;
        if (monster1Y > screenHeight) {
            monster1X = (int) Math.floor(Math.random() * (screenWidth - monster1.getWidth()));
            monster1Y = -100;
        }
        if(monster1.getVisibility() == View.VISIBLE) {
            if ((tour.getX() -1 < monster1X || monster1X < tour.getX() + 1) && (monster1Y > tour.getY() - 1  && monster1Y < tour.getY() + 1)) {
                curHP = curHP - 10;
                hpBar.setProgress(curHP);
                monster1X = -100;
            }
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
        if(monster2.getVisibility() == View.VISIBLE) {
            if ((tour.getX() -1 < monster2X  || monster2X < tour.getX() + 1) && (monster2Y > tour.getY() - 1 && monster2Y < tour.getY() + 1)) {
                curHP = curHP - 10;
                hpBar.setProgress(curHP);
                monster2X= -100;
                monster2Y = -100;
            }
        }
        monster2.setX(monster2X);
        monster2.setY(monster2Y);
        if (monster2.getY() < 0) monster2.setVisibility(View.VISIBLE);
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
}
