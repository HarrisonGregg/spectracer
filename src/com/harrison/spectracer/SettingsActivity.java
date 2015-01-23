package com.harrison.spectracer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	SharedPreferences sharedPref; 
	SeekBar sbVolume;
	EditText etName;
	Button btnResetHighscore;
	Button btnUnlinkUserId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        sharedPref = this.getSharedPreferences("com.harrison.spectracer", Context.MODE_PRIVATE);
        float volume = sharedPref.getFloat(Game.STATE_VOLUME_KEY,1);
        String name = sharedPref.getString(Game.STATE_NAME_KEY, "User");
        
        sbVolume = (SeekBar) findViewById(R.id.sbVolume);	
        etName = (EditText) findViewById(R.id.etName);
        btnResetHighscore = (Button) findViewById(R.id.btnResetHighscore);
        btnUnlinkUserId = (Button) findViewById(R.id.btnUnlinkUserId);
        
        sbVolume.setMax(1000);
        sbVolume.setProgress((int)(1000*volume));

        etName.setText(name);

        btnResetHighscore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 sharedPref.edit().putLong(Game.STATE_HIGHSCORE_KEY, 0).commit();
				 Log.d("HIGHSCORE", "RESET");
				 Log.d("HIGHSCORE", ""+sharedPref.getLong(Game.STATE_HIGHSCORE_KEY,0));
				 Toast.makeText(
						SettingsActivity.this, 
						"Highscore = "+sharedPref.getLong(Game.STATE_HIGHSCORE_KEY,0), 
						Toast.LENGTH_LONG).show();
			}
		});

        btnUnlinkUserId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 sharedPref.edit().putString(Game.STATE_PARSE_OBJECT_KEY, "").commit();
				 Toast.makeText(
						SettingsActivity.this, 
						"User ID Unlinked", 
						Toast.LENGTH_LONG).show();
			}
		});
	}

    @Override
    protected void onPause() {
        super.onPause();
        sharedPref.edit().putFloat(Game.STATE_VOLUME_KEY, (float)sbVolume.getProgress()/1000).commit();
        sharedPref.edit().putString(Game.STATE_NAME_KEY, etName.getText().toString().trim()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}