package com.jimin.selfgif;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.jimin.selfgif.SaveGif.AnimatedGifImageView;
import com.jimin.selfgif.SaveGif.AnimatedGifImageView.TYPE;

import java.util.ArrayList;
import java.util.List;


public class SelectActivity extends Activity {
    public static int count_gif=4;

    public static int click_gifresource;
    public static int click_gifscene1;
    public static int click_gifscene2;
    public static int click_gifnumber;
    public static int first_resource_number = R.drawable.gifimage_1;
    public static int first_scene_number = R.drawable.gifimagescene_1_1;

    public static String click_makegifroot;

    public static String basicroot = Environment.getExternalStorageDirectory().toString() + "/Nimoticon/";

    public static String basicsavegifroot = Environment.getExternalStorageDirectory().toString() + "/Nimoticon/SaveGif/";
    public static String basicsavephotoroot = Environment.getExternalStorageDirectory().toString() + "/Nimoticon/SavePhoto/";

    public static String now_made_gifname;
    public static String now_made_gifroot;
    public static String now_take_photoname;
    public static String now_take_photoroot;
    public static ArrayList<String> take_photoroot = new ArrayList<>();

    public static Bitmap cropimage;
    public static ArrayList<Bitmap> crop_list = new ArrayList<>();

    public static boolean fromcamera = true;

    Button btn_makepage;
    Button btn_allnimoticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select);


        btn_makepage = (Button) findViewById(R.id.btn_makepage);
        btn_makepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectActivity.this, SelectItemActivity.class);
                startActivity(i);
            }
        });

        btn_allnimoticon = (Button) findViewById(R.id.btn_allnimoticon);
        btn_allnimoticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectActivity.this, SelectGifActivity.class);
                startActivity(i);
            }
        });
    }
}
