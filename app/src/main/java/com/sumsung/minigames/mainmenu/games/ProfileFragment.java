package com.sumsung.minigames.mainmenu.games;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
import com.sumsung.minigames.authorization.PasswordResetFragment;
import com.sumsung.minigames.mainmenu.MainMenuActivity;
import com.sumsung.minigames.models.User;


public class ProfileFragment extends Fragment {

    private User user;

    private ImageButton closeButton;
    private Button exitAccountButton;
    private Button saveChangesButton;
    private Button editPasswordButton;

    private CardView recordCard;
    private TextView gameRecord1;
    private TextView gameRecord2;
    private TextView gameRecord3;

    private EditText editName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = ((MainMenuActivity)getActivity()).getUser();

        recordCard = view.findViewById(R.id.record_card);

        editName = view.findViewById(R.id.edit_name);
        editName.setText(user.getName());

        gameRecord1 = view.findViewById(R.id.game_record_1);
        gameRecord2 = view.findViewById(R.id.game_record_2);
        gameRecord3 = view.findViewById(R.id.game_record_3);

        gameRecord1.setText("Таблица Шульте: " + user.getRecord1());
        gameRecord2.setText("Повтори рисунок: " + user.getRecord2());
        gameRecord3.setText("Посчитай пример: " + user.getRecord3());

        closeButton = view.findViewById(R.id.close_profile_button);
        closeButton.setOnClickListener(view1 -> {
            getActivity().
                    getSupportFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .commit();
        });

        exitAccountButton = view.findViewById(R.id.exit_account_button);
        exitAccountButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(),getActivity().getClass()));
        });

        saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(v -> {
            String newName = editName.getText().toString();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

            databaseReference.child(firebaseUser.getUid()).child("name").setValue(newName)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
                    });
        });

        editPasswordButton = view.findViewById(R.id.edit_password_button);
        editPasswordButton.setOnClickListener(v -> {
            getActivity().
                    getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.games_content, new PasswordResetFragment())
                    .commit();
        });

        return view;
    }

}