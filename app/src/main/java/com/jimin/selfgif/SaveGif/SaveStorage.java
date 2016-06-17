package com.jimin.selfgif.SaveGif;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by jimin on 2016-03-20.
 */
public class SaveStorage {
    public SaveStorage()
    {
    }
    public ArrayList<Bitmap> bitmaps= new ArrayList<Bitmap>(); //Add your bitmaps from internal or external storage.
    ProgressDialog progressBar;
    Context thiscont;

    public void makeGif(ArrayList<Bitmap> bitmapss, Context context) {
        bitmaps = bitmapss;
        thiscont = context;
        String name = "MyGif";
        //Toast.makeText(this, "The .gifs is now saving. This will take ~3 sec per frame", Toast.LENGTH_LONG).show();

        progressBar = ProgressDialog.show(context, "Converting...", "~3 sec/frame", true, false);

        GifThread gt = new GifThread(name);
        gt.start();

        /*Toast.makeText(this, "You can access the gif in your SD Card storage, under the file Flippy. This directory is: "+ Environment.getExternalStorageDirectory().toString()
                        + "/Gifs. Or, you can see your saved .gifs in the Gallery, in the album \"Gifs\"",
                Toast.LENGTH_LONG).show();*/
    }

    private class GifThread extends Thread{
        private String name;

        public GifThread(String proj) { // ONLY WORKS AFTER SAVING
            name=proj;
        }

        @Override
        public void run(){
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/GIFMaker/");
            if(!myDir.exists())
                myDir.mkdirs();
            String fname = name;
            File file = new File(myDir, fname + ".gif");
            if (file.exists()){
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                AnimatedGifMaker gifs = new AnimatedGifMaker();
                gifs.start(out);
                gifs.setDelay(200); //프레임 당 딜레이
                gifs.setRepeat(0);
                gifs.setQuality(20); //10이 디폴트, 1이 최상, 20이  최하
                gifs.setTransparent(new Color());

                for (int i = 0; i < bitmaps.size(); i++) {
                    gifs.addFrame(bitmaps.get(i));
                }
                gifs.finish();
                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()))); //미디어 스캐닝
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressBar.dismiss();
                Toast.makeText(thiscont, "SUCCESS!", Toast.LENGTH_LONG).show();
                //initImageLoader();
                //init();
            }
        };
    }

}
