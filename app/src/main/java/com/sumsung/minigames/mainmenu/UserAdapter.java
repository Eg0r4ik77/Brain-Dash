package com.sumsung.minigames.mainmenu;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumsung.minigames.R;
import com.sumsung.minigames.models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> users;

    public UserAdapter(ArrayList<User> users){
        this.users = users;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private final ImageView userImage;
        private final TextView rank;
        private final TextView userName;
        private final TextView points;
        public UserViewHolder(View view){
            super(view);
            this.view = view;
            userImage = view.findViewById(R.id.user_image);
            rank = view.findViewById(R.id.rank_text);
            userName = view.findViewById(R.id.user_name_text);
            points = view.findViewById(R.id.points_text);
        }

        public void bind(User user){
            rank.setText(String.valueOf(users.indexOf(user)+1));
            userImage.setImageResource(R.drawable.ic_user);
            userName.setText(user.getName());
            if(user.getName().equals("Вы")){
                view.setBackgroundColor(Color.GREEN);
            }
            points.setText(String.valueOf(user.getRating()));
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
