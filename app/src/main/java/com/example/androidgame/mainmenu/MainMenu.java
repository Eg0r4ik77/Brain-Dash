package com.example.androidgame.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.example.androidgame.R;

public class MainMenu extends AppCompatActivity {

    private Button trainingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        trainingButton = findViewById(R.id.training_button);
        trainingButton.setOnClickListener(v -> {
            GamesMenuFragment fr = new GamesMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.games_menu, fr);
            ft.commit();
        });
    }
}