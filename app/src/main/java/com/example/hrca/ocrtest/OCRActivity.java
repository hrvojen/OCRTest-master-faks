package com.example.hrca.ocrtest;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;

public class OCRActivity extends AppCompatActivity {

    private ImageView imageResult;
    private SparseArray<TextBlock> textBlocks;
    private ArrayList<String> namirnice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intentToList = new Intent(view.getContext(), RecyclerViewExample.class);

                intentToList.putExtra("namirnice", namirnice);
                startActivity(intentToList);
            }
        });

        Intent intent = getIntent();
        Bitmap previousImage = (Bitmap) intent.getParcelableExtra("slika");
        imageResult = (ImageView) findViewById(R.id.imageView);
        imageResult.setImageBitmap(previousImage);

        // imageBitmap is the Bitmap image you're trying to process for text
        if (previousImage != null) {

            TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();

            if (!textRecognizer.isOperational()) {
                // Note: The first time that an app using a Vision API is installed on a
                // device, GMS will download a native libraries to the device in order to do detection.
                // Usually this completes before the app is run for the first time.  But if that
                // download has not yet completed, then the above call will not detect any text,
                // barcodes, or faces.
                // isOperational() can be used to check if the required native libraries are currently
                // available.  The detectors will automatically become operational once the library
                // downloads complete on device.
                Log.w("LOG_TAG", "Detector dependencies are not yet available.");

                // Check for low storage.  If there is low storage, the native library will not be
                // downloaded, so detection will not become operational.
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                if (hasLowStorage) {
                    Toast.makeText(this, "Low Storage", Toast.LENGTH_LONG).show();
                    Log.w("LOG_TAG", "Low Storage");
                }
            }


            Frame imageFrame = new Frame.Builder()
                    .setBitmap(previousImage)
                    .build();

            textBlocks = textRecognizer.detect(imageFrame);
            namirnice=new ArrayList<String>();
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

                     Log.i("LOG_TAG", textBlock.getValue());
                     System.out.print(textBlock.getValue());
//                if ((textBlock.getValue()).equals(null)==true)
//                    continue;
//                else {
                namirnice.add(textBlock.getValue());
//                }
            }

            Log.i("OUTSIDE", "OF the loop");
            for (String namirnica :namirnice ) {

                System.out.println(namirnica);
            }

        }
    }

}
