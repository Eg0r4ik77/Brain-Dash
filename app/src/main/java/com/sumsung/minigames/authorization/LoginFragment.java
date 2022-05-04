package com.sumsung.minigames.authorization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;

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

        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getContext(), "Введите почту", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
            return;
        }

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
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(getContext(), "Учетная запись с таким email не сущесвует ", Toast.LENGTH_SHORT).show();
                }else if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(getContext(), "Введен неправильный пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}