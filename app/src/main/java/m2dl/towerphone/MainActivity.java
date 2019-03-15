package m2dl.towerphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }


}
