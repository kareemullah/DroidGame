package com.sample;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
	
    private Bitmap bmp,fire1,fire2,fire3,laser,explode[];
    int thee=1,c=0,targetPath,c1=0,targetTimer=0;
    boolean bullet=false;
    int currentX,appleCount=0;
	Typeface scoreFont = null;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private BulletThread bulletThread[];
    private TargetThread targetThread[];
    private int x = 0; 
    Handler handler=new Handler();
    Matrix matrix=null;
    boolean onTouch=false,collapsed=false;
    GameView view = null;
    private Bitmap bgBitmap = null,target=null;
    private Bitmap bgBitmap1 = null;
    private int mFarX = 0;
    private int mFarX1 = 0;
    Paint paint = new Paint();
	private SensorManager sensorManager;
   
    public GameView(Context context, AttributeSet attrs) {
    	
        super(context, attrs);
        gameLoopThread = new GameLoopThread(this);
        view = this;
        bulletThread = new BulletThread[100];
        targetThread = new TargetThread[50];
        holder = getHolder();
        matrix = new Matrix();
   	    SensorEventListener listener = new SensorEventListener(){
   	        @Override
   	        public void onAccuracyChanged(Sensor sensor, int accuracy) {

   	        }
   	        @Override
   	        public void onSensorChanged(SensorEvent event) {
	   	     	float x1=event.values[0];
	   			int newY = 100+(int)(x1*60);
	   			if(newY > 2 && newY < getHeight() - bmp.getHeight())
	   			{
	   				x = newY;
	   			}
   	        }
   	    };
        scoreFont = Typeface.createFromAsset(context.getAssets(),"fonts/Pokemon.ttf");
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setTypeface(scoreFont);
        holder.addCallback(new SurfaceHolder.Callback() {

               @Override
               public void surfaceDestroyed(SurfaceHolder holder) {
                      boolean retry = true;
                      gameLoopThread.setRunning(false);
                      while (retry) {
                             try {
                                   gameLoopThread.join();
                                   retry = false;
                             } catch (InterruptedException e) {
                             }
                      }
               }

               @Override
               public void surfaceCreated(SurfaceHolder holder) {
            	   	  targetPath = getWidth();
                      gameLoopThread.setRunning(true);
                      gameLoopThread.start();
               }

               @Override
               public void surfaceChanged(SurfaceHolder holder, int format,
                             int width, int height) {
               }
        });
        explode = new Bitmap[4];
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.andro_launcher);
        fire1=BitmapFactory.decodeResource(context.getResources(), R.drawable.thee1);
        fire2=BitmapFactory.decodeResource(context.getResources(), R.drawable.thee2);
        fire3=BitmapFactory.decodeResource(context.getResources(), R.drawable.thee3);
        laser=BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_b);
        target=BitmapFactory.decodeResource(context.getResources(), R.drawable.target);
        bgBitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_a);
        explode[0]=BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_explode1);
        explode[1]=BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_explode2);
        explode[2]=BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_explode3);
        explode[3]=BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid_explode4);
        
   	 	sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
   		sensorManager.registerListener(listener,
   				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
   				SensorManager.SENSOR_DELAY_NORMAL);
   		
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
    		thee = (thee==4) ? 1 : thee+1;
    		canvas.drawColor(Color.BLACK);
          
//    		x = onTouch ? ((x>2) ? x-4:x) : ((x < getHeight() - bmp.getHeight()) ? x+4:x);
        	  
          drawBG1(canvas);
          drawBG(canvas);
          
          if(thee==1)
        	  canvas.drawBitmap(fire1, 80, x+14, null);
          if(thee==2)
        	  canvas.drawBitmap(fire2, 80, x+14, null);
          if(thee==3)
        	  canvas.drawBitmap(fire3, 80, x+14, null);
