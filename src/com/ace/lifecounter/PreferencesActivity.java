package com.ace.lifecounter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class PreferencesActivity extends Activity {
	Spinner lifeSpinner;
	Spinner colorSpinner;
	
	public static final String STARTING_HP = "STARTING_HP";
	public static final String COLOR = "COLOR";
	
	SharedPreferences prefs;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);
		
		lifeSpinner=(Spinner)findViewById(R.id.lifeSpinner);

		colorSpinner=(Spinner)findViewById(R.id.colorSpinner);
		
		populateSpinners();
		
		Context context = getApplicationContext();
		prefs=PreferenceManager.getDefaultSharedPreferences(context);
		updateUIFromPreferences();
		
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				savePreferences();
				PreferencesActivity.this.setResult(RESULT_OK);
				finish();
			}
		});
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				savePreferences();
				PreferencesActivity.this.setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		
	}
	
	private void savePreferences() {
		int maxHP = lifeSpinner.getSelectedItemPosition();
		int color = colorSpinner.getSelectedItemPosition();
		Editor editor = prefs.edit();
		editor.putInt(STARTING_HP, maxHP);
		editor.putInt(COLOR, color);
		editor.commit();
	}
	
	private void updateUIFromPreferences() {
		int maxHP = prefs.getInt(STARTING_HP, 1);
		int color = prefs.getInt(COLOR, 3);
		lifeSpinner.setSelection(maxHP);
		colorSpinner.setSelection(color);
	}
	
	private void populateSpinners() {
		int spinner_dd_item=android.R.layout.simple_spinner_dropdown_item;
		ArrayAdapter<CharSequence> lAdapter;
		lAdapter=ArrayAdapter.createFromResource(this, R.array.max_hp_options, android.R.layout.simple_spinner_item);
		lAdapter.setDropDownViewResource(spinner_dd_item);
		lifeSpinner.setAdapter(lAdapter);
		
		ArrayAdapter<CharSequence> cAdapter;
		cAdapter=ArrayAdapter.createFromResource(this, R.array.colors, android.R.layout.simple_spinner_item);
		cAdapter.setDropDownViewResource(spinner_dd_item);
		colorSpinner.setAdapter(cAdapter);
		
	
	}
	
}
