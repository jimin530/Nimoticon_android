package com.jimin.selfgif;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectItemActivity extends AppCompatActivity {


    // references to our images
    //public Integer[] mThumbIds = new Integer[4];

    public Integer[] mThumbIds = { R.drawable.gifimage_1,
            R.drawable.gifimage_2, R.drawable.gifimage_3,
            R.drawable.gifimage_4
    };

    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectitem);

        /*for(int i=0;i<SelectActivity.count_gif;i++)
        {
            mThumbIds[i] = R.drawable.gifimage_1;
        }*/

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }
    private GridView.OnItemClickListener gridviewOnItemClickListener
            = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            //Toast.makeText(SelectItemActivity.this, arg0.getAdapter().getItem(arg2).toString(), Toast.LENGTH_LONG).show();
            SelectActivity.click_gifresource = (int)arg0.getAdapter().getItem(arg2);
            SelectActivity.click_gifnumber = SelectActivity.click_gifresource - SelectActivity.first_resource_number+1;
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
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth,rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(1, 1, 1, 1);
            } else {
                imageView = (ImageView) convertView;
            }
            //imageView.setImageDrawable(mThumbIds[position]);
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }
    }
}
