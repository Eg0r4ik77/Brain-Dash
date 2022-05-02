package com.sumsung.minigames.mainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sumsung.minigames.R;

public class GamesMenuFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_menu, container, false);
        view.setClickable(true);

        Button calculateExpressionGameButton = view.findViewById(R.id.calcualte_expression_game);
        Button schulteTableGameButton = view.findViewById(R.id.schulte_table_game);
        Button repeatDrawingGameButton = view.findViewById(R.id.repeat_drawing_game);
        Button closeGamesMenuButton = view.findViewById(R.id.close_games_menu_button);

        schulteTableGameButton.setOnClickListener(v ->{
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("gameNumber", 1);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        repeatDrawingGameButton.setOnClickListener(v -> {
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("gameNumber", 2);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        calculateExpressionGameButton.setOnClickListener(v -> {
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("gameNumber", 3);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        closeGamesMenuButton.setOnClickListener(view1 -> {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .commit();
        });
       return view;
    }
}