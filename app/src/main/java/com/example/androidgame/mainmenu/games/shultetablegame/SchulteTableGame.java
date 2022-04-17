package com.example.androidgame.mainmenu.games.shultetablegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.gamecontrollers.gamecomlicators.SchulteTableGameComplicator;
import com.example.androidgame.mainmenu.games.GameOverFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SchulteTableGame extends AppCompatActivity {

    private SchulteTable schulteTable;
    private TableLayout tableLayout;
    private int currentNumber;

    private int score;
    private final int[] gamePoints = {20, 50, 100};

    private SchulteTableGameComplicator gameComplicator;
    private Timer timer;

    private Button[][] buttons;

    private TextView timerText;
    private TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schulte_table_game);

        timerText = findViewById(R.id.timer1_text);
        scoreText = findViewById(R.id.score1_text);

        tableLayout = findViewById(R.id.schulte_table_place);

        schulteTable = new SchulteTable();
        gameComplicator = new SchulteTableGameComplicator(schulteTable);

        timer = new Timer(10000, timerText) {
            @Override
            public void finish() {
                GameOverFragment fragment = new GameOverFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                fragment.setArguments(bundle);
                transaction.replace(R.id.game1_over_window, fragment);
                transaction.commit();
            }
        };
        timer.run();
        create();
    }

    private void applyChoice(Button button){
        int buttonNumber = Integer.valueOf(button.getText().toString());
        if(buttonNumber - currentNumber == 1){
            currentNumber++;
            button.setBackgroundColor(Color.GREEN);
        }
        if(currentNumber == buttons.length*buttons.length){
            score += getGamePoints();
            scoreText.setText(String.valueOf(score));
            tableLayout.removeAllViews();
            gameComplicator.complicateGame();
            create();
        }
    }

    private void create(){
        currentNumber = 0;
        schulteTable.generate();
        buttons = new Button[schulteTable.getTable().length][schulteTable.getTable().length];

        for(int i = 0; i<buttons.length; i++){
            TableRow row = new TableRow(tableLayout.getContext());
            for(int j = 0; j<buttons.length; j++){
                buttons[i][j] = new Button(tableLayout.getContext());
                Button button = buttons[i][j];

                button.setText(String.valueOf(schulteTable.getTable()[i][j]));

                button.setOnClickListener(v -> {
                    applyChoice(button);
                });
                row.addView(buttons[i][j]);
            }
            tableLayout.addView(row);
        }
    }

    private int getGamePoints(){
        switch (buttons.length){
            case 2:
                return gamePoints[0];
            case 3:
                return gamePoints[1];
            case 4:
                return gamePoints[2];
            default:
                break;
        }
        return 0;
    }

}