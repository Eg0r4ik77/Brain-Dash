package com.sumsung.minigames.mainmenu.games;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sumsung.minigames.R;
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

        gameRecord1.setText("Таблица Шульте: " + user.getRecord1());
        gameRecord2.setText("Повтори рисунок: " + user.getRecord2());
        gameRecord3.setText("Посчитай пример: " + user.getRecord3());

        closeButton = view.findViewById(R.id.close_profile_button);
        closeButton.setOnClickListener(view1 -> {
            String newName = editName.getText().toString();
            if(!user.getName().equals((newName))){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Сохранить изменения?").
                        setPositiveButton("Да", (dialogInterface, i) -> {
                            saveChanges();
                        }).setNegativeButton("Нет", (dialogInterface, i) -> {
                            dialogInterface.cancel();
                            close();
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("Сохранение изменений");
                dialog.show();
            }else{
                close();
            }
        });

        exitAccountButton = view.findViewById(R.id.exit_account_button);
        exitAccountButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Действительно хотите выйти из аккаунта?").
                    setPositiveButton("Да", (dialogInterface, i) -> {
                        getActivity()
                                .getSharedPreferences("Records", getActivity().MODE_PRIVATE)
                                .edit()
                                .clear()
                                .apply();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(),getActivity().getClass()));
                    }).setNegativeButton("Нет", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Выход из аккаунта");
            dialog.show();
        });

        saveChangesButton = view.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(v -> {
            saveChanges();
        });

        editPasswordButton = view.findViewById(R.id.edit_password_button);
        editPasswordButton.setOnClickListener(v -> {
            getActivity().
                    getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.games_content, new PasswordResetFragment())
                    .commit();
        });

        deleteAccountButton = view.findViewById(R.id.delete_account_button);
        deleteAccountButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Действительно хотите удалить аккаунт?").
                    setPositiveButton("Да", (dialogInterface, i) -> {
                        deleteAccount();
                    }).setNegativeButton("Нет", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Удаление аккаунта");
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
                        Toast.makeText(getContext(), "Аккаунт удален", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
            return;
        }

        if(newName.length() < 6){
            Toast.makeText(getContext(), "Логин должет иметь более 5 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newName.length() > 20){
            Toast.makeText(getContext(), "Логин должет иметь не более 20 символов", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newName.matches("^[a-zA-Z0-9_.-]*$")){
            Toast.makeText(getContext(), "Логин должет иметь только латинские буквы, цифры, символы тире, подчеркивания и точки", Toast.LENGTH_SHORT).show();
            return;
        }
        if(newName.charAt(newName.length()-1) == '.'){
            Toast.makeText(getContext(), "Логин не может заканитваться точкой", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Character.isLetter(newName.charAt(0))){
            Toast.makeText(getContext(), "Логин должен начинаться с буквы", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<User> users = ((MainMenuActivity)getActivity()).getUsers();
        for (User user : users){
            if(user.getName().equals(newName)){
                Toast.makeText(getContext(), "Пользователь с таким именем уже существует", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser firebaseUser =  FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(firebaseUser.getUid()).child("name").setValue(newName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Имя изменено", Toast.LENGTH_SHORT).show();
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