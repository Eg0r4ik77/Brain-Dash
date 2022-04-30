package com.example.androidgame.mainmenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidgame.R;

public class MainMenuActivity extends AppCompatActivity {

    private TextView gameRecord1;
    private TextView gameRecord2;
    private TextView gameRecord3;
    private TextView ratingPointsText;

    private Button showRecordsButton;
    private Button toGameMenuButton;
    private Button exitButton;
    private Button closeRecordCardButton;

    CardView recordCard;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);

        recordCard = findViewById(R.id.record_card);
        recordCard.setVisibility(View.INVISIBLE);

        gameRecord1 = findViewById(R.id.game_record_1);
        gameRecord2 = findViewById(R.id.game_record_2);
        gameRecord3 = findViewById(R.id.game_record_3);
        ratingPointsText = findViewById(R.id.rating_points_text);

        gameRecord1.setText("Schulte: " + sharedPreferences.getInt("SchulteTableGameBestScore", 0));
        gameRecord2.setText("Draw: " + sharedPreferences.getInt("RepeatDrawingGameBestScore", 0));
        gameRecord3.setText("Calc: " + sharedPreferences.getInt("CalculateExpressionGameBestScore", 0));
        ratingPointsText.setText("Rating: " + sharedPreferences.getInt("Rating", 0));

        showRecordsButton = findViewById(R.id.show_records_button);
        toGameMenuButton = findViewById(R.id.to_game_menu_button);
        exitButton = findViewById(R.id.exit_button);
        closeRecordCardButton = findViewById(R.id.close_record_card_button);

        closeRecordCardButton.setOnClickListener(view -> {
            recordCard.setVisibility(View.INVISIBLE);
        });

        showRecordsButton.setOnClickListener(view -> {
            recordCard.setVisibility(View.VISIBLE);
        });

        toGameMenuButton.setOnClickListener(v -> {
            GamesMenuFragment fr = new GamesMenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.games_content, fr);
            ft.commit();
        });

        exitButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.exit_alert_text).
            setPositiveButton("Да", (dialogInterface, i) -> {
                finishAffinity();
                System.exit(0);
            }).setNegativeButton("Нет", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Выход из игры");
            dialog.show();
        });
    }
}