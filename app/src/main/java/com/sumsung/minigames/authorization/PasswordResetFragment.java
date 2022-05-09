package com.sumsung.minigames.authorization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.sumsung.minigames.mainmenu.MainMenuActivity;


public class PasswordResetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_password_reset, container, false);
        view.setClickable(true);

        EditText emailForPasswordReset = view.findViewById(R.id.email_for_password_reset);

        Button resetPasswordButton = view.findViewById(R.id.reset_password_button);
        resetPasswordButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            String email = emailForPasswordReset.getText().toString();
            if(email.isEmpty()){
                Toast.makeText(getContext(), getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(getContext(), getString(R.string.email_is_incorrect), Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.games_content, new AuthorizationFragment())
                                .commit();

                        Toast.makeText(getContext(), getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        Button cancelResetButton = view.findViewById(R.id.cancel_reset_button);
        cancelResetButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .commit();
        });

        return view;

    }
}