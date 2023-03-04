package com.example.huehunter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
//    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        view = (ImageView) this.findViewById(R.id.imageView);

        //request for camera runtime permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
    }
    public void cameraClick(View view){
        Intent intent = new Intent(this,Analyze.class);
        startActivity(intent);

    }
//    public void cameraClick(View view) {
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,100);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==100){
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//            if(null == view) {
//                Log.e("Error", "Ouh! there is no there is no child view with R.id.imageView ID within my parent view View.");
//            }
//            view.setImageBitmap(bitmap);
//        }
//    }

}