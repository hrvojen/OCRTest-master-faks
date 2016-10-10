package com.example.hrca.ocrtest;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener {

    final int CAMERA_CAPTURE = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    //captured picture uri
    private Uri picUri;
    final int PIC_CROP = 2;
    private Button dugme;
    private Button proceed;
    private Bitmap thePic;
    private Button btnChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dugme=(Button)findViewById(R.id.buttonMain);
        dugme.setOnClickListener(this);
        proceed = (Button)findViewById(R.id.btnProceed);
        proceed.setOnClickListener(this);
        btnChoose=(Button)findViewById(R.id.buttonChoose);
        btnChoose.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.e("Dugme pritisnuto", "ll");
        if (view.getId() == R.id.buttonMain) {
            try {
                Log.e("Dugme pritisnuto", "ll");
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
            catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (view.getId()== R.id.btnProceed){
            Intent intentOCR=new Intent(this, OCRActivity.class);
            intentOCR.putExtra("slika", thePic);
            startActivity(intentOCR);
        }
        if (view.getId()==R.id.buttonChoose){
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                picUri = data.getData();
                performCrop();
            }

           else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.imageViewMain);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

}
                else if(requestCode == PIC_CROP){
            Log.e("Jedan","l");

            //get the returned data
            Bundle extras = data.getExtras();
            Log.e("Dva","l");
            //get the cropped bitmap
            thePic = extras.getParcelable("data");
            Log.e("Tri","l");
            //retrieve a reference to the ImageView
            ImageView picView = (ImageView)findViewById(R.id.imageViewMain);

            Log.e("uskoro cu postaviti","l");


            //display the returned cropped image
            picView.setImageBitmap(thePic);

            Log.e("slika postavljena", "lll");

                proceed.setVisibility(View.VISIBLE);
        }

        }
    }

    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
