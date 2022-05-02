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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
import com.sumsung.minigames.models.User;


public class SignUpFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Button signUpButton;

    private EditText login;
    private EditText email;
    private EditText password;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        login = view.findViewById(R.id.login_edit_text);
        email = view.findViewById(R.id.email_edit_text);
        password = view.findViewById(R.id.password_edit_text);

        signUpButton = view.findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), getActivity().getClass()));
            signUp();
        });

        return view;
    }

    private void signUp() {
        // код для проверки корректности данных
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
        .addOnSuccessListener(authResult -> {
            User user = new User(login.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString());
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Пользователь добавлен!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), getActivity().getClass()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Ошибка авторизации. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}