package com.example.androidgame.gamecontrollers.levelcomlicators;

import com.example.androidgame.mainmenu.games.calculationsmodegames.calculateexpressiongame.Expression;

public class CalculateExpressionLevelComplicator extends LevelComplicator {

    private final Expression expression;

    private final int easyLevelDifficulty = 0;
    private final int mediumLevelDifficulty = 10;
    private final int hardLevelDifficulty = 30;

    public CalculateExpressionLevelComplicator(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void complicateLevel() {
        incrementLevelDifficulty();
        switch (getLevelDifficulty()){
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
