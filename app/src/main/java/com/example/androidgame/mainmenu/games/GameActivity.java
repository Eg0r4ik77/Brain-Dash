package com.example.androidgame.mainmenu.games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.calculateexpressiongame.CalculateExpressionGameFragment;
import com.example.androidgame.mainmenu.games.repeatdrawinggame.RepeatDrawingGameFragment;
import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTableGameFragment;

public class GameActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private TextView scoreText;
    private TextView timerText;

    private FrameLayout gameLayout;
    private ProgressBar progressBar;

    public Timer timer;
    private int score;

    private Fragment fragment;

    private FrameLayout flashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        //flashScreen = findViewById(R.id.flash_screen);
        progressBar = findViewById(R.id.progress_bar);
        scoreText = findViewById(R.id.score_text);
        timerText = findViewById(R.id.timer_text);
        gameLayout = findViewById(R.id.game_fragment);
        timer = new Timer(11000, timerText, progressBar) {
            @Override
            public void finish() {
                handleGameResult();
               progressBar.setProgress(100);
               new Handler().postDelayed((Runnable) () -> {
                   progressBar.setProgress(0);
               }, 1000);

                GameOverFragment fragment = new GameOverFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game_over_window, fragment)
                        .commit();
            }
        };

        startGame(getIntent().getIntExtra("gameNumber", 1));
    }

    @SuppressLint("SetTextI18n")
    private void startGame(int gameNumber){
        scoreText.setText(getResources().getString(R.string.game_score_text) + score);

        timer.run();
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(gameLayout.getId(), fragment)
                .commit();
    }

    @SuppressLint("SetTextI18n")
    public void updateScore(){
        if(fragment instanceof SchulteTableGameFragment){
            score += ((SchulteTableGameFragment)fragment).getGamePoints();
        }else if(fragment instanceof RepeatDrawingGameFragment){
            score+=((RepeatDrawingGameFragment)fragment).getGamePoints();
        }else{
            int gamePoints = ((CalculateExpressionGameFragment)fragment).getGamePoints();
            score = Math.max(score + gamePoints, 0);
        }
        scoreText.setText(getResources().getString(R.string.game_score_text)+ score);
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

    public void handleGameResult(){
        sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(fragment instanceof SchulteTableGameFragment){
            editor.putInt("SchulteTableGameBestScore",
                    Math.max(score,
                            sharedPreferences.getInt("SchulteTableGameBestScore",0)));
        }else if(fragment instanceof RepeatDrawingGameFragment){
            editor.putInt("RepeatDrawingGameBestScore",
                    Math.max(score,
                            sharedPreferences.getInt("RepeatDrawingGameBestScore",0)));
        }else{
            editor.putInt("CalculateExpressionGameBestScore",
                    Math.max(score,
                            sharedPreferences.getInt("CalculateExpressionGameBestScore",0)));
        }
        editor.putInt("Rating", score + sharedPreferences.getInt("Rating",0));
        editor.commit();
    }

    public int getBestScore(){
        sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);
        if(fragment instanceof SchulteTableGameFragment){
            return sharedPreferences.getInt("SchulteTableGameBestScore",0);
        }else if(fragment instanceof RepeatDrawingGameFragment){
            return sharedPreferences.getInt("RepeatDrawingGameBestScore",0);
        }else{
            return sharedPreferences.getInt("CalculateExpressionGameBestScore",0);
        }
    }
}