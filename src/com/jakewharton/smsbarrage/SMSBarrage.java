package com.jakewharton.smsbarrage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SMSBarrage extends Activity {
	private static final int PROGRESS_OFFSET = 2;
	
	private SmsManager manager;
	private int count;
	private int current;
	private String number;
	private String message;
	private ProgressDialog progress;
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (current >= count) {
                progress.dismiss();
            } else {
            	try {
            		manager.sendTextMessage(number, null, message, null, null);
            	}
            	catch (StringIndexOutOfBoundsException e) {
            		AlertDialog.Builder error = new AlertDialog.Builder(SMSBarrage.this);
            		error.setTitle(R.string.error_title);
            		error.setMessage(R.string.error);
            		error.setCancelable(false);
            		error.setPositiveButton(R.string.ok, null);
            		error.show();
            		progress.dismiss();
            		return;
            	}
            	current++;
            	Log.i("SMSBarrage", "Sent message #" + current);
                progress.incrementProgressBy(1);
                handler.sendEmptyMessageDelayed(0, 1000);
            }
        }
	};
	
	private OnClickListener sendListener = new OnClickListener() {
		public void onClick(View arg0) {
			current = 0;
			count = ((SeekBar)findViewById(R.id.number_of_messages)).getProgress() + PROGRESS_OFFSET;
			progress.setMax(count);
			progress.setProgress(0);
			number = ((EditText)findViewById(R.id.number)).getText().toString();
			if (number.length() == 0)
				return;
			
			message = ((EditText)findViewById(R.id.message)).getText().toString();
			if (message.length() == 0)
				return;
			
			progress.show();
			handler.sendEmptyMessage(0);
		}
	};
	private OnSeekBarChangeListener countListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			((TextView)findViewById(R.id.number_of_messages_count)).setText(Integer.toString(arg1 + PROGRESS_OFFSET));
		}
		public void onStartTrackingTouch(SeekBar arg0) {}
		public void onStopTrackingTouch(SeekBar arg0) {}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        AlertDialog.Builder warning = new AlertDialog.Builder(SMSBarrage.this);
        warning.setTitle(R.string.warning_title);
        warning.setMessage(R.string.warning);
        warning.setPositiveButton(R.string.agree, null);
        warning.setNegativeButton(R.string.disagree, new android.content.DialogInterface.OnClickListener(){ public void onClick(DialogInterface arg0, int arg1) { finish(); }});
        warning.setCancelable(false);
        warning.show();

        manager = SmsManager.getDefault();
        
		progress = new ProgressDialog(SMSBarrage.this);
		progress.setTitle(R.string.sending);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setButton(getString(R.string.stop),
        	new android.content.DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface arg0, int arg1) {
        			((SeekBar)findViewById(R.id.number_of_messages)).setProgress(count - current - PROGRESS_OFFSET);
        			count = 0;
        		}
        	}
        );
        
        SeekBar count = (SeekBar)findViewById(R.id.number_of_messages);
        count.setOnSeekBarChangeListener(countListener);
        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(sendListener);
    }
}