package com.example.fayadh.androidfinance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getBalance(View v) {

        EditText balance = (EditText) findViewById(R.id.edit);
        String stringOf = balance.getText().toString();

        TextView myTextView = (TextView) findViewById(R.id.textBalance);
        myTextView.setText("$" + stringOf);
    }
}
