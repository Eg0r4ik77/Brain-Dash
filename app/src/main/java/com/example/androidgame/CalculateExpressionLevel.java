package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        expressionText = findViewById(R.id.expression_text);
        solutionText = findViewById(R.id.solution_text);

        Expression expression = getRandomExpression();
        expressionText.setText(expression.toString());
        solutionText.setText(String.valueOf(expression.getSolution()));

    }

    public Expression getRandomExpression(){
        Random randomValue = new Random();
        //продумать рандомное значение Operation
        return new Expression(Math.abs(randomValue.nextInt()%100),
                Math.abs(randomValue.nextInt()%100),
                Operation.ADDITION);
    }


}