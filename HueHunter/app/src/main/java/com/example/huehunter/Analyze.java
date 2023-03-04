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
import java.io.InputStream;
import java.util.Arrays;
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
    TextView colorTellingText;

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

        view = (ImageView) this.findViewById(R.id.imageView);
        circle = (TextView)findViewById(R.id.invisibleCircle);
        colorTellingText = findViewById(R.id.colorTellingText);
        galleryButton = (Button) findViewById(R.id.gallery_button);
        cameraButton = (Button) findViewById(R.id.camera_button);

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
                        magnifier.show(event.getRawX() - viewPosition[0],
                                event.getRawY() - viewPosition[1]);
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

                        int[] pixel_sum = {0,0,0,0};

                        for(int i=0; i<zoom_pixels.length; i++){
                            pixel_sum[0] = pixel_sum[0] + ((zoom_pixels[i] >> 24) & 0xff); // or color >>> 24
                            pixel_sum[1] = pixel_sum[1] + ((zoom_pixels[i] >> 16) & 0xff);
                            pixel_sum[2] = pixel_sum[2] + ((zoom_pixels[i] >>  8) & 0xff);
                            pixel_sum[3] = pixel_sum[3] + ((zoom_pixels[i]      ) & 0xff);
                        }

                        for(int i=0; i<pixel_sum.length; i++){
                            pixel_sum[i] = pixel_sum[i] / zoom_pixels.length;

                        }


                        //float mean_pixel = pixel_sum / zoom_pixels.length;
                        System.out.println(Arrays.toString(pixel_sum));

                        // Extract the rgb values
                        //int pixel = bitmap.getPixel((int)X, (int)Y);
                        //int r = Color.red(pixel);
                        //int g = Color.green(pixel);
                        //int b = Color.blue(pixel);

                        //String finalColour = String.format("#%02x%02x%02x", r, g, b); // rgb in hex format

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

        galleryButton.setOnClickListener(view -> {
            MainActivity.isItGallery = true;
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });

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