package com.jimin.selfgif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.maker.outlinecropperlib.Interfaces.CropperCallback;
import com.maker.outlinecropperlib.OutlineCropper;
import com.maker.outlinecropperlib.Views.CropperDrawingView;

public class CropActivity extends Activity {

    private CropperDrawingView cropperDrawingView;
    private OutlineCropper outlineCropper;
    private ProgressDialog pd;

    DisplayMetrics mMetrics;
    LinearLayout ll_crop;
    ImageButton btn_cancel;
    ImageButton btn_next2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ll_crop = (LinearLayout) findViewById(R.id.ll_crop);
        GridView gridview = (GridView) findViewById(R.id.gv_shotlist);
        gridview.setAdapter(new CropActivity.ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        btn_cancel = (ImageButton) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PathClass.take_photoroot.clear();
                finish();
            }
        });

        btn_next2 = (ImageButton) findViewById(R.id.btn_next2);
        btn_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PlusActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_next2.setEnabled(false);

        pd = new ProgressDialog(this);
        pd.setMessage("처리중..");

        cropperDrawingView = (CropperDrawingView) findViewById(R.id.cropper_drawing_view);

        Log.d("확인",ll_crop.getWidth() +"   "+ll_crop.getHeight());


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
                            PathClass.crop_list.add(cropResultBitmap);
                            btn_next2.setEnabled(true);
                        }
                    }
                });
            }
        });

        ImageButton btnCrop = (ImageButton) findViewById(R.id.btn_crop);
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outlineCropper.isReadyForCrop()) {
                    outlineCropper.startCrop();
                }
            }
        });
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            cropperDrawingView.setImageCrop(BitmapFactory.decodeFile(arg0.getAdapter().getItem(arg2).toString()), ll_crop.getWidth(), ll_crop.getHeight());
        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return PathClass.take_photoroot.size();
        }

        public Object getItem(int position) {
            return PathClass.take_photoroot.get(position);
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
            Bitmap myBitmap = BitmapFactory.decodeFile(PathClass.take_photoroot.get(position));
            imageView.setImageBitmap(myBitmap);
            return imageView;
        }
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
