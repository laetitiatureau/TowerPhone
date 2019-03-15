package m2dl.towerphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // disable title bar, ask for fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void highScore(View view){
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
    }

    public void exit(View view){
        finish();
        moveTaskToBack(true);
    }
}
