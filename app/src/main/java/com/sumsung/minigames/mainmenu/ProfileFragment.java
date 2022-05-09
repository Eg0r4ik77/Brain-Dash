package com.sumsung.minigames.mainmenu;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
import com.sumsung.minigames.authorization.AuthorizationFragment;
import com.sumsung.minigames.authorization.PasswordResetFragment;
import com.sumsung.minigames.mainmenu.MainMenuActivity;
import com.sumsung.minigames.models.User;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private User user;
    private  FirebaseUser firebaseUser;

    private ImageButton closeButton;
    private Button exitAccountButton;
    private Button saveChangesButton;
    private Button editPasswordButton;
    private Button deleteAccountButton;

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

        editName = view.findViewById(R.id.edit_name);
        editName.setText(user.getName());

        gameRecord1 = view.findViewById(R.id.game_record_1);
        gameRecord2 = view.findViewById(R.id.game_record_2);
        gameRecord3 = view.findViewById(R.id.game_record_3);

        gameRecord1.setText(getString(R.string.schulte_table) +": " + user.getRecord1());
        gameRecord2.setText(getString(R.string.repeat_drawing)+": " + user.getRecord2());
        gameRecord3.setText(getString(R.string.calculate_expression)+": " + user.getRecord3());

        closeButton = view.findViewById(R.id.close_profile_button);
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

        exitAccountButton = view.findViewById(R.id.exit_account_button);
        exitAccountButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.do_you_want_to_log_out)).
                    setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        getActivity()
                                .getSharedPreferences("Records", getActivity().MODE_PRIVATE)
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

        saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            saveChanges();
        });

        editPasswordButton = view.findViewById(R.id.edit_password_button);
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

        deleteAccountButton = view.findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(v -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.do_you_want_to_delete_account).
                    setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
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
        FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).
                removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), getString(R.string.account_is_deleted), Toast.LENGTH_SHORT).show();
                    }
                });
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

        if(newName.length() < 6){
            Toast.makeText(getContext(), getString(R.string.login_min_length), Toast.LENGTH_SHORT).show();
            return;
        }
        if(newName.length() > 20){
            Toast.makeText(getContext(), getString(R.string.login_max_length), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newName.matches("^[a-zA-Z0-9_.-]*$")){
            Toast.makeText(getContext(), getString(R.string.login_contains_specific_characters), Toast.LENGTH_SHORT).show();
            return;
        }
        if(newName.charAt(newName.length()-1) == '.'){
            Toast.makeText(getContext(), getString(R.string.login_does_not_end_with_a_dot), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Character.isLetter(newName.charAt(0))){
            Toast.makeText(getContext(), getString(R.string.login_starts_with_letter), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<User> users = ((MainMenuActivity)getActivity()).getUsers();
        for (User user : users){
            if(user.getName().equals(newName)){
                Toast.makeText(getContext(), getString(R.string.user_with_name_exists), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(firebaseUser.getUid()).child("name").setValue(newName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), getString(R.string.name_is_updated), Toast.LENGTH_SHORT).show();
                    close();
                });
    }

    private void close(){
        getActivity().
                getSupportFragmentManager()
                .beginTransaction()
                .detach(this)
                .commit();
    }

}