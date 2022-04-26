package com.example.androidgame.mainmenu.games.calculateexpressiongame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.gamecomplicators.CalculateExpressionGameComplicator;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.GameOverFragment;

public class CalculateExpressionGame extends AppCompatActivity {

    private TextView expressionText;
    private TextView solutionText;
    private TextView scoreText;
    private String currentSolutionText = "";

    private Timer timer;
    private LinearLayout flashScreen;

    private int score;

    private Expression expression;

    private final int[] expressionDifficulties = {18, 108, 198};
    private final int[] gamePoints = {20, 50, 100};

    private CalculateExpressionGameComplicator gameComplicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_expression_game);

        TextView timerText = findViewById(R.id.timer3_text);
        expressionText = findViewById(R.id.expression_text);
        solutionText = findViewById(R.id.solution_text);
        scoreText = findViewById(R.id.score3_text);


        timer = new Timer(10000, timerText) {
            @Override
            public void finish() {
                GameOverFragment fragment = new GameOverFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putInt("score", score);

                fragment.setArguments(bundle);
                //transaction.replace(R.id.game3_over_window, fragment);
                transaction.commit();
            }
        };


        createExpression();
        gameComplicator = new CalculateExpressionGameComplicator(expression);

        scoreText.setText(String.valueOf(score));

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
            int levelPoints = getGamePoints();
            try {
                if (checkAnswer(expression, Integer.parseInt(solutionText.getText().toString()))){
                    flashScreen.setBackgroundResource(R.drawable.right_answer_anim);
                    gameComplicator.complicateGame();
                    score += levelPoints;
                }else{
                    score = score < levelPoints ? 0 : score - levelPoints;
                    flashScreen.setBackgroundResource(R.drawable.wrong_answer_anim);
                }
            }catch (Exception exception){
                score = score < levelPoints ? 0 : score - levelPoints;
                flashScreen.setBackgroundResource(R.drawable.wrong_answer_anim);
            }
            scoreText.setText(String.valueOf(score));
            updateGame();
        });

    }

    private void resetSolution(){
        solutionText.setText(null);
        currentSolutionText = "";
    }

    private void updateGame(){
        screenAnimationPlay();
        generateExpression();
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
        generateExpression();
    }

    private void generateExpression(){
        expression.generate();
        presentExpression();
    }

    private boolean checkAnswer(Expression expression, int answer){
        return expression.getSolution() == answer;
    }
    private void presentExpression(){
        expressionText.setText(expression.toString());
    }

    private int getGamePoints(){
        int solution = expression.getSolution();

        for(int i = 0; i< expressionDifficulties.length; i++){
            if(solution <= expressionDifficulties[i]){
                return gamePoints[i];
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