package com.example.androidgame.mainmenu.games.repeatdrawinggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidgame.R;

import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.gamecontrollers.gamecomplicators.GameComplicator;
import com.example.androidgame.gamecontrollers.gamecomplicators.RepeatDrawingGameComplcator;
import com.example.androidgame.mainmenu.games.GameOverFragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.TimerTask;

public class RepeatDrawingGame extends AppCompatActivity {

    private Drawing drawing = new Drawing();
    private TableLayout drawingLayout;
    private boolean[][] drawingFlags;
    private Button[][] tiles;

    Timer timer;

    private TextView timerText;
    private TextView scoreText;

    private int score;

    private int selectedTilesCount;
    private int correctlySelectedTilesCount;

    private GameComplicator gameComplicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_drawing_game);

        gameComplicator = new RepeatDrawingGameComplcator(drawing);
        scoreText = findViewById(R.id.score2_text);
        timerText = findViewById(R.id.timer2_text);
        timer = new Timer(20000, timerText) {
            @Override
            public void finish() {
                GameOverFragment fragment = new GameOverFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                fragment.setArguments(bundle);
                transaction.replace(R.id.game2_over_window, fragment);
                transaction.commit();
            }
        };
        timer.run();
        drawingLayout = findViewById(R.id.drawing_layout);
        play();
    }

    private void play(){
        scoreText.setText(String.valueOf(score));
        drawing.create();
        drawingFlags = drawing.getDrawing();
        selectedTilesCount = correctlySelectedTilesCount = 0;
        showDrawing();

        timer.pause();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                timer.run();
                clearDrawing();
            }
        },2500);
    }

    private void clearDrawing(){
        for(int i = 0; i< tiles.length; i++){
            for(int j =0; j< tiles.length; j++){
               tiles[i][j].setBackgroundColor(Color.WHITE);
            }
        }
        setButtonsClickable(true);
    }

    private void setButtonsClickable(boolean clickable){
        for(int i = 0; i< drawingFlags.length; i++){
            for(int j = 0; j < drawingFlags.length; j++) {
                tiles[i][j].setClickable(clickable);
            }
        }
    }

    private void showDrawing(){
        tiles = new Button[drawingFlags.length][drawingFlags.length];
        drawingLayout.removeAllViews();
        for(int i = 0; i<drawingFlags.length; i++){
            TableRow row = new TableRow(drawingLayout.getContext());
            for(int j = 0; j<drawingFlags.length; j++){
                tiles[i][j] = new Button(drawingLayout.getContext());

                Button button = tiles[i][j];
                button.setBackgroundColor(Color.WHITE);
                boolean drawingFlag = drawingFlags[i][j];

                if(drawingFlag) {
                    button.setBackgroundColor(Color.BLUE);
                }

                button.setOnClickListener(v -> {
                    int color = ((ColorDrawable)button.getBackground()).getColor();
                    if(color == Color.WHITE){
                        button.setBackgroundColor(Color.BLUE);
                        selectedTilesCount++;
                        if(drawingFlag){
                            correctlySelectedTilesCount++;
                        }
                    }else{
                        button.setBackgroundColor(Color.WHITE);
                        selectedTilesCount--;
                        if(drawingFlag){
                            correctlySelectedTilesCount--;
                        }
                    }

                    if(correctlySelectedTilesCount == drawing.drawingTilesCount
                            && correctlySelectedTilesCount == selectedTilesCount){
                            score += getGamePoints();
                            gameComplicator.complicateGame();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    play();
                                }
                            },500);
                    }
                });

                row.addView(tiles[i][j]);
            }
            drawingLayout.addView(row);
        }
        setButtonsClickable(false);
    }

    private int getGamePoints(){
        return 10 * correctlySelectedTilesCount;
    }
}