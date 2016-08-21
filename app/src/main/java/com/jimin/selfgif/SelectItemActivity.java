package com.jimin.selfgif;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SelectItemActivity extends AppCompatActivity {
    ArrayList<String> items = new ArrayList<String>();

    DisplayMetrics mMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectitem);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        insert_Item();
        ListAdapter list = new ListAdapter(this, items);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(list);
        gridview.setOnItemClickListener(gridviewOnItemClickListener);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            PathClass.click_gifresource = getApplicationContext().getResources().getIdentifier(arg0.getAdapter().getItem(arg2)+"", "drawable", getApplicationContext().getPackageName());
            PathClass.click_gifnumber = PathClass.click_gifresource - PathClass.first_resource_number + 1;
            startActivity(new Intent(getApplicationContext(), PopupGifActivity.class));
        }
    };

    public class ListAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> items;
        private LruCache<String, Bitmap> mMemoryCache;

        public ListAdapter(Context context, ArrayList<String> items) {
            this.context = context;
            this.items = items;

            // Get memory class of this device, exceeding this amount will throw an
            // OutOfMemory exception.
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;

            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

                protected int sizeOf(String key, Bitmap bitmap) {
                    // The cache size will be measured in bytes rather than number
                    // of items.
                    return bitmap.getByteCount();
                }

            };
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            ImageView img = null;
            int rowWidth = (mMetrics.widthPixels) / 4;

            if (convertView == null) {
                img = new ImageView(context);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setLayoutParams(new GridView.LayoutParams(rowWidth, rowWidth));
                img.setPadding(1, 1, 1, 1);
            } else {
                img = (ImageView) convertView;
            }

            int resId = context.getResources().getIdentifier(items.get(arg0), "drawable", context.getPackageName());

            loadBitmap(resId, img);

            return img;
        }

        public void loadBitmap(int resId, ImageView imageView) {
            if (cancelPotentialWork(resId, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                imageView.setBackgroundResource(R.drawable.empty_photo);
                task.execute(resId);
            }
        }

        class AsyncDrawable extends BitmapDrawable {
            private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

            public AsyncDrawable(Resources res, Bitmap bitmap,
                                 BitmapWorkerTask bitmapWorkerTask) {
                super(res, bitmap);
                bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(
                        bitmapWorkerTask);
            }

            public BitmapWorkerTask getBitmapWorkerTask() {
                return bitmapWorkerTaskReference.get();
            }
        }

        public boolean cancelPotentialWork(int data, ImageView imageView) {
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (bitmapWorkerTask != null) {
                final int bitmapData = bitmapWorkerTask.data;
                if (bitmapData != data) {
                    // Cancel previous task
                    bitmapWorkerTask.cancel(true);
                } else {
                    // The same work is already in progress
                    return false;
                }
            }
            // No task associated with the ImageView, or an existing task was
            // cancelled
            return true;
        }

        private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
            if (imageView != null) {
                final Drawable drawable = imageView.getDrawable();
                if (drawable instanceof AsyncDrawable) {
                    final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    return asyncDrawable.getBitmapWorkerTask();
                }
            }
            return null;
        }

        public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
            if (getBitmapFromMemCache(key) == null) {
                mMemoryCache.put(key, bitmap);
            }
        }

        public Bitmap getBitmapFromMemCache(String key) {
            return (Bitmap) mMemoryCache.get(key);
        }

        class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
            public int data = 0;
            private final WeakReference<ImageView> imageViewReference;

            public BitmapWorkerTask(ImageView imageView) {
                // Use a WeakReference to ensure the ImageView can be garbage
                // collected
                imageViewReference = new WeakReference<ImageView>(imageView);
            }

            // Decode image in background.
            @Override
            protected Bitmap doInBackground(Integer... params) {
                data = params[0];
                final Bitmap bitmap = decodeSampledBitmapFromResource(
                        context.getResources(), data, 100, 100);
                addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
                return bitmap;
            }

            // Once complete, see if ImageView is still around and set bitmap.
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (imageViewReference != null && bitmap != null) {
                    final ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res,
                                                             int resId, int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        public int calculateInSampleSize(BitmapFactory.Options options,
                                                int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                // Calculate ratios of height and width to requested height and
                // width
                final int heightRatio = Math.round((float) height
                        / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                // Choose the smallest ratio as inSampleSize value, this will
                // guarantee
                // a final image with both dimensions larger than or equal to the
                // requested height and width.
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            return inSampleSize;
        }

    }

    public void insert_Item()
    {
        items.add("gifimage_001"); items.add("gifimage_002"); items.add("gifimage_003"); items.add("gifimage_004"); items.add("gifimage_005");
        items.add("gifimage_006"); items.add("gifimage_007"); items.add("gifimage_008"); items.add("gifimage_009"); items.add("gifimage_010");
        items.add("gifimage_011"); items.add("gifimage_012"); items.add("gifimage_013"); items.add("gifimage_014"); items.add("gifimage_015");
        items.add("gifimage_016"); items.add("gifimage_017"); items.add("gifimage_018"); items.add("gifimage_019"); items.add("gifimage_020");
        items.add("gifimage_021"); items.add("gifimage_022"); items.add("gifimage_023"); items.add("gifimage_024"); items.add("gifimage_025");
        items.add("gifimage_026"); items.add("gifimage_027"); items.add("gifimage_028"); items.add("gifimage_029"); items.add("gifimage_030");
        items.add("gifimage_031"); items.add("gifimage_032"); items.add("gifimage_033"); items.add("gifimage_034"); items.add("gifimage_035");
        items.add("gifimage_036"); items.add("gifimage_037"); items.add("gifimage_038"); items.add("gifimage_039"); items.add("gifimage_040");
        items.add("gifimage_041"); items.add("gifimage_042"); items.add("gifimage_043"); items.add("gifimage_044"); items.add("gifimage_045");
        items.add("gifimage_046"); items.add("gifimage_047"); items.add("gifimage_048"); items.add("gifimage_049"); items.add("gifimage_050");
        items.add("gifimage_051"); items.add("gifimage_052"); items.add("gifimage_053"); items.add("gifimage_054"); items.add("gifimage_055");
        items.add("gifimage_056"); items.add("gifimage_057"); items.add("gifimage_058"); items.add("gifimage_059"); items.add("gifimage_060");
        items.add("gifimage_061"); items.add("gifimage_062"); items.add("gifimage_063"); items.add("gifimage_064"); items.add("gifimage_065");
        items.add("gifimage_066");
        /*for (int p = 0; p <= 9; p++) {
            items.add("gifimage_00"+p);
        }
        for (int p = 10; p <= 66; p++) {
            items.add("gifimage_0"+p);
        }*/
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
