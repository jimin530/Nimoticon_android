package com.jimin.selfgif;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.maker.outlinecropperlib.Interfaces.CropperCallback;
import com.maker.outlinecropperlib.OutlineCropper;
import com.maker.outlinecropperlib.Views.CropperDrawingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CropActivity extends Activity {

    private CropperDrawingView cropperDrawingView;
    private OutlineCropper outlineCropper;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        pd = new ProgressDialog(this);
        pd.setMessage("Cropping...");

        cropperDrawingView = (CropperDrawingView) findViewById(R.id.cropper_drawing_view);

        //cropperDrawingView.setImageCrop(BitmapFactory.decodeResource(getResources(), R.drawable.test_s));
        cropperDrawingView.setImageCrop(BitmapFactory.decodeFile(SelectActivity.now_take_photoroot));

        outlineCropper = new OutlineCropper(cropperDrawingView, new CropperCallback() {
            @Override
            public void onCropStart() {
                pd.show();
            }

            @Override
            public void onCropEnd(final Bitmap cropResultBitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        if (cropResultBitmap != null) {
                            SelectActivity.cropimage = cropResultBitmap;

                            startActivity(new Intent(getApplicationContext(), PlusActivity.class));

                            finish();

                            //AlertDialog.Builder builder = new AlertDialog.Builder(CropActivity.this);

                            //ImageView iv = new ImageView(CropActivity.this);
                            //iv.setImageBitmap(cropResultBitmap);

                            //builder.setView(iv);
                            //builder.create().show();

                            //String root = Environment.getExternalStorageDirectory().toString();

                            //SaveBitmapToFileCache(cropResultBitmap, root + "/GIFMaker/", "pleasett.jpg");
                        }
                    }
                });
            }
        });

        Button btnCrop = (Button) findViewById(R.id.btn_crop);
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outlineCropper.isReadyForCrop()) {
                    outlineCropper.startCrop();
                }
            }
        });

    }

}
