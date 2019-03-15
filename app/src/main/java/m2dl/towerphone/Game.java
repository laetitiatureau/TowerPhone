package m2dl.towerphone;

import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;
    private int score = 0;
    private float monster1X, monster1Y, monster2X, monster2Y, minionsX, minionsY;

    private boolean start_flg = false;
    private boolean action_flg = false;

    private Handler handler = new Handler();
    private Timer timer = new Timer();



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

        screenWidth = size.x;
        screenHeight = size.y;

        monster1.setX(-80.0f);
        monster1.setY(-80.0f);

        monster2.setX(-80.0f);
        monster2.setY(-80.0f);

        minions.setX(-150.0f);
        minions.setY(-150.0f);

        scoreLabel.setText("SCORE: 0");
    }

    private void changePosition(){

        minionsX -= 5;
        if (minionsX < 0){
            minionsX = (float) Math.floor(Math.random() * (frameHeight - minions.getHeight()));
            minionsY = screenHeight + 550.0f;
        }
        minions.setX(minionsX);
        minions.setY(minionsY);

        //Down
        monster1X += 5;
        if (monster1.getY()> screenHeight){
            monster1X = (float) Math.floor(Math.random() * (screenWidth - monster1.getWidth()));
            monster1Y = -100.0f;
        }
        monster1.setX(monster1X);
        monster1.setY(monster1Y);

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
}
