package com.example.huehunter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    static boolean isItGallery;
    static HashMap<String, int[]> colours = new HashMap<>();
//    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colours = new HashMap<>();
        fillColors();
//        view = (ImageView) this.findViewById(R.id.imageView);

        //request for camera runtime permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
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

    public void fillColors(){
        colours.put("Red", new int[]{255, 0, 0});
        colours.put("Green", new int[]{0, 255, 0});
        colours.put("Blue", new int[]{0, 0, 255});
        colours.put("Yellow", new int[]{255, 255, 0});
        colours.put("Magenta", new int[]{255, 0, 255});
        colours.put("Cyan", new int[]{0, 255, 255});
        colours.put("Black", new int[]{0, 0, 0});
        colours.put("White", new int[]{255, 255, 255});
        colours.put("Gray", new int[]{128, 128, 128});
        colours.put("Orange", new int[]{255, 165, 0});
        colours.put("Brown", new int[]{165, 42, 42});
        colours.put("Purple", new int[]{128, 0, 128});
        colours.put("Pink", new int[]{255, 192, 203});
        colours.put("Lime", new int[]{0, 255, 0});
        colours.put("Teal", new int[]{0, 128, 128});
        colours.put("Turquoise", new int[]{64, 224, 208});
        colours.put("Gold", new int[]{255, 215, 0});
        colours.put("Silver", new int[]{192, 192, 192});
        colours.put("Beige", new int[]{245, 245, 220});
        colours.put("Maroon", new int[]{128, 0, 0});

        colours.put("Red", new int[]{255, 0, 0});
        colours.put("Green", new int[]{0, 255, 0});
        colours.put("Blue", new int[]{0, 0, 255});
        colours.put("Yellow", new int[]{255, 255, 0});
        colours.put("Magenta", new int[]{255, 0, 255});
        colours.put("Cyan", new int[]{0, 255, 255});
        colours.put("Black", new int[]{0, 0, 0});
        colours.put("White", new int[]{255, 255, 255});
        colours.put("Gray", new int[]{128, 128, 128});
        colours.put("Orange", new int[]{255, 165, 0});
        colours.put("Brown", new int[]{165, 42, 42});
        colours.put("Purple", new int[]{128, 0, 128});
        colours.put("Pink", new int[]{255, 192, 203});
        colours.put("Lime", new int[]{0, 255, 0});
        colours.put("Teal", new int[]{0, 128, 128});
        colours.put("Turquoise", new int[]{64, 224, 208});
        colours.put("Gold", new int[]{255, 215, 0});
        colours.put("Silver", new int[]{192, 192, 192});
        colours.put("Beige", new int[]{245, 245, 220});
        colours.put("Maroon", new int[]{128, 0, 0});

        colours.put("Dark Red", new int[]{139, 0, 0});
        colours.put("Dark Green", new int[]{0, 100, 0});
        colours.put("Dark Blue", new int[]{0, 0, 139});
        colours.put("Dark Purple", new int[]{128, 0, 128});
        colours.put("Dark Pink", new int[]{231, 84, 128});
        colours.put("Dark Magenta", new int[]{139, 0, 139});
        colours.put("Dark Teal", new int[]{0, 128, 128});
        colours.put("Dark Yellow", new int[]{204, 204, 0});

        colours.put("Light Red", new int[]{255, 153, 153});
        colours.put("Light Green", new int[]{153, 255, 153});
        colours.put("Light Blue", new int[]{173, 216, 230});
        colours.put("Light Yellow", new int[]{255, 255, 224});
        colours.put("Light Green", new int[]{144, 238, 144});
        colours.put("Light Blue", new int[]{173, 216, 230});
        colours.put("Light Pink", new int[]{255, 182, 193});
        colours.put("Light Purple", new int[]{230, 230, 250});
        colours.put("Light Gray", new int[]{211, 211, 211});
        colours.put("Light Brown", new int[]{205, 133, 63});
        colours.put("Light Orange", new int[]{255, 204, 153});

        colours.put("Pale Red", new int[]{255, 240, 240});
        colours.put("Pale Green", new int[]{224, 255, 224});
        colours.put("Pale Blue", new int[]{223, 239, 255});
        colours.put("Pale Yellow", new int[]{255, 255, 224});
        colours.put("Pale Magenta", new int[]{255, 224, 255});
        colours.put("Pale Cyan", new int[]{224, 255, 255});
        colours.put("Pale Pink", new int[]{255, 192, 203});
        colours.put("Pale Orange", new int[]{255, 218, 185});

        colours.put("Olive", new int[]{128, 128, 0});
        colours.put("Khaki", new int[]{240, 230, 140});
        colours.put("Navy", new int[]{0, 0, 128});
        colours.put("Indigo", new int[]{75, 0, 130});
        colours.put("Salmon", new int[]{250, 128, 114});
        colours.put("Coral", new int[]{255, 127, 80});
        colours.put("Aquamarine", new int[]{127, 255, 212});
        colours.put("Violet", new int[]{238, 130, 238});
        colours.put("Plum", new int[]{221, 160, 221});
        colours.put("Saddle Brown", new int[]{139, 69, 19});
        colours.put("Steel Blue", new int[]{70, 130, 180});
        colours.put("Dark Slate Gray", new int[]{47, 79, 79});
        colours.put("Sea Green", new int[]{46, 139, 87});
        colours.put("Dark Olive Green", new int[]{85, 107, 47});
        colours.put("Fire Brick", new int[]{178, 34, 34});
        colours.put("Sandy Brown", new int[]{244, 164, 96});
        colours.put("Sienna", new int[]{160, 82, 45});
        colours.put("Sky Blue", new int[]{135, 206, 235});
        colours.put("Indian Red", new int[]{205, 92, 92});
        colours.put("Rosy Brown", new int[]{188, 143, 143});
        colours.put("Dark Turquoise", new int[]{0, 206, 209});
        colours.put("Cadet Blue", new int[]{95, 158, 160});
        colours.put("Tomato", new int[]{255, 99, 71});
        colours.put("Chocolate", new int[]{210, 105, 30});
    }
}