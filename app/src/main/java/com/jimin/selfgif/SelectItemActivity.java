package com.jimin.selfgif;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class SelectItemActivity extends AppCompatActivity {

    public Integer[] mThumbIds = {R.drawable.gifimage_001,
            R.drawable.gifimage_002, R.drawable.gifimage_003,
            R.drawable.gifimage_004, R.drawable.gifimage_005,
            R.drawable.gifimage_006, R.drawable.gifimage_007,
            R.drawable.gifimage_008, R.drawable.gifimage_009,
            R.drawable.gifimage_010, R.drawable.gifimage_011,
            R.drawable.gifimage_012, R.drawable.gifimage_013,
            R.drawable.gifimage_014, R.drawable.gifimage_015,
            R.drawable.gifimage_016, R.drawable.gifimage_017,
            R.drawable.gifimage_018, R.drawable.gifimage_019,
            R.drawable.gifimage_020, R.drawable.gifimage_021,
            R.drawable.gifimage_022, R.drawable.gifimage_023,
            R.drawable.gifimage_024, R.drawable.gifimage_025,
            R.drawable.gifimage_026, R.drawable.gifimage_027,
            R.drawable.gifimage_028, R.drawable.gifimage_029,
            R.drawable.gifimage_030, R.drawable.gifimage_031,
            R.drawable.gifimage_032, R.drawable.gifimage_033,
            R.drawable.gifimage_034, R.drawable.gifimage_035,
            R.drawable.gifimage_036, R.drawable.gifimage_037,
            R.drawable.gifimage_038, R.drawable.gifimage_039,
            R.drawable.gifimage_040, R.drawable.gifimage_041,
            R.drawable.gifimage_042, R.drawable.gifimage_043,
            R.drawable.gifimage_044, R.drawable.gifimage_045,
            R.drawable.gifimage_046, R.drawable.gifimage_047,
            R.drawable.gifimage_048, R.drawable.gifimage_049,
            R.drawable.gifimage_050, R.drawable.gifimage_051,
            R.drawable.gifimage_052, R.drawable.gifimage_053,
            R.drawable.gifimage_054, R.drawable.gifimage_055,
            R.drawable.gifimage_056, R.drawable.gifimage_057,
            R.drawable.gifimage_058, R.drawable.gifimage_059,
            R.drawable.gifimage_060, R.drawable.gifimage_061,
            R.drawable.gifimage_062, R.drawable.gifimage_063,
            R.drawable.gifimage_064, R.drawable.gifimage_065,
            R.drawable.gifimage_066
    };
    /*public Integer[] mThumbIds = {R.drawable.gifimagescene_001_1,
            R.drawable.gifimagescene_002_1, R.drawable.gifimagescene_003_1,
            R.drawable.gifimagescene_004_1, R.drawable.gifimagescene_005_1,
            R.drawable.gifimagescene_006_1, R.drawable.gifimagescene_007_1,
            R.drawable.gifimagescene_008_1, R.drawable.gifimagescene_009_1,
            R.drawable.gifimagescene_010_1, R.drawable.gifimagescene_011_1,
            R.drawable.gifimagescene_012_1, R.drawable.gifimagescene_013_1,
            R.drawable.gifimagescene_014_1, R.drawable.gifimagescene_015_1,
            R.drawable.gifimagescene_016_1, R.drawable.gifimagescene_017_1,
            R.drawable.gifimagescene_018_1, R.drawable.gifimagescene_019_1,
            R.drawable.gifimagescene_020_1, R.drawable.gifimagescene_021_1,
            R.drawable.gifimagescene_022_1, R.drawable.gifimagescene_023_1,
            R.drawable.gifimagescene_024_1, R.drawable.gifimagescene_025_1,
            R.drawable.gifimagescene_026_1, R.drawable.gifimagescene_027_1,
            R.drawable.gifimagescene_028_1, R.drawable.gifimagescene_029_1,
            R.drawable.gifimagescene_030_1, R.drawable.gifimagescene_031_1,
            R.drawable.gifimagescene_032_1, R.drawable.gifimagescene_033_1,
            R.drawable.gifimagescene_034_1, R.drawable.gifimagescene_035_1,
            R.drawable.gifimagescene_036_1, R.drawable.gifimagescene_037_1,
            R.drawable.gifimagescene_038_1, R.drawable.gifimagescene_039_1,
            R.drawable.gifimagescene_040_1, R.drawable.gifimagescene_041_1,
            R.drawable.gifimagescene_042_1, R.drawable.gifimagescene_043_1,
            R.drawable.gifimagescene_044_1, R.drawable.gifimagescene_045_1,
            R.drawable.gifimagescene_046_1, R.drawable.gifimagescene_047_1,
            R.drawable.gifimagescene_048_1, R.drawable.gifimagescene_049_1,
            R.drawable.gifimagescene_050_1, R.drawable.gifimagescene_051_1,
            R.drawable.gifimagescene_052_1, R.drawable.gifimagescene_053_1,
            R.drawable.gifimagescene_054_1, R.drawable.gifimagescene_055_1,
            R.drawable.gifimagescene_056_1, R.drawable.gifimagescene_057_1,
            R.drawable.gifimagescene_058_1, R.drawable.gifimagescene_059_1,
            R.drawable.gifimagescene_060_1, R.drawable.gifimagescene_061_1,
            R.drawable.gifimagescene_062_1, R.drawable.gifimagescene_063_1,
            R.drawable.gifimagescene_064_1, R.drawable.gifimagescene_065_1,
            R.drawable.gifimagescene_066_1
    };*/

    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectitem);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            PathClass.click_gifresource = (int) arg0.getAdapter().getItem(arg2);
            PathClass.click_gifnumber = PathClass.click_gifresource - PathClass.first_resource_number + 1;
            startActivity(new Intent(getApplicationContext(), PopupGifActivity.class));
        }
    };

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return mThumbIds[position];
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
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

    }
    /*@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { // 백 버튼
            Intent i = new Intent(getApplicationContext(), CameraViewActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }*/
}
