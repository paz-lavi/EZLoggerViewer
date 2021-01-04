package com.paz.ezloggerview.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.callbacks.LogoutCallback;
import com.paz.ezloggerview.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private LogoutCallback logoutCallback;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.accountLBLDevKey.setText(getString(R.string.devKey, auth.getUid()));
        binding.accountLBLEmail.setText(getString(R.string.email, auth.getCurrentUser().getEmail()));
        binding.accountLBLName.setText(getString(R.string.name, auth.getCurrentUser().getDisplayName()));
        binding.accountLBLPhoneNumber.setText(getString(R.string.phoneNumber, auth.getCurrentUser().getPhoneNumber()));


        setButtonsAction();
    }

    private void setButtonsAction() {
        binding.accountBTNLogout.setOnClickListener(e -> logoutCallback.logout());
        binding.accountLBLDevKey.setOnClickListener(e -> copyDevKeyToClipBoard());
    }


    private void copyDevKeyToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(auth.getUid(), auth.getUid());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Dev Key Copied", Toast.LENGTH_SHORT).show();
    }

    public void setLogoutCallback(LogoutCallback logoutCallback) {
        this.logoutCallback = logoutCallback;
    }
}
