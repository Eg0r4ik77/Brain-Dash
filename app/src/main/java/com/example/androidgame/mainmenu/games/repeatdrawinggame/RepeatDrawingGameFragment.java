package com.example.androidgame.mainmenu.games.repeatdrawinggame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.gamecomplicators.GameComplicator;
import com.example.androidgame.gamecontrollers.gamecomplicators.RepeatDrawingGameComplicator;
import com.example.androidgame.mainmenu.games.GamePanel;


public class RepeatDrawingGameFragment extends Fragment {

    private Drawing drawing = new Drawing();
    private TableLayout drawingLayout;
    private boolean[][] drawingFlags;
    private Button[][] tiles;


    private int selectedTilesCount;
    private int correctlySelectedTilesCount;

    private GameComplicator gameComplicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repeat_drawing_game, container, false);

        gameComplicator = new RepeatDrawingGameComplicator(drawing);
        drawingLayout = view.findViewById(R.id.drawing_layout);

        play();

        return view;
    }

    private void play(){
        drawing.create();
        drawingFlags = drawing.getDrawing();
        selectedTilesCount = correctlySelectedTilesCount = 0;
        showDrawing();

        ((GamePanel)getActivity()).setTimerPaused(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((GamePanel)getActivity()).setTimerPaused(false);
                clearDrawing();
            }
        },2500);
    }

    private void clearDrawing(){
        for(int i = 0; i< tiles.length; i++){
            for(int j =0; j< tiles.length; j++){
                tiles[i][j].setBackgroundColor(Color.WHITE);
            }
        }
        setButtonsClickable(true);
    }

    private void setButtonsClickable(boolean clickable){
        for(int i = 0; i< drawingFlags.length; i++){
            for(int j = 0; j < drawingFlags.length; j++) {
                tiles[i][j].setClickable(clickable);
            }
        }
    }

    private void showDrawing(){
        tiles = new Button[drawingFlags.length][drawingFlags.length];
        drawingLayout.removeAllViews();
        for(int i = 0; i<drawingFlags.length; i++){
            TableRow row = new TableRow(drawingLayout.getContext());
            for(int j = 0; j<drawingFlags.length; j++){
                tiles[i][j] = new Button(drawingLayout.getContext());

                Button button = tiles[i][j];
                button.setBackgroundColor(Color.WHITE);
                boolean drawingFlag = drawingFlags[i][j];

                if(drawingFlag) {
                    button.setBackgroundColor(Color.BLUE);
                }

                button.setOnClickListener(v -> {
                    int color = ((ColorDrawable)button.getBackground()).getColor();
                    if(color == Color.WHITE){
                        button.setBackgroundColor(Color.BLUE);
                        selectedTilesCount++;
                        if(drawingFlag){
                            correctlySelectedTilesCount++;
                        }
                    }else{
                        button.setBackgroundColor(Color.WHITE);
                        selectedTilesCount--;
                        if(drawingFlag){
                            correctlySelectedTilesCount--;
                        }
                    }

                    if(correctlySelectedTilesCount == drawing.drawingTilesCount
                            && correctlySelectedTilesCount == selectedTilesCount){
                        ((GamePanel)getActivity()).updateScore();
                        gameComplicator.complicateGame();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                play();
                            }
                        },500);
                    }
                });

                row.addView(tiles[i][j]);
            }
            drawingLayout.addView(row);
        }
        setButtonsClickable(false);
    }

    public int getGamePoints(){
        return 10 * correctlySelectedTilesCount;
    }

}