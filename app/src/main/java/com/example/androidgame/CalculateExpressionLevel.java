package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CalculateExpressionLevel extends AppCompatActivity {

    private TextView expressionText;
    private TextView solutionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_template);

        Intent intent = getIntent();
        String new_level = intent.getStringExtra("new_level");
        if(new_level == null){
            finish();
        }

        Button[] number_buttons = {
                findViewById(R.id.button_0),
                findViewById(R.id.button_1),
                findViewById(R.id.button_2),
                findViewById(R.id.button_3),
                findViewById(R.id.button_4),
                findViewById(R.id.button_5),
                findViewById(R.id.button_6),
                findViewById(R.id.button_7),
                findViewById(R.id.button_8),
                findViewById(R.id.button_9)
        };

        expressionText = findViewById(R.id.expression_text);
        solutionText = findViewById(R.id.solution_text);

        Expression expression = getRandomExpression();
        expressionText.setText(expression.toString());

        for (Button button : number_buttons) {
            button.setOnClickListener(v -> {
                solutionText.setText(solutionText.getText().toString()
                        +button.getText().toString());
            });
        }

        Button cleanButton = findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(v -> {
          solutionText.setText(null);
        });

        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> {
            //есть случай, когда в solutionText не лежит строка
            solutionText.setText(checkSolution(expression,
                    Integer.parseInt(solutionText.getText().toString())));
        });

    }

    public Expression getRandomExpression(){
        Random randomValue = new Random();
        return new Expression(Math.abs(randomValue.nextInt()%100),
                Math.abs(randomValue.nextInt()%100),
                Operation.values()[Math.abs(randomValue.nextInt()%2)]);
    }

    public String checkSolution(Expression expression, int answer){
         if(answer == expression.getSolution()){
             return "ПРАВИЛЬНО!";
         }
         return "НЕПРАВИЛЬНО!";
    }
}