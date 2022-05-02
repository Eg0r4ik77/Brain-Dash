package com.sumsung.minigames.gamecontrollers.gamecomplicators;

import com.sumsung.minigames.mainmenu.games.calculateexpressiongame.Expression;

public class CalculateExpressionGameComplicator extends GameComplicator {

    private final Expression expression;

    private static final int MEDIUM_LEVEL_DIFFICULTY = 5;
    private static final int HARD_LEVEL_DIFFICULTY = 15;

    public CalculateExpressionGameComplicator(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void complicateGame() {
        incrementGameDifficulty();
        switch (getGameDifficulty()){
            case MEDIUM_LEVEL_DIFFICULTY:
                expression.setOperandsUpperBounds(100, 10);
                break;
            case HARD_LEVEL_DIFFICULTY:
                expression.setOperandsUpperBounds(100, 100);
                break;
            default:
                break;
        }
    }
}
