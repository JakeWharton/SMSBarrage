package com.jakewharton.smsbarrage.transaction;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.telephony.gsm.SmsManager;

public class BarrageService extends Service {
	private static final String TAG="BarrageService";
	
	private ServiceHandler mServiceHandler;
	private Looper mServiceLooper;
	private final Object mLock = new Object();
	private SmsManager mSmsManager;
	
	@Override
	public void onCreate() {
		HandlerThread thread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
		mSmsManager = SmsManager.getDefault();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}
	
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			final int    serviceId = msg.arg1;
			final Intent intent    = (Intent)msg.obj;
			final String action    = intent.getAction();
			
			if (action.equals(ACTION_BOOT_COMPLETED)) {
				handleBootCompleted();
			}
			
			EventReceiver.finishStartingService(BarrageService.this, serviceId);
		}
	}

	public void handleBootCompleted() {
		
	}
	
	protected void enqueueMessages(String[] numbers, String message, int count) {
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < numbers.length; j++) {
				//TODO: enqueue message (numbers[j], message)
			}
		}
	}
}
