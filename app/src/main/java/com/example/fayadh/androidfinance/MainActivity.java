package com.example.fayadh.androidfinance;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.GsonFactory;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

public class MainActivity extends AppCompatActivity implements AIListener {

    public static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_AUDIO_PERMISSIONS_ID = 33;
    float totalBalance = 0.0f;
    private Gson gson = GsonFactory.getGson();
    private AIService aiService;

    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAudioRecordPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        final AIConfiguration config = new AIConfiguration("ed7278e9f1da4da79b5803e4f4d4a690",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        TTS.init(getApplicationContext());

    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    @Override
    public void onListeningStarted() {
    }

    @Override
    public void onListeningCanceled() {
    }

    @Override
    public void onListeningFinished() {
    }

    @Override
    public void onAudioLevel(final float level) {
    }

    @Override
    public void onError(final AIError error) {
    }


    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();

                String intentType = "Can I buy a coffee";

                if (result.getResolvedQuery().toLowerCase().contains("coffee")) {

                    if (totalBalance < 50.0) {
                        TTS.speak("I'm sorry but you don't have enough for coffee.");
                    } else if (totalBalance > 50) {
                        TTS.speak("Go grab yourself a nice hot cup!");
                    }

                } else if (result.getResolvedQuery().toLowerCase().contains("money")) {
                    TTS.speak("You have " + totalBalance + "Dollars left");
                } else {
                    TTS.speak(speech);
                }


                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
    }

    public void getBalance(View v) {

        float currentBalance = 0.0f;

        try {
            EditText balance = (EditText) findViewById(R.id.edit);
            String stringOf;
            stringOf = balance.getText().toString();
            currentBalance = Float.parseFloat(stringOf);
        } catch (NumberFormatException e) {
        }

        totalBalance += currentBalance;
        setBalance();

        saveData();
    }

    private void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("balance", totalBalance);
        editor.commit();
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        totalBalance = sp.getFloat("balance", totalBalance);

        setBalance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void resetBalance(View v) {
        totalBalance = 0.0f;
        saveData();
        setBalance();
    }

    public void setBalance() {
        TextView myTextView = (TextView) findViewById(R.id.textBalance);
        myTextView.setText("$ " + totalBalance);
    }

    protected void checkAudioRecordPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        REQUEST_AUDIO_PERMISSIONS_ID);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSIONS_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }



}
