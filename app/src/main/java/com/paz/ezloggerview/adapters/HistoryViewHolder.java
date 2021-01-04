package com.paz.ezloggerview.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.callbacks.SearchCallback;
import com.paz.ezloggerview.data.SavedQuery;
import com.paz.ezloggerview.helpers.TimestampGenerator;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    Type type = new TypeToken<ArrayList<String>>() {
    }.getType();
    Gson gson = new Gson();
    private TextView history_LBL_ezLoggerId, history_LBL_manufacturer, history_LBL_date, history_LBL_sessionCounter, history_LBL_costumerId, history_LBL_searchDate, history_LBL_packageName;
    private MaterialButton history_BTN_showLogs;


    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        history_BTN_showLogs = itemView.findViewById(R.id.history_BTN_showLogs);
        history_LBL_packageName = itemView.findViewById(R.id.history_LBL_packageName);
        history_LBL_ezLoggerId = itemView.findViewById(R.id.history_LBL_ezLoggerId);
        history_LBL_manufacturer = itemView.findViewById(R.id.history_LBL_manufacturer);
        history_LBL_date = itemView.findViewById(R.id.history_LBL_date);
        history_LBL_sessionCounter = itemView.findViewById(R.id.history_LBL_sessionCounter);
        history_LBL_costumerId = itemView.findViewById(R.id.history_LBL_costumerId);
        history_LBL_searchDate = itemView.findViewById(R.id.history_LBL_searchDate);

    }

    public void bind(Context context, SavedQuery savedQuery, SearchCallback callback) {

        history_LBL_ezLoggerId.setText(context.getString(R.string.ezLoggerIdVal, ArrayListToStringConvert(gson.fromJson(savedQuery.getEzLogId(), type))));
        history_LBL_costumerId.setText(context.getString(R.string.costumerIdVal, ArrayListToStringConvert(gson.fromJson(savedQuery.getCostumerId(), type))));
        String d1 = TimestampGenerator.getDateAsString(savedQuery.getD1());
        String d2 = TimestampGenerator.getDateAsString(savedQuery.getD2());
        history_LBL_date.setText(context.getString(R.string.dateBetween, d1, d2));
        history_LBL_manufacturer.setText(context.getString(R.string.manufacturerNameVal, ArrayListToStringConvert(gson.fromJson(savedQuery.getManufacturerName(), type))));
        history_LBL_searchDate.setText(context.getString(R.string.searchDate, savedQuery.getDate()));
        history_LBL_packageName.setText(context.getString(R.string.packageNameVal, savedQuery.getPackageName()));
        history_BTN_showLogs.setOnClickListener(e -> {
            callback.SearchLogs(savedQuery.toBundle());
        });
    }

    private String ArrayListToStringConvert(ArrayList<String> list) {
        if (list.size() == 0)
            return "Not Used";
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(", ");
        }
        String res = sb.toString();
        return res.substring(0, res.length() - 2);
    }
}
