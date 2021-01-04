package com.paz.ezloggerview.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.callbacks.SearchCallback;
import com.paz.ezloggerview.databinding.FragmentSearchBinding;
import com.paz.ezloggerview.helpers.TimestampGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import static com.paz.ezloggerview.helpers.Constants.CHIP_INDEX;
import static com.paz.ezloggerview.helpers.Constants.COSTUMER_ID;
import static com.paz.ezloggerview.helpers.Constants.DATE_MAX;
import static com.paz.ezloggerview.helpers.Constants.DATE_MIN;
import static com.paz.ezloggerview.helpers.Constants.EDT_COSTUMERS;
import static com.paz.ezloggerview.helpers.Constants.EDT_ID;
import static com.paz.ezloggerview.helpers.Constants.EDT_MANUFACTURER;
import static com.paz.ezloggerview.helpers.Constants.EDT_PACKAGE;
import static com.paz.ezloggerview.helpers.Constants.EZ_LOG_ID;
import static com.paz.ezloggerview.helpers.Constants.LIST_COSTUMERS;
import static com.paz.ezloggerview.helpers.Constants.LIST_ID;
import static com.paz.ezloggerview.helpers.Constants.LIST_MANUFACTURER;
import static com.paz.ezloggerview.helpers.Constants.LIST_PACKAGES;
import static com.paz.ezloggerview.helpers.Constants.MANUFACTURER_NAME;
import static com.paz.ezloggerview.helpers.Constants.PACKAGE_NAME;
import static com.paz.ezloggerview.helpers.Constants.SESSION_COUNTER;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private int chip = 1;
    private SearchCallback searchCallback;
    private long d1 = 0L, d2 = 0L;
    private ArrayList<String> ezLogIds, costumerIds, manufacturers, packageNames;

    public SearchFragment(SearchCallback searchCallback) {
        this.searchCallback = searchCallback;
    }

    public SearchFragment() {
    }

    public boolean isCallbackNull() {
        return searchCallback == null;
    }

    public void setSearchCallback(SearchCallback searchCallback) {
        Log.d("pttt", "setSearchCallback: ");

        this.searchCallback = searchCallback;
        if (isCallbackNull())
            Log.d("pttt", "setSearchCallback: null");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d("pttt", "onCreateView: view null? " + String.valueOf(view == null));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ezLogIds = new ArrayList<>();
        costumerIds = new ArrayList<>();
        manufacturers = new ArrayList<>();
        packageNames = new ArrayList<>();
        if (savedInstanceState != null) {
            restoreValues(savedInstanceState);
            restoreChips();
        }
        setChipsListener();
        setButtonsAction();
    }


    private void setChipsListener() {
//        binding.searchCHIPSChipGroup.setSingleSelection(chip);
        binding.searchCHIPSChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            chip = checkedId;
        });
    }

    private void setButtonsAction() {
        binding.searchBTNDates.setOnClickListener(e -> selectDates());
        if (isCallbackNull())
            Log.d("pttt", "setButtonsAction: null");
        binding.searchBTNSearch.setOnClickListener(e -> {
            if (d1 != 0L && d2 != 0L)
                search();
            else
                selectDates();
        });


        binding.searchEDTId.setEndIconOnClickListener(a -> idsClick());

        binding.searchEDTPackageName.setEndIconOnClickListener(a -> packageNameClick());

        binding.searchEDTCostumerId.setEndIconOnClickListener(a -> costumerClick());

        binding.searchEDTManufacturer.setEndIconOnClickListener(a -> manufacturerClick());

    }

    private void search() {

        if (multiWhereInQuery()) {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                    .setTitle(getContext().getResources().getString(R.string.errorTitle))
                    .setMessage(getContext().getResources().getString(R.string.supporting_text))
                    .setNeutralButton(getContext().getResources().getString(R.string.understand), null)
                    .show();
            return;
        }
        Bundle parms = new Bundle();
        Gson gson = new Gson();
        String counter = binding.searchEDTSessionCounter.getEditText().getText().toString();
        parms.putInt(SESSION_COUNTER, counter.isEmpty() ? -1 : Integer.parseInt(counter));

        int index = binding.searchCHIPSChipGroup.getCheckedChipId();
        if (binding.searchCPEquals.getId() == index)
            parms.putInt(CHIP_INDEX, 1);
        else if (binding.searchCPLess.getId() == index)
            parms.putInt(CHIP_INDEX, 2);
        else
            parms.putInt(CHIP_INDEX, 0);


        String id = gson.toJson(ezLogIds);

        parms.putString(EZ_LOG_ID, id);


        String packages = gson.toJson(packageNames);

        parms.putString(PACKAGE_NAME, packages);

        String costumer = gson.toJson(costumerIds);


        parms.putString(COSTUMER_ID, costumer);

        String manufacturer = gson.toJson(manufacturers);

        parms.putString(MANUFACTURER_NAME, manufacturer);

        parms.putLong(DATE_MIN, d1);
        parms.putLong(DATE_MAX, d2);
        if (this.searchCallback == null) {
            Log.d("pttt", "search: null");
            return;
        }
        searchCallback.SearchLogs(parms);
    }

    private boolean multiWhereInQuery() {
//        boolean a = ezLogIds.size() > 1;
//        boolean b = costumerIds.size() > 1;
//        boolean c = manufacturers.size() > 1;
//        boolean d = packageNames.size() > 1;
        ArrayList[] lists = new ArrayList[]{ezLogIds, costumerIds, manufacturers, packageNames};
        int counter = 0;
        for (ArrayList list : lists) {
            Log.d("pttt", "multiWhereInQuery: ");
            if (list.size() > 1) {
                counter++;
                Log.d("pttt", "counter: " + counter);

            }
        }

        return counter > 1;
    }

    private void selectDates() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getParentFragmentManager(), picker.toString());
        picker.addOnPositiveButtonClickListener(selection -> {

            Calendar c1 = Calendar.getInstance();
            c1.setTimeZone(TimeZone.getDefault());
            Date date1 = new Date();
            date1.setTime(selection.first);
            Date date2 = new Date();
            date2.setTime(selection.second);

            c1.setTime(date1);
            c1.set(Calendar.HOUR, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            d1 = c1.getTimeInMillis();

            c1.setTime(date2);
            c1.set(Calendar.HOUR, 23);
            c1.set(Calendar.MINUTE, 59);
            c1.set(Calendar.SECOND, 59);
            d2 = c1.getTimeInMillis();

            Log.d("pttt", "selectDates: d1=" + d1 + " d2=" + d2);
            binding.searchBTNDates.setText(getString(R.string.dates, TimestampGenerator.getDateAsString(d1), TimestampGenerator.getDateAsString(d2)));
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addChip(final String str, ChipGroup chipGroup, final int listIndex) {


        Chip chip = new Chip(Objects.requireNonNull(getActivity()));
        chip.setText(str);
        chip.setCloseIcon(Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_clear));
        chip.setCloseIconTintResource(R.color.blue_500);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            chipGroup.removeView(chip);
            switch (listIndex) {
                case LIST_ID:
                    ezLogIds.remove(str);
                    break;
                case LIST_COSTUMERS:
                    costumerIds.remove(str);
                    break;
                case LIST_MANUFACTURER:
                    manufacturers.remove(str);
                    break;
                case LIST_PACKAGES:
                    packageNames.remove(str);
                    break;
            }

        });
        //chip.setCheckable(true);
        chipGroup.addView(chip);
        switch (listIndex) {
            case LIST_ID:
                ezLogIds.add(str);
                break;
            case LIST_COSTUMERS:
                costumerIds.add(str);
                break;
            case LIST_MANUFACTURER:
                manufacturers.add(str);
                break;
            case LIST_PACKAGES:
                packageNames.add(str);
                break;
        }

    }

    private void idsClick() {
        String str = binding.searchEDTId.getEditText().getText().toString();
        binding.searchEDTId.getEditText().setText("");
        if (str.isEmpty() || ezLogIds.contains(str))
            return;
        addChip(str, binding.searchCHIPSEz, LIST_ID);
    }

    private void costumerClick() {
        String str = binding.searchEDTCostumerId.getEditText().getText().toString();
        binding.searchEDTCostumerId.getEditText().setText("");

        if (str.isEmpty() || costumerIds.contains(str))
            return;
        addChip(str, binding.searchCHIPSCostumers, LIST_COSTUMERS);
    }

    private void manufacturerClick() {
        String str = binding.searchEDTManufacturer.getEditText().getText().toString();
        binding.searchEDTManufacturer.getEditText().setText("");

        if (str.isEmpty() || manufacturers.contains(str))
            return;
        addChip(str, binding.searchCHIPSManufacturer, LIST_MANUFACTURER);
    }

    private void packageNameClick() {
        String str = binding.searchEDTPackageName.getEditText().getText().toString();
        binding.searchEDTPackageName.getEditText().setText("");

        if (str.isEmpty() || packageNames.contains(str))
            return;
        addChip(str, binding.searchCHIPSPackageName, LIST_PACKAGES);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(EZ_LOG_ID, ezLogIds);
        outState.putStringArrayList(COSTUMER_ID, costumerIds);
        outState.putStringArrayList(MANUFACTURER_NAME, manufacturers);
        outState.putStringArrayList(PACKAGE_NAME, packageNames);
        outState.putLong(DATE_MIN, d1);
        outState.putLong(DATE_MAX, d2);
        outState.putString(EDT_ID, binding.searchEDTId.getEditText().getText().toString());
        outState.putString(EDT_COSTUMERS, binding.searchEDTCostumerId.getEditText().getText().toString());
        outState.putString(EDT_MANUFACTURER, binding.searchEDTManufacturer.getEditText().getText().toString());
        outState.putString(EDT_PACKAGE, binding.searchEDTPackageName.getEditText().getText().toString());
    }

    private void restoreValues(Bundle savedInstanceState) {
        ezLogIds = (savedInstanceState.getStringArrayList(EZ_LOG_ID));
        costumerIds = (savedInstanceState.getStringArrayList(COSTUMER_ID));
        manufacturers = (savedInstanceState.getStringArrayList(MANUFACTURER_NAME));
        packageNames = (savedInstanceState.getStringArrayList(PACKAGE_NAME));

        d1 = savedInstanceState.getLong(DATE_MIN, 0);
        d2 = savedInstanceState.getLong(DATE_MAX, 0);
        if (d1 != 0 && d2 != 0)
            binding.searchBTNDates.setText(getString(R.string.dates, TimestampGenerator.getDateAsString(d1), TimestampGenerator.getDateAsString(d2)));

        binding.searchEDTId.getEditText().setText(savedInstanceState.getString(EDT_ID, binding.searchEDTId.getEditText().getText().toString()));
        binding.searchEDTCostumerId.getEditText().setText(savedInstanceState.getString(EDT_COSTUMERS, binding.searchEDTCostumerId.getEditText().getText().toString()));
        binding.searchEDTManufacturer.getEditText().setText(savedInstanceState.getString(EDT_MANUFACTURER, binding.searchEDTManufacturer.getEditText().getText().toString()));
        binding.searchEDTPackageName.getEditText().setText(savedInstanceState.getString(EDT_PACKAGE, binding.searchEDTPackageName.getEditText().getText().toString()));
    }

    private void restoreChips() {
        binding.searchCHIPSEz.removeAllViews();
        ezLogIds = removeDuplicateValues(ezLogIds);
        for (String str : ezLogIds.toArray(new String[0])) {
            addChip(str, binding.searchCHIPSEz, LIST_ID);
        }
        binding.searchCHIPSCostumers.removeAllViews();
        costumerIds = removeDuplicateValues(costumerIds);
        for (String str : costumerIds.toArray(new String[0])) {
            addChip(str, binding.searchCHIPSCostumers, LIST_COSTUMERS);
        }
        binding.searchCHIPSManufacturer.removeAllViews();

        manufacturers = removeDuplicateValues(manufacturers);
        for (String str : manufacturers.toArray(new String[0])) {
            addChip(str, binding.searchCHIPSManufacturer, LIST_MANUFACTURER);
        }
    }

    private ArrayList<String> removeDuplicateValues(ArrayList<String> list) {
        Set<String> set = new HashSet<>(list);
        return new ArrayList<String>(set);

    }

}
