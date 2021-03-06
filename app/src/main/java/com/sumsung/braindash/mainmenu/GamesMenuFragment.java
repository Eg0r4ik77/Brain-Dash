package com.sumsung.braindash.mainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sumsung.braindash.R;
import com.sumsung.braindash.Strings;

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
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Strings.GAME_NUMBER, 1);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.alpha_anim, R.anim.fade)
                    .addToBackStack(null)
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        repeatDrawingGameButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Strings.GAME_NUMBER, 2);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.alpha_anim, R.anim.fade)
                    .addToBackStack(null)
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        calculateExpressionGameButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Strings.GAME_NUMBER, 3);
            fr.setArguments(bundle);

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.alpha_anim, R.anim.fade)
                    .addToBackStack(null)
                    .replace(R.id.games_content, fr)
                    .commit();
        });

        closeGamesMenuButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            getActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
       return view;
    }
}