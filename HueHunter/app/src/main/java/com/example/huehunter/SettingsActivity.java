package com.example.huehunter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_COLOUR_BLIND = "colour_blind_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new SettingsFragment(), null).commit();
        }
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

