package com.example.androidgame.mainmenu.games;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidgame.R;
import com.example.androidgame.mainmenu.GameDescriptionFragment;
import com.example.androidgame.mainmenu.MainMenu;

public class GameOverFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        Button toMenuButton = view.findViewById(R.id.to_menu_button);
        Button restartButton = view.findViewById(R.id.restart_button);

        TextView scoreText = view.findViewById(R.id.score_text);
        TextView bestScoreText = view.findViewById(R.id.best_score_text);

        scoreText.setText("Результат: " +  this.getArguments().getInt("score"));
        bestScoreText.setText("Лучший результат" + this.getArguments().getInt("score"));

        toMenuButton.setOnClickListener(v ->{
            startActivity(new Intent(getContext(), MainMenu.class));
            getActivity().finish();
        });

        restartButton.setOnClickListener(v -> {
            GameDescriptionFragment fragment = new GameDescriptionFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.game3_over_window, fragment);
            transaction.commit();
        });

        return view;
    }
}