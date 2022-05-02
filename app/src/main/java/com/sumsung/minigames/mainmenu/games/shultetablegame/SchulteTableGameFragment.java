package com.sumsung.minigames.mainmenu.games.shultetablegame;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.sumsung.minigames.R;
import com.sumsung.minigames.gamecontrollers.gamecomplicators.SchulteTableGameComplicator;
import com.sumsung.minigames.mainmenu.games.GameActivity;


public class SchulteTableGameFragment extends Fragment {


    private SchulteTable schulteTable;
    private TableLayout tableLayout;
    private int currentNumber;

    private final int[] gamePoints = {20, 50, 100};

    private SchulteTableGameComplicator gameComplicator;

    private Button[][] buttons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_schulte_table_game, container, false);
        view.setClickable(true);

        tableLayout = view.findViewById(R.id.schulte_table_layout);
        schulteTable = new SchulteTable();
        gameComplicator = new SchulteTableGameComplicator(schulteTable);

        create();

        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void applyChoice(Button button){
        int buttonNumber = Integer.parseInt(button.getText().toString());

        if(buttonNumber == currentNumber){
            currentNumber++;
            button.setBackground(getResources().getDrawable(R.drawable.green_button_rounded_corner));
        }

        if(currentNumber > buttons.length*buttons.length){
            if(getActivity() instanceof GameActivity){
                ((GameActivity)getActivity()).updateScore();
            }
            gameComplicator.complicateGame();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tableLayout.removeAllViews();
                    create();
                }
            },100);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void create(){
        currentNumber = 1;
        schulteTable.generate();
        buttons = new Button[schulteTable.getTable().length][schulteTable.getTable().length];

        for(int i = 0; i<buttons.length; i++){
            TableRow row = new TableRow(tableLayout.getContext());
            for(int j = 0; j<buttons.length; j++){
                buttons[i][j] = new Button(tableLayout.getContext());
                Button button = buttons[i][j];

                button.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "button_font2.otf"));
                button.setTextSize(20);
                button.setTextColor(getResources().getColor(R.color.white));
                button.setBackground(getResources().getDrawable(R.drawable.purple_button_rounded_corner));
                button.setText(String.valueOf(schulteTable.getTable()[i][j]));

                android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
                p.rightMargin = (int)(5*getActivity().getResources().getDisplayMetrics().density);
                p.bottomMargin = (int)(5*getActivity().getResources().getDisplayMetrics().density);
                button.setLayoutParams(p);


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