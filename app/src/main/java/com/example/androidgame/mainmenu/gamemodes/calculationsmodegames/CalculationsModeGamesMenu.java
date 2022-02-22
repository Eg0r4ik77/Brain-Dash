package com.example.androidgame.mainmenu.gamemodes.calculationsmodegames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidgame.mainmenu.gamemodes.TransitionActivity;
import com.example.androidgame.mainmenu.gamemodes.calculationsmodegames.calculateexpressionlevel.CalculateExpressionLevel;
import com.example.androidgame.R;

public class CalculationsModeGamesMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations_mode_games_menu);

        Button calculateExpressionGameButton = findViewById(R.id.calculate_expression_game);
        calculateExpressionGameButton.setOnClickListener(v -> {
            try{
                Intent intent = new Intent(this, TransitionActivity.class);
                intent.putExtra("game rules",
                        "На игру отводится 60 секунд. Постарайтесь решить как можно больше примеров.");
                startActivity(intent);
                finish();
            }catch (Exception exception){
                Toast.makeText(getBaseContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}