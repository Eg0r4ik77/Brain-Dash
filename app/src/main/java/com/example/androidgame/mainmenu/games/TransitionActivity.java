package com.example.androidgame.mainmenu.games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.calculationsmodegames.calculateexpressiongame.CalculateExpressionGame;

public class TransitionActivity extends AppCompatActivity {

    private Timer timer;

    private TextView timerText;
    private TextView rulesText;

    private Button startButton;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        intent = new Intent(this, CalculateExpressionGame.class);

        timerText = findViewById(R.id.transition_timer_text);
        rulesText = findViewById(R.id.rules_text);
        startButton = findViewById(R.id.start_calculate_expression_level);
        timer = new Timer(4000, timerText) {
            @Override
            public void finish() {
                startLevel();
            }
        };

        Intent intent1 = getIntent();

        startButton.setOnClickListener(v -> {
            startButton.setClickable(false);
            timer.run();
        });
    }

    public void startLevel(){
        try{
            startActivity(intent);
            finish();
        }catch (Exception exception){
            Toast.makeText(getBaseContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }
}