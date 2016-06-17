package com.jimin.selfgif.CameraView;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jimin.selfgif.CropActivity;
import com.jimin.selfgif.Gallery.Action;
import com.jimin.selfgif.MainActivity;
import com.jimin.selfgif.R;
import com.jimin.selfgif.SelectActivity;
import com.jimin.selfgif.SelectPhotoActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraActivity extends Activity {

	ImageView image;
	Activity context;
	Preview preview;
	Camera camera;
	Button exitButton;
	ImageView fotoButton;
	LinearLayout progressLayout;
	String path = "/sdcard/KutCamera/cache/images/";
	Button btn_album;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		context=this;

		fotoButton = (ImageView) findViewById(R.id.imageView_foto);
		exitButton = (Button) findViewById(R.id.button_exit);
		image = (ImageView) findViewById(R.id.imageView_photo);
		progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

		preview = new Preview(this,
				(SurfaceView) findViewById(R.id.KutCameraFragment));
		FrameLayout frame = (FrameLayout) findViewById(R.id.preview);
		frame.addView(preview);
		preview.setKeepScreenOn(true);
		fotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					takeFocusedPicture();
				} catch (Exception e) {

				}
				exitButton.setClickable(false);
				fotoButton.setClickable(false);
				progressLayout.setVisibility(View.VISIBLE);
			}
		});

		btn_album = (Button) findViewById(R.id.btn_album);
		btn_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_LONG).show();
				//Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                //startActivityForResult(i, 200);
				Intent i = new Intent(getApplicationContext(), SelectPhotoActivity.class);
				startActivity(i);

				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// TODO Auto-generated method stub
		if(camera==null){
		camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
		camera.startPreview();
		camera.setErrorCallback(new ErrorCallback() {
			public void onError(int error, Camera mcamera) {

				camera.release();
				camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
				Log.d("Camera died", "error camera");

			}
		});
		}
		if (camera != null) {
			if (Build.VERSION.SDK_INT >= 14)
				setCameraDisplayOrientation(context, CameraInfo.CAMERA_FACING_FRONT, camera);

			//camera = camera.open(CameraInfo.CAMERA_FACING_FRONT);
			//camera.setDisplayOrientation(90);
			//camera = camera.open(CameraInfo.CAMERA_FACING_BACK);

			preview.setCamera(camera);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(camera!=null)
		{
			camera.release();
		}
	}

	private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		int result;
		if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);


	}



	Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {

			try{
			camera.takePicture(mShutterCallback, null, jpegCallback);
			}catch(Exception e){

			}

		}
	};

	Camera.ShutterCallback mShutterCallback = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
	};
	public void takeFocusedPicture() {
		camera.autoFocus(mAutoFocusCallback);

	}

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		@SuppressWarnings("deprecation")
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			Matrix m = new Matrix();
			m.setRotate(270, (float) bmp.getWidth(), (float) bmp.getHeight());
			Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false);
			OutputStream outStream = null;
			try {
				File file = new File(SelectActivity.basicsavephotoroot);
				if(!file.exists())
					file.mkdirs();
				// 현재 시간을 msec으로 구한다.
				long now = System.currentTimeMillis();

				// 현재 시간을 저장 한다.
				Date date = new Date(now);
				SimpleDateFormat CurDateFormat = new SimpleDateFormat("MM_dd_HH_mm_ss");

				String name = "Photo"+CurDateFormat.format(date);
				SelectActivity.now_take_photoname = name;
				outStream = new FileOutputStream(file+"/"+name+".jpg");
				rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.close();
			}
			catch (Exception e)
			{

			}

//			FileOutputStream outStream = null;
			Calendar c = Calendar.getInstance();
//			File videoDirectory = new File(path);
//
//			if (!videoDirectory.exists()) {
//				videoDirectory.mkdirs();
//			}
//
//			try {
//				// Write to SD Card
//				outStream = new FileOutputStream(path + c.getTime().getSeconds() + ".jpg");
//				outStream.write(data);
//				outStream.close();
//
//
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//
//			}
			
			
//			Bitmap realImage;
//			 final BitmapFactory.Options options = new BitmapFactory.Options();
//			  options.inSampleSize = 5;
//
//			    options.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
//
//			    options.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
//
//
//			realImage = BitmapFactory.decodeByteArray(data,0,data.length,options);
//			ExifInterface exif = null;
//			try {
//				exif = new ExifInterface(path + c.getTime().getSeconds() + ".jpg");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			try {
//				Log.d("EXIF value",	exif.getAttribute(ExifInterface.TAG_ORIENTATION));
//				if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("1")) {
//					realImage = rotate(realImage, 270);
//				} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
//					realImage = rotate(realImage, 90);
//				} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
//					realImage = rotate(realImage, 90);
//				} else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("0")) {
//					realImage = rotate(realImage, 90);
//				}
//			} catch (Exception e) {
//
//			}
//
//			image.setImageBitmap(realImage);

			

			fotoButton.setClickable(true);
			//camera.startPreview();
			progressLayout.setVisibility(View.GONE);
			exitButton.setClickable(true);

			SelectActivity.now_take_photoroot = SelectActivity.basicsavephotoroot+SelectActivity.now_take_photoname+".jpg";
			startActivity(new Intent(getApplicationContext(), CropActivity.class));

			finish();
		}
	};

	public static Bitmap rotate(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
	}

}
