package com.example.huehunter;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

public class MainActivity extends AppCompatActivity {
    static boolean isItGallery;
    static HashMap<String, int[]> colours = new HashMap<>();
//    ImageView view;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hiding the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();



        colours = new HashMap<>();
        fillColors();
//        view = (ImageView) this.findViewById(R.id.imageView);

        //request for camera runtime permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public void openGallery(View view) {
        isItGallery=true;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    public void openAbout(View view) {}

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void cameraClick(View view){
        isItGallery=false;
        Intent intent = new Intent(this,Analyze.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Intent intent = new Intent(MainActivity.this, Analyze.class);
            intent.putExtra("Image", selectedImage.toString()); // Pass image to the other activity
            // Send the user to the Analyze activity
            startActivity(intent);
        }
    }

    public void fillColors() {
        colours.put("Alice Blue", new int[] {0xF0, 0xF8, 0xFF});
        colours.put("Antique White", new int[] {0xFA, 0xEB, 0xD7});
        colours.put("Aqua", new int[] {0x00, 0xFF, 0xFF});
        colours.put("Aquamarine", new int[] {0x7F, 0xFF, 0xD4});
        colours.put("Azure", new int[] {0xF0, 0xFF, 0xFF});
        colours.put("Beige", new int[] {0xF5, 0xF5, 0xDC});
        colours.put("Bisque", new int[] {0xFF, 0xE4, 0xC4});
        colours.put("Black", new int[] {0x00, 0x00, 0x00});
        colours.put("Blanched Almond", new int[] {0xFF, 0xEB, 0xCD});
        colours.put("Blue", new int[] {0x00, 0x00, 0xFF});
        colours.put("Blue Violet", new int[] {0x8A, 0x2B, 0xE2});
        colours.put("Brown", new int[] {0xA5, 0x2A, 0x2A});
        colours.put("Burlywood", new int[] {0xDE, 0xB8, 0x87});
        colours.put("Cadet Blue", new int[] {0x5F, 0x9E, 0xA0});
        colours.put("Chartreuse", new int[] {0x7F, 0xFF, 0xF0});
        colours.put("Chocolate", new int[] {0xD2, 0x69, 0x1E});
        colours.put("Coral", new int[] {0xFF, 0x7F, 0x50});
        colours.put("Cornflower Blue", new int[] {0x64, 0x95, 0xED});
        colours.put("Cornsilk", new int[] {0xFF, 0xF8, 0xDC});
        colours.put("Crimson", new int[] {0xDC, 0x14, 0x3C});
        colours.put("Cyan", new int[] {0x00, 0xFF, 0xFF});
        colours.put("Dark Blue", new int[] {0x00, 0x00, 0x8B});
        colours.put("Dark Cyan", new int[] {0x00, 0x8B, 0x8B});
        colours.put("Dark Goldenrod", new int[] {0xB8, 0x86, 0x0B});
        colours.put("Dark Gray", new int[] {0xA9, 0xA9, 0xA9});
        colours.put("Dark Green", new int[] {0x00, 0x64, 0x00});
        colours.put("Dark Khaki", new int[] {0xBD, 0xB7, 0x6B});
        colours.put("Dark Magenta", new int[] {0x8B, 0x00, 0x8B});
        colours.put("Dark Olive Green", new int[] {0x55, 0x6B, 0x2F});
        colours.put("Dark Orange", new int[] {0xFF, 0x8C, 0x00});
        colours.put("Dark Orchid", new int[] {0x99, 0x32, 0xCC});
        colours.put("Dark Red", new int[] {0x8B, 0x00, 0x00});
        colours.put("Dark Salmon", new int[] {0xE9, 0x96, 0x7A});
        colours.put("Dark Sea Green", new int[] {0x8F, 0xBC, 0x8F});
        colours.put("Dark Slate Blue", new int[] {0x48, 0x3D, 0x8B});
        colours.put("Dark Slate Gray", new int[] {0x2F, 0x4F, 0x4F});
        colours.put("Dark Turqoise", new int[] {0x00, 0xCE, 0xD1});
        colours.put("Dark Violet", new int[] {0x94, 0x00, 0xD3});
        colours.put("Deep Pink", new int[] {0xFF, 0x14, 0x93});
        colours.put("Deep Sky Blue", new int[] {0x00, 0xBF, 0xFF});
        colours.put("Dim Gray", new int[] {0x69, 0x69, 0x69});
        colours.put("Dodger Blue", new int[] {0x1E, 0x90, 0xFF});
        colours.put("Firebrick", new int[] {0xB2, 0x22, 0x22});
        colours.put("Floral White", new int[] {0xFF, 0xFA, 0xF0});
        colours.put("Forest Green", new int[] {0x22, 0x8B, 0x22});
        colours.put("Fuschia", new int[] {0xFF, 0x00, 0xFF});
        colours.put("Gainsboro", new int[] {0xDC, 0xDC, 0xDC});
        colours.put("Ghost White", new int[] {0xF8, 0xF8, 0xFF});
        colours.put("Gold", new int[] {0xFF, 0xD7, 0x00});
        colours.put("Goldenrod", new int[] {0xDA, 0xA5, 0x20});
        colours.put("Gray", new int[] {0x80, 0x80, 0x80});
        colours.put("Green", new int[] {0x00, 0x80, 0x00});
        colours.put("Green Yellow", new int[] {0xAD, 0xFF, 0x2F});
        colours.put("Honeydew", new int[] {0xF0, 0xFF, 0xF0});
        colours.put("Hot Pink", new int[] {0xFF, 0x69, 0xB4});
        colours.put("Indigo", new int[]{0x4B, 0x00, 0x82});
        colours.put("Ivory", new int[]{0xFF, 0xFF, 0x00});
        colours.put("Khaki", new int[]{0xF0, 0xE6, 0x8C});
        colours.put("Lavender", new int[]{0xE6, 0xE6, 0xFA});
        colours.put("Lavender Blush", new int[]{0xFF, 0xF0, 0xF5});
        colours.put("Lawn Green", new int[]{0x7C, 0xFC, 0x00});
        colours.put("Lemon Chiffon", new int[]{0xFF, 0xFA, 0xCD});
        colours.put("Light Blue", new int[]{0xAD, 0xD8, 0xE6});
        colours.put("Light Coral", new int[]{0xF0, 0x80, 0x80});
        colours.put("Light Cyan", new int[]{0xE0, 0xFF, 0xFF});
        colours.put("Light Goldenrod Yellow", new int[]{0xFA, 0xFa, 0xD2});
        colours.put("Light Gray", new int[]{0xD3, 0xD3, 0xD3});
        colours.put("Light Green", new int[]{0x90, 0xEE, 0x90});
        colours.put("Light Pink", new int[]{0xFF, 0xB6, 0xC1});
        colours.put("Light Salmon", new int[]{0xFF, 0xA0, 0x7A});
        colours.put("Light Sea Green", new int[]{0x20, 0xB2, 0xAA});
        colours.put("Light Sky Blue", new int[]{0x87, 0xCE, 0xFA});
        colours.put("Light Slate Gray", new int[]{0x77, 0x88, 0x99});
        colours.put("Light Steel Blue", new int[]{0xB0, 0xC4, 0xDE});
        colours.put("Light Yellow", new int[]{0xFF, 0xFF, 0xE0});
        colours.put("Lime", new int[]{0x00, 0xFF, 0x00});
        colours.put("Lime Green", new int[]{0x32, 0xCD, 0x32});
        colours.put("Linen", new int[]{0xFA, 0xF0, 0xE6});
        colours.put("Magenta", new int[]{0xFF, 0x00, 0xFF});
        colours.put("Maroon", new int[]{0x80, 0x00, 0x00});
        colours.put("Medium Aquamarine", new int[]{0x66, 0xCD, 0xAA});
        colours.put("Medium Blue", new int[]{0x00, 0x00, 0xCD});
        colours.put("Medium Orchid", new int[]{0xBA, 0x55, 0xD3});
        colours.put("Medium Purple", new int[]{0x93, 0x70, 0xDB});
        colours.put("Medium Sea Green", new int[]{0x3C, 0xB3, 0x71});
        colours.put("Medium Slate Blue", new int[]{0x7B, 0x68, 0xEE});
        colours.put("Medium Spring Green", new int[]{0x00, 0xFA, 0x9A});
        colours.put("Medium Turquoise", new int[]{0x48, 0xD1, 0xCC});
        colours.put("Medium Violet Red", new int[]{0xC7, 0x15, 0x85});
        colours.put("Midnight Blue", new int[]{0x19, 0x19, 0x70});
        colours.put("Mint Cream", new int[]{0xF5, 0xFF, 0xFA});
        colours.put("Misty Rose", new int[]{0xFF, 0xE4, 0xE1});
        colours.put("Moccasin", new int[]{0xFF, 0xE4, 0xB5});
        colours.put("Navajo White", new int[]{0xFF, 0xDE, 0xAD});
        colours.put("Navy", new int[]{0x00, 0x00, 0x80});
        colours.put("Old Lace", new int[]{0xFD, 0xF5, 0xE6});
        colours.put("Olive", new int[]{0x80, 0x80, 0x00});
        colours.put("Olive Drab", new int[]{0x6B, 0x8E, 0x23});
        colours.put("Orange", new int[]{0xFF, 0xA5, 0x00});
        colours.put("Orange Red", new int[]{0xFF, 0x45, 0x00});
        colours.put("Orchid", new int[]{0xDA, 0x70, 0xD6});
        colours.put("Pale Goldenrod", new int[]{0xEE, 0xE8, 0xAA});
        colours.put("Pale Green", new int[]{0x98, 0xFB, 0x98});
        colours.put("Pale Turquoise", new int[]{0xAF, 0xEE, 0xEE});
        colours.put("Pale Violet Red", new int[]{0xDB, 0x70, 0x93});
        colours.put("Papaya Whip", new int[]{0xFF, 0xEF, 0xD5});
        colours.put("Peach Puff", new int[]{0xFF, 0xDA, 0xB9});
        colours.put("Peru", new int[]{0xCD, 0x85, 0x3F});
        colours.put("Pink", new int[]{0xFF, 0xC0, 0xCB});
        colours.put("Plum", new int[]{0xDD, 0xA0, 0xDD});
        colours.put("Powder Blue", new int[]{0xB0, 0xE0, 0xE6});
        colours.put("Purple", new int[]{0x80, 0x00, 0x80});
        colours.put("Rebecca Purple", new int[]{0x66, 0x33, 0x99});
        colours.put("Red", new int[]{0xFF, 0x00, 0x00});
        colours.put("Rosy Brown", new int[]{0xBC, 0x8F, 0x8F});
        colours.put("Royal Blue", new int[]{0x41, 0x69, 0xE1});
        colours.put("Rust", new int[] {0xCD, 0x5C, 0x5C});
        colours.put("Saddle Brown", new int[]{0x8B, 0x45, 0x13});
        colours.put("Salmon", new int[]{0xFA, 0x80, 0x72});
        colours.put("Sandy Brown", new int[]{0xF4, 0xA4, 0x60});
        colours.put("Sea Green", new int[]{0x2E, 0x8B, 0x57});
        colours.put("Sea Shell", new int[]{0xFF, 0xF5, 0xEE});
        colours.put("Sienna", new int[]{0xA0, 0x52, 0x2D});
        colours.put("Silver", new int[]{0xC0, 0xC0, 0xC0});
        colours.put("Sky Blue", new int[]{0x87, 0xCE, 0xEB});
        colours.put("Slate Blue", new int[]{0x6A, 0x5A, 0xCD});
        colours.put("Slate Gray", new int[]{0x70, 0x80, 0x90});
        colours.put("Snow", new int[]{0xFF, 0xFA, 0xFA});
        colours.put("Spring Green", new int[]{0x00, 0xFF, 0x7F});
        colours.put("Steel Blue", new int[]{0x46, 0x82, 0xB4});
        colours.put("Tan", new int[]{0xD2, 0xB4, 0x8C});
        colours.put("Teal", new int[]{0x00, 0x80, 0x80});
        colours.put("Thistle", new int[]{0xD8, 0xBF, 0xD8});
        colours.put("Tomato", new int[]{0xFF, 0x63, 0x47});
        colours.put("Turquoise", new int[]{0x40, 0xE0, 0xD0});
        colours.put("Violet", new int[]{0xEE, 0x82, 0xEE});
        colours.put("Wheat", new int[]{0xF5, 0xDE, 0xB3});
        colours.put("White", new int[]{0xFF, 0xFF, 0xFF});
        colours.put("White Smoke", new int[]{0xF5, 0xF5, 0xF5});
        colours.put("Yellow", new int[]{0xFF, 0xFF, 0x00});
        colours.put("Yellow Green", new int[]{0x9A, 0xCD, 0x32});
    }

    public void goColorTest(View view){
        Intent intent = new Intent(this,TestActivity.class);
        startActivity(intent);
    }
    public void goSettings(View view){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
}