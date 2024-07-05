package com.devmarufit.spinwheelerdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {


    private static final String PREFS_NAME = "MyPrefs";
    private static final String TOTAL_KEY = "totalKey";
    private int remainingTotal;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        remainingTotal = sharedPreferences.getInt(TOTAL_KEY, 0);

        // Setup UI elements
        EditText inputValueEditText = findViewById(R.id.inputValueEditText);
        Button addButton = findViewById(R.id.addButton);
        TextView totalTextView = findViewById(R.id.totalTextView);

        // Display the current total
        totalTextView.setText(String.valueOf(remainingTotal));

        // Set up button click listener
        addButton.setOnClickListener(v -> {
            String inputValueString = inputValueEditText.getText().toString();
            if (!inputValueString.isEmpty()) {
                int newValue = Integer.parseInt(inputValueString);
                addToTotal(newValue, totalTextView);
            }
        });

    }

    private void addToTotal(int newLife, TextView totalTextView) {
        remainingTotal += newLife;
        // Save the updated total to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TOTAL_KEY, remainingTotal);
        editor.apply();
        // Update the TextView
        totalTextView.setText(String.valueOf(remainingTotal));
    }



}