package com.example.androidgame.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.example.androidgame.R;

public class MainMenuActivity extends AppCompatActivity {

    private Button toGameMenuButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();


        toGameMenuButton = findViewById(R.id.to_game_menu_button);
        exitButton = findViewById(R.id.exit_button);

        toGameMenuButton.setOnClickListener(v -> {
            GamesMenuFragment fr = new GamesMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.games_content, fr);
            ft.commit();
        });

        exitButton.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });
    }
}