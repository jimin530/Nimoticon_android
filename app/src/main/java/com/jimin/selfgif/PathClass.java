package com.jimin.selfgif;

import android.graphics.Bitmap;
import android.os.Environment;

import java.util.ArrayList;

/**
 * Created by jimin on 2016-07-29.
 */
public class PathClass {
    public PathClass()
    {
    }
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
    public static ArrayList<Bitmap> crop_list = new ArrayList<>();

    public static boolean fromcamera = true;
}