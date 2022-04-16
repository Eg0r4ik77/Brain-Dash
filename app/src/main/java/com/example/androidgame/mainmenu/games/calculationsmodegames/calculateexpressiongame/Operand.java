package com.example.androidgame.mainmenu.games.calculationsmodegames.calculateexpressiongame;

public class Operand {

    private int value;
    private int upperBound;

    public Operand() {}

    public Operand(int value) {
        this.value = value;
    }

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

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}