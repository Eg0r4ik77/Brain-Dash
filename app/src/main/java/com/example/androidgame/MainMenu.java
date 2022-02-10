package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    private Button trainingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        trainingButton = findViewById(R.id.training_button);
        trainingButton.setOnClickListener(v -> {
            try{
                Intent intent = new Intent(this, GameModesMenu.class);
                startActivity(intent);
                finish();
            }catch (Exception exception){
                Toast.makeText(getBaseContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}