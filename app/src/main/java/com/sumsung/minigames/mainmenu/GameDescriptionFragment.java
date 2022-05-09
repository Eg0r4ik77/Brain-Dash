package com.sumsung.minigames.mainmenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sumsung.minigames.R;
import com.sumsung.minigames.gamecontrollers.Timer;
import com.sumsung.minigames.mainmenu.games.GameActivity;

public class GameDescriptionFragment extends Fragment {

    private Timer timer;
    private TextView timerText;

    private Button startButton;
    private Button backButton;

    private String[] descriptions;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        descriptions = getResources().getStringArray(R.array.descriptions);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_description, container, false);
        view.setClickable(true);

        TextView descriptionText = view.findViewById(R.id.description_text);
        timerText = view.findViewById(R.id.transition_timer_text);
        backButton = view.findViewById(R.id.back_button);
        startButton = view.findViewById(R.id.start_game_button);

        descriptionText.setText(descriptions[this.getArguments().getInt("gameNumber")-1]);

        startButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            v.setClickable(false);
            timer = new Timer(4000, timerText) {
                @Override
                public void finish() {
                    startLevel();
                }
            };
            timer.run();
        });

        backButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            if(timer != null){
                timer.pause();
            }
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.games_content, new GamesMenuFragment())
                    .commit();
        });

        return view;
    }

    public void startLevel(){
        startActivity(new Intent(getContext(), GameActivity.class)
                .putExtra("gameNumber", this.getArguments().getInt("gameNumber")));
        getActivity().finish();
    }

}