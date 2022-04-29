package com.example.androidgame.mainmenu.games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

    private String gameScoreText;
    private String bestGameScoreText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        gameScoreText = getString(R.string.game_score_text);
        bestGameScoreText = getString(R.string.best_game_score_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);

        Button toMenuButton = view.findViewById(R.id.to_menu_button);
        Button restartButton = view.findViewById(R.id.restart_button);


        TextView gameResultText = view.findViewById(R.id.game_result_text);

        gameResultText.setText(gameScoreText +  getArguments().getInt("score") +
                "\n" + bestGameScoreText + getArguments().getInt("score"));

        toMenuButton.setOnClickListener(v ->{
            startActivity(new Intent(getContext(), MainMenu.class));
            getActivity().finish();
        });

        restartButton.setOnClickListener(v -> {
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("gameNumber", this.getArguments().getInt("gameNumber"));
            fr.setArguments(bundle);

            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.game_over_window, fr);
            ft.commit();
//            Intent intent = new Intent(getContext(), MainMenu.class);
//            intent.putExtra("Restart", getArguments().getInt("gameNumber"));
//            startActivity(intent);
//            getActivity().finish();
        });

        return view;
    }
}