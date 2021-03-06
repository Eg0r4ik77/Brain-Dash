package com.sumsung.braindash.authorization;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sumsung.braindash.R;
import com.sumsung.braindash.adapters.AuthorizationAdapter;
import com.sumsung.braindash.mainmenu.MainMenuActivity;


public class AuthorizationFragment extends Fragment {

    private String[] names;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        names = new String[]{getString(R.string.log_in), getString(R.string.sign_up)};
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        Button closeAuthorizationButton = view.findViewById(R.id.close_authorization_button);

        viewPager.setAdapter(new AuthorizationAdapter(getActivity(), tabLayout.getTabCount()));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(names[position]);
        }).attach();

        closeAuthorizationButton.setOnClickListener(view1 -> {
            ((MainMenuActivity)getActivity()).playMenuButtonSound();
            getActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });

        return view;
    }

}