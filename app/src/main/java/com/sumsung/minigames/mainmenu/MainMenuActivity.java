package com.sumsung.minigames.mainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumsung.minigames.R;
import com.sumsung.minigames.authorization.AuthorizationFragment;
import com.google.android.material.button.MaterialButton;
import com.sumsung.minigames.mainmenu.games.GameActivity;
import com.sumsung.minigames.models.User;

public class MainMenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;

    private User user = new User();

    private TextView gameRecord1;
    private TextView gameRecord2;
    private TextView gameRecord3;

    private Button showRecordsButton;
    private Button toGameMenuButton;
    private Button exitButton;
    private Button exitAccountButton;

    private MaterialButton userButton;

    private CardView profiledCard;
    private CardView recordCard;

    private ProgressBar accountLoadingProgress;

    private boolean isShowRecordsButtonClicked;
    private boolean isUserProfileShown;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        accountLoadingProgress = findViewById(R.id.account_loading_progress);

        sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);

        recordCard = findViewById(R.id.record_card);
        recordCard.setVisibility(View.INVISIBLE);

        profiledCard = findViewById(R.id.profile_card);
        profiledCard.setVisibility(View.INVISIBLE);

        gameRecord1 = findViewById(R.id.game_record_1);
        gameRecord2 = findViewById(R.id.game_record_2);
        gameRecord3 = findViewById(R.id.game_record_3);

        gameRecord1.setText("Таблица Шульте: " + user.getRecord1());
        gameRecord2.setText("Повтори рисунок: " + user.getRecord2());
        gameRecord3.setText("Посчитай пример: " + user.getRecord3());

        showRecordsButton = findViewById(R.id.show_records_button);
        toGameMenuButton = findViewById(R.id.to_game_menu_button);
        exitButton = findViewById(R.id.exit_button);
        userButton = findViewById(R.id.user_button);
        exitAccountButton = findViewById(R.id.exit_account_button);

        userButton.setVisibility(View.INVISIBLE);

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser == null){
                    user = new User();
                }
                else{
                    user = snapshot.child(firebaseUser.getUid()).getValue(User.class);
                }
                accountLoadingProgress.setVisibility(View.GONE);
                userButton.setVisibility(View.VISIBLE);
                updateScores();
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        userButton.setText(user.getName()+"\nRating: " + sharedPreferences.getInt("Rating", 0));
        userButton.setOnClickListener(view -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.games_content, new AuthorizationFragment())
                    .commit();
        });

        exitAccountButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            //updateUi();
            startActivity(new Intent(this, MainMenuActivity.class));
        });

        showRecordsButton.setOnClickListener(view -> {
            if(isShowRecordsButtonClicked){
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_decrease_anim);
                recordCard.startAnimation(animation);
                recordCard.setVisibility(View.INVISIBLE);
            }else{
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_increase_anim);
                recordCard.startAnimation(animation);
                recordCard.setVisibility(View.VISIBLE);
            }
            isShowRecordsButtonClicked = !isShowRecordsButtonClicked;
        });

        toGameMenuButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.games_content, new GamesMenuFragment())
                    .commit();
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

    private void updateUi(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            userButton.setText("Авторизация");
            userButton.setOnClickListener(view -> {
               getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.games_content, new AuthorizationFragment())
                       .commit();
           });
        }else{
            userButton.setText(user.getName()+"\nRating: " + sharedPreferences.getInt("rating", 0));

            userButton.setOnClickListener(view -> {
                if (isUserProfileShown) profiledCard.setVisibility(View.VISIBLE);
                else profiledCard.setVisibility(View.INVISIBLE);
                isUserProfileShown = !isUserProfileShown;
            });

            gameRecord1.setText("Таблица Шульте: " + user.getRecord1());
            gameRecord2.setText("Повтори рисунок: " + user.getRecord2());
            gameRecord3.setText("Посчитай пример: " + user.getRecord3());
        }
    }

    public void updateScores(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null) return;
        databaseReference.child(firebaseUser.getUid()).child("record1")
                .setValue(sharedPreferences.getInt("SchulteTableGameBestScore",0));

        databaseReference.child(firebaseUser.getUid()).child("record2")
                .setValue(sharedPreferences.getInt("RepeatDrawingGameBestScore",0));

        databaseReference.child(firebaseUser.getUid()).child("record3")
                .setValue(sharedPreferences.getInt("CalculateExpressionGameBestScore",0));

        databaseReference.child(firebaseUser.getUid()).child("rating")
                .setValue(sharedPreferences.getInt("rating",0));
    }
}