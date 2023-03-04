package com.example.huehunter;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Locale;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Analyze extends AppCompatActivity {
    ImageView view;
    Uri outPutfileUri;
    String mCurrentPhotoPath;
    TextView circle;

    Magnifier magnifier;

    TextToSpeech tts;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        view = (ImageView) this.findViewById(R.id.imageView);
        circle = (TextView)findViewById(R.id.invisibleCircle);
        if(MainActivity.isItGallery==false) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = new File(Environment.getExternalStorageDirectory(),"MyPhoto.jpg");
            outPutfileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", createImageFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            startActivityForResult(intent, 1);

        } else {
            // Get image URI
            Intent intent = getIntent();
            Uri uriFromGallery = Uri.parse(intent.getStringExtra("Image"));

            // Load the image
            try {
                final InputStream imageStream = getContentResolver().openInputStream(uriFromGallery);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                Toast.makeText(Analyze.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }

        }


        View view2 = findViewById(R.id.imageView);
        magnifier = new Magnifier(view2);
        magnifier.setZoom(4);
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        // Fall through.
                    case MotionEvent.ACTION_MOVE: {
                        circle.setVisibility(View.VISIBLE);
                        final int[] viewPosition = new int[2];
                        v.getLocationOnScreen(viewPosition);
                        magnifier.show(event.getRawX() - viewPosition[0],
                                event.getRawY() - viewPosition[1]);
                        circle.setX(event.getRawX() - viewPosition[0]+70);
                        circle.setY(event.getRawY() - viewPosition[1]+130);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        // Fall through.
                    case MotionEvent.ACTION_UP: {
                        magnifier.dismiss();
                        circle.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    int lang = tts.setLanguage(Locale.getDefault()); // Get the phone's locale
                    // Check if the language is supported
                    if (lang == TextToSpeech.LANG_MISSING_DATA) {
                        Toast.makeText(getApplicationContext(), "Language isn't supported by TTS", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "TTS initialization failed!", Toast.LENGTH_SHORT).show();
                }
            };
        });

    }

    Bitmap bitmap = null;

    private File createImageFile()  {
        // Create an image file name
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "Camera");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
            return image;
        }
        catch(Exception e){
            Log.e("Error creating image", Log.getStackTraceString(e));
            return null;
        }
    }

    private void sayColour(String colourName) {
        int status = tts.speak(colourName, TextToSpeech.QUEUE_FLUSH, null, "ID");
        if (status == TextToSpeech.ERROR) {
            Toast.makeText(this, "Can't use TTS engine!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            sayColour("blue");

            String uri = outPutfileUri.toString();
            Log.e("uri-:", uri);
            Toast.makeText(this, outPutfileUri.toString(), Toast.LENGTH_LONG).show();

            //Bitmap myBitmap = BitmapFactory.decodeFile(uri);
            // mImageView.setImageURI(Uri.parse(uri));   OR drawable make image strechable so try bleow also

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                view.setImageDrawable(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

    ///
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==100){
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//            onCaptureImageResult(data);
//        }
//    }
//    private void onCaptureImageResult(Intent data) {
//        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//        File destination = new File(Environment.getExternalStorageDirectory(),
//                System.currentTimeMillis() + ".jpg");
//
//        FileOutputStream fo;
//        try {
//            destination.createNewFile();
//            fo = new FileOutputStream(destination);
//            fo.write(bytes.toByteArray());
//            fo.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        view.setImageBitmap(thumbnail);
//    }
//}