package com.sumsung.minigames.mainmenu.games.repeatdrawinggame;

import android.annotation.SuppressLint;

import android.graphics.drawable.Drawable;
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
import com.sumsung.minigames.gamecontrollers.gamecomplicators.GameComplicator;
import com.sumsung.minigames.gamecontrollers.gamecomplicators.RepeatDrawingGameComplicator;
import com.sumsung.minigames.mainmenu.games.GameActivity;


public class RepeatDrawingGameFragment extends Fragment {

    private Drawing drawing = new Drawing();
    private TableLayout drawingLayout;

    private boolean[][] drawingFlags;
    private Button[][] buttons;

    private int selectedButtonsCount;
    private int correctlySelectedButtonsCount;

    private GameComplicator gameComplicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repeat_drawing_game, container, false);
        view.setClickable(true);

        gameComplicator = new RepeatDrawingGameComplicator(drawing);
        drawingLayout = view.findViewById(R.id.drawing_layout);

        play();

        return view;
    }

    private void play(){
        drawing.create();
        drawingFlags = drawing.getDrawing();
        selectedButtonsCount = correctlySelectedButtonsCount = 0;
        showDrawing();
        if(getActivity() instanceof GameActivity){
            ((GameActivity) getActivity()).setTimerPaused(true);
        }

        if(getActivity() instanceof GameActivity){
            ((GameActivity) getActivity()).setTimerPaused(true);
        }

        new Handler().postDelayed(() -> {
            ((GameActivity)getActivity()).setTimerPaused(false);
            clearDrawing();
        },2500);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void clearDrawing(){
        for(int i = 0; i< buttons.length; i++){
            for(int j = 0; j< buttons.length; j++){
                buttons[i][j].setBackground(getResources().getDrawable(R.drawable.white_button_rounded_corner));
            }
        }
        setButtonsClickable(true);
    }

    private void setButtonsClickable(boolean clickable){
        for(int i = 0; i< drawingFlags.length; i++){
            for(int j = 0; j < drawingFlags.length; j++) {
                buttons[i][j].setClickable(clickable);
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDrawing(){
        buttons = new Button[drawingFlags.length][drawingFlags.length];
        drawingLayout.removeAllViews();
        for(int i = 0; i<drawingFlags.length; i++){
            TableRow row = new TableRow(drawingLayout.getContext());
            for(int j = 0; j<drawingFlags.length; j++){
                buttons[i][j] = new Button(drawingLayout.getContext());

                Button button = buttons[i][j];

                button.setBackground(getResources().getDrawable(R.drawable.white_button_rounded_corner));
                android.widget.TableRow.LayoutParams p = new android.widget.TableRow.LayoutParams();
                p.rightMargin = (int)(5*getActivity().getResources().getDisplayMetrics().density);
                p.bottomMargin = (int)(5*getActivity().getResources().getDisplayMetrics().density);
                button.setLayoutParams(p);

                boolean drawingFlag = drawingFlags[i][j];

                if(drawingFlag) {
                    button.setBackground(getResources().getDrawable(R.drawable.green_button_rounded_corner));
                }

                button.setOnClickListener(v -> {
                    ((GameActivity)getActivity()).playGameButtonSound();
                    Drawable drawable = (Drawable)button.getBackground();
                    if(drawable.getConstantState().equals(getResources().getDrawable(R.drawable.white_button_rounded_corner).getConstantState())){
                        button.setBackground(getResources().getDrawable(R.drawable.green_button_rounded_corner));
                        selectedButtonsCount++;

                        if(drawingFlag){
                            correctlySelectedButtonsCount++;
                        }
                    }else{
                        button.setBackground(getResources().getDrawable(R.drawable.white_button_rounded_corner));

                        selectedButtonsCount--;
                        if(drawingFlag){
                            correctlySelectedButtonsCount--;
                        }
                    }

                    if(correctlySelectedButtonsCount == drawing.drawingTilesCount
                            && correctlySelectedButtonsCount == selectedButtonsCount){
                        ((GameActivity)getActivity()).updateScore();
                        gameComplicator.complicateGame();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                play();
                            }
                        },500);
                    }
                });

                row.addView(buttons[i][j]);
            }
            drawingLayout.addView(row);
        }
        setButtonsClickable(false);
    }

    public int getGamePoints(){
        return 10 * correctlySelectedButtonsCount;
    }
}