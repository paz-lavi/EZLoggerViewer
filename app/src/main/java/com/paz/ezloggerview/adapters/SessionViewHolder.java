package com.paz.ezloggerview.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.callbacks.ShowLogsCallback;
import com.paz.ezloggerview.data.SessionDocument;

public class SessionViewHolder extends RecyclerView.ViewHolder {
    private TextView card_LBL_ezLoggerId, card_LBL_manufacturer, card_LBL_date, card_LBL_sessionCounter, card_LBL_costumerId, card_LBL_packageName;
    private MaterialButton card_BTN_showLogs;

    public SessionViewHolder(@NonNull View itemView) {
        super(itemView);
        card_BTN_showLogs = itemView.findViewById(R.id.card_BTN_showLogs);
        card_LBL_ezLoggerId = itemView.findViewById(R.id.card_LBL_ezLoggerId);
        card_LBL_manufacturer = itemView.findViewById(R.id.card_LBL_manufacturer);
        card_LBL_date = itemView.findViewById(R.id.card_LBL_date);
        card_LBL_sessionCounter = itemView.findViewById(R.id.card_LBL_sessionCounter);
        card_LBL_costumerId = itemView.findViewById(R.id.card_LBL_costumerId);
        card_LBL_packageName = itemView.findViewById(R.id.card_LBL_packageName);

    }

    public void bind(Context context, SessionDocument document, ShowLogsCallback callback) {
        card_LBL_ezLoggerId.setText(context.getString(R.string.ezLoggerIdVal, document.getEzLogId()));
        card_LBL_packageName.setText(context.getString(R.string.packageNameVal, document.getPackageName()));
        card_LBL_costumerId.setText(context.getString(R.string.costumerIdVal, document.getCostumerId()));
        card_LBL_sessionCounter.setText(context.getString(R.string.sessionCounterVal, document.getSessionCounter()));
        card_LBL_date.setText(context.getString(R.string.dateVal, document.getDate()));
        card_LBL_manufacturer.setText(context.getString(R.string.manufacturerNameVal, document.getManufacturerName()));
        card_BTN_showLogs.setOnClickListener(e -> {
            callback.showLogs(document);
        });
    }
}
