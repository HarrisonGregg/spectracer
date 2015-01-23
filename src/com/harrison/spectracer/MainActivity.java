package com.harrison.spectracer;

import com.parse.Parse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tvPlay;
	TextView tvAbout;
	TextView tvHighscores;
	TextView tvSettings;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "KkBkoW7TyaJ7drk3m8DwQwur7U8IfQ1wp3uyMvaY", "IgbhIJsuvyZ0b7Q8VoJvOrQwvRFVu3a6dimzeCRZ");

        tvPlay = (TextView) findViewById(R.id.tvPlay);
        tvAbout = (TextView) findViewById(R.id.tvAbout);
        tvHighscores = (TextView) findViewById(R.id.tvHighscores);
        tvSettings = (TextView) findViewById(R.id.tvSettings);

        tvPlay.setOnClickListener(buttonClick);
        tvAbout.setOnClickListener(buttonClick);
        tvSettings.setOnClickListener(buttonClick);
        tvHighscores.setOnClickListener(buttonClick);
	}

    OnClickListener buttonClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i = new Intent();
			switch(v.getId()){
				case R.id.tvAbout:
					i.setClass(MainActivity.this, AboutActivity.class);
					break;
				case R.id.tvSettings:
					i.setClass(MainActivity.this, SettingsActivity.class);
					break;
				case R.id.tvHighscores:
					i.setClass(MainActivity.this, HighscoresActivity.class);
					break;
				case R.id.tvPlay:
					i.setClass(MainActivity.this, GameActivity.class);
					break;
				default:
					return;
			}
			startActivity(i);
		}
	};

	@Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}