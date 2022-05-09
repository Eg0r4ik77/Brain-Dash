package com.sumsung.minigames.mainmenu.games;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
import com.sumsung.minigames.gamecontrollers.Timer;
import com.sumsung.minigames.mainmenu.MusicService;
import com.sumsung.minigames.mainmenu.games.calculateexpressiongame.CalculateExpressionGameFragment;
import com.sumsung.minigames.mainmenu.games.repeatdrawinggame.RepeatDrawingGameFragment;
import com.sumsung.minigames.mainmenu.games.shultetablegame.SchulteTableGameFragment;
import com.sumsung.minigames.models.User;

public class GameActivity extends AppCompatActivity {

    private SharedPreferences soundSharedPreferences;

    private MediaPlayer gameButtonSound;
    private MediaPlayer gameOverSound;
    private MediaPlayer menuButtonSound;

    private User user;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

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

        soundSharedPreferences = getSharedPreferences("Sound", MODE_PRIVATE);


        gameOverSound = MediaPlayer.create(this, R.raw.sound_game_over);
        menuButtonSound = MediaPlayer.create(this, R.raw.sound_menu_button);

        flashScreen = findViewById(R.id.flash_screen);
        flashScreen.setBackgroundResource(R.drawable.right_answer_anim);

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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


               playGameOverSound();

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

        if(firebaseUser != null){
            databaseReference.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        user = task.getResult().getValue(User.class);
                    }else{
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
        }


        startGame(getIntent().getIntExtra("gameNumber", 1));
    }

    @SuppressLint("SetTextI18n")
    private void startGame(int gameNumber){
        scoreText.setText(getResources().getString(R.string.game_score) + score);

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
        screenAnimationPlay();
        if(fragment instanceof SchulteTableGameFragment){
            score += ((SchulteTableGameFragment)fragment).getGamePoints();
        }else if(fragment instanceof RepeatDrawingGameFragment){
            score += ((RepeatDrawingGameFragment)fragment).getGamePoints();
        }else{
            score += ((CalculateExpressionGameFragment)fragment).getGamePoints();
        }
        scoreText.setText(getResources().getString(R.string.game_score)+ score);
    }

    private void screenAnimationPlay(){
        Drawable current = flashScreen.getBackground();
        if(current instanceof AnimationDrawable){
            AnimationDrawable animationDrawable = (AnimationDrawable) current;
            animationDrawable.setEnterFadeDuration(1000);
            animationDrawable.setExitFadeDuration(1000);
            animationDrawable.start();
        }
    }

    public void setTimerPaused(boolean paused){
        if(paused) timer.pause();
        else timer.run();
    }

    public void handleGameResult(){
        if(firebaseUser == null){
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
            editor.commit();
        }else{
            if(fragment instanceof SchulteTableGameFragment){
                databaseReference.child(firebaseUser.getUid()).child("record1")
                        .setValue(Math.max(score, user.getRecord1()));
            }else if(fragment instanceof RepeatDrawingGameFragment){
                databaseReference.child(firebaseUser.getUid()).child("record2")
                        .setValue(Math.max(score, user.getRecord2()));
            }else{
                databaseReference.child(firebaseUser.getUid()).child("record3")
                        .setValue(Math.max(score, user.getRecord3()));
            }
            databaseReference.child(firebaseUser.getUid()).child("rating").setValue(score+user.getRating());
        }
    }


    public int getBestScore(){
        if(firebaseUser == null){
            sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);
            if(fragment instanceof SchulteTableGameFragment){
                return sharedPreferences.getInt("SchulteTableGameBestScore",0);
            }else if(fragment instanceof RepeatDrawingGameFragment){
                return sharedPreferences.getInt("RepeatDrawingGameBestScore",0);
            }else{
                return sharedPreferences.getInt("CalculateExpressionGameBestScore",0);
            }
        }else{
            if(fragment instanceof SchulteTableGameFragment){
                return user.getRecord1();
            }else if(fragment instanceof RepeatDrawingGameFragment){
                return user.getRecord2();
            }else{
                return user.getRecord3();
            }
        }
    }

    public void playGameButtonSound(){
        if(soundSharedPreferences.getInt("On", 1) == 0) return;
        gameButtonSound = MediaPlayer.create(this, R.raw.sound_game_button);
        gameButtonSound.setOnCompletionListener(mediaPlayer -> mediaPlayer.reset());
        gameButtonSound.setVolume(0.2f, 0.2f);
        gameButtonSound.start();
    }
    public void playGameOverSound(){
        if(soundSharedPreferences.getInt("On", 1) == 0) return;
        stopService(new Intent(this, MusicService.class));
        gameOverSound.start();
    }
    public void playMenuButtonSound(){
        if(soundSharedPreferences.getInt("On", 1) == 0) return;
        menuButtonSound.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(soundSharedPreferences.getInt("On", 1) == 1){
            startService(new Intent(this, MusicService.class).putExtra("Music", R.raw.music_background_game));
        }
    }

    @Override
    public void onBackPressed() {}
}