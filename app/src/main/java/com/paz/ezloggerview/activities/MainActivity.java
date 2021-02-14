package com.paz.ezloggerview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paz.ezloggerview.adapters.TabAdapter;
import com.paz.ezloggerview.callbacks.LogoutCallback;
import com.paz.ezloggerview.callbacks.SearchCallback;
import com.paz.ezloggerview.databinding.ActivityMainBinding;
import com.paz.ezloggerview.fragments.AccountFragment;
import com.paz.ezloggerview.fragments.SavedFragment;
import com.paz.ezloggerview.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AccountFragment accountFragment;
    private SearchFragment searchFragment;
    private SavedFragment savedFragment;
    private LogoutCallback logoutCallback = () -> AuthUI.getInstance()
            .signOut(MainActivity.this)
            .addOnCompleteListener(task -> {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            });
    private SearchCallback searchCallback = params -> {
        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
        i.putExtras(params);

        startActivity(i);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountFragment = null;
        savedFragment = null;
        logoutCallback = null;
        Log.d("pttt", "fragments counts = " + getSupportFragmentManager().getFragments().size());
        getSupportFragmentManager().getFragments().clear();
        // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        createFragments();
        bindFragments();
        //setButtonsAction();
    }

    private void bindFragments() {
        addTabs(new String[]{"Account", "Search", "History"});
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), this.getLifecycle(), binding.mainLAYTab.getTabCount(), accountFragment, searchFragment, savedFragment);
        binding.mainVPPage.setAdapter(adapter);
        binding.mainVPPage.setCurrentItem(1);
        new TabLayoutMediator(binding.mainLAYTab, binding.mainVPPage,
                (tab, position) -> {

                    switch (position) {
                        case 0:
                            tab.setText("Account");
                            break;
                        case 1:
                            tab.setText("Search");
                            break;
                        case 2:
                            tab.setText("History");
                            break;

                    }


                }
        ).attach();

    }

    private void addTabs(String[] strings) {
        for (String s : strings) {
            binding.mainLAYTab.addTab(binding.mainLAYTab.newTab().setText(s));
        }
    }

    private void createFragments() {
        Log.d("pttt", "createFragments: ");
        searchFragment = new SearchFragment();
        accountFragment = new AccountFragment();
        savedFragment = new SavedFragment();
        if (searchCallback == null)
            Log.d("pttt", "null??: ");

        searchFragment.setSearchCallback(searchCallback);
        savedFragment.setSearchCallback(searchCallback);
        accountFragment.setLogoutCallback(logoutCallback);

        Log.d("pttt", "createFragments: isCallbackNull " + searchFragment.isCallbackNull());
    }


}