//          Log.i("gfu"," draw X :" + x);
          canvas.drawBitmap(bmp, 100, x, null);
          
          drawTarget(canvas);
          
          for(int bullet_i=0;bullet_i<c;bullet_i++)
          {
        	if(bulletThread[bullet_i]==null)
        		return;
        	else
        	{
	      	if(bulletThread[bullet_i].getRunning()) {
	        	if(bulletThread[bullet_i].getLeft() > getWidth())
	        		bulletThread[bullet_i].setRunning(false);
	        	else
		        	{

		        	bulletThread[bullet_i].setLeft(bulletThread[bullet_i].getLeft() + 5);
		        	canvas.drawBitmap(laser, bulletThread[bullet_i].getLeft(), bulletThread[bullet_i].getTop(), null);
		        	int bullet = bullet_i;
		        		for(int j=0;j<c1;j++)
		        		{
		        		    if(targetThread[j]==null)
		        	      		return;
		        	      	else
		        	      	{
			        		if( ((targetThread[j].getRunning()) && (bulletThread[bullet].getTop()>targetThread[j].getTop() && bulletThread[bullet].getTop()<targetThread[j].getTop() + 60)) && (bulletThread[bullet].getLeft()>targetThread[j].getLeft() && targetThread[j].getLeft()>100))
			        			{
			    	    			Log.i("Collapsed..","" +bulletThread[bullet].getTop()+ " > " +targetThread[j].getTop()+ " && " +bulletThread[bullet].getTop()+ " < " +(targetThread[j].getTop() + 60)+ " | && | " +bulletThread[bullet].getLeft()+ " > "  +targetThread[j].getLeft());
			        				bulletThread[bullet].setRunning(false);
			        				targetThread[j].setRunning(false);
			        				appleCount++;
			        			}
		        	      	}
		        		}
		        	}
	      		  }
        	}
          }
          
         setX(x);
 		canvas.drawText("Apple count : " + appleCount, 30, getHeight() - 30, paint);
    }
    
    public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	private void drawBG(Canvas canvas)
    {
        mFarX = mFarX - 1;
        
        int newFarX = bgBitmap.getWidth() - (-mFarX);
        
        if (newFarX <= 0) {
        	mFarX = 0;
            canvas.drawBitmap(bgBitmap, mFarX, 0, null);
        } else {
            canvas.drawBitmap(bgBitmap, mFarX, 0, null);
            canvas.drawBitmap(bgBitmap, newFarX, 0, null);
        }
    }
	
	private void drawTarget(Canvas canvas)
	{
		targetTimer++;
		if(targetTimer % 100 == 0)
		{
			Random random = new Random();
			int cTop = random.nextInt(canvas.getHeight());
			try {
			targetThread[c1] = new TargetThread(view);
			targetThread[c1].setRunning(true);
			targetThread[c1].setTop(cTop);
			targetThread[c1].setLeft(canvas.getWidth());
			c1++;
			} catch (ArrayIndexOutOfBoundsException e) {
				c1=0;
			}
		}
		
		for(int i=0;i<c1;i++)
		{
	    if(targetThread[i]==null)
      		return;
      	else
      	{
			targetThread[i].setLeft(targetThread[i].getLeft() - 4);
			if(targetThread[i].getRunning())
			{
			if(targetThread[i].getLeft()>0)
				canvas.drawBitmap(target, targetThread[i].getLeft(), targetThread[i].getTop(), null);
			}
			else
			{
				if(targetThread[i].collapseCount>=0 && targetThread[i].collapseCount<4)
				{
				canvas.drawBitmap(explode[0], targetThread[i].getLeft(), targetThread[i].getTop(), null);
				targetThread[i].collapseCount++;
				}
				else if(targetThread[i].collapseCount>=4 && targetThread[i].collapseCount<8)
				{
				canvas.drawBitmap(explode[1], targetThread[i].getLeft(), targetThread[i].getTop(), null);
				targetThread[i].collapseCount++;
				}
				if(targetThread[i].collapseCount>=8 && targetThread[i].collapseCount<12)
				{
				canvas.drawBitmap(explode[2], targetThread[i].getLeft(), targetThread[i].getTop(), null);
				targetThread[i].collapseCount++;
				}
				if(targetThread[i].collapseCount>=12 && targetThread[i].collapseCount<16)
				{
				canvas.drawBitmap(explode[3], targetThread[i].getLeft(), targetThread[i].getTop(), null);
				targetThread[i].collapseCount++;
				}
			}
      	}
		}
	}
    
    private void drawBG1(Canvas canvas)
    {
        mFarX1 = mFarX1 - 4;
        
        int newFarX1 = bgBitmap1.getWidth() - (-mFarX1);
        
        if (newFarX1 <= 0) {
        	mFarX1 = 0;
            canvas.drawBitmap(bgBitmap1, mFarX1, 0, null);
        } else {
            canvas.drawBitmap(bgBitmap1, mFarX1, 0, null);
            canvas.drawBitmap(bgBitmap1, newFarX1, 0, null);
        }
    }
    
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		int action = event.getAction();
//			if(action==MotionEvent.ACTION_DOWN)
//			{
//				onTouch = true;
//				if (x < getHeight() - bmp.getHeight()) {
//	                x-=4;
//				}
////				matrix.setRotate(-10);
////				bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//			}
//			if(action==MotionEvent.ACTION_UP)
//			{
//				onTouch=false;
//				if (x < getHeight() - bmp.getHeight()) {
//	                x+=4;
//				}
////				matrix.setRotate(10);
////				bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
//			}
//		return true;
//	}
	
	 public void fire_buttonClicked(){
		 	currentX = getX() + 15;
		 	try {
		 	bulletThread[c] = new BulletThread(view);
		 	bulletThread[c].setRunning(true);
		 	bulletThread[c].setTop(currentX);
		 	c++;
		 	} catch (ArrayIndexOutOfBoundsException e) {
				c=0;
			}
	    }
	 
//	@Override
//	public void onAccuracyChanged(Sensor sensor, int accuracy) {
//		
//	}

//	@Override
//	public void onSensorChanged(SensorEvent event) {
//		float x1=event.values[0];
//		int newY = 100+(int)(x1*10);
//		Log.i("SensorChanged.."," Y : " + newY);
////		if(newY > 2 && newY < getHeight() - bmp.getHeight())
//		{
////			x = newY;
//			x+=4;
//		}
//	}

}
