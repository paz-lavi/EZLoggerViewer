package com.paz.ezloggerview.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.paz.ezloggerview.fragments.AccountFragment;
import com.paz.ezloggerview.fragments.SavedFragment;
import com.paz.ezloggerview.fragments.SearchFragment;


public class TabAdapter extends FragmentStateAdapter {
    private int tabCount;
    private AccountFragment accountFragment;
    private SearchFragment searchFragment;
    private SavedFragment savedFragment;

    public TabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int tabCount,
                      AccountFragment accountFragment, SearchFragment searchFragment, SavedFragment savedFragment) {
        super(fragmentManager, lifecycle);
        this.tabCount = tabCount;
        this.accountFragment = accountFragment;
        this.searchFragment = searchFragment;
        this.savedFragment = savedFragment;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return accountFragment;
            case 1:
                return searchFragment;
            case 2:
                return savedFragment;

            default:
                throw new RuntimeException("fragment not found");
        }

    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}