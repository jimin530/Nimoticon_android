package com.jimin.selfgif.Camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jimin.selfgif.CropActivity;
import com.jimin.selfgif.R;
import com.jimin.selfgif.SelectActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraViewActivity extends Activity implements OnClickListener {

	private final String TAG = "Camera Activity";
	private int mCurrentCamera;

	private Camera mCamera;
	private CameraPreview mPreview;
	private FrameLayout mPreviewContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cameraview);

		((ImageButton) findViewById(R.id.button_capture))
				.setOnClickListener(this);
		((ImageButton) findViewById(R.id.button_switch_camera))
				.setOnClickListener(this);

		mPreviewContainer = (FrameLayout) findViewById(R.id.camera_preview);

		if (!checkCameraHardware(this)) {
			showToast("This device has no camera");
			return;
		}

		mCurrentCamera = CameraInfo.CAMERA_FACING_BACK;

		// Create an instance of Camera
		mCamera = getCameraInstance(mCurrentCamera);

		if (mCamera == null)
			return;

		initPreview();

	}

	private void switchCamera() {

		if (mCamera != null) {
			mCamera.stopPreview(); // stop preview
			mCamera.release(); // release previous camera
		}

		if (mCurrentCamera == CameraInfo.CAMERA_FACING_BACK) {
			mCurrentCamera = CameraInfo.CAMERA_FACING_FRONT;
		} else {
			mCurrentCamera = CameraInfo.CAMERA_FACING_BACK;
		}

		// Create an instance of Camera
		mCamera = getCameraInstance(mCurrentCamera);

		if (mCamera == null)
			return;

		initPreview();
		// mCamera.startPreview();
	}

	private void initPreview() {
		// Create Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		mPreviewContainer.removeAllViews();

		setCameraDisplayOrientation(this, CameraInfo.CAMERA_FACING_FRONT, mCamera);

		mPreviewContainer.addView(mPreview);
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

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/** Check if device has camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private Camera getCameraInstance(int type) {
		Camera c = null;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == type) {
				try {
					c = Camera.open(i); // attempt to get a Camera instance
				} catch (Exception e) {
					// Camera is not available
					showToast("Camera not available.");
				}
				break;
			}
		}
		return c;
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			mPreview.initPreview();

			/*File pictureFile = Utilities.getOutputMediaFile();
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions.");
				showToast("Error creating media file.");
				return;
			}*/
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			Matrix m = new Matrix();
			m.setRotate(270, (float) bmp.getWidth(), (float) bmp.getHeight());
			Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, false);
			OutputStream outStream = null;

			try {
				/*FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();

				Intent mediaScanIntent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri contentUri = Uri.fromFile(pictureFile);
				mediaScanIntent.setData(contentUri);
				CameraViewActivity.this.sendBroadcast(mediaScanIntent);*/
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

			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			SelectActivity.now_take_photoroot = SelectActivity.basicsavephotoroot+SelectActivity.now_take_photoname+".jpg";
			startActivity(new Intent(getApplicationContext(), CropActivity.class));

			finish();
		}
	};

	ShutterCallback mShutterCallback = new ShutterCallback() {
		public void onShutter() {
			// do stuff like playing shutter sound here
		}
	};

	private void captureImage() {
		mCamera.takePicture(mShutterCallback, null, mPicture);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.button_switch_camera) {
			switchCamera();
		} else if (id == R.id.button_capture) {
			captureImage();
		}

	}

}
