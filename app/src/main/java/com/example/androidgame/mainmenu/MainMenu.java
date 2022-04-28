package com.example.androidgame.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.androidgame.R;

public class MainMenu extends AppCompatActivity {

    private Button trainingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();


        trainingButton = findViewById(R.id.training_button);
        trainingButton.setOnClickListener(v -> {
            GamesMenuFragment fr = new GamesMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.games_content, fr);
            ft.commit();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int restart = getIntent().getIntExtra("Restart", 0);
        if(restart > 0){
            GameDescriptionFragment fr = new GameDescriptionFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("gameNumber", restart);
            fr.setArguments(bundle);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.games_content, fr);
            ft.commit();
        }
    }
}