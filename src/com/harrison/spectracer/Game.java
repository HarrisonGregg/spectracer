package com.harrison.spectracer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Game {
	static final String STATE_HIGHSCORE_KEY = "com.harrison.spectracer.highscore";
	static final String STATE_VOLUME_KEY = "com.harrison.spectracer.volume";
	static final String STATE_NAME_KEY = "com.harrison.spectracer.name";
	static final String STATE_PARSE_OBJECT_KEY = "com.harrison.spectracer.parseobject";
	
	static final int ENDED = 0;
	static final int STARTING = 1;
	static final int PAUSED = 2;
	static final int UNPAUSED = 3;
	static final int GAMEOVER = 4;	
	
	private Random r;

	Activity activity;
    SharedPreferences sharedPref;
    TextView tvScore;
    TextView tvHighScore;
    TextView tvMessage;
    LinearLayout llHighscore;
    EditText etName;
    
    MediaPlayer mediaPlayer;

	private ArrayList<Obstacle> obstacles; 
	private Player triangle;
	private Rect leftBorder;
	private Rect rightBorder;

	int [][] board;

	private double height;
	public int gameState;

	private long gameStart;
	private long gameTime;
	private long highscore;
	private long lastTime;
	private int lastSide = -1;
	private double countdown;
	private double colorCountdown;
	private double gameOverCountdown;
	private Colorf gameColor;
	private Colorf targetColor;

	public Game(TextView tvMessage, TextView tvScore, TextView tvHighScore, Activity activity1){
		this.activity = activity1;
		this.tvScore = tvScore;
		this.tvHighScore = tvHighScore;
		this.tvMessage = tvMessage;
		llHighscore = (LinearLayout) activity.findViewById(R.id.llHighscore);
		etName = (EditText) activity.findViewById(R.id.etName);
		
		Button btnSubmit = (Button) activity.findViewById(R.id.btnSubmit);
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				highscore = gameTime;
				saveHighscoreOnParse(highscore);
//				etName.clearFocus();
//				activity.getWindow().setSoftInputMode(
//					      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});
		
		gameColor = Colorf.GRAY();
        targetColor = Colorf.GRAY();
        
        sharedPref = activity.getSharedPreferences("com.harrison.spectracer", Context.MODE_PRIVATE);

        initParsObject();

		etName.setText(sharedPref.getString(Game.STATE_NAME_KEY, ""));

		highscore = sharedPref.getLong(STATE_HIGHSCORE_KEY,0);
        float volume = sharedPref.getFloat(STATE_VOLUME_KEY,1);
		mediaPlayer = MediaPlayer.create(activity, R.raw.song);
        mediaPlayer.setVolume(volume, volume);
        
		obstacles = new ArrayList<Obstacle>();

		r = new Random(System.currentTimeMillis());

		triangle = new Player(new Vector(0, 0.5), Color.WHITE());
		triangle.rotVel = (float) 800;

		leftBorder = new Rect(new Vector(-1, 2), new Vector(.2, 5), Color.RED());
		rightBorder = new Rect(new Vector(1, 2), new Vector(.2, 5), Color.RED());
   	}
	
	void reset(){
		gameTime = 0;
		gameState = STARTING;
		lastTime = SystemClock.currentThreadTimeMillis();		
		gameStart = SystemClock.currentThreadTimeMillis();		
	}

	void uiUpdate(){
	    double score = gameTime/1000.0;

	    DecimalFormat nf = new DecimalFormat("###########0.00");
	    tvScore.setText(" "+nf.format(score));
	    tvHighScore.setText("  "+nf.format(highscore/1000.0));
	    List<String> buttonText = Arrays.asList("", "Tap to start", "Resume", "", "Game Over\nTap to try again");
    	tvMessage.setText(buttonText.get(gameState));
		llHighscore.setVisibility(View.INVISIBLE);;
    	if(gameState == GAMEOVER){
    		if(gameTime > highscore){
    			llHighscore.setVisibility(View.VISIBLE);;
    	        tvMessage.setText("Tap to try again");
    		}
    	}
	}
	
	void resize(int surfaceWidth, int surfaceHeight){
		height = 2*surfaceHeight/surfaceWidth;
		leftBorder.size = new Vector(.2, height);
		leftBorder.position = new Vector(-1, height/2);
		rightBorder.size = new Vector(.2, height);
		rightBorder.position = new Vector(1, height/2);
	}
	
    public boolean onTouchEvent(MotionEvent e) {

		switch (e.getAction()) {
    	case MotionEvent.ACTION_DOWN:
	    	switch(gameState){
		    case UNPAUSED:
		    	triangle.side *= -1;
				triangle.rotVel = -800*triangle.side;
		    	break;
		    case ENDED:
		    	break;
 		    default:
		    	setGameState(UNPAUSED);
		    	break;
		    }
        }
		return true;
    }
    
    ParseObject gameScore;
    String objectId;
    void initParsObject(){
    	Log.d("PARSEOBJECT", "INITIALIZING");
    	objectId = sharedPref.getString(STATE_PARSE_OBJECT_KEY, "");
    	if(objectId.equals("")){
    		gameScore = new ParseObject("GameScore");
    		gameScore.put("score", highscore);
    		gameScore.saveInBackground();
    		gameScore.fetchInBackground(new GetCallback<ParseObject>() {
    			public void done(ParseObject object, ParseException e) {
    		    	Log.d("PARSEOBJECT", "CALLED BACK");
    			    if (e != null) {
    			    	objectId = gameScore.getObjectId();
    			        Log.d("OBJECT_ID", objectId);
    			    	sharedPref.edit().putString(STATE_PARSE_OBJECT_KEY, objectId).commit();
    			    }
    			}
			});
		}
    }

    void saveHighscoreOnParse(final long score){
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
    	query.getInBackground(objectId, new GetCallback<ParseObject>() {
    	  public void done(ParseObject gameScore, ParseException e) {
    	    if (e == null) {
    	    	gameScore.put("name", etName.getText().toString());
    	    	gameScore.put("score", score);
    	    	gameScore.saveInBackground();
    	    }
    	  }
    	});
    }
    
	void setGameState(int newState){
		if(gameState==ENDED && newState != STARTING)
			return;
		switch(newState){
		case GAMEOVER:
			mediaPlayer.pause();
			mediaPlayer.seekTo(0);
			targetColor = Colorf.GRAY();
			gameColor = Colorf.GRAY();
			if(gameTime > highscore){
				gameOverCountdown = .5;
//				saveHighscoreOnParse(gameTime);
    	        sharedPref.edit().putLong(STATE_HIGHSCORE_KEY, gameTime).commit();
			}
			else
				gameOverCountdown = .25;
			break;
		case ENDED:
	        sharedPref.edit().putString(STATE_NAME_KEY, etName.getText().toString()).commit();
			obstacles.clear();
//			mediaPlayer.reset();
			mediaPlayer.release();
			break;
		case UNPAUSED:
			if(gameState==GAMEOVER || gameState==STARTING){
				etName.clearFocus();
				InputMethodManager imm = (InputMethodManager)activity.getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
				if(gameOverCountdown > 0)
					return;
				Log.d("GAMESTART","NOW: "+gameStart);
	    		if(gameTime > highscore){
	    			highscore = gameTime;
//	    	        saveHighscoreOnParse(highscore);
	    		}
	    		try {
					mediaPlayer.start();
				} catch (IllegalStateException e) {
					mediaPlayer = MediaPlayer.create(activity, R.raw.song);
			        float volume = sharedPref.getFloat(STATE_VOLUME_KEY,1);
			        mediaPlayer.setVolume(volume, volume);
			        mediaPlayer.start();
				}
				obstacles.clear();
				gameStart = lastTime;
				gameTime = 0;
				countdown = 0;
				colorCountdown = 2;
				gameOverCountdown = 1;
				targetColor = Colorf.RED();
			}
			triangle.rotVel = -800*triangle.side;
			break;
		}
		gameState = newState;
		List<String> states = Arrays.asList("ENDED", "STARTING", "PAUSED", "UNPAUSED", "GAMEOVER");
    	Log.d("GAMESTATE", states.get(gameState));
	}

	void update(double dt){
		long time = SystemClock.currentThreadTimeMillis();

		gameOverCountdown -= dt;
		
		if(gameState == UNPAUSED){
			colorCountdown -= dt;
		}
		if(colorCountdown < 0){
			targetColor = Colorf.random(r);
			colorCountdown += 2;
		}
		gameColor.r += (targetColor.r-gameColor.r)*dt; 
		gameColor.g += (targetColor.g-gameColor.g)*dt; 
		gameColor.b += (targetColor.b-gameColor.b)*dt; 
		
//		Log.d("GAMECOLOR", gameColor.r+", " + gameColor.g + ", " + gameColor.b);

		if(gameState!=UNPAUSED){
			lastTime = time;
			triangle.rotation += dt*triangle.rotVel;
			if(triangle.rotVel < 0){
				triangle.rotVel = Math.min(triangle.rotVel + 300*dt, 0);
			}else if(triangle.rotVel > 0){
				triangle.rotVel = Math.max(triangle.rotVel - 300*dt, 0);
			}
			return;
		}
//		gameTime += time-lastTime;		
//		gameTime = mediaPlayer.getCurrentPosition();
		gameTime = time - gameStart;
		lastTime = time;
		countdown -= dt;
		if(countdown < 0){
			lastSide = -1*lastSide;
			Obstacle o = new Obstacle(lastSide, gameTime, 4.0);
			obstacles.add(o);
			countdown += (.25 + r.nextDouble()/2.0)*(30000.0/(30000.0+gameTime));
		}
		
		triangle.update(dt);

		for(int i = obstacles.size()-1; i >= 0; i--){
			Obstacle shape = obstacles.get(i);
			shape.update(dt);
			if(shape.delete){
				obstacles.remove(i);
			} else if(shape.contains(triangle.position)) {
				setGameState(GAMEOVER);
				break;
			}
		}
		
	}
	
	void draw(GL10 gl, int windowWidth, int windowHeight){
		Colorf objectColor = gameColor;
		Colorf clearColor = new Colorf((float)(0.3+.05*Math.sin(lastTime*20.01))*gameColor.r, 
	    		(float)(0.3+.05*Math.sin(lastTime*20))*gameColor.g, 
	    		(float)(0.3+.05*Math.sin(lastTime*20))*gameColor.b);
		if(gameTime > 21500 || (gameTime > 40000 && gameTime%800 < 400)){
			objectColor = clearColor;
			clearColor = gameColor;
		}
		gl.glClearColor(
				clearColor.r,
				clearColor.g,
				clearColor.b,
	    		0.0f);        	// clear color
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // Clears the screen and depth buffer.
		
		double beat = (Math.max(Math.sin(lastTime*20.01),0.5)-.5)/50;
		for(int i = 0; i < obstacles.size(); i++){
			try {
				gl.glPushMatrix();
				gl.glTranslatef(3*(float)beat*((obstacles.get(i).position.x < 0)?1:-1), 0, 0);
				obstacles.get(i).color = objectColor.intColor();
				obstacles.get(i).draw(gl);
				gl.glPopMatrix();
			} catch (IndexOutOfBoundsException e) {
				Log.d("ERROR", "Index out of bounds");
			}
		}
		gl.glPushMatrix();
		gl.glTranslatef(3*(float)beat, 0, 0);
		leftBorder.color = objectColor.intColor();
		leftBorder.draw(gl);
		gl.glTranslatef(-6*(float)beat, 0, 0);	
		rightBorder.color = objectColor.intColor();
		rightBorder.draw(gl);
		gl.glPopMatrix();
		triangle.draw(gl);
	}
};