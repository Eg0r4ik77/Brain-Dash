package com.example.androidgame.mainmenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidgame.R;

public class MainMenuActivity extends AppCompatActivity {

    private TextView gameRecord1;
    private TextView gameRecord2;
    private TextView gameRecord3;
    private TextView ratingPointsText;

    private Button toGameMenuButton;
    private Button exitButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);

        gameRecord1 = findViewById(R.id.game_record_1);
        gameRecord2 = findViewById(R.id.game_record_2);
        gameRecord3 = findViewById(R.id.game_record_3);
        ratingPointsText = findViewById(R.id.rating_points_text);

        gameRecord1.setText("Schulte: " + sharedPreferences.getInt("SchulteTableGameBestScore", 0));
        gameRecord2.setText("Draw: " + sharedPreferences.getInt("RepeatDrawingGameBestScore", 0));
        gameRecord3.setText("Calc: " + sharedPreferences.getInt("CalculateExpressionGameBestScore", 0));
        ratingPointsText.setText("Rating: " + sharedPreferences.getInt("Rating", 0));

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