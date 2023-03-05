package com.example.huehunter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Magnifier;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.lang.Math;

public class Analyze extends AppCompatActivity {

    ImageView view;
    Uri outPutfileUri;
    String mCurrentPhotoPath;
    TextView colorTellingText;

    ConstraintLayout layout;

    Magnifier magnifier;
    TextToSpeech tts;
    Bitmap bitmap;
    Button galleryButton;
    Button cameraButton;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        //hiding the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        view = (ImageView) this.findViewById(R.id.imageView);
        colorTellingText = findViewById(R.id.colorTellingText);
        layout = (ConstraintLayout) findViewById(R.id.constraint_layout);


        if (MainActivity.isItGallery == false) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
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
                Toast.makeText(Analyze.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }

        }

        View view2 = findViewById(R.id.imageView);

        magnifier = new Magnifier(view2);
        magnifier.setZoom(4);

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        // Fall through.
                    case MotionEvent.ACTION_MOVE: {
                        final int[] viewPosition = new int[2];
                        v.getLocationOnScreen(viewPosition);
                        magnifier.show(event.getRawX() - viewPosition[0], event.getRawY() - viewPosition[1]);

                        bitmap = view.getDrawingCache();

                        int pixel = bitmap.getPixel((int)event.getX(),(int)event.getY());
                        int r = Color.red(pixel);
                        int g = Color.green(pixel);
                        int b = Color.blue(pixel);
                        colorTellingText.setBackgroundColor(Color.rgb(r,g,b));
                        layout.setBackgroundColor(Color.rgb(r,g,b));


                        int n = 4;
                        int center_x = (int)event.getX();
                        int center_y = (int)event.getY();
                        int bitmap_height = bitmap.getHeight();
                        int bitmap_width = bitmap.getWidth();

                        int leftBound = center_x - n;
                        int rightBound = center_x + n;
                        int upBound = center_y - n;
                        int downBound = center_y + n;

                        if(rightBound >= bitmap_width - 1){
                            int offset = rightBound - (bitmap_width - 1);
                            rightBound = rightBound - offset;
                            leftBound = leftBound - offset;
                        }

                        if(0 > leftBound){
                            int offset = 0 - leftBound;
                            rightBound = rightBound + offset;
                            leftBound = leftBound + offset;
                        }

                        if(downBound >= bitmap_height){
                            int offset = downBound - (bitmap_height - 1);
                            downBound = downBound - offset;
                            upBound = upBound - offset;
                        }

                        if(0 > upBound){
                            int offset = 0 - upBound;
                            upBound = upBound + offset;
                            downBound = downBound + offset;
                        }

                        int[] zoom_pixels = new int[(2*n + 1) * (2*n + 1)];
                        /*
                        try {
                            bitmap.getPixels(zoom_pixels, 0, 32*n, leftBound, upBound, n, n);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid Position");
                            break;
                        }
                        */
                        int pixel_counter = 0;

                        for(int q = leftBound; q <= rightBound; q++){
                            for(int a = upBound; a <= downBound; a++){
                                zoom_pixels[pixel_counter] = bitmap.getPixel(q,a);
                                pixel_counter = pixel_counter + 1;
                            }
                        }
                        //System.out.println(center_x + " " + center_y);
                        //System.out.println(bitmap_height + " " + bitmap_width + " " + upBound + " " + leftBound + " " + downBound + " " + rightBound);
                        //System.out.println(Arrays.toString(zoom_pixels));

                        //int pixel = bitmap.getPixel((int)event.getX(),(int)event.getY());
                        //int r = Color.red(pixel);
                        //int g = Color.green(pixel);
                        //int b = Color.blue(pixel)
//                        int orig_height = bitmap.getHeight();
//                        int orig_width = bitmap.getWidth();
//
//                        int height_offset = magnifier.getHeight();
//                        int width_offset = magnifier.getWidth();
//
//
//                        //int[] zoom_pixels = new int[height_offset * width_offset];
//                        int[] zoom_pixels = new int[1];
//
//                        //System.out.println(orig_height + " " + orig_width + " " + height_offset + " " + width_offset + " " + pos.y + " " + pos.x);
//
//                        try {
//                            bitmap.getPixels(zoom_pixels, 0, 1, (int)X, (int)Y, 1, 1);
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Invalid Position");
//                            break;
//                        }

                        // Convert the retrieved pixel array into a normalized one (RGB instead of packed integer).
                        double [][] zoom_pixels_normalized = new double[zoom_pixels.length][3];
                        for(int i=0; i<zoom_pixels.length; i++) {
                            //pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 24) & 0xff); // or color >>> 24
                            zoom_pixels_normalized[i][0] = (double)((zoom_pixels[i] >> 16) & 0xff);
                            zoom_pixels_normalized[i][1] = (double)((zoom_pixels[i] >> 8) & 0xff);
                            zoom_pixels_normalized[i][2] = (double)((zoom_pixels[i]) & 0xff);
                        }

                        // Run k-means and get assignment.
                        KMeans clustering = new KMeans.Builder(2, zoom_pixels_normalized)
                                .iterations(20)
                                .pp(true)
                                .epsilon(.01)
                                .useEpsilon(true)
                                .build();

                        int[] assignment = clustering.getAssignment();
                        clustering = null;

                        // Some math to determine if there are similar amounts of different colours.
                        int one_count = 0;

                        for(int p = 0; p < assignment.length; p++){
                            one_count = one_count + assignment[p];
                        }

                        int zero_count = assignment.length - one_count;

                        // Partition and sum cluster elements to get both colours.
                        int [] sum_zero = {0,0,0};
                        int [] sum_one =  {0,0,0};

                        for(int t = 0; t < assignment.length; t++){
                            if(assignment[t] == 0){
                                sum_zero[0] = sum_zero[0] + (int)zoom_pixels_normalized[t][0];
                                sum_zero[1] = sum_zero[1] + (int)zoom_pixels_normalized[t][1];
                                sum_zero[2] = sum_zero[2] + (int)zoom_pixels_normalized[t][2];
                            }
                            else{
                                sum_one[0] = sum_one[0] + (int)zoom_pixels_normalized[t][0];
                                sum_one[1] = sum_one[1] + (int)zoom_pixels_normalized[t][1];
                                sum_one[2] = sum_one[2] + (int)zoom_pixels_normalized[t][2];
                            }
                        }

                        /*
                        // Sum pixels to find the mean.
                        int[] pixel_sum = {0,0,0};
//
                        //ARGB format: Alpha, R, G, B Note: Removed alpha so it's RGB
                        for(int i=0; i<zoom_pixels.length; i++) {
                           //pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 24) & 0xff); // or color >>> 24
                            pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 16) & 0xff);
                            pixel_sum[1] = pixel_sum[1] + ((zoom_pixels[i] >> 8) & 0xff);
                            pixel_sum[2] = pixel_sum[2] + ((zoom_pixels[i]) & 0xff);
                        }
//                      */

                        for (int a = 0; a < sum_zero.length; a++) {
                            if(zero_count == 0){
                                break;
                            }
                            sum_zero[a] = sum_zero[a] / zero_count;
                        }

                        for (int a = 0; a < sum_one.length; a++) {
                            if(one_count == 0){
                                break;
                            }
                            sum_one[a] = sum_one[a] / one_count;
                        }

                        //colorTellingText.setBackgroundColor(Color.rgb(pixel_sum[0],pixel_sum[1],pixel_sum[2]));

                        double[] colourDistances0 = new double[MainActivity.colours.size()];
                        double[] colourDistances1 = new double[MainActivity.colours.size()];
                        //int[][] colourVals = (int[][])ColourMap.keySet().toArray();

                        double minDist0 = Double.MAX_VALUE;
                        double minDist1 = Double.MAX_VALUE;

                        String cName0 = "INVALID";
                        String cName1 = "INVALID";

                        int counter = 0;
                        int[] curCol0 = {0,0,0};
                        int[] curCol1 = {0,0,0};

                        for (Map.Entry<String, int[]> entry : MainActivity.colours.entrySet()) {
                            String key = entry.getKey();
                            int[] value = entry.getValue();

                            double curDist0 = cDist(sum_zero, value);
                            double curDist1 = cDist(sum_one, value);

                            colourDistances0[counter] = curDist0;
                            colourDistances1[counter] = curDist1;

                            if(curDist0 < minDist0){
                                minDist0 = curDist0;
                                cName0 = key;
                                curCol0 = value;
                            }

                            if(curDist1 < minDist1){
                                minDist1 = curDist1;
                                cName1 = key;
                                curCol1 = value;
                            }

                            counter = counter + 1;

                        }
                        //int[] temp = MainActivity.colours.get(cName);
                        //colorTellingText.setTextColor(Color.rgb(temp[0],temp[1],temp[2]));

                        String outString = "";


                        //colorTellingText.setText(cName);

                        //sayColour(cName);

                        if(Math.abs(zero_count - one_count) > 0.8 * assignment.length){
                            if(one_count > zero_count){
                                outString = cName1;
                                colorTellingText.setText(outString/* + " " + Arrays.toString(curCol1)*/);
                                colorTellingText.setBackgroundColor(Color.rgb(curCol1[0],curCol1[1],curCol1[2]));
                            }

                            else{
                                outString = cName0;
                                colorTellingText.setText(outString/* + " " + Arrays.toString(curCol0)*/);
                                colorTellingText.setBackgroundColor(Color.rgb(curCol0[0],curCol0[1],curCol0[2]));
                            }
                            // This means that there are much more of one cluster than another.
                        }

                        else{
                            if(cName0.equals(cName1)){
                                outString = cName0;
                                colorTellingText.setText(cName0 /*+ " " + Arrays.toString(curCol0)*/);
                                colorTellingText.setBackgroundColor(Color.rgb(curCol0[0],curCol0[1],curCol0[2]));
                            }

                            else {
                                outString = cName0 + " and " + cName1;
                                colorTellingText.setText(cName0/* + " " + Arrays.toString(sum_zero) + cName1 + " " + Arrays.toString(sum_one)*/);
                            }
                        }
                        //colorTellingText.setText(cName + " " + Arrays.toString(pixel_sum));
                        sayColour(outString);

                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                        // Fall through.
                    case MotionEvent.ACTION_UP: {
                        magnifier.dismiss();
                    }
                }
                return true;
            }
        });

        tts = new TextToSpeech(getApplicationContext(), i -> {
            if (i == TextToSpeech.SUCCESS) {
                int lang = tts.setLanguage(Locale.ENGLISH); // Get the phone's locale
                // Check if the language is supported
                if (lang == TextToSpeech.LANG_MISSING_DATA) {
                    Toast.makeText(getApplicationContext(), "Language isn't supported by TTS", Toast.LENGTH_LONG).show();
                    tts.setLanguage(Locale.US); // Set to en_US as a fallback
                }

                tts.setSpeechRate(0.75f);

            } else {
                Toast.makeText(getApplicationContext(), "TTS initialization failed!", Toast.LENGTH_SHORT).show();
            }


        });



/*
=======
    }

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
*/
    Bitmap bitmap = null;
    }


//    cameraButton.setOnClickListener(view -> {
//        MainActivity.isItGallery = false;
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//    });



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


    public double cDist(int[] x, int[] y){
        return Math.sqrt(Math.pow(y[2] - x[2],2) + Math.pow(y[1] - x[1],2) + Math.pow(y[0] - x[0],2));
    }

    public void goHome(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}

