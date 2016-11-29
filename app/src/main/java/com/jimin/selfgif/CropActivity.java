package com.jimin.selfgif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jimin.selfgif.Camera.CameraViewActivity;
import com.jimin.selfgif.Tutorials.Tutorial2Activity;
import com.maker.outlinecropperlib.Interfaces.CropperCallback;
import com.maker.outlinecropperlib.OutlineCropper;
import com.maker.outlinecropperlib.Views.CropperDrawingView;

public class CropActivity extends Activity {

    private CropperDrawingView cropperDrawingView;
    private OutlineCropper outlineCropper;
    private ProgressDialog pd;

    DisplayMetrics mMetrics;

    LinearLayout left_ll_crop;
    LinearLayout ll_crop;
    LinearLayout right_ll_crop;
    LinearLayout top_ll_crop;
    LinearLayout bottom_ll_crop;
    LinearLayout center_ll_crop;

    LinearLayout.LayoutParams left_params;
    LinearLayout.LayoutParams ll_crop_params;
    LinearLayout.LayoutParams right_params;
    LinearLayout.LayoutParams top_params;
    LinearLayout.LayoutParams center_params;
    LinearLayout.LayoutParams bottom_params;

    float first_window_width = 0;
    float first_window_height = 0;

    float window_width = 0;
    float window_height = 0;
    float rate_window_width = 0;
    float rate_window_height = 0;

    AdapterView<?> tmp_arg0;
    int tmp_arg2;

    ImageButton btn_cancel;
    ImageButton btn_next2;
    public static ImageButton btnCrop;
    ImageButton btn_inform2;

    View tmp_view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        left_ll_crop = (LinearLayout) findViewById(R.id.left_ll_crop);
        ll_crop = (LinearLayout) findViewById(R.id.ll_crop);
        right_ll_crop = (LinearLayout) findViewById(R.id.right_ll_crop);
        top_ll_crop = (LinearLayout) findViewById(R.id.top_ll_crop);
        bottom_ll_crop = (LinearLayout) findViewById(R.id.bottom_ll_crop);
        center_ll_crop = (LinearLayout) findViewById(R.id.center_ll_crop);

