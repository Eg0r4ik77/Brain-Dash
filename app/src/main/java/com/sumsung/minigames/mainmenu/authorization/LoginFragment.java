package com.sumsung.minigames.mainmenu.authorization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumsung.minigames.R;
import com.sumsung.minigames.mainmenu.GamesMenuFragment;
import com.sumsung.minigames.models.User;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private Button signInButton;

    private EditText email;
    private EditText password;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        email = view.findViewById(R.id.email_field);
        password = view.findViewById(R.id.password_field);

        signInButton = view.findViewById(R.id.sing_in_button);

        signInButton.setOnClickListener(view1 -> {
            signIn();
        });

        return view;
    }

    private void signIn() {
        // код для проверки корректности данных

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "Вход выполнен", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), getActivity().getClass()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Ошибка авторизации. "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}