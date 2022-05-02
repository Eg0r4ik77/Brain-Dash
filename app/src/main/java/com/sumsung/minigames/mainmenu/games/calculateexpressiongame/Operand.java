package com.sumsung.minigames.mainmenu.games.calculateexpressiongame;

import androidx.annotation.NonNull;

public class Operand {

    private int value;
    private int upperBound;

    public Operand() {}

    public int getUpperBound() {
        return upperBound;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUpperBound(int upperBound) {
        this.upperBound = upperBound;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}