package com.example.androidgame;

import java.util.HashMap;
import java.util.Map;

public class Expression {
    private final int firstOperand;
    private final int secondOperand;
    private final Operation operation;
    Map<Operation, Integer> actions = new HashMap<Operation, Integer>();

    public Expression(int firstOperand, int secondOperand, Operation operation) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.operation = operation;

        actions.put(Operation.ADDITION, this.firstOperand+this.secondOperand);
        actions.put(Operation.SUBTRACTION, this.firstOperand-this.secondOperand);
    }

    //не ну а как еще
    public int getSolution(){
        Integer solution = actions.get(operation);
        if(solution == null){
            return 0;
        }
        return solution;
    }

    // проработать Operation
    @Override
    public String toString() {
        return firstOperand + " + " + secondOperand + " = ";
    }
}