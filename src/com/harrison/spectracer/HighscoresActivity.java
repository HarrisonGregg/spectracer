package com.harrison.spectracer;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HighscoresActivity extends Activity {
	TextView tvNames;
	TextView tvHighscores;
	
	SharedPreferences sharedPref;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_highscores);

        sharedPref = this.getSharedPreferences("com.harrison.spectracer", Context.MODE_PRIVATE);
        
        tvNames = (TextView) findViewById(R.id.tvNames);
        tvHighscores = (TextView) findViewById(R.id.tvHighscores);
        
//        Parse.initialize(this, "KkBkoW7TyaJ7drk3m8DwQwur7U8IfQ1wp3uyMvaY", "IgbhIJsuvyZ0b7Q8VoJvOrQwvRFVu3a6dimzeCRZ");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.orderByDescending("score");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
            	    DecimalFormat nf = new DecimalFormat("###########0.00");
                	String names = "";
                	String highscores = "";
                	names += "Your highscore:\nGlobal highscores:\n";
                	highscores += nf.format(sharedPref.getLong(Game.STATE_HIGHSCORE_KEY,0)/1000.0)+"\n\n";
                	for(int i = 0; i < scoreList.size(); i++){
                		names += (i+1) + ". " + scoreList.get(i).getString("name") + "\n";
                		highscores += nf.format(scoreList.get(i).getInt("score")/1000.0) + "\n";
                	}
                	tvNames.setText(names);
                	tvHighscores.setText(highscores);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}