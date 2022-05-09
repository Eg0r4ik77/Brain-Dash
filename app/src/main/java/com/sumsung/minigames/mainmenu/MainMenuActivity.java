package com.sumsung.minigames.mainmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.sumsung.minigames.models.User;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences soundSharedPreferences;
    private DatabaseReference databaseReference;

    private FirebaseUser firebaseUser;
    private User user;

    private Button toGameMenuButton;
    private Button leaderboardButton;
    private Button exitButton;

    private ImageButton soundButton;
    private ImageButton closeLeaderboardButton;

    private MaterialButton userButton;

    private ProgressBar accountLoadingProgress;

    private ConstraintLayout recordsLayout;

    private ConstraintLayout leaderboardLayout;
    private RecyclerView leaderboard;

    private ArrayList<User> users;

    private MediaPlayer menuButtonSound;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();


        menuButtonSound = MediaPlayer.create(this, R.raw.sound_menu_button);

        accountLoadingProgress = findViewById(R.id.account_loading_progress);
        sharedPreferences = getSharedPreferences("Records", MODE_PRIVATE);
        soundSharedPreferences = getSharedPreferences("Sound", MODE_PRIVATE);


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
        soundButton = findViewById(R.id.sound_button);
        updateSoundButtonImage();

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

                if(i == 1){
                    setLocale("ru");
                    getSharedPreferences("Languages", MODE_PRIVATE)
                            .edit().putString("Language","ru")
                            .apply();
                }
                else{
                    setLocale("en");
                    getSharedPreferences("Languages", MODE_PRIVATE)
                            .edit().putString("Language","en")
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        languageSpinner.setSelection( getSharedPreferences("Languages", MODE_PRIVATE)
                .getString("Language", "en")
                .equals("ru") ? 1 : 0);

        toGameMenuButton.setOnClickListener(v -> {
            playMenuButtonSound();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.games_content, new GamesMenuFragment())
                    .commit();
        });

        leaderboardButton.setOnClickListener(v -> {
            playMenuButtonSound();
            if(firebaseUser == null){
                Toast.makeText(this, getString(R.string.Log_in_to_see_the_leaderboard), Toast.LENGTH_SHORT).show();
            }else{
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_increase_anim);
                ((ConstraintLayout)findViewById(R.id.leaderboard_layout)).startAnimation(animation);
                leaderboardLayout.setVisibility(View.VISIBLE);
            }
        });

        closeLeaderboardButton.setOnClickListener(v -> {
            menuButtonSound.start();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_decrease_anim);
            ((ConstraintLayout)findViewById(R.id.leaderboard_layout)).startAnimation(animation);
            leaderboardLayout.setVisibility(View.INVISIBLE);
        });

        exitButton.setOnClickListener(v -> {
            playMenuButtonSound();
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

        soundButton.setOnClickListener(view -> {
            playMenuButtonSound();
            if(soundSharedPreferences.getInt("On", 1) == 1){
                soundSharedPreferences.edit().putInt("On", 0).apply();
                stopService(new Intent(this, MusicService.class));
            }else{
                soundSharedPreferences.edit().putInt("On", 1).apply();
                startService(new Intent(this, MusicService.class).putExtra("Music", R.raw.music_background_menu));
            }
            updateSoundButtonImage();
            Log.i("Sound",String.valueOf(getSharedPreferences("Sound", MODE_PRIVATE).getInt("On", 1)));
        });
    }

    private void updateUi(){
        exitButton.setText(getString(R.string.exit));
        leaderboardButton.setText(getString(R.string.leaderboard));
        toGameMenuButton.setText(getString(R.string.games));

        if(firebaseUser == null){
            userButton.setText(R.string.authorization);
            userButton.setOnClickListener(view -> {
                playMenuButtonSound();
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.games_content, new AuthorizationFragment())
                        .commit();
           });
        }else{
            userButton.setText(user.getName()+"\n"+getString(R.string.points) + user.getRating());
            userButton.setOnClickListener(view -> {
                playMenuButtonSound();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.trans_down, R.anim.trans_up)
                        .addToBackStack(null)
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

    public void playMenuButtonSound(){
        if(soundSharedPreferences.getInt("On", 1) == 1){
            menuButtonSound.start();
        }
    }

    private void updateSoundButtonImage(){
        if(soundSharedPreferences.getInt("On", 1) == 1){
            soundButton.setBackgroundResource(R.drawable.ic_sound_on);
        }else{
            soundButton.setBackgroundResource(R.drawable.ic_sound_off);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(soundSharedPreferences.getInt("On", 1) == 1){
            startService(new Intent(this, MusicService.class).putExtra("Music", R.raw.music_background_menu));
        }
    }

}