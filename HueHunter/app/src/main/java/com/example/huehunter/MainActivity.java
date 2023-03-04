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
    static HashMap<String, int[]> ColourMap = new HashMap<>();
//    ImageView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ColourMap = new HashMap<>();
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
        colors.put("#000000", "Black");
        colors.put("#000080", "Navy Blue");
        colors.put("#0000C8", "Dark Blue");
        colors.put("#0000FF", "Blue");
        colors.put("#000741", "Stratos");
        colors.put("#001B1C", "Swamp");
        colors.put("#002387", "Resolution Blue");
        colors.put("#002900", "Deep Fir");
        colors.put("#002E20", "Burnham");
        colors.put("#002FA7", "International Klein Blue");
        colors.put("#003153", "Prussian Blue");
        colors.put("#003366", "Midnight Blue");
        colors.put("#003399", "Smalt");
        colors.put("#003532", "Deep Teal");
        colors.put("#003E40", "Cyprus");
        colors.put("#004620", "Kaitoke Green");
        colors.put("#0047AB", "Cobalt");
        colors.put("#004816", "Crusoe");
        colors.put("#004950", "Sherpa Blue");
        colors.put("#0056A7", "Endeavour");
        colors.put("#00581A", "Camarone");
        colors.put("#0066CC", "Science Blue");
        colors.put("#0066FF", "Blue Ribbon");
        colors.put("#00755E", "Tropical Rain Forest");
        colors.put("#0076A3", "Allports");
        colors.put("#007BA7", "Deep Cerulean");
        colors.put("#007EC7", "Lochmara");
        colors.put("#007FFF", "Azure Radiance");
        colors.put("#008080", "Teal");
        colors.put("#0095B6", "Bondi Blue");
        colors.put("#009DC4", "Pacific Blue");
        colors.put("#00A693", "Persian Green");
        colors.put("#00A86B", "Jade");
        colors.put("#00CC99", "Caribbean Green");
        colors.put("#00CCCC", "Robin's Egg Blue");
        colors.put("#00FF00", "Green");
        colors.put("#00FF7F", "Spring Green");
        colors.put("#00FFFF", "Cyan / Aqua");
        colors.put("#010D1A", "Blue Charcoal");
        colors.put("#011635", "Midnight");
        colors.put("#011D13", "Holly");
        colors.put("#012731", "Daintree");
        colors.put("#01361C", "Cardin Green");
        colors.put("#01371A", "County Green");
        colors.put("#013E62", "Astronaut Blue");
        colors.put("#013F6A", "Regal Blue");
        colors.put("#014B43", "Sherwood Green");
        colors.put("#015E85", "Orient");
        colors.put("#016162", "Blue Stone");
        colors.put("#016D39", "Fun Green");
        colors.put("#01796F", "Pine Green");
        colors.put("#017987", "Blue Lagoon");
        colors.put("#01826B", "Deep Sea");
        colors.put("#01A368", "Green Haze");
        colors.put("#022D15", "English Holly");
        colors.put("#02402C", "Sherpa Blue");
        colors.put("#02478E", "Congress Blue");
        colors.put("#024E46", "Evening Sea");
        colors.put("#026395", "Bahama Blue");
        colors.put("#02866F", "Observatory");
        colors.put("#02A4D3", "Cerulean");
        colors.put("#03163C", "Tangaroa");
        colors.put("#032B52", "Green Vogue");
        colors.put("#036A6E", "Mosque");
        colors.put("#041004", "Midnight Moss");
        colors.put("#041322", "Black Pearl");
        colors.put("#042E4C", "Blue Whale");
        colors.put("#044022", "Zuccini");
        colors.put("#044259", "Teal Blue");
        colors.put("#051040", "Deep Cove");
        colors.put("#051657", "Gulf Blue");
        colors.put("#055989", "Venice Blue");
        colors.put("#056F57", "Watercourse");
        colors.put("#062A78", "Catalina Blue");
        colors.put("#063537", "Tiber");
        colors.put("#069B81", "Gossamer");
        colors.put("#06A189", "Niagara");
        colors.put("#073A50", "Tarawera");
        colors.put("#080110", "Black Rose");
        colors.put("#081910", "Dark Fern");
        colors.put("#082567", "Deep Sapphire");
        colors.put("#088370", "Elf Green");
        colors.put("#08E8DE", "Bright Turquoise");
        colors.put("#092256", "Downriver");
        colors.put("#09230F", "Palm Green");
        colors.put("#093624", "Bottle Green");
        colors.put("#095859", "Deep Sea Green");
        colors.put("#097F4B", "Salem");
        colors.put("#0A001C", "Black Russian");
        colors.put("#0A480D", "Dark Fern");
        colors.put("#0A6906", "Japanese Laurel");
        colors.put("#0A6F75", "Atoll");
        colors.put("#0B0B0B", "Black");
        colors.put("#0B0F08", "Marshland");
        colors.put("#0B1107", "Gordons Green");
        colors.put("#0B1304", "Black Forest");
        colors.put("#0B6207", "San Felix");
        colors.put("#0BDA51", "Malachite");
        colors.put("#0C0B1D", "Ebony");
        colors.put("#0C0D0F", "Woodsmoke");
        colors.put("#0C1911", "Racing Green");
        colors.put("#0C7A79", "Surfie Green");
        colors.put("#0C8990", "Blue Chill");
        colors.put("#0D0332", "Black Rock");
        colors.put("#0D1117", "Bunker");
        colors.put("#0D1C19", "Aztec");
        colors.put("#0D2E1C", "Bush");
        colors.put("#0E0E18", "Cinder");
        colors.put("#0E2A30", "Firefly");
        colors.put("#0F2D9E", "Torea Bay");
        colors.put("#0F52BA", "Tory Blue");
        colors.put("#10121D", "Vulcan");
        colors.put("#101405", "Green Waterloo");
        colors.put("#105852", "Eden");
        colors.put("#110C6C", "Arapawa");
    }

    public static String getColorName(String hexCode){
        if(colors.containsKey(hexCode))
            return colors.get(hexCode);
        else
            return "null";
    }
}