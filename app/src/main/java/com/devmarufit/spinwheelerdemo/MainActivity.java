package com.devmarufit.spinwheelerdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final Random random = new Random();
    private final int SECTORS = 360 / 8;
    private ImageView wheel;
    private TextView remain_lives;
    private boolean isSpinning = false;
    private String selectedCategory = "";
    private Button ad_btn, new_activity;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "QuizPrefs";
    private static final String KEY_LIVES = "lives";
    private RewardedAd mRewardedAd;
    private final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
    private int CURRENT_LIVES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton spin = findViewById(R.id.spin);
        wheel = findViewById(R.id.wheel);
        remain_lives = findViewById(R.id.remain_lives);

        new_activity = findViewById(R.id.new_activity);
        new_activity.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        });

        loadRewardedAd();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        CURRENT_LIVES = sharedPreferences.getInt(KEY_LIVES, 0);
        remain_lives.setText(String.valueOf(CURRENT_LIVES));

        spin.setOnClickListener(v -> {
            if (!isSpinning) {
                spinTheWheel();
            }
        });

        ad_btn = findViewById(R.id.ad_btn);
        ad_btn.setOnClickListener(v -> {
            showRewardedAd();
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
        //String[] categories = {"5", "2", "1", "2", "2", "3", "2", "1"};
        selectedCategory = categories[sector];
        // Display the selected category (you could use a Toast, a TextView, etc.)
        spinLives();
        remain_lives.setText(selectedCategory);

        Toast.makeText(this, "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
    }

    private void spinLives() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        CURRENT_LIVES = sharedPreferences.getInt(KEY_LIVES, 0); // Default to 0 if not found

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LIVES, Integer.parseInt(selectedCategory));
        editor.apply();
        Toast.makeText(this, "You have earned " + selectedCategory + " life/lives!", Toast.LENGTH_SHORT).show();

    }



    private void loadRewardedAd() {
        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();

        RewardedAd.load(this, AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
                //Toast.makeText(Spin_Wheeler.this, "Ad Loaded", Toast.LENGTH_SHORT).show();

                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        loadRewardedAd(); // Preload the next ad
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mRewardedAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                mRewardedAd = null;
                Toast.makeText(MainActivity.this, "Ad Failed to Load", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd.show(MainActivity.this, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(RewardItem rewardItem) {
                    // Handle the reward
                    //int rewardAmount = rewardItem.getAmount();
                    //String rewardType = rewardItem.getType();
                    //selectedCategory = rewardItem.getType();
                    spinTheWheel();
                    //Toast.makeText(MainActivity.this, "User earned reward: " + " " + selectedCategory, Toast.LENGTH_SHORT).show();
                    remain_lives.setText(selectedCategory);// Initialize currentLives
                }
            });
        } else {
            Toast.makeText(this, "The rewarded ad wasn't ready yet.", Toast.LENGTH_SHORT).show();
        }
    }


}