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


public class MainActivity extends AppCompatActivity {
    static boolean isItGallery;
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

    public void openGallery(View view) {
        isItGallery=true;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    public void openAbout(View view) {}

    public void openSettings(View view) {
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