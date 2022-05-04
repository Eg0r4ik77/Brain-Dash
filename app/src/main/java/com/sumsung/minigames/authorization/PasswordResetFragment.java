package com.sumsung.minigames.authorization;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sumsung.minigames.R;


public class PasswordResetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        view.setClickable(true);

        EditText emailForPasswordReset = view.findViewById(R.id.email_for_password_reset);

        Button resetPasswordButton = view.findViewById(R.id.reset_password_button_2);
        resetPasswordButton.setOnClickListener(view1 -> {
            String email = emailForPasswordReset.getText().toString();
            if(email.isEmpty()){
                Toast.makeText(getContext(), "Введите почту", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(getContext(), "Почта введена неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Проверьте почту для восстановления пароля", Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(getContext(), "Попробуйте еще раз. Что то пошло не так", Toast.LENGTH_SHORT);
                    }
                }
            });
        });

        return view;

    }
}