package com.example.androidgame.mainmenu.games;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.calculationsmodegames.calculateexpressiongame.CalculateExpressionGame;

public class GameDescriptionFragment extends Fragment {

    private Timer timer;

    private TextView timerText;
    private TextView rulesText;

    private Button startButton;

    private Intent intent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_description, container, false);

        intent = new Intent(getContext(), CalculateExpressionGame.class);

        timerText = view.findViewById(R.id.transition_timer_text);
        rulesText = view.findViewById(R.id.rules_text);
        startButton = view.findViewById(R.id.start_calculate_expression_level);
        timer = new Timer(4000, timerText) {
            @Override
            public void finish() {
                startLevel();
            }
        };

        startButton.setOnClickListener(v -> {
            startButton.setClickable(false);
            timer.run();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void startLevel(){
        try{
            startActivity(intent);
            getActivity().finish();
        }catch (Exception exception){
            Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
        }
    }

}