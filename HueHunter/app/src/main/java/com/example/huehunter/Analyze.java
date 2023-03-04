package com.example.huehunter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
<<<<<<< Updated upstream
import androidx.core.content.FileProvider;

=======
>>>>>>> Stashed changes
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);


        view = (ImageView) this.findViewById(R.id.imageView);

<<<<<<< Updated upstream
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "MyPhoto.jpg");
        outPutfileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", createImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, 1);
=======
        if(MainActivity.isItGallery==false) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        }

>>>>>>> Stashed changes
    }
    Bitmap bitmap = null;

    private File createImageFile()  {
        // Create an image file name
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "Camera");
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
            return null;
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
    }}



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