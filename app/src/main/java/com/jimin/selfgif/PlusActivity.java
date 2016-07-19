package com.jimin.selfgif;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jimin.selfgif.SaveGif.AnimatedGifMaker;
import com.jimin.selfgif.SaveGif.SaveStorage;
import com.maker.outlinecropperlib.OutlineCropper;
import com.maker.outlinecropperlib.Views.CropperDrawingView;
import com.munon.turboimageview.MultiTouchObject;
import com.munon.turboimageview.TurboImageView;
import com.munon.turboimageview.TurboImageViewListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PlusActivity extends Activity implements TurboImageViewListener {
    private static final String TAG = "SampleActivity";

    private TurboImageView turboImageView;
    ImageView iv_background;
    String[] allPath = new String[2];

    ProgressDialog progressBar;
    public ArrayList<Bitmap> bitmaps= new ArrayList<Bitmap>(); //Add your bitmaps from internal or external storage.
    SaveStorage savestorage = new SaveStorage();

    //String basicroot = Environment.getExternalStorageDirectory().toString() + "/GIFMaker/MyGif2.gif";

    private CropperDrawingView cropperDrawingView;
    private OutlineCropper outlineCropper;
    private ProgressDialog pd;

    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);

        GridView gridview = (GridView) findViewById(R.id.gv_croplist);
        gridview.setAdapter(new PlusActivity.ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        turboImageView = (TurboImageView) findViewById(R.id.turboImageView);
        turboImageView.setListener(this);

        iv_background = (ImageView) findViewById(R.id.iv_background);

        turboImageView.addObject(PlusActivity.this, SelectActivity.crop_list.get(0));
        //turboImageView.addSetObject(PlusActivity.this, SelectActivity.cropimage, (float)547.47626, (float)634.906);
        //iv_background.setImageDrawable(getResources().getDrawable(R.drawable.gif_one));


//        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //turboImageView.addObject(PlusActivity.this, getBitmapFromAsset(PlusActivity.this, "bitmaps/bitmap.png"));
//                turboImageView.addObject(PlusActivity.this, CropActivity.cropimage);
//            }
//        });
//
//        findViewById(R.id.removeButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean deleted = turboImageView.removeSelectedObject();
//                Log.d(TAG, "deleted: " + deleted);
//            }
//        });
//
        findViewById(R.id.removeAllButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //turboImageView.removeAllObjects();

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(SelectActivity.now_made_gifroot+".gif"));
                startActivity(Intent.createChooser(shareIntent, "공유하기"));
            }
        });
//
//        findViewById(R.id.deselectButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                turboImageView.deselectAll();
//            }
//        });

        findViewById(R.id.flipButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.toggleFlippedHorizontallySelectedObject();
            }
        });

        findViewById(R.id.btn_addfirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iv_background.setImageDrawable(getResources().getDrawable(R.drawable.gif_one));
                //iv_background.setImageDrawable(getResources().getDrawable(R.drawable.image_4_1));
                iv_background.setImageResource(SelectActivity.click_gifscene1);
            }
        });
        findViewById(R.id.btn_addsecond).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iv_background.setImageDrawable(getResources().getDrawable(R.drawable.gif_two));
                //iv_background.setImageDrawable(getResources().getDrawable(R.drawable.image_4_2));
                iv_background.setImageResource(SelectActivity.click_gifscene2);

            }
        });
        findViewById(R.id.btn_addgif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iv_background.bringToFront();//맨앞으로오기@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                /*RelativeLayout view = (RelativeLayout)findViewById(R.id.mainlayout);
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bm = view.getDrawingCache();
                bitmaps.add(bm);*/
                Drawable d = getResources().getDrawable(R.drawable.gifimage_1);
                RelativeLayout view = (RelativeLayout)findViewById(R.id.mainlayout);
                //Log.i("확인",d.getIntrinsicWidth()+"@@@@@@"+d.getIntrinsicHeight());
                //Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                //Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
                Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()-120, d.getIntrinsicHeight()-120, Bitmap.Config.ARGB_8888);
                //Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
                bitmaps.add(bitmap);
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*for (int i = 0; i < 2; i++) {
                    allPath[i] = selected.get(i).sdcardPath;
                }
                Intent data = new Intent().putExtra("all_path", allPath);
                setResult(RESULT_OK, data);
                finish();*/
                makeGif();
                //savestorage.makeGif(bitmaps, getBaseContext());
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        findViewById(R.id.btn_selectemoticon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectActivity.fromcamera = false;
                Intent i = new Intent(getApplicationContext(), SelectItemActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.btn_nimoticon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SelectGifActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return SelectActivity.crop_list.size();
        }

        public Object getItem(int position) {
            return SelectActivity.crop_list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            int rowWidth = (mMetrics.widthPixels) / 4;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth,rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }
            Bitmap myBitmap = SelectActivity.crop_list.get(position);
            imageView.setImageBitmap(myBitmap);
            return imageView;
        }

    }

    @Override
    public void onImageObjectSelected(MultiTouchObject multiTouchObject) {
        Log.d(TAG, "image object selected");
    }

    @Override
    public void onImageObjectDropped() {
        Log.d(TAG, "image object dropped");
    }

    @Override
    public void onCanvasTouched() {
        turboImageView.deselectAll();
        Log.d(TAG, "canvas touched");
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream;
        Bitmap bitmap = null;
        try {
            inputStream = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException ignored) {
        }

        return bitmap;
    }

    public void makeGif() {

        // 현재 시간을 msec으로 구한다.
        long now = System.currentTimeMillis();

        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("MM_dd_HH_mm_ss");

        String name = "Gif"+CurDateFormat.format(date);
        //Toast.makeText(this, "The .gifs is now saving. This will take ~3 sec per frame", Toast.LENGTH_LONG).show();

        progressBar = ProgressDialog.show(this, "Converting...", "~3 sec/frame", true, false);

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
            File myDir = new File(SelectActivity.basicroot + "SaveGif/");
            if(!myDir.exists())
                myDir.mkdirs();
            String fname = name;
            SelectActivity.now_made_gifname = name;
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
                Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_LONG).show();
                SelectActivity.now_made_gifroot = SelectActivity.basicsavegifroot+SelectActivity.now_made_gifname;
            }
        };
    }

}
