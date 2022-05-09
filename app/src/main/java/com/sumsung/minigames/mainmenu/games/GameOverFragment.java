package com.sumsung.minigames.mainmenu.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sumsung.minigames.R;
import com.sumsung.minigames.mainmenu.MainMenuActivity;

public class GameOverFragment extends Fragment {

    private String gameScoreText;
    private String bestGameScoreText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        gameScoreText = getString(R.string.game_score);
        bestGameScoreText = getString(R.string.best_game_score);
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

        Animation animationRight = AnimationUtils.loadAnimation(getContext(), R.anim.trans_right);
        Animation animationLeft = AnimationUtils.loadAnimation(getContext(), R.anim.trans_left);
        gameResultText.startAnimation(animationRight);
        toMenuButton.startAnimation(animationLeft);
        restartButton.startAnimation(animationRight);

        toMenuButton.setOnClickListener(v ->{
            ((GameActivity)getActivity()).playMenuButtonSound();
            startActivity(new Intent(getContext(), MainMenuActivity.class));
            getActivity().finish();
        });

        restartButton.setOnClickListener(v -> {
            ((GameActivity)getActivity()).playMenuButtonSound();
            startActivity(new Intent(getContext(), getActivity().getClass())
                    .putExtra("gameNumber", getActivity().getIntent()
                            .getIntExtra("gameNumber", 1)));
        });

        return view;
    }
}