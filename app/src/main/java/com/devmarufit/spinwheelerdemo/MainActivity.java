package com.devmarufit.spinwheelerdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final Random random = new Random();
    private final int SECTORS = 360 / 8;
    private ImageView wheel;
    private boolean isSpinning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton spin = findViewById(R.id.spin);
        wheel = findViewById(R.id.wheel);

        spin.setOnClickListener(v -> {
            if (!isSpinning) {
                spinTheWheel();
            }
        });

    }

    private Integer getDegree(int... numbers) {
        int degree = random.nextInt(360) + 3600;
        for (int number : numbers) {
            if ((degree % 360) == number) return getDegree(numbers);
        }
        return degree;
    }

    private void spinTheWheel() {

        // 47, 97, 186, 234, 275, 317
        int degree = getDegree(0, 1, 2, 3, 4, 5, 6, 7, 8, 46, 47, 92, 93, 94, 95, 96, 97, 270, 271, 272, 273, 274, 275, 276, 277, 278);
        wheel.getWidth();
        wheel.getHeight();

        ObjectAnimator animator = ObjectAnimator.ofFloat(wheel, "rotation", 0, degree);
        animator.setDuration(3000); // Duration of the spin
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isSpinning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isSpinning = false;
                int resultSector = (degree % 360) / SECTORS;
                handleSpinResult(resultSector);
            }
        });
        animator.start();

    }

    private void handleSpinResult(int sector) {
        // Handle the result of the spin
        // For example, display the selected category
        String[] categories = {"3", "1", "3", "1", "1", "1", "2", "2"};
        String selectedCategory = categories[sector];
        // Display the selected category (you could use a Toast, a TextView, etc.)

        Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
    }


}