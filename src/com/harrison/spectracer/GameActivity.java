package com.harrison.spectracer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameActivity extends Activity {
	private MyGLSurfaceView mGLView;
    private Game b;
    
    MyGLRenderer mRenderer;
    
    Handler uiHandler = new Handler();
    Runnable uiRunnable = new Runnable() {
        @Override
        public void run() {
        	b.uiUpdate();
//        	if(b.gameState>0){
        		uiHandler.postDelayed(this, 10);
//        	}
        }
    };
    Runnable gameRunnable = new Runnable() {
//        @Override
        public void run() {
        	Log.d("THREAD", "RUNNING");
    		long lastTime = SystemClock.currentThreadTimeMillis();
            b.setGameState(Game.STARTING);
//        	while(true){
    		while(b.gameState>0){
        		long time = SystemClock.currentThreadTimeMillis();
//        		Log.d("TIME",Long.toString(time));
        		long dt = time-lastTime;
        		if(dt > 10){
            		b.update(dt/1000.0);
            		lastTime = time;
        		}
    		}   
        	Log.d("THREAD","FINISHED");
    	}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        b = new Game(
        		(TextView)findViewById(R.id.tvMessage), 
        		(TextView)findViewById(R.id.tvScore), 
        		(TextView)findViewById(R.id.tvHighScore),
        		this); 

        mGLView = (MyGLSurfaceView)findViewById(R.id.myGLSurfaceView);
        mRenderer = new MyGLRenderer(b);
        mGLView.setRenderer(mRenderer);
        mGLView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
            	return b.onTouchEvent(e);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
        Log.d("ACTIVITY", "PAUSED");
        b.setGameState(Game.ENDED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
        b.reset();
        b.setGameState(Game.STARTING);
        Log.d("THREAD", "STARTING");
        (new Thread(gameRunnable)).start();
        Log.d("THREAD", "STARTED");
        uiHandler.postDelayed(uiRunnable, 1);
    }
}