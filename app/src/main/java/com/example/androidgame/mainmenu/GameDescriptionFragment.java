package com.example.androidgame.mainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidgame.R;
import com.example.androidgame.gamecontrollers.Timer;
import com.example.androidgame.mainmenu.games.calculateexpressiongame.CalculateExpressionGame;
import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTableGame;

public class GameDescriptionFragment extends Fragment {

    private Timer timer;

    private TextView timerText;
    private TextView descriptionText;

    private Button startButton;

    private Intent intent;

    private final String[] descriptions = {
            "Игра1",
            "Игра2",
            "Игра3"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_description, container, false);

        switch (this.getArguments().getInt("gameNumber")){
            case 1:
                intent = new Intent(getContext(), SchulteTableGame.class);
                break;
            case 2:
                break;
            case 3:
                intent = new Intent(getContext(), CalculateExpressionGame.class);
                break;
            default:
                break;
        }

        timerText = view.findViewById(R.id.transition_timer_text);
        descriptionText = view.findViewById(R.id.description_text);
        startButton = view.findViewById(R.id.start_calculate_expression_level);
        timer = new Timer(4000, timerText) {
            @Override
            public void finish() {
                startLevel();
            }
        };

        descriptionText.setText(descriptions[this.getArguments().getInt("gameNumber")-1]);

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