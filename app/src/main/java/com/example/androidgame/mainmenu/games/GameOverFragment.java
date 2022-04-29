package com.example.androidgame.mainmenu.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.mainmenu.GameDescriptionFragment;
import com.example.androidgame.mainmenu.MainMenuActivity;
import com.example.androidgame.mainmenu.games.repeatdrawinggame.RepeatDrawingGameFragment;
import com.example.androidgame.mainmenu.games.shultetablegame.SchulteTableGameFragment;

public class GameOverFragment extends Fragment {

    private String gameScoreText;
    private String bestGameScoreText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        gameScoreText = getString(R.string.game_score_text);
        bestGameScoreText = getString(R.string.best_game_score_text);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        view.setClickable(true);

        Button toMenuButton = view.findViewById(R.id.to_menu_button);
        Button restartButton = view.findViewById(R.id.restart_button);

        TextView gameResultText = view.findViewById(R.id.game_result_text);

        gameResultText.setText(gameScoreText +  getArguments().getInt("score") +
                "\n" + bestGameScoreText + ((GameActivity)getActivity()).getBestScore());

        toMenuButton.setOnClickListener(v ->{
            startActivity(new Intent(getContext(), MainMenuActivity.class));
            getActivity().finish();
        });

        restartButton.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), getActivity().getClass())
                    .putExtra("gameNumber", getActivity().getIntent()
                            .getIntExtra("gameNumber", 1)));
        });

        return view;
    }
}