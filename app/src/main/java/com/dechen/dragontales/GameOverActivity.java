package com.dechen.dragontales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreTextView = findViewById(R.id.final_score_text_view);
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText("Final Score: " + score);
    }

    public void onRestartClick(View view) {
        Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void onMainMenuClick(View view) {
        Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
