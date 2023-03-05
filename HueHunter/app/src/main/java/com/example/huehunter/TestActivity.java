package com.example.huehunter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    boolean redGreen;
    boolean blueYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //hiding the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        Bitmap redBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas redCanvas = new Canvas(redBitmap);
        Bitmap greenBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas greenCanvas = new Canvas(greenBitmap);
        Bitmap blueBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas blueCanvas = new Canvas(blueBitmap);
        Bitmap yellowBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas yellowCanvas = new Canvas(yellowBitmap);

        Paint paint = new Paint();
        //paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#b1766f"));
        redCanvas.drawRect(0, 0, 100, 100, paint);
        paint.setColor(Color.parseColor("#7e9760"));
        greenCanvas.drawRect(0, 0, 100, 100, paint);
        paint.setColor(Color.parseColor("#4e9689"));
        blueCanvas.drawRect(0, 0, 100, 100, paint);
        paint.setColor(Color.parseColor("#6693a8"));
        yellowCanvas.drawRect(0, 0, 100, 100, paint);

        ImageView redView = (ImageView)findViewById(R.id.red_square);
        ImageView greenView = (ImageView)findViewById(R.id.green_square);
        ImageView blueView = (ImageView)findViewById(R.id.blue_square);
        ImageView yellowView = (ImageView)findViewById(R.id.yellow_square);

        redView.setImageBitmap(redBitmap);
        greenView.setImageBitmap(greenBitmap);
        blueView.setImageBitmap(blueBitmap);
        yellowView.setImageBitmap(yellowBitmap);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPref.edit();
        int type  = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_COLOUR_BLIND, "0"));
        Switch rgSwitch = (Switch) findViewById(R.id.rg_switch);
        Switch bySwitch = (Switch) findViewById(R.id.by_switch);

        switch (type) {
            case 0:
                rgSwitch.setChecked(false);
                bySwitch.setChecked(false);
                redGreen = false;
                blueYellow = false;
                break;
            case 1:
                rgSwitch.setChecked(true);
                bySwitch.setChecked(false);
                redGreen = true;
                blueYellow = false;
                break;
            case 2:
                rgSwitch.setChecked(false);
                bySwitch.setChecked(true);
                redGreen = false;
                blueYellow = true;
                break;
            case 3:
                rgSwitch.setChecked(true);
                bySwitch.setChecked(true);
                redGreen = true;
                blueYellow = true;
                break;
        }

        rgSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                redGreen = true;
                if (blueYellow) {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(3));
                    Toast.makeText(TestActivity.this, "You are monochromatic", Toast.LENGTH_SHORT).show();
                } else {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(1));
                    Toast.makeText(TestActivity.this, "You are red-green colour blind", Toast.LENGTH_SHORT).show();
                }
            } else {
                redGreen = false;
                if (blueYellow) {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(2));
                    Toast.makeText(TestActivity.this, "You are blue-yellow colour blind", Toast.LENGTH_SHORT).show();
                } else {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(0));
                    Toast.makeText(TestActivity.this, "You are not colour blind", Toast.LENGTH_SHORT).show();
                }
            }
            edit.putBoolean("isDirtyPrefs", true);
            edit.apply();
        });

        bySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                blueYellow = true;
                if (redGreen) {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(3));
                    Toast.makeText(TestActivity.this, "You are monochromatic", Toast.LENGTH_SHORT).show();
                } else {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(2));
                    Toast.makeText(TestActivity.this, "You are blue-yellow colour blind", Toast.LENGTH_SHORT).show();
                }
            } else {
                blueYellow = false;
                if (redGreen) {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(1));
                    Toast.makeText(TestActivity.this, "You are red-green colour blind", Toast.LENGTH_SHORT).show();
                } else {
                    edit.putString(SettingsActivity.KEY_PREF_COLOUR_BLIND, Integer.toString(0));
                    Toast.makeText(TestActivity.this, "You are not colour blind", Toast.LENGTH_SHORT).show();
                }
            }
            edit.putBoolean("isDirtyPrefs", true);
            edit.apply();

        });

    }
    public void goHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}