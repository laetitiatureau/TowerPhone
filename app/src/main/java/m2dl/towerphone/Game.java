package m2dl.towerphone;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Game extends AppCompatActivity {

    private ProgressBar hpBar;
    private CanvasView customCanvas;
    private int curHp = 0;

    private Thread thread;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
        hpBar = (ProgressBar) findViewById(R.id.progressBar);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (curHp < 100)
                    curHp++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            hpBar.setProgress(curHp);
                        }
                    });
            }
        });
    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }
}