        GridView gridview = (GridView) findViewById(R.id.gv_shotlist);
        gridview.setAdapter(new CropActivity.ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        left_params = (LinearLayout.LayoutParams) left_ll_crop.getLayoutParams();
        ll_crop_params = (LinearLayout.LayoutParams) ll_crop.getLayoutParams();
        right_params = (LinearLayout.LayoutParams) right_ll_crop.getLayoutParams();
        top_params = (LinearLayout.LayoutParams) top_ll_crop.getLayoutParams();
        center_params = (LinearLayout.LayoutParams) center_ll_crop.getLayoutParams();
        bottom_params = (LinearLayout.LayoutParams) bottom_ll_crop.getLayoutParams();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                first_window_width = ll_crop.getWidth();
                first_window_height = ll_crop.getHeight();
                Log.d("ll_crop 크기 확인", first_window_width + "/" + first_window_height);
            }
        }, 100);

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

        btn_inform2 = (ImageButton) findViewById(R.id.btn_inform2);
        btn_inform2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tutorial2Activity.class));
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Cutting..");

        cropperDrawingView = (CropperDrawingView) findViewById(R.id.cropper_drawing_view);

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
                            Bitmap croppedBitmap = adjustOpacity(cropResultBitmap, 255);
                            PathClass.crop_list.add(croppedBitmap);
                            btn_next2.setEnabled(true);
                        }
                    }
                });
            }
        });

        btnCrop = (ImageButton) findViewById(R.id.btn_crop);
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
            btnCrop.setBackgroundResource(R.drawable.btn_crop);
            if (tmp_view == null) {
                tmp_view = arg1;
            } else {
                tmp_view.setBackgroundResource(R.drawable.image_basic_border);
                tmp_view = arg1;
            }
            arg1.setBackgroundResource(R.drawable.image_border);
            cropperDrawingView.setImageCrop(((BitmapDrawable) getResources().getDrawable(R.drawable.img_transparent)).getBitmap(), ll_crop.getWidth(), ll_crop.getHeight());
            Bitmap selected_image = BitmapFactory.decodeFile(arg0.getAdapter().getItem(arg2).toString());

            setSizeCropView(selected_image.getWidth(), selected_image.getHeight());
            Log.d("이미지 크기 확인", selected_image.getWidth() + "/" + selected_image.getHeight());
            tmp_arg0 = arg0;
            tmp_arg2 = arg2;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("ll_crop 크기 확인", ll_crop.getWidth() + "/" + ll_crop.getHeight());
                    cropperDrawingView.setImageCrop(BitmapFactory.decodeFile(tmp_arg0.getAdapter().getItem(tmp_arg2).toString()), ll_crop.getWidth(), ll_crop.getHeight());
                }
            }, 100);
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

            int rowWidth = (mMetrics.widthPixels) / 6;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setPadding(1, 1, 1, 1);
                imageView.setBackgroundColor(Color.rgb(255, 255, 255));
                imageView.setBackgroundResource(R.drawable.image_basic_border);
            } else {
                imageView = (ImageView) convertView;
            }
            Bitmap myBitmap = BitmapFactory.decodeFile(PathClass.take_photoroot.get(position));
            imageView.setImageBitmap(myBitmap);
            return imageView;
        }
    }

    private Bitmap adjustOpacity(Bitmap bitmap, int opacity) {
        Bitmap mutableBitmap = bitmap.isMutable()
                ? bitmap
                : bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        int colour = (opacity & 0xFF) << 24;
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        return mutableBitmap;
    }

    public void setSizeCropView(float image_width, float image_height) {
        window_width = first_window_width;
        window_height = first_window_height;
        Log.d("화면 크기 확인", window_width + "/" + window_height);

        float rate_image_width = 0;
        float rate_image_height = 0;

        float rate_diff = 0;

        if (image_width >= image_height) { //가로가 길거나 같은 사진
            rate_window_width = 1;
            rate_window_height = window_height / window_width;

            rate_image_width = 1;
            rate_image_height = image_height / image_width;

            rate_diff = rate_image_height / rate_window_height;

            top_params.weight = 1000 * (1 - rate_diff) / 2f;
            center_params.weight = 1000 - (1000 * (1 - rate_diff)) + 0f;
            bottom_params.weight = 1000 * (1 - rate_diff) / 2f;

            left_params.weight = 0f;
            ll_crop_params.weight = 1000f;
            right_params.weight = 0f;
        } else //세로가 긴 사진
        {
            rate_window_width = 1;
            rate_window_height = window_height / window_width;

            rate_image_width = 1;
            rate_image_height = image_height / image_width;

            if (rate_window_height > rate_image_height) { //화면 높이 비율이 더 높은 경우
                rate_diff = rate_image_height / rate_window_height;

                top_params.weight = 1000 * (1 - rate_diff) / 2f;
                center_params.weight = 1000 - (1000 * (1 - rate_diff)) + 0f;
                bottom_params.weight = 1000 * (1 - rate_diff) / 2f;

                left_params.weight = 0f;
                ll_crop_params.weight = 1000f;
                right_params.weight = 0f;
            } else { //화면 높이 비율이 더 작은 경우
                rate_diff = rate_window_height / rate_image_height;

                top_params.weight = 0f;
                center_params.weight = 1000f;
                bottom_params.weight = 0f;

                left_params.weight = 1000 * (1 - rate_diff) / 2f;
                ll_crop_params.weight = 1000 - (1000 * (1 - rate_diff)) + 0f;
                right_params.weight = 1000 * (1 - rate_diff) / 2f;
            }
        }

        top_ll_crop.setLayoutParams(top_params);
        center_ll_crop.setLayoutParams(center_params);
        bottom_ll_crop.setLayoutParams(bottom_params);

        left_ll_crop.setLayoutParams(left_params);
        ll_crop.setLayoutParams(ll_crop_params);
        right_ll_crop.setLayoutParams(right_params);
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
