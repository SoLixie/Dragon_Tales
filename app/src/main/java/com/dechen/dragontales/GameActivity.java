package com.dechen.dragontales;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Button startButton;
    private Button newGameButton;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game); // Use your XML layout

        // Get references to the GameView and TextView
        gameView = findViewById(R.id.game_view);
        scoreTextView = findViewById(R.id.score_text_view);

        // Create Start Button
        startButton = new Button(this);
        startButton.setText("Start Game");
        startButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        // Create New Game Button
        newGameButton = new Button(this);
        newGameButton.setText("New Game");
        newGameButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newGameButton.setVisibility(View.GONE); // Initially hidden
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        // Update score listener
        gameView.setOnScoreChangeListener(new GameView.OnScoreChangeListener() {
            @Override
            public void onScoreChanged(int newScore) {
                if (newScore >= 0) {
                    scoreTextView.setText("Score: " + newScore);
                } else {
                    // Handle game over here if needed
                }
            }
        });
    }

    private void startGame() {
        gameView.startGame();
        startButton.setVisibility(View.GONE); // Hide Start button after starting
        newGameButton.setVisibility(View.VISIBLE); // Show New Game button
    }

    private void newGame() {
        gameView.stopGame(); // Stop any existing game logic
        gameView.startGame(); // Start a new game
        scoreTextView.setText("Score: 0"); // Reset the score display
    }

}
