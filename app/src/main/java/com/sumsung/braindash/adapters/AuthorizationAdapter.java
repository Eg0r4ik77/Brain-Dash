package com.sumsung.braindash.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sumsung.braindash.authorization.LoginFragment;
import com.sumsung.braindash.authorization.SignUpFragment;

public class AuthorizationAdapter extends FragmentStateAdapter {

    int count;

    public AuthorizationAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }
}
