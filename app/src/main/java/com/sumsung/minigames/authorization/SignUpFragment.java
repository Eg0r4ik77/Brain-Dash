package com.sumsung.minigames.authorization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
import com.sumsung.minigames.models.User;


public class SignUpFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Button signUpButton;

    private EditText name;
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

        name = view.findViewById(R.id.login_edit_text);
        email = view.findViewById(R.id.email_edit_text);
        password = view.findViewById(R.id.password_edit_text);

        signUpButton = view.findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(view1 -> {
            signUp();
        });

        return view;
    }

    private void signUp() {

        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(getContext(), "Введите логин", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(getContext(), "Введите почту", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password.getText().toString())){
            Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
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
                                getActivity().getSharedPreferences("Records", getActivity().MODE_PRIVATE).edit().clear().commit();
                                Toast.makeText(getContext(), "Пользователь добавлен!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), getActivity().getClass()));
                            }
                        })
                       .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthWeakPasswordException){
                            Toast.makeText(getContext(), "Пароль должет иметь более 5 символов", Toast.LENGTH_SHORT).show();
                        }
                        else if (e instanceof FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(getContext(), "Email адрес имеет неправильный формат", Toast.LENGTH_SHORT).show();
                        }
                        else if (e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getContext(), "Учетная запись с таким email уже сущесвует", Toast.LENGTH_SHORT).show();
                        }
                }
            });
        });
    }
}