package com.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class SampleActivity extends Activity {
	
	int x=0;
	Bitmap bmp = null;
	ImageButton fireButton=null;
	Typeface scoreFont = null;
	TextView scoreText = null;
	GameView mySurfaceView = null;
	 public void onCreate(Bundle savedInstanceState) {
		 
       super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
       setContentView(R.layout.main);
       scoreFont = Typeface.createFromAsset(getAssets(),"fonts/Pokemon.ttf");
       mySurfaceView = (GameView)findViewById(R.id.gameView);
       fireButton=(ImageButton)findViewById(R.id.firebutton);
       scoreText=(TextView)findViewById(R.id.score_text);
//       bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
       fireButton.setOnClickListener(new OnClickListener() {
    	   @Override
    	   public void onClick(View v) {
    		   mySurfaceView.fire_buttonClicked();
			}
       });
       
	 }
	 
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	 
}