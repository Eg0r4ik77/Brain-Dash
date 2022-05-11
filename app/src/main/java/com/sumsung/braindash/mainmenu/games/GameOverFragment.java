package com.sumsung.braindash.mainmenu.games;

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

import com.sumsung.braindash.R;
import com.sumsung.braindash.Strings;
import com.sumsung.braindash.mainmenu.MainMenuActivity;

public class GameOverFragment extends Fragment {

    private String gameScoreText;
    private String bestGameScoreText;
    private String newBestScore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        gameScoreText = getString(R.string.game_score);
        bestGameScoreText = getString(R.string.best_game_score);
        newBestScore = getString(R.string.new_best_score);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        view.setClickable(true);

        Button toMenuButton = view.findViewById(R.id.to_menu_button);
        Button restartButton = view.findViewById(R.id.restart_button);

        TextView gameResultText = view.findViewById(R.id.game_result_text);
        TextView newBestScore = view.findViewById(R.id.new_best_score);

        int score = getArguments().getInt(Strings.SCORE);
        int bestScore = ((GameActivity)getActivity()).getBestScore();

        gameResultText.setText(gameScoreText + score +
                "\n" + bestGameScoreText + bestScore);

        newBestScore.setVisibility(score > bestScore ? View.VISIBLE : View.INVISIBLE);

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
                    .putExtra(Strings.GAME_NUMBER, getActivity().getIntent()
                            .getIntExtra(Strings.GAME_NUMBER, 1)));
        });

        return view;
    }
}