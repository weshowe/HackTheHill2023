package com.example.huehunter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_COLOUR_BLIND = "colour_blind_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_settings);
        if (getFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment(), null).commit();
        }
        //hiding the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

    }

    public void goHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPrefs.getBoolean("isDirtyPrefs", false)) {
            SharedPreferences.Editor edit = sharedPrefs.edit();
            edit.putBoolean("isDirtyPrefs", false);
            edit.apply();
            recreate();
        }
    }
}

