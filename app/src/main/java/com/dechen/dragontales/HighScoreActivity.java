package com.dechen.dragontales;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreActivity extends AppCompatActivity {

    private ListView highScoreListView;
    private ArrayList<String> highScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highScoreListView = findViewById(R.id.high_score_list);
        highScores = loadHighScores();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        highScoreListView.setAdapter(adapter);
    }

    private ArrayList<String> loadHighScores() {
        SharedPreferences sharedPreferences = getSharedPreferences("DragonTales", MODE_PRIVATE);
        ArrayList<String> scores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String score = sharedPreferences.getString("high_score_" + i, null);
            if (score != null) {
                scores.add(score);
            }
        }
        Collections.sort(scores);
        return scores;
    }
}
