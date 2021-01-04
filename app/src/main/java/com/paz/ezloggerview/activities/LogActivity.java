package com.paz.ezloggerview.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.paz.accesstolib.DialogListener;
import com.paz.accesstolib.GiveMe;
import com.paz.accesstolib.GrantListener;
import com.paz.ezloggerview.R;
import com.paz.ezloggerview.data.Log;
import com.paz.ezloggerview.data.LogLevel;
import com.paz.ezloggerview.data.SessionDocument;
import com.paz.ezloggerview.databinding.ActivityLogBinding;
import com.paz.ezloggerview.helpers.Constants;
import com.paz.taskrunnerlib.task_runner.RunnerCallback;
import com.paz.taskrunnerlib.task_runner.TaskRunner;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static com.paz.ezloggerview.data.LogLevel.ASSERTS;
import static com.paz.ezloggerview.data.LogLevel.DEBUG;
import static com.paz.ezloggerview.data.LogLevel.ERROR;
import static com.paz.ezloggerview.data.LogLevel.INFO;
import static com.paz.ezloggerview.data.LogLevel.VERBOSE;
import static com.paz.ezloggerview.data.LogLevel.WARN;


public class LogActivity extends AppCompatActivity {

    private static int WRITE_TEXT_REQUEST_CODE = 2918;
    GiveMe giveMe;
    GrantListener grantListener = new GrantListener() {
        @Override
        public void onGranted(boolean allGranted) {
            if (allGranted)
                createFilePath();
        }

        @Override
        public void onNotGranted(String[] permissions) {

        }

        @Override
        public void onNeverAskAgain(String[] permissions) {

        }
    };
    DialogListener dialogListener = new DialogListener() {
        @Override
        public void onPositiveButton() {
            //   binding.logsBTNExport.performClick();
        }

        @Override
        public void onNegativeButton() {

        }
    };
    private ActivityLogBinding binding;
    private SessionDocument document;
    private Gson gson;
    private Log[] logs;
    private boolean showInfo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        giveMe = new GiveMe(this);

        String json = getIntent().getStringExtra(Constants.DOC);
        if (json.isEmpty())
            binding.logsLBLText.setText("document not found");
        gson = new Gson();
        document = gson.fromJson(json, SessionDocument.class);
        logs = gson.fromJson(document.getLogs(), Log[].class);

        setButton();
        setInfo();
        setLogsToTextFiled();

    }

    private void setButton() {
        binding.logsBTNShowLogs.setOnClickListener(e -> onClick());
        binding.logsBTNExport.setOnClickListener(e -> export());

    }

    private void export() {
        giveMe.requestPermissionsWithDialog(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                grantListener,
                "Permission required!",
                "Please grant permission to write files for logs export purposes",
                dialogListener);
    }

    private void onClick() {
        showInfo = !showInfo;
        if (showInfo) {
            binding.logsLAYInfo.setVisibility(View.VISIBLE);
            binding.logsBTNShowLogs.setText(getString(R.string.hideDetails));
        } else {
            binding.logsLAYInfo.setVisibility(View.GONE);
            binding.logsBTNShowLogs.setText(getString(R.string.showDetails));

        }
    }

    private void setInfo() {
        binding.logsLBLEzLoggerId.setText(getString(R.string.ezLoggerIdVal, document.getEzLogId()));
        binding.logsLBLCostumerId.setText(getString(R.string.costumerIdVal, document.getCostumerId()));
        binding.logsLBLSessionCounter.setText(getString(R.string.sessionCounterVal, document.getSessionCounter()));
        binding.logsLBLDate.setText(getString(R.string.dateVal, document.getDate()));
        binding.logsLBLManufacturer.setText(getString(R.string.manufacturerNameVal, document.getManufacturerName()));
        binding.logsLBLBatteryLevel.setText(getString(R.string.batteryLevel, document.getBatteryLevel().intValue()));
        binding.logsLBLBrightness.setText(getString(R.string.brightness, document.getBrightness()));
        binding.logsLBLRingerMode.setText(getString(R.string.ringerMode, document.getRingerMode()));
        binding.logsLBLBtEnable.setText(getString(R.string.btEnable, String.valueOf(document.isBtEnable())));
        binding.logsLBLConnectedToWifi.setText(getString(R.string.connectedToWifi, String.valueOf(document.isConnectedToWifi())));
        binding.logsLBLGpsEnable.setText(getString(R.string.gpsEnable, String.valueOf(document.isGpsEnable())));
        binding.logsLBLDeviceLang.setText(getString(R.string.deviceLang, document.getDeviceLang()));

        binding.logsLBLManufacturer.setText(getString(R.string.manufacturerNameVal, document.getManufacturerName()));
    }

    private void setLogsToTextFiled() {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        int index = 0;
        for (Log log : logs) {
            index = sb.length();
            sb.append(log.getMsg()).append("\n");
            sb.setSpan(
                    getLogColor(log),
                    index, sb.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        binding.logsLBLText.setText(sb);

    }

    private ForegroundColorSpan getLogColor(Log log) {
        int color = 0;
        switch (log.getLevel()) {
            case DEBUG:
                color = getResources().getColor(R.color.debug, getTheme());
                break;
            case ASSERTS:
                color = getResources().getColor(R.color.asserts, getTheme());
                break;
            case VERBOSE:
                color = getResources().getColor(R.color.verbose, getTheme());
                break;
            case ERROR:
                color = getResources().getColor(R.color.error, getTheme());
                break;
            case INFO:
                color = getResources().getColor(R.color.info, getTheme());
                break;
            case WARN:
                color = getResources().getColor(R.color.warn, getTheme());
                break;


        }
        return new ForegroundColorSpan(color);
    }

    private void createFilePath() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_TITLE, Constants.Export_FILE_NAME);
        intent.setType(Constants.MIME_TEXT);
        startActivityForResult(intent, WRITE_TEXT_REQUEST_CODE);
    }

    private void writeTXTFile(@NonNull Uri uri) {

        TaskRunner<Boolean> taskRunner = new TaskRunner();
        taskRunner.executeAsync(new RunnerCallback<Boolean>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public Boolean call() throws Exception {
                try {

                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(outputStream, 1028 * 8), StandardCharsets.UTF_8);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < logs.length; i++) {

                        sb.append(logs[i].getDate()).append(" - ")
                                .append(LogLevel.getLevelText(logs[i].getLevel()))
                                .append(": ").append(logs[i].getMsg()).append("\n");
                    }

                    out.write(sb.toString());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            public void onPostExecute(Boolean result) {
                if (result) {
                    new MaterialAlertDialogBuilder(LogActivity.this)
                            .setTitle("SAVED!")
                            .setMessage("The file was saved successfully")
                            .setPositiveButton("OK", null)
                            .setIcon(R.drawable.ic_check_circle)
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(LogActivity.this)
                            .setTitle("Error")
                            .setMessage("An error occurred while creating the file. Please try again")
                            .setPositiveButton("OK", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        giveMe.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WRITE_TEXT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                writeTXTFile(data.getData());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                ///
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        giveMe.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}