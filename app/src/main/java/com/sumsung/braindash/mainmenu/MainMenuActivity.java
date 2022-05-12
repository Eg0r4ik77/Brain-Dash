package com.sumsung.braindash.mainmenu;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.sumsung.braindash.Strings;
import com.sumsung.braindash.adapters.LanguageAdapter;
import com.sumsung.braindash.adapters.UserAdapter;
import com.sumsung.braindash.authorization.AuthorizationFragment;
import com.sumsung.braindash.models.User;
import com.sumsung.braindash.R;
import com.google.android.material.button.MaterialButton;
import com.sumsung.braindash.services.MusicService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainMenuActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences soundSharedPreferences;

    private FirebaseUser firebaseUser;
    private User user;
    private ArrayList<User> users;

    private Button toGameMenuButton;
    private Button leaderboardButton;
    private Button exitButton;

    private ImageButton soundButton;

    private TextView record1;
    private TextView record2;
    private TextView record3;
    private TextView recordsLayoutLabel;
    private TextView leaderboardTextView;

    private MaterialButton userButton;

    private ConstraintLayout recordsLayout;
    private ConstraintLayout leaderboardLayout;
    private RecyclerView leaderboard;

    private MediaPlayer menuButtonSound;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getSupportActionBar().hide();

        menuButtonSound = MediaPlayer.create(this, R.raw.sound_menu_button);

        sharedPreferences = getSharedPreferences(Strings.RECORDS, MODE_PRIVATE);
        soundSharedPreferences = getSharedPreferences(Strings.SOUND, MODE_PRIVATE);

        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        leaderboard = findViewById(R.id.leaderboard_view);
        recordsLayout = findViewById(R.id.records_layout);

        record1 = findViewById(R.id.record1_text);
        record2 = findViewById(R.id.record2_text);
        record3 = findViewById(R.id.record3_text);
        recordsLayoutLabel = findViewById(R.id.records_layout_label);

        leaderboardTextView = findViewById(R.id.leaderboard_textview);

        toGameMenuButton = findViewById(R.id.to_game_menu_button);
        leaderboardButton = findViewById(R.id.leaderboard_button);
        exitButton = findViewById(R.id.exit_button);
        userButton = findViewById(R.id.user_button);
        ImageButton closeLeaderboardButton = findViewById(R.id.close_leaderboard_button);
        soundButton = findViewById(R.id.sound_button);
        updateSoundButtonImage();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Strings.USERS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                users = new ArrayList<>();

                if(firebaseUser == null){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        users.add(dataSnapshot.getValue(User.class));
                    }
                }
                else {
                    user = snapshot.child(firebaseUser.getUid()).getValue(User.class);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser.getName().equals(user.getName())) {
                            currentUser.setName(getString(R.string.you));
                        }
                        users.add(currentUser);
                    }

                    Collections.sort(users, ((user1, user2) -> {
                        int points1 = user1.getRating();
                        int points2 = user2.getRating();
                        return Integer.compare(points2, points1);
                    }));
                    leaderboard.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    leaderboard.setAdapter(new UserAdapter(users));
                }
                userButton.setVisibility(View.VISIBLE);
                updateUi();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        ArrayList<Drawable> languages = new ArrayList<>();
        languages.add(getDrawable(R.drawable.ic_english));
        languages.add(getDrawable(R.drawable.ic_russian));
        Spinner languageSpinner = findViewById(R.id.language_spinner);
        languageSpinner.setAdapter(new LanguageAdapter(this, languages));

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1){
                    setLocale(Strings.RU);
                    getSharedPreferences(Strings.LANGUAGES, MODE_PRIVATE)
                            .edit().putString(Strings.LANGUAGE,Strings.RU)
                            .apply();
                }
                else{
                    setLocale(Strings.EN);
                    getSharedPreferences(Strings.LANGUAGES, MODE_PRIVATE)
                            .edit().putString(Strings.LANGUAGE,Strings.EN)
                            .apply();
                }
                leaderboard.setAdapter(new UserAdapter(users));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        languageSpinner.setSelection( getSharedPreferences(Strings.LANGUAGES, MODE_PRIVATE)
                .getString(Strings.LANGUAGE, Strings.EN)
                .equals(Strings.RU) ? 1 : 0);

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
                findViewById(R.id.leaderboard_layout).startAnimation(animation);
                leaderboardLayout.setVisibility(View.VISIBLE);
            }
        });

        closeLeaderboardButton.setOnClickListener(v -> {
            playMenuButtonSound();
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_decrease_anim);
            findViewById(R.id.leaderboard_layout).startAnimation(animation);
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
            if(isSoundOn()){
                soundSharedPreferences.edit().putInt(Strings.ON, 0).apply();
                stopService(new Intent(this, MusicService.class));
            }else{
                soundSharedPreferences.edit().putInt(Strings.ON, 1).apply();
                startService(new Intent(this, MusicService.class).putExtra(Strings.MUSIC, R.raw.music_background_menu));
            }
            updateSoundButtonImage();
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUi(){
        exitButton.setText(getString(R.string.exit));
        leaderboardButton.setText(getString(R.string.leaderboard));
        toGameMenuButton.setText(getString(R.string.games));
        leaderboardTextView.setText(getString(R.string.leaderboard));
        recordsLayoutLabel.setText(getString(R.string.best_scores ));
        //recordsLayout.setVisibility(firebaseUser == null ? View.VISIBLE : View.INVISIBLE);
        recordsLayout.setVisibility(sharedPreferences.getBoolean(Strings.AUTHORIZED, false) ? View.INVISIBLE : View.VISIBLE);


        if(firebaseUser == null){
            record1.setText(getString(R.string.schulte_table) +": "+ sharedPreferences.getInt(Strings.SCHULTE_TABLE_GAME_BEST_SCORE, 0));
            record2.setText(getString(R.string.repeat_drawing) +": "+ sharedPreferences.getInt(Strings.REPEAT_DRAWING_GAME_BEST_SCORE, 0));
            record3.setText(getString(R.string.calculate_expression) +": "+ sharedPreferences.getInt(Strings.CALCULATE_EXPRESSION_GAME_BEST_SCORE, 0));
            userButton.setText(getString(R.string.authorization));

            userButton.setOnClickListener(view -> {
                playMenuButtonSound();
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.games_content, new AuthorizationFragment())
                        .commit();
           });
        }else{
            sharedPreferences.edit().putBoolean(Strings.AUTHORIZED, true).apply();
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
        if(isSoundOn()){
            menuButtonSound.start();
        }
    }

    private void updateSoundButtonImage(){
        soundButton.setBackgroundResource(isSoundOn() ? R.drawable.ic_sound_on : R.drawable.ic_sound_off);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isSoundOn()){
            startService(new Intent(this, MusicService.class).putExtra(Strings.MUSIC, R.raw.music_background_menu));
        }
    }

    public boolean isSoundOn(){
        return soundSharedPreferences.getInt(Strings.ON, 1) == 1;
    }
}