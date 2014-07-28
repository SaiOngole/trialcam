package com.is3av.trialcam;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.touchpad.GestureDetector.BaseListener;


public class AndroidVideoCapture extends Activity implements BaseListener{
	private GestureDetector mGestureDetector;
	private Camera myCamera;
	private MyCameraSurfaceView myCameraSurfaceView;
	private MediaRecorder mediaRecorder;
	private String videoPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + 
			File.separator + "newvideo.mp4";

	
	SurfaceHolder surfaceHolder;
	boolean recording;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		recording = false;

		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setBaseListener(this);


		//Get Camera for preview
		myCamera = getCameraInstance();
		if(myCamera == null){
			Toast.makeText(AndroidVideoCapture.this,
					"Fail to get Camera",
					Toast.LENGTH_LONG).show();
		}

		myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
		FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
		myCameraPreview.addView(myCameraSurfaceView);

	}



	private Camera getCameraInstance(){
		// TODO Auto-generated method stub
		Camera c = null;
		
		try {
			c = Camera.open(); // attempt to get a Camera instance
			this.setCameraParameters(c);
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}
	public void setCameraParameters(Camera camera)
	{
		if (camera != null)
		{
			Parameters parameters = camera.getParameters();
			parameters.setPreviewFpsRange(30000, 30000);
			camera.setParameters(parameters);	
		}
	}
	private boolean prepareMediaRecorder(){
		myCamera = getCameraInstance();
		mediaRecorder = new MediaRecorder();

		myCamera.unlock();
		mediaRecorder.setCamera(myCamera);

		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		mediaRecorder.setOutputFile(videoPath);
		Log.d("VideoPath", videoPath);
		mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
		mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
		
		mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			releaseMediaRecorder();
			return false;
		} catch (IOException e) {
			releaseMediaRecorder();
			return false;
		}
		return true;

	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaRecorder();       // if you are using MediaRecorder, release it first
		releaseCamera();              // release the camera immediately on pause event
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(myCamera==null) 
			myCamera = getCameraInstance();
	}

	private void releaseMediaRecorder(){
		if (mediaRecorder != null) {
			mediaRecorder.reset();   // clear recorder configuration
			mediaRecorder.release(); // release the recorder object
			mediaRecorder = null;
			myCamera.lock();           // lock camera for later use
		}
	}

	private void releaseCamera(){
		if (myCamera != null){
			myCamera.release();        // release the camera for other applications
			myCamera = null;
		}
	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if(mGestureDetector!=null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}
	public synchronized boolean onGesture(Gesture g) {
		switch(g) {
		case TAP:
			releaseCamera();
			if(!prepareMediaRecorder()){
		         Toast.makeText(AndroidVideoCapture.this,
		           "Fail in prepareMediaRecorder()!\n - Ended -",
		           Toast.LENGTH_LONG).show();
		         finish();
		        }
			System.out.println("Trying to start recorder");
			mediaRecorder.start();
			
			System.out.println("Recorder started");
			recording = true;
			break;
		case SWIPE_RIGHT:
			if(recording){
				// stop recording and release camera
				mediaRecorder.stop();  // stop the recording
				releaseMediaRecorder(); // release the MediaRecorder object

				//Exit after saved
				finish();
			}
			break;
		default:
			break;

		}
		return true;

	}

}