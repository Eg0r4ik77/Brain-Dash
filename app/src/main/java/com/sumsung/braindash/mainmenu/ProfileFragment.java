package com.sumsung.braindash.mainmenu;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.braindash.Strings;
import com.sumsung.braindash.authorization.SignUpFragment;
import com.sumsung.braindash.models.User;
import com.sumsung.braindash.R;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private User user;
    private  FirebaseUser firebaseUser;

    SharedPreferences sharedPreferences;

    private EditText editName;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = ((MainMenuActivity)getActivity()).getUser();

        sharedPreferences = getContext().getSharedPreferences(Strings.RECORDS, getContext().MODE_PRIVATE);

        editName = view.findViewById(R.id.edit_name);
        editName.setText(user.getName());

        TextView gameRecord1 = view.findViewById(R.id.game_record_1);
        TextView gameRecord2 = view.findViewById(R.id.game_record_2);
        TextView gameRecord3 = view.findViewById(R.id.game_record_3);

        gameRecord1.setText(getString(R.string.schulte_table) +": " + user.getRecord1());
        gameRecord2.setText(getString(R.string.repeat_drawing)+": " + user.getRecord2());
        gameRecord3.setText(getString(R.string.calculate_expression)+": " + user.getRecord3());

        ImageButton closeButton = view.findViewById(R.id.close_profile_button);
        closeButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            String newName = editName.getText().toString();
            if(!user.getName().equals((newName))){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.save_changes)+"?").
                        setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            saveChanges();
                        }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            close();
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle(getString(R.string.changes_saving));
                dialog.show();
            }else{
                close();
            }
        });

        Button exitAccountButton = view.findViewById(R.id.exit_account_button);
        exitAccountButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.do_you_want_to_log_out)).
                    setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        getActivity()
                                .getSharedPreferences(Strings.RECORDS, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(Strings.AUTHORIZED, false)
                                .apply();

                       sharedPreferences
                                .edit()
                                .clear()
                                .apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(),getActivity().getClass()));
                    }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle(getString(R.string.logout));
            dialog.show();
        });

        Button saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            saveChanges();
        });

        Button editPasswordButton = view.findViewById(R.id.edit_password_button);
        editPasswordButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.do_you_want_to_update_password).
                    setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getContext(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle(getString(R.string.password_update));
            dialog.show();
        });

        Button deleteAccountButton = view.findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.do_you_want_to_delete_account).
                    setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        sharedPreferences
                                .edit()
                                .clear()
                                .apply();
                        deleteAccount();
                    }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle(getString(R.string.account_deletion));
            dialog.show();
        });

        return view;
    }

    private void deleteAccount(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference(Strings.USERS).child(firebaseUser.getUid()).
                            removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), getString(R.string.account_is_deleted), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), getActivity().getClass()));
    }

    private void saveChanges(){
        String newName = editName.getText().toString();
        if(newName.isEmpty()){
            Toast.makeText(getContext(), getString(R.string.enter_login), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!SignUpFragment.isValidLogin(getContext(), newName)){
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Strings.USERS);
        FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(firebaseUser.getUid()).child(Strings.NAME).setValue(newName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), getString(R.string.name_is_updated), Toast.LENGTH_SHORT).show();
                    close();
                });
    }

    private void close(){
        getActivity().
                getSupportFragmentManager()
                .popBackStack();
    }
}