package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class GameModesMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_modes_menu);

        Button calculationsModeButton = findViewById(R.id.calculations_mode);
        calculationsModeButton.setOnClickListener(v -> {
            try{
                Intent intent = new Intent(this, CalculationsModeGamesMenu.class);
                startActivity(intent);
                finish();
            }catch (Exception exception){
                Toast.makeText(getBaseContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}