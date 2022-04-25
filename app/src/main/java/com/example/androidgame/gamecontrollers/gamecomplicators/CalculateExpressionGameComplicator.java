package com.example.androidgame.gamecontrollers.gamecomplicators;

import com.example.androidgame.mainmenu.games.calculateexpressiongame.Expression;

public class CalculateExpressionGameComplicator extends GameComplicator {

    private final Expression expression;

    private final int mediumLevelDifficulty = 10;
    private final int hardLevelDifficulty = 30;

    public CalculateExpressionGameComplicator(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        switch (getGameDifficulty()){
            case mediumLevelDifficulty:
                expression.setOperandsUpperBounds(100, 10);
                break;
            case hardLevelDifficulty:
                expression.setOperandsUpperBounds(100, 100);
                break;
            default:
                break;
        }
    }
}
