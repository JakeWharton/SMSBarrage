package com.jakewharton.smsbarrage.ui;

import com.jakewharton.smsbarrage.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity {
	private static final int MENU_RESTORE_DEFAULTS = 0;
	
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.clear();
		menu.add(0, MENU_RESTORE_DEFAULTS, 0, R.string.menu_restore_defaults);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_RESTORE_DEFAULTS:
				restoreDefaultPreferences();
				return true;
		}
		return false;
	}
	
	private void restoreDefaultPreferences() {
		PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();
		setPreferenceScreen(null);
		addPreferencesFromResource(R.xml.preferences);
	}
}
