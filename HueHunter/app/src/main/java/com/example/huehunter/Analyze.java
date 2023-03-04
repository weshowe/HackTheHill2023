package com.example.huehunter;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.graphics.Point;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Analyze extends AppCompatActivity {
    ImageView view;
    Uri outPutfileUri;
    String mCurrentPhotoPath;
    TextView circle;
    TextView colorTellingText;

    Magnifier magnifier;
    Map<String, Object> colorNames;
    TextToSpeech tts;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        view = (ImageView) this.findViewById(R.id.imageView);
        circle = (TextView)findViewById(R.id.invisibleCircle);
        colorTellingText = findViewById(R.id.colorTellingText);

        getColors();

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
                        magnifier.show(event.getRawX() - viewPosition[0], event.getRawY() - viewPosition[1]);
                        circle.setX(event.getRawX() - viewPosition[0]+70);
                        circle.setY(event.getRawY() - viewPosition[1]+130);

                        // Get location of the small circle
                        float X = circle.getX();
                        float Y = circle.getY();

                        // Extract the rgb values
                        int pixel = bitmap.getPixel((int)X, (int)Y);
                        int r = Color.red(pixel);
                        int g = Color.green(pixel);
                        int b = Color.blue(pixel);

                        String finalColour = String.format("#%02x%02x%02x", r, g, b); // rgb in hex format
                        //Color newColor = new Color(r,g,b);
                        Log.i("i",getColorName(finalColour,colorNames));
                        colorTellingText.setText(getColorName(finalColour,colorNames));
                        sayColour(getColorName(finalColour,colorNames));
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

    private static String getColorName(String hexColorCode, Map<String, Object> colorMap) {
        String colorName = "unknown";
        if (hexColorCode.length() == 7 && hexColorCode.startsWith("#")) {
            String hex = hexColorCode.substring(1).toLowerCase();
            if (colorMap.containsKey(hex)) {
                colorName = colorMap.get(hex).toString();
            }
        }
        return colorName;
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

    public void getColors(){
        ObjectMapper mapper = new ObjectMapper();


        try {
            // create instance of the File class
            File fileObj = new File("https://github.com/cheprasov/json-colors/blob/master/colors.json");
            colorNames = mapper.readValue(
                    fileObj, new TypeReference<Map<String, Object>>() {
                    });
            Log.i("i",colorNames.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

