package com.dechen.dragontales;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // Duration of splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your splash layout

        // Get references to views
        View dragonImage = findViewById(R.id.dragon_image);
        View dragonText = findViewById(R.id.dragon_text);
        View talesText = findViewById(R.id.tales_text);
        View statusText = findViewById(R.id.status_text);

        // Animate dragon image
        ObjectAnimator dragonAnimator = ObjectAnimator.ofFloat(dragonImage, "translationY", -20f, 0f);
        dragonAnimator.setDuration(1000);
        dragonAnimator.start();

        // Fade-in animations for text
        AlphaAnimation fadeInDragonText = new AlphaAnimation(0, 1);
        fadeInDragonText.setDuration(1500);
        fadeInDragonText.setFillAfter(true);
        dragonText.startAnimation(fadeInDragonText);

        AlphaAnimation fadeInTalesText = new AlphaAnimation(0, 1);
        fadeInTalesText.setDuration(1500);
        fadeInTalesText.setFillAfter(true);
        fadeInTalesText.setStartOffset(500); // Delay slightly for better effect
        talesText.startAnimation(fadeInTalesText);

        AlphaAnimation fadeInStatusText = new AlphaAnimation(0, 1);
        fadeInStatusText.setDuration(1500);
        fadeInStatusText.setFillAfter(true);
        fadeInStatusText.setStartOffset(1000); // Delay slightly
        statusText.startAnimation(fadeInStatusText);

        // Delay before transitioning to the main activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class); // Change to your main menu activity
            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}