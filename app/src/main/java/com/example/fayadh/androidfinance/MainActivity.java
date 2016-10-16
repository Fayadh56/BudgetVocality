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

    Double totalBalance = 0.0;
    String FILENAME = "data_file";
    String stringOf = "";
    String stringSaved = "";


    public MainActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void getBalance(View v) {

        Double currentBalance = 0.0;

        try {
            EditText balance = (EditText) findViewById(R.id.edit);
            stringOf = balance.getText().toString();
            currentBalance = Double.parseDouble(stringOf);
        } catch (NumberFormatException e) {
        }

        totalBalance += currentBalance;

        TextView myTextView = (TextView) findViewById(R.id.textBalance);
        myTextView.setText("$ " + totalBalance);
    }

    private void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("currency", stringOf);
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        stringSaved = sp.getString("currency", stringOf);

        TextView myTextView = (TextView) findViewById(R.id.textBalance);
        myTextView.setText("$ " + stringOf);
    }


}
