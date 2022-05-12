package com.sumsung.braindash.authorization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.braindash.R;
import com.sumsung.braindash.Strings;
import com.sumsung.braindash.mainmenu.MainMenuActivity;
import com.sumsung.braindash.models.User;

import java.util.ArrayList;


public class SignUpFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText name;
    private EditText email;
    private EditText password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Strings.USERS);

        name = view.findViewById(R.id.login_edit_text);
        email = view.findViewById(R.id.email_edit_text);
        password = view.findViewById(R.id.password_edit_text);

        Button signUpButton = view.findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            signUp();
        });

        return view;
    }

    private void signUp() {

        String nameString = name.getText().toString();

        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(getContext(), getString(R.string.enter_login), Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(getContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidLogin(getContext(), nameString)){
            return;
        }

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
            .addOnSuccessListener(authResult -> {
                User user = new User(name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                getActivity().getSharedPreferences(Strings.RECORDS, getActivity().MODE_PRIVATE).edit().clear().commit();
                                Toast.makeText(getContext(), getString(R.string.user_is_added), Toast.LENGTH_SHORT).show();
                                getActivity()
                                        .getSharedPreferences(Strings.RECORDS, getActivity().MODE_PRIVATE)
                                        .edit()
                                        .putBoolean(Strings.AUTHORIZED, true)
                                        .apply();
                                startActivity(new Intent(getContext(), getActivity().getClass()));
                            }
                        });
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(getContext(), getString(R.string.invalid_email_or_password), Toast.LENGTH_SHORT).show();
                    }
                    else if (e instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getContext(), getString(R.string.user_with_email_exists), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public static boolean isValidLogin(Context context, String newName){
        if(newName.length() < 6){
            Toast.makeText(context, context.getString(R.string.login_min_length), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newName.length() > 15){
            Toast.makeText(context, context.getString(R.string.login_max_length), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!newName.matches("^[a-zA-Z0-9_.-]*$")){
            Toast.makeText(context, context.getString(R.string.login_contains_specific_characters), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newName.charAt(newName.length()-1) == '.'){
            Toast.makeText(context, context.getString(R.string.login_does_not_end_with_a_dot), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Character.isLetter(newName.charAt(0))){
            Toast.makeText(context, context.getString(R.string.login_starts_with_letter), Toast.LENGTH_SHORT).show();
            return false;
        }

        ArrayList<User> users = ((MainMenuActivity)context).getUsers();
        for (User user : users){
            if(user.getName().equals(newName)){
                Toast.makeText(context, context.getString(R.string.user_with_name_exists), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}