package com.example.huehunter;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

import java.net.URL;
import java.util.HashMap;

import java.util.Arrays;

import java.util.Locale;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.lang.Math;

public class Analyze extends AppCompatActivity {
    ImageView view;
    Uri outPutfileUri;
    String mCurrentPhotoPath;
    TextView circle;
    TextView colorTellingText;

    Magnifier magnifier;
    Map<String, Object> colorNames;
    TextToSpeech tts;

    Bitmap bitmap;

    Button galleryButton;
    Button cameraButton;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        view = (ImageView) this.findViewById(R.id.imageView);
        circle = (TextView)findViewById(R.id.invisibleCircle);
        colorTellingText = findViewById(R.id.colorTellingText);
        galleryButton = (Button) findViewById(R.id.gallery_button);
        cameraButton = (Button) findViewById(R.id.camera_button);

        //getColors();

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
                bitmap = BitmapFactory.decodeStream(imageStream);
                view.setImageBitmap(bitmap);
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

                        int orig_height = bitmap.getHeight();
                        int orig_width = bitmap.getWidth();

                        int height_offset = magnifier.getSourceHeight();
                        int width_offset = magnifier.getSourceWidth();
                        Point pos = magnifier.getSourcePosition();

                        int[] zoom_pixels = new int[height_offset*width_offset];

                        System.out.println(orig_height + " " + orig_width + " " + height_offset + " " + width_offset + " " + pos.y + " " + pos.x);

                        try {
                            bitmap.getPixels(zoom_pixels, 0, width_offset, pos.x, pos.y, width_offset, height_offset);
                        }catch(IllegalArgumentException e){
                            System.out.println("Invalid Position");
                            break;
                        }

                        //System.out.println(Arrays.toString(zoom_pixels));

                        //int[] pixel_sum = {0,0,0,0};
                        int[] pixel_sum = {0,0,0};

                        // ARGB format: Alpha, R, G, B Note: Removed alpha so it's RGB
                        for(int i=0; i<zoom_pixels.length; i++){
                            //pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 24) & 0xff); // or color >>> 24
                            pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 16) & 0xff);
                            pixel_sum[1] = pixel_sum[1] + ((zoom_pixels[i] >>  8) & 0xff);
                            pixel_sum[2] = pixel_sum[2] + ((zoom_pixels[i]      ) & 0xff);
                        }

                        for(int i=0; i<pixel_sum.length; i++){
                            pixel_sum[i] = pixel_sum[i] / zoom_pixels.length;

                        }

                        int[] white = {0,0,0};
                        int[] black = {255,255,255};
                        int[] olive = {128,128,0};
                        int[] silver = {192,192,192};
                        int[] lsalmon = {255,160,122};
                        int[] red = {255,0,0};
                        int[] blue = {0,0,255};
                        int[] green = {0,255,0};
                        Map<String, int[]> ColourMap  = new HashMap<String, int[]>() {{
                            put("Black", black);
                            put("White", white);
                            put("Olive", olive);
                            put("Silver", silver);
                            put("Light Salmon", lsalmon);
                            put("Red", red);
                            put("Green", green);
                            put("Blue", blue);
                        }};

                        double[] colourDistances = new double[ColourMap.size()];
                        //int[][] colourVals = (int[][])ColourMap.keySet().toArray();

                        double minDist = Double.MAX_VALUE;
                        String cName = "INVALID";
                        int counter = 0;

                        for (Map.Entry<String, int[]> entry : ColourMap.entrySet()) {
                            String key = entry.getKey();
                            int[] value = entry.getValue();

                            double curDist = cDist(pixel_sum, value);
                            colourDistances[counter] = curDist;

                            if(curDist < minDist){
                                minDist = curDist;
                                cName = key;
                            }

                            counter = counter + 1;

                        }

                        //float mean_pixel = pixel_sum / zoom_pixels.length;
                        System.out.println(Arrays.toString(pixel_sum));

                        // Extract the rgb values
                        //int pixel = bitmap.getPixel((int)X, (int)Y);
                        //int r = Color.red(pixel);
                        //int g = Color.green(pixel);
                        //int b = Color.blue(pixel);

                        //String finalColour = String.format("#%02x%02x%02x", r, g, b); // rgb in hex format
                        //Color newColor = new Color(r,g,b);
                        //Log.i("i",getColorName(finalColour,colorNames));
                        //colorTellingText.setText(getColorName(finalColour,colorNames));
                        //sayColour(getColorName(finalColour,colorNames));
                        //String finalColour = String.format("#%02x%02x%02x", r, g, b); // rgb in hex format
                        sayColour(cName);
                        colorTellingText.setText(cName);

                        //colorTellingText.setText(finalColour);
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

        tts = new TextToSpeech(getApplicationContext(), i -> {
            if (i == TextToSpeech.SUCCESS) {
                int lang = tts.setLanguage(Locale.getDefault()); // Get the phone's locale
                // Check if the language is supported
                if (lang == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(getApplicationContext(), "Language isn't supported by TTS", Toast.LENGTH_LONG).show();
                    tts.setLanguage(Locale.US); // Set to en_US as a fallback
                }
            } else {
                Toast.makeText(getApplicationContext(), "TTS initialization failed!", Toast.LENGTH_SHORT).show();
            }
        });

        galleryButton.setOnClickListener(view -> {
            MainActivity.isItGallery = true;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });

/*
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
*/
    Bitmap bitmap = null;
        cameraButton.setOnClickListener(view -> {
            MainActivity.isItGallery = false;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });
    }

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
        else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Intent intent = getIntent();
            intent.putExtra("Image", selectedImage.toString());
            finish();
            startActivity(intent);
        }
    }

    /*
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
*/
    public double cDist(int[] x, int[] y){
        return Math.sqrt(Math.pow(y[2] - x[2],2) + Math.pow(y[1] - x[1],2) + Math.pow(y[0] - x[0],2));
    }
}

