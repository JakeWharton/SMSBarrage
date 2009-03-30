package com.jakewharton.smsbarrage.transaction;

import com.jakewharton.smsbarrage.R;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

public class EventReceiver extends BroadcastReceiver {
	private static final String TAG = "EventReceiver";
	
	//Defaults
	private static final boolean DEFAULT_AUTO_START = true;
	
	private static final Object mStartingServiceSync = new Object();
	private static PowerManager.WakeLock mStartingService;
	
	//Instance
	private static SharedPreferences settings;

	@Override
	public void onReceive(Context context, Intent intent) {
		settings = PreferenceManager.getDefaultSharedPreferences(context);
		if (settings.getBoolean(context.getString(R.string.preference_auto_start), DEFAULT_AUTO_START)) {
			intent.setClass(context, BarrageService.class);
			intent.putExtra("result", getResultCode());
			beginStartingService(context, intent);
		}
	}
	
	public static void beginStartingService(Context context, Intent intent) {
		synchronized (mStartingServiceSync) {
			if (mStartingService == null) {
				PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
				mStartingService = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StartingEventReceiver");
				mStartingService.setReferenceCounted(false);
			}
			mStartingService.acquire();
			context.startService(intent);
		}
	}
	
	public static void finishStartingService(Service service, int startId) {
		synchronized (mStartingServiceSync) {
			if (mStartingService != null) {
				if (service.stopSelfResult(startId)) {
					mStartingService.release();
				}
			}
		}
	}
}
