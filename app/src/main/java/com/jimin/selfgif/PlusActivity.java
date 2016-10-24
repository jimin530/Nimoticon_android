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
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.jimin.selfgif.SaveGif.AnimatedGifMaker;
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

    Button btn_addcropped;
    Button btn_deletecropped;
    Button btn_share;
    Button btn_flip;
    Button btn_addfirst;
    Button btn_addsecond;
    Button btn_addgif;
    Button btn_send;
    Button btn_selectemoticon;
    Button btn_nimoticon;

    private TurboImageView turboImageView;
    ImageView iv_background;

    ProgressDialog progressBar;
    public ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>(); //Add your bitmaps from internal or external storage.
    DisplayMetrics mMetrics;
    Bitmap selected_cropped = PathClass.crop_list.get(0);

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

        btn_addcropped = (Button) findViewById(R.id.btn_addcropped);
        btn_addcropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.addObject(PlusActivity.this, selected_cropped);
            }
        });
        btn_deletecropped = (Button) findViewById(R.id.btn_deletecropped);
        btn_deletecropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.removeSelectedObject();
            }
        });

        btn_share = (Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/gif");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(PathClass.now_made_gifroot + ".gif"));
                startActivity(Intent.createChooser(shareIntent, "공유하기"));
            }
        });

        btn_flip = (Button) findViewById(R.id.btn_flip);
        btn_flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.toggleFlippedHorizontallySelectedObject();
            }
        });

        btn_addfirst = (Button) findViewById(R.id.btn_addfirst);
        btn_addfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_background.bringToFront();
                iv_background.setImageResource(PathClass.click_gifscene1);
            }
        });

        btn_addsecond = (Button) findViewById(R.id.btn_addsecond);
        btn_addsecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_background.bringToFront();
                iv_background.setImageResource(PathClass.click_gifscene2);
            }
        });

        btn_addgif = (Button) findViewById(R.id.btn_addgif);
        btn_addgif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turboImageView.deselectAll();
                Drawable d = getResources().getDrawable(R.drawable.gifimagescene_001_1);
                RelativeLayout view = (RelativeLayout) findViewById(R.id.mainlayout);
                //Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()/2, d.getIntrinsicHeight()/2, Bitmap.Config.ARGB_8888);
                Bitmap bitmap = Bitmap.createBitmap(iv_background.getWidth(), iv_background.getHeight(), Bitmap.Config.ARGB_8888);
                //Bitmap bitmap = Bitmap.createBitmap(640, 631, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.draw(canvas);
                bitmaps.add(bitmap);
            }
        });

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeGif();
            }
        });

        btn_selectemoticon = (Button) findViewById(R.id.btn_selectemoticon);
        btn_selectemoticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathClass.fromcamera = false;
                Intent i = new Intent(getApplicationContext(), SelectItemActivity.class);
                startActivity(i);
            }
        });

        btn_nimoticon = (Button) findViewById(R.id.btn_nimoticon);
        btn_nimoticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SelectGifActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            selected_cropped = (Bitmap) arg0.getAdapter().getItem(arg2);
        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return PathClass.crop_list.size();
        }

        public Object getItem(int position) {
            return PathClass.crop_list.get(position);
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
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }
            Bitmap myBitmap = PathClass.crop_list.get(position);

            imageView.setImageBitmap(myBitmap);
            return imageView;
        }

    }

    @Override
    public void onImageObjectSelected(MultiTouchObject multiTouchObject) {
    }

    @Override
    public void onImageObjectDropped() {
    }

    @Override
    public void onCanvasTouched() {
        turboImageView.deselectAll();
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

        String name = "Gif" + CurDateFormat.format(date);

        progressBar = ProgressDialog.show(this, "Converting...", "~3 sec/frame", true, false);

        GifThread gt = new GifThread(name);
        gt.start();
    }

    private class GifThread extends Thread {
        private String name;

        public GifThread(String proj) { // ONLY WORKS AFTER SAVING
            name = proj;
        }

        @Override
        public void run() {
            File myDir = new File(PathClass.basicroot + "SaveGif/");
            if (!myDir.exists())
                myDir.mkdirs();
            String fname = name;
            PathClass.now_made_gifname = name;
            File file = new File(myDir, fname + ".gif");
            if (file.exists()) {
                file.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(file);
                AnimatedGifMaker gifs = new AnimatedGifMaker();
                gifs.start(out);
                gifs.setDelay(500); //프레임 당 딜레이
                gifs.setRepeat(0);
                gifs.setQuality(10); //10이 디폴트, 1이 최상, 20이  최하
                //gifs.setTransparent(new Color());

                for (int i = 0; i < bitmaps.size(); i++) {
                    gifs.addFrame(bitmaps.get(i));
                }
                gifs.finish();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + myDir + "/" + fname + ".gif")));
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
                PathClass.now_made_gifroot = PathClass.basicsavegifroot + PathClass.now_made_gifname;
            }
        };
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
            PathClass.clearValue();
            Intent i = new Intent(getApplicationContext(), CameraViewActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}