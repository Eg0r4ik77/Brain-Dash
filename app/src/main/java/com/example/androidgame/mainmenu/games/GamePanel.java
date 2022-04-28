package com.example.androidgame.mainmenu.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.calculateexpressiongame.CalculateExpressionGameFragment;
import com.example.androidgame.mainmenu.games.repeatdrawinggame.RepeatDrawingGameFragment;
import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTableGameFragment;

public class GamePanel extends AppCompatActivity {

    private TextView scoreText;
    private TextView timerText;
    private FrameLayout gameLayout;
    private Timer timer;
    private int score;

    private Fragment fragment;

    private FrameLayout flashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_panel);
        getSupportActionBar().hide();

        //flashScreen = findViewById(R.id.flash_screen);

        scoreText = findViewById(R.id.score_text);
        timerText = findViewById(R.id.timer_text);
        gameLayout = findViewById(R.id.game_fragment);
        timer = new Timer(10000, timerText) {
            @Override
            public void finish() {
                GameOverFragment fragment = new GameOverFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                fragment.setArguments(bundle);
                transaction.replace(R.id.game_over_window, fragment);
                transaction.commit();
            }
        };

        startGame(getIntent().getIntExtra("gameNumber", 1));
    }

    private void startGame(int gameNumber){
        timer.run();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (gameNumber){
            case 1:
                fragment = new SchulteTableGameFragment();
                break;
            case 2:
                fragment = new RepeatDrawingGameFragment();
                break;
            case 3:
                fragment = new CalculateExpressionGameFragment();
                break;
        }

        ft.replace(gameLayout.getId(), fragment);
        ft.commit();
    }

    public void updateScore(){
        if(fragment instanceof SchulteTableGameFragment){
            score += ((SchulteTableGameFragment)fragment).getGamePoints();
        }else if(fragment instanceof RepeatDrawingGameFragment){
            score+=((RepeatDrawingGameFragment)fragment).getGamePoints();
        }else{
            int gamePoints = ((CalculateExpressionGameFragment)fragment).getGamePoints();
            score = score + gamePoints < gamePoints ? 0 : score + gamePoints;
        }

        scoreText.setText(String.valueOf(score));
    }

//    private void screenAnimationPlay(){
//        AnimationDrawable animationDrawable = (AnimationDrawable) flashScreen.getBackground();
//        animationDrawable.setEnterFadeDuration(1000);
//        animationDrawable.setExitFadeDuration(1000);
//        animationDrawable.start();
//    }

    public void setTimerPaused(boolean paused){
        if(paused) timer.pause();
        else timer.run();
    }

    public void setFlashScreen(int id){
        flashScreen.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.pause();
    }

}