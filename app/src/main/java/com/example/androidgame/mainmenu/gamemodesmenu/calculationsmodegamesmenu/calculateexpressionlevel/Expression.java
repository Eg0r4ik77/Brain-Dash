package com.example.androidgame.mainmenu.gamemodesmenu.calculationsmodegamesmenu.calculateexpressionlevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Expression {
    private final Operand firstOperand;
    private final Operand secondOperand;
    private Operation operation;
    Map<Operation, Integer> actions = new HashMap<Operation, Integer>();

    public Expression() {
        firstOperand = new Operand();
        secondOperand = new Operand();
    };

    public int getSolution(){

        actions.put(Operation.ADDITION, firstOperand.getValue()+secondOperand.getValue());
        actions.put(Operation.SUBTRACTION, firstOperand.getValue()-secondOperand.getValue());

        Integer solution = actions.get(operation);
        if(solution == null){
            return 0;
        }
        return solution;
    }

    @Override
    public String toString() {
        char sign;
        switch (operation){
            case ADDITION:
                sign = '+';
                break;
            case SUBTRACTION:
                sign = '-';
                break;
            default:
                sign = ' ';
        }
        return firstOperand + " " + sign + " " + secondOperand + " = ";
    }

    private void swapOperandValues(){
        int tmp = firstOperand.getValue();
        firstOperand.setValue(secondOperand.getValue());
        secondOperand.setValue(tmp);
    }

    public void setOperandsUpperBounds(int firstUpperBound, int secondUpperBound){
        firstOperand.setUpperBound(firstUpperBound);
        secondOperand.setUpperBound(secondUpperBound);
    }

    public void update(){
        Random randomValue = new Random();
        firstOperand.setValue(Math.abs(randomValue.nextInt()% firstOperand.getUpperBound()));
        secondOperand.setValue(Math.abs(randomValue.nextInt()% secondOperand.getUpperBound()));
        operation = Operation.values()[Math.abs(randomValue.nextInt()%2)];

        if(secondOperand.getValue() > firstOperand.getValue() && operation == Operation.SUBTRACTION){
            swapOperandValues();
        }
    }
}