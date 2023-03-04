package com.example.huehunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class Analyze extends AppCompatActivity {
    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        ttsr=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
         @Override
         public void onInit(int status) {
            if(status != TextToSpeech.ERROR) {
               ttsr.setLanguage(Locale.US);
            }
         }
        });

        ttsr.speak("This is a test to see if the Android TTS works", TextToSpeech.QUEUE_FLUSH, null);

        view = (ImageView) this.findViewById(R.id.imageView);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            if(null == view) {
                Log.e("Error", "Ouh! there is no there is no child view with R.id.imageView ID within my parent view View.");
            }
            view.setImageBitmap(bitmap);
        }
    }
}