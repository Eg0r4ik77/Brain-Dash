package com.example.androidgame.mainmenu.games.calculateexpressiongame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.gamecomplicators.CalculateExpressionGameComplicator;
import com.example.androidgame.mainmenu.games.GameActivity;

public class CalculateExpressionGameFragment extends Fragment {
    private final int[] expressionDifficulties = {18, 108, 198};
    private final int[] gamePoints = {20, 50, 100};

    private TextView expressionText;
    private TextView solutionText;
    private String currentSolutionText;

    private Expression expression;
    private CalculateExpressionGameComplicator gameComplicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculate_expression_game, container, false);
        view.setClickable(true);

        expressionText = view.findViewById(R.id.expression_text);
        solutionText = view.findViewById(R.id.solution_text);

        generateFirstExpression();
        gameComplicator = new CalculateExpressionGameComplicator(expression);

        Button[] number_buttons = {
                view.findViewById(R.id.button_0),
                view.findViewById(R.id.button_1),
                view.findViewById(R.id.button_2),
                view.findViewById(R.id.button_3),
                view.findViewById(R.id.button_4),
                view.findViewById(R.id.button_5),
                view.findViewById(R.id.button_6),
                view.findViewById(R.id.button_7),
                view.findViewById(R.id.button_8),
                view.findViewById(R.id.button_9)
        };

        for (Button button : number_buttons) {
            button.setOnClickListener(v -> {
                currentSolutionText = currentSolutionText == null
                        ? button.getText().toString()
                        : currentSolutionText + button.getText().toString();
                solutionText.setText(currentSolutionText);
            });
        }

        Button cleanButton = view.findViewById(R.id.clean_button);
        cleanButton.setOnClickListener(v -> resetSolution());

        Button commitButton = view.findViewById(R.id.commit_button);
        commitButton.setOnClickListener(v -> {
            if(solutionText.getText().toString().isEmpty()){
                return;
            }
            if (getGamePoints() > 0) {
                gameComplicator.complicateGame();
            }
            updateGame();
        });
        return view;
    }

    private void resetSolution(){
        solutionText.setText(null);
        currentSolutionText = "";
    }

    private void updateGame(){
        if(getActivity() instanceof GameActivity){
            ((GameActivity)getActivity()).updateScore();
        }
        generateExpression();
        resetSolution();
    }


    private void generateFirstExpression(){
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

    public int getGamePoints(){
        int solution = expression.getSolution();
        int points = 0;
        for(int i = 0; i< expressionDifficulties.length; i++){
            if(solution <= expressionDifficulties[i]){
                points = gamePoints[i];
                break;
            }
        }
        if(checkAnswer(expression, Integer.parseInt(solutionText.getText().toString()))){
            return points;
        }else{
            return -points;
        }
    }

}