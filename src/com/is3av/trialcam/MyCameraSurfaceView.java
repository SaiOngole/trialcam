package com.is3av.trialcam;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

private SurfaceHolder mHolder;
   private Camera mCamera;
   private Paint paint = new Paint();

public MyCameraSurfaceView(Context context, Camera camera) {
       super(context);
       mCamera = camera;
       this.setWillNotDraw(false);
       // Install a SurfaceHolder.Callback so we get notified when the
       // underlying surface is created and destroyed.
       mHolder = getHolder();
       mHolder.addCallback(this);
       // deprecated setting, but required on Android versions prior to 3.0
       mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
   }

@Override
public void surfaceChanged(SurfaceHolder holder, int format, int weight,
  int height) {
       // If your preview can change or rotate, take care of those events here.
       // Make sure to stop the preview before resizing or reformatting it.
System.out.println("In surface changed");
       if (mHolder.getSurface() == null){
         // preview surface does not exist
         return;
       }

       // stop preview before making changes
       try {
           mCamera.stopPreview();
       } catch (Exception e){
         // ignore: tried to stop a non-existent preview
       }

       // make any resize, rotate or reformatting changes here

       // start preview with new settings
       try {
           mCamera.setPreviewDisplay(mHolder);
           mCamera.startPreview();

       } catch (Exception e){
       }
}

@Override
public void surfaceCreated(SurfaceHolder holder) {
 // TODO Auto-generated method stub
 // The Surface has been created, now tell the camera where to draw the preview.
	System.out.println("Surface created");
       try {
           mCamera.setPreviewDisplay(holder);
           mCamera.startPreview();
       } catch (IOException e) {
       }
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
 // TODO Auto-generated method stub
	System.out.println("Surface Destroyed");

}
@Override
protected void onDraw(Canvas canvas) {
	DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics(); 
	int screenWidth = metrics.widthPixels ;
	int screenHeight = metrics.heightPixels;
	paint.setAntiAlias(true);
	paint.setStrokeWidth(3);  
	paint.setStyle(Paint.Style.STROKE);
	paint.setColor(Color.RED);
	paint.setTextSize(36);
	int thirdline = (int) (screenHeight/1.3); //calculation to draw the third horizontal line

	//horizontal lines
	canvas.drawLine(0, screenHeight/4, screenWidth,screenHeight/4, paint);
	canvas.drawLine(0,(screenHeight)/2,screenWidth,(screenHeight)/2, paint);
	canvas.drawLine(0, thirdline, screenWidth, thirdline, paint);
	//canvas.drawLine(0,(screenHeight*2)/3,screenWidth,(screenHeight*2)/3, paint);
	//canvas.drawLine(startX, startY, stopX, stopY, paint);
	//vertical lines
	canvas.drawLine(screenWidth/3, 0, screenWidth/3, screenHeight, paint);
	canvas.drawLine((screenWidth*2)/3, 0, (screenWidth*2)/3, screenHeight, paint);

	//Calculation of coordinates to place numbers
	//x -> width
	int x1 = screenWidth/6;
	int x2 = screenWidth/2;
	int x3 = (int)(screenWidth/1.2);
	//y-> height
	int y1 = screenHeight/8;
	int y2 = (int)(screenHeight/2.6);
	int y3 = (int)(screenHeight/1.6);
	int y4 = (int)((screenHeight/1.1)-30);
	//set text in the corresponding grid box
//	canvas.drawText(numbers.get(0).toString(), x1, y1, paint);
//	canvas.drawText(numbers.get(1).toString(), x2,y1, paint);
//	canvas.drawText(numbers.get(2).toString(), x3,y1, paint);
//	canvas.drawText(numbers.get(3).toString(), x1,y2, paint);
//	canvas.drawText(numbers.get(4).toString(), x2,y2, paint);
//	canvas.drawText(numbers.get(5).toString(), x3,y2, paint);
//	canvas.drawText(numbers.get(6).toString(), x1,y3, paint);
//	canvas.drawText(numbers.get(7).toString(), x2,y3, paint);
//	canvas.drawText(numbers.get(8).toString(), x3,y3, paint);
//	canvas.drawText(numbers.get(9).toString(), x2,y4+20, paint); 

} 
}
