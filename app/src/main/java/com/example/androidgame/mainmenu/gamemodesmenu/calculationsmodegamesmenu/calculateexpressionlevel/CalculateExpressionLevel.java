package com.example.androidgame.mainmenu.gamemodesmenu.calculationsmodegamesmenu.calculateexpressionlevel;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.levelcomlicators.CalculateExpressionLevelComplicator;
import com.example.androidgame.gamecontrollers.Timer;

public class CalculateExpressionLevel extends AppCompatActivity {

    private TextView expressionText;
    private TextView solutionText;
    private TextView pointsText;
    private String currentSolutionText = "";

    private Timer timer;
    private LinearLayout flashScreen;

    private int points;

    private Expression expression;

    private final int[] expressionDifficulties = {18, 108, 198};
    private final int[] levelPoints = {20, 50, 100};

    private CalculateExpressionLevelComplicator levelComplicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_expression_level);

        TextView timerText = findViewById(R.id.timer_text);
        expressionText = findViewById(R.id.expression_text);
        solutionText = findViewById(R.id.solution_text);
        pointsText = findViewById(R.id.points_text);
        timer = new Timer(timerText);
        flashScreen = findViewById(R.id.flash_screen);

        createExpression();
        levelComplicator = new CalculateExpressionLevelComplicator(expression);

        pointsText.setText(String.valueOf(points));

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

        for (Button button : number_buttons) {
            button.setOnClickListener(v -> {
                currentSolutionText += button.getText().toString();
                solutionText.setText(currentSolutionText);
            });
        }

        Button cleanButton = findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(v -> resetSolution());

        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> {
            int levelPoints = getLevelPoints();
            try {
                if (checkAnswer(expression, Integer.parseInt(solutionText.getText().toString()))){
                    flashScreen.setBackgroundResource(R.drawable.right_answer_anim);
                    levelComplicator.complicateLevel();
                    points += levelPoints;
                }
                else{
                    points = points < levelPoints ? 0 : points - levelPoints;
                    flashScreen.setBackgroundResource(R.drawable.wrong_answer_anim);
                }
                pointsText.setText(String.valueOf(points));
            }catch (Exception exception){
                flashScreen.setBackgroundResource(R.drawable.wrong_answer_anim);
            }
            updateLevel();

        });

    }

    private void resetSolution(){
        solutionText.setText(null);
        currentSolutionText = "";
    }

    private void updateLevel(){
        screenAnimationPlay();
        updateExpression();
        resetSolution();
    }

    private void screenAnimationPlay(){
        AnimationDrawable animationDrawable = (AnimationDrawable) flashScreen.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
    }

    private void createExpression(){
        expression = new Expression();
        expression.setOperandsUpperBounds(10, 10);
        updateExpression();
    }

    private void updateExpression(){
        expression.update();
        presentExpression();
    }

    private boolean checkAnswer(Expression expression, int answer){
        return expression.getSolution() == answer;
    }
    private void presentExpression(){
        expressionText.setText(expression.toString());
    }

    private int getLevelPoints(){
        int solution = expression.getSolution();

        for(int i = 0; i< expressionDifficulties.length; i++){
            if(solution <= expressionDifficulties[i]){
                return levelPoints[i];
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.pause();
    }
}