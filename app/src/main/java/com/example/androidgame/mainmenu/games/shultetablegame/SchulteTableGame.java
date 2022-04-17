package com.example.androidgame.mainmenu.games.shultetablegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidgame.R;
import com.google.android.material.tabs.TabLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;

public class SchulteTableGame extends AppCompatActivity {

    SchulteTable schulteTable = new SchulteTable();
    TableLayout tableLayout;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schulte_table_game);

        button = findViewById(R.id.generate_schulte_table);
        button.setOnClickListener(v -> {

            startActivity(new Intent(getBaseContext(), SchulteTableGame.class));

        });

        tableLayout = findViewById(R.id.schulte_table_place);
        schulteTable.generate(tableLayout);
    }
}