package com.example.fayadh.androidfinance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    float totalBalance = 0.0f;


    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();

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




}
