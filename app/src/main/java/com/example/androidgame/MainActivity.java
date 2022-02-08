package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button trainingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingButton = findViewById(R.id.training_button);
        trainingButton.setOnClickListener(v -> {
            Intent startCalculateExpressionLevel = new Intent(this, CalculateExpressionLevel.class);
            startCalculateExpressionLevel.putExtra("new_level", "calculate_expression_level");
            startActivity(startCalculateExpressionLevel);
        });
    }
}