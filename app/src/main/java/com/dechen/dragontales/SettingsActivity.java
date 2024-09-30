package com.dechen.dragontales;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox soundCheckBox;
    private CheckBox notificationsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        soundCheckBox = findViewById(R.id.sound_checkbox);
        notificationsCheckBox = findViewById(R.id.notifications_checkbox);

        SharedPreferences prefs = getSharedPreferences("DragonTales", MODE_PRIVATE);
        soundCheckBox.setChecked(prefs.getBoolean("sound_enabled", true));
        notificationsCheckBox.setChecked(prefs.getBoolean("notifications_enabled", true));

        soundCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sound_enabled", isChecked);
            editor.apply();
        });

        notificationsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("notifications_enabled", isChecked);
            editor.apply();
        });
    }
}
