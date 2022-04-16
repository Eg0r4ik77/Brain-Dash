package com.example.androidgame.mainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidgame.R;
import com.example.androidgame.mainmenu.games.GameDescriptionFragment;
import com.example.androidgame.mainmenu.games.TransitionActivity;


public class GamesMenuFragment extends Fragment {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_games_menu, container, false);

        Button calculateExpressionGameButton = view.findViewById(R.id.calcualte_expression_game);
        calculateExpressionGameButton.setOnClickListener(v -> {
            GameDescriptionFragment fr = new GameDescriptionFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.game_description, fr);
            ft.commit();
        });

       return view;
    }
}