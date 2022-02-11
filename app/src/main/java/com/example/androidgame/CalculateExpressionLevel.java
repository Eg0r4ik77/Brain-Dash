package com.example.androidgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class CalculateExpressionLevel extends AppCompatActivity {

    private TextView expressionText;
    private TextView solutionText;

    private Expression expression;

    private LevelComplicator levelComplicator = new LevelComplicator() {
        @Override
        public void complicateLevel() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_expression_level);

        // секундомер
        // усложнение

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

        presentRandomExpression();

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
            try {
                if (checkAnswer(expression, Integer.parseInt(solutionText.getText().toString()))){
                    solutionText.setBackgroundResource(R.drawable.right_answer_anim);
                }
                else{
                    solutionText.setBackgroundResource(R.drawable.wrong_answer_anim);
                }
            }catch (Exception exception){
                solutionText.setBackgroundResource(R.drawable.wrong_answer_anim);
            }

            presentRandomExpression();
            solutionText.setText(null);
            AnimationDrawable animationDrawable = (AnimationDrawable) solutionText.getBackground();
            animationDrawable.setEnterFadeDuration(500);
            animationDrawable.setExitFadeDuration(500);
            animationDrawable.start();
        });

    }

    public Expression getRandomExpression(){
        Random randomValue = new Random();
        return new Expression(Math.abs(randomValue.nextInt()%100),
                Math.abs(randomValue.nextInt()%100),
                Operation.values()[Math.abs(randomValue.nextInt()%2)]);
    }

    public boolean checkAnswer(Expression expression, int answer){
        return expression.getSolution() == answer;
    }
    public void presentRandomExpression(){
        expression = getRandomExpression();
        expressionText.setText(expression.toString());
    }

}