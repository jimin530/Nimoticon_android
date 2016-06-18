package com.jimin.selfgif;

import android.os.Bundle;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ViewSwitcher;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.jimin.selfgif.Gallery.CustomGallery;
import com.jimin.selfgif.Gallery.GalleryAdapter;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainActivity extends Activity {

    ProgressDialog progressBar;
    public static ArrayList<Bitmap> bitmaps; //Add your bitmaps from internal or external storage.
    Button btn_giflist;

    /*****************************************************************************************/
    //갤러리 관련
    GridView gridGallery;
    Handler handler_pic;
    GalleryAdapter adapter;

    Button btn_GalleryPickMul;

    String action;
    ViewSwitcher viewSwitcher;
    ImageLoader imageLoader;
    /*****************************************************************************************/

    public static String ClickedPath;

    ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmaps = new ArrayList<Bitmap>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.gifimage_1));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.gifimage_2));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.gifimage_3));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.gifimage_4));


        //bitmaps.add(BitmapFactory.decodeFile("/storage/external_SD/target11.jpg"));
        //bitmaps.add(BitmapFactory.decodeFile("/storage/external_SD/target12.jpg"));
        //bitmaps.add(BitmapFactory.decodeFile("/storage/external_SD/target13.jpg"));
        initImageLoader();
        init();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void init() {
        bitmaps = new ArrayList<Bitmap>();

        handler_pic = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        btn_giflist = (Button) findViewById(R.id.btn_giflist);
        btn_giflist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), GifListActivity.class));
                startActivity(new Intent(getApplicationContext(), GifListActivity.class));
            }
        });

        btn_GalleryPickMul = (Button) findViewById(R.id.btn_GalleryPickMul);
        btn_GalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
//                startActivityForResult(i, 200);
//                Intent i = new Intent(MainActivity.this, CustomGalleryActivity.class);
//                startActivity(i);
                Intent i = new Intent(MainActivity.this, CameraViewActivity.class);
                startActivity(i);
            }
        });
        gridGallery.setOnItemClickListener(mItemClickListener);
        //bitmaps.clear();
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            CustomGallery item = adapter.getItem(position);
            ClickedPath = item.sdcardPath;
            startActivity(new Intent(getApplicationContext(), CropActivity.class));
            Log.i("Clicked",item.sdcardPath);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            //ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;

                dataT.add(item);
            }

            viewSwitcher.setDisplayedChild(0);
            adapter.addAll(dataT);
        }
    }

    /*@Override //오른쪽 상당 메뉴바?
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

}
