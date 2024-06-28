package com.devmarufit.spinwheelerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageButton spin;
    private ImageView wheel;
    private boolean isSpinning = false;
    private static final int SECTORS = 8;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin = findViewById(R.id.spin);
        wheel = findViewById(R.id.wheel);

        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSpinning) {
                    spinTheWheel();
                }
            }

        });

    }
    private void spinTheWheel(){

        int degree = random.nextInt(360) + 3600; // Random degree for spinning
        float pivotX = wheel.getWidth() / 5f;
        float pivotY = wheel.getHeight() / 5f;

        ObjectAnimator animator = ObjectAnimator.ofFloat(wheel, "rotation", 0f, degree);
        animator.setDuration(3000); // Duration of the spin
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSpinning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSpinning = false;
                int resultSector = (degree % 360) / (360 / SECTORS);
                handleSpinResult(resultSector);
            }
        });
        animator.start();

    }
    private void handleSpinResult(int sector) {
        // Handle the result of the spin
        // For example, display the selected category
        String[] categories = {"1", "2", "3", "2", "2", "1", "2", "5"};
        String selectedCategory = categories[sector];
        // Display the selected category (you could use a Toast, a TextView, etc.)
        Toast.makeText(this,"Selected: " + selectedCategory,Toast.LENGTH_SHORT).show();
    }


}