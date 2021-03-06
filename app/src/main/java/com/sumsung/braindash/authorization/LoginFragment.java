package com.sumsung.braindash.authorization;

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

import com.sumsung.braindash.R;
import com.sumsung.braindash.Strings;
import com.sumsung.braindash.mainmenu.MainMenuActivity;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    private EditText email;
    private EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        email = view.findViewById(R.id.email_field);
        password = view.findViewById(R.id.password_field);

        Button signInButton = view.findViewById(R.id.sing_in_button);
        Button toResetPasswordFragmentButton = view.findViewById(R.id.to_reset_password_fragment_button);

        signInButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            signIn();
        });

        toResetPasswordFragmentButton.setOnClickListener(view12 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.games_content, new PasswordResetFragment())
                    .commit();
        });

        return view;
    }

    private void signIn() {

        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(getContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), getString(R.string.login_completed), Toast.LENGTH_SHORT).show();
                        getActivity()
                                .getSharedPreferences(Strings.RECORDS, getActivity().MODE_PRIVATE)
                                .edit()
                                .putBoolean(Strings.AUTHORIZED, true)
                                .apply();
                        startActivity(new Intent(getContext(), getActivity().getClass()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(getContext(), getString(R.string.account_does_not_exist), Toast.LENGTH_SHORT).show();
                        }else if(e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(getContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}