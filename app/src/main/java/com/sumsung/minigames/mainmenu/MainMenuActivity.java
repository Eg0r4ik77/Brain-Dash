package com.sumsung.minigames.mainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sumsung.minigames.mainmenu.games.ProfileFragment;
import com.sumsung.minigames.models.User;

import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;
    private User user;

    private Button toGameMenuButton;
    private Button leaderboardButton;
    private Button exitButton;

    private ImageButton closeLeaderboardButton;

    private MaterialButton userButton;

    private ProgressBar accountLoadingProgress;

    private ConstraintLayout recordsLayout;

    private ConstraintLayout leaderboardLayout;
    private RecyclerView leaderboard;

    private ArrayList<User> users;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        accountLoadingProgress = findViewById(R.id.account_loading_progress);
        sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);

        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        leaderboard = findViewById(R.id.leaderboard_view);
        recordsLayout = findViewById(R.id.records_layout);

        TextView textView1 = findViewById(R.id.record1_text);
        TextView textView2 = findViewById(R.id.record2_text);
        TextView textView3 = findViewById(R.id.record3_text);

        toGameMenuButton = findViewById(R.id.to_game_menu_button);
        leaderboardButton = findViewById(R.id.leaderboard_button);
        exitButton = findViewById(R.id.exit_button);
        userButton = findViewById(R.id.user_button);
        closeLeaderboardButton = findViewById(R.id.close_leaderboard_button);

        userButton.setVisibility(View.INVISIBLE);

        databaseReference =  FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if(firebaseUser == null){
                    users = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        users.add(dataSnapshot.getValue(User.class));
                    }

                    user = new User();
                    recordsLayout.setVisibility(View.VISIBLE);
                    textView1.setText(String.valueOf(sharedPreferences.getInt("SchulteTableGameBestScore", 0)));
                    textView2.setText(String.valueOf(sharedPreferences.getInt("RepeatDrawingGameBestScore", 0)));
                    textView3.setText(String.valueOf(sharedPreferences.getInt("CalculateExpressionGameBestScore", 0)));
                }
                else {
                    user = snapshot.child(firebaseUser.getUid()).getValue(User.class);
                    recordsLayout.setVisibility(View.INVISIBLE);

                    users = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser.getName().equals(user.getName())) {
                            currentUser.setName(getString(R.string.you));
                        }
                        users.add(currentUser);
                    }

                    Collections.sort(users, (new Comparator<User>() {
                        @Override
                        public int compare(User user1, User user2) {
                            int points1 = user1.getRating();
                            int points2 = user2.getRating();
                            if (points1 > points2) return -1;
                            if (points1 == points2) return 0;
                            return 1;
                        }
                    }));
                    leaderboard.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    leaderboard.setAdapter(new UserAdapter(users));
                }
                accountLoadingProgress.setVisibility(View.GONE);
                userButton.setVisibility(View.VISIBLE);
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        ArrayList<Drawable> languages = new ArrayList<>();
        languages.add(getDrawable(R.drawable.ic_english));
        languages.add(getDrawable(R.drawable.ic_russian));
        LanguageAdapter languageAdapter = new LanguageAdapter(this, languages);;
        Spinner languageSpinner = findViewById(R.id.language_spinner);
        languageSpinner.setAdapter(languageAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1) setLocale("ru");
                else setLocale("en");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setLocale("en");
            }
        });

        toGameMenuButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.games_content, new GamesMenuFragment())
                    .commit();
        });

        leaderboardButton.setOnClickListener(v -> {
            if(firebaseUser == null){
                Toast.makeText(this, getString(R.string.Log_in_to_see_the_leaderboard), Toast.LENGTH_SHORT).show();
            }else{
                leaderboardLayout.setVisibility(View.VISIBLE);
            }
        });

        closeLeaderboardButton.setOnClickListener(v -> {
            leaderboardLayout.setVisibility(View.INVISIBLE);
        });

        exitButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.exit_alert).
            setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                finishAffinity();
                System.exit(0);
            }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle(getString(R.string.quiting_the_game));
            dialog.show();
        });
    }

    private void updateUi(){

        exitButton.setText(getString(R.string.exit));
        leaderboardButton.setText(getString(R.string.leaderboard));
        toGameMenuButton.setText(getString(R.string.games));

        if(firebaseUser == null){
            userButton.setText(R.string.authorization);
            userButton.setOnClickListener(view -> {
               getSupportFragmentManager()
                       .beginTransaction()
                       .replace(R.id.games_content, new AuthorizationFragment())
                       .commit();
           });
        }else{
            userButton.setText(user.getName()+"\n"+getString(R.string.points) + user.getRating());
            userButton.setOnClickListener(view -> {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.games_content, new ProfileFragment())
                        .commit();
            });
        }
    }

    public User getUser(){
        return user;
    }
    public ArrayList<User> getUsers(){
        return users;
    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        updateUi();
    }
}