package com.example.androidgame.mainmenu.games.shultetablegame;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.gamecomplicators.SchulteTableGameComplicator;
import com.example.androidgame.mainmenu.games.GamePanel;


public class SchulteTableGameFragment extends Fragment {


    private SchulteTable schulteTable;
    private TableLayout tableLayout;
    private int currentNumber;

    private final int[] gamePoints = {20, 50, 100};

    private SchulteTableGameComplicator gameComplicator;

    private Button[][] buttons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_schulte_table_game, container, false);

        tableLayout = view.findViewById(R.id.schulte_table_layout);

        schulteTable = new SchulteTable();
        gameComplicator = new SchulteTableGameComplicator(schulteTable);

        create();

        return view;
    }

    private void applyChoice(Button button){
        int buttonNumber = Integer.valueOf(button.getText().toString());
        if(buttonNumber - currentNumber == 1){
            currentNumber++;
            button.setBackgroundColor(Color.GREEN);
        }
        if(currentNumber == buttons.length*buttons.length){
            ((GamePanel)getActivity()).updateScore();

            tableLayout.removeAllViews();
            gameComplicator.complicateGame();
            create();
        }
    }

    private void create(){
        currentNumber = 0;
        schulteTable.generate();
        buttons = new Button[schulteTable.getTable().length][schulteTable.getTable().length];

        for(int i = 0; i<buttons.length; i++){
            TableRow row = new TableRow(tableLayout.getContext());
            for(int j = 0; j<buttons.length; j++){
                buttons[i][j] = new Button(tableLayout.getContext());
                Button button = buttons[i][j];

                button.setText(String.valueOf(schulteTable.getTable()[i][j]));

                button.setOnClickListener(v -> {
                    applyChoice(button);
                });
                row.addView(buttons[i][j]);
            }
            tableLayout.addView(row);
        }
    }

    public int getGamePoints(){
        switch (buttons.length){
            case 2:
                return gamePoints[0];
            case 3:
                return gamePoints[1];
            case 4:
                return gamePoints[2];
            default:
                break;
        }
        return 0;
    }

}