package com.jakewharton.smsbarrage.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.android.mms.ui.RecipientsAdapter;
import com.android.mms.ui.RecipientsEditor;
import com.jakewharton.smsbarrage.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SMSBarrage extends Activity {
	private static final int PROGRESS_OFFSET = 2;
	private static final String TAG = "SMSBarrage";
	private static final String AGREED = "AGREED";
	private static final String SMS_SENT = "SMS_SENT";
	
	private static final int MENU_SEND = 0;
	private static final int MENU_ABOUT = 1;
	private static final int MENU_UPDATE = 2;
	
	//UI Elements
	private RecipientsEditor reTo;
	private TextView textCount;
	private SeekBar seekCount;
	private EditText editMessage;
	private Button buttonSend;
	
	private SmsManager manager;
	private int count = 2;
	private int current = 0;
	private String[] numbers;
	private String message;
	private ProgressDialog progress;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	private PendingIntent sentIntent;
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
        	try {
    			String number = numbers[current % numbers.length];
    			manager.sendTextMessage(number, null, message, sentIntent, null);
    			Log.i(TAG, "Sending message " + ((current / numbers.length) + 1) + " to " + number);
        	}
        	catch (StringIndexOutOfBoundsException e) {
        		AlertDialog.Builder error = new AlertDialog.Builder(SMSBarrage.this)
        			.setTitle(R.string.error_title)
        			.setMessage(R.string.error)
        			.setCancelable(false)
        			.setPositiveButton(R.string.ok, null);
        		error.show();
        		progress.dismiss();
        		progress = null;
        		return;
        	}
        }
	};
	
	private OnClickListener sendListener = new OnClickListener() {
		public void onClick(View arg0) {
			current = 0;
			
			numbers = reTo.getRecipientList().getNumbers();
			if (numbers.length == 0) {
				Toast.makeText(getBaseContext(), "Recipients must not be blank.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			count = seekCount.getProgress() + PROGRESS_OFFSET;
			count *= numbers.length;
			
			progress = new ProgressDialog(SMSBarrage.this);
			progress.setTitle(R.string.sending);
	        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        progress.setButton(getString(R.string.stop), cancelListener);
			progress.setMax(count);
			progress.setProgress(0);
			
			message = editMessage.getText().toString();
			if (message.length() == 0) {
				Toast.makeText(getBaseContext(), "Message must not be blank.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			progress.show();
			
			sentIntent = PendingIntent.getBroadcast(SMSBarrage.this, 0, new Intent(SMS_SENT), 0);
			
			handler.sendEmptyMessage(0);
		}
	};
	private OnSeekBarChangeListener countListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int newValue, boolean fromTouch) {
			textCount.setText(Integer.toString(newValue + PROGRESS_OFFSET));
		}
		public void onStartTrackingTouch(SeekBar seekBar) {}
		public void onStopTrackingTouch(SeekBar seekBar) {
			int progress = seekCount.getProgress();
			if (progress == seekCount.getMax()) {
				seekCount.setMax((2 * (seekCount.getMax() + PROGRESS_OFFSET)) - PROGRESS_OFFSET);
				seekCount.setProgress(0);
				seekCount.setProgress(progress);
			}
			else if ((progress <= seekCount.getMax() / 2) && (seekCount.getMax() > 100 - PROGRESS_OFFSET)) {
				seekCount.setMax(((seekCount.getMax() + PROGRESS_OFFSET) / 2) - PROGRESS_OFFSET);
				seekCount.setProgress(0);
				seekCount.setProgress(progress);
			}
		}
	};
	private android.content.DialogInterface.OnClickListener agreeListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			SMSBarrage.this.editor = SMSBarrage.this.settings.edit();
			SMSBarrage.this.editor.putBoolean(SMSBarrage.AGREED, true);
			SMSBarrage.this.editor.commit();
		}
	};
	private android.content.DialogInterface.OnClickListener disagreeListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			SMSBarrage.this.finish();
		}
	};
	private android.content.DialogInterface.OnClickListener cancelListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			seekCount.setProgress(count - current - PROGRESS_OFFSET);
			count = 0;
		}
	};
	private android.content.DialogInterface.OnClickListener updateListener = new android.content.DialogInterface.OnClickListener() {
		public void onClick(DialogInterface arg0, int arg1) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse("http://jakewharton.com/downloads/");
			i.setData(u);
			startActivity(i);
			SMSBarrage.this.finish();
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		settings = getSharedPreferences(TAG, 0);
		if (!settings.getBoolean(AGREED, false)) {
			new AlertDialog.Builder(SMSBarrage.this)
				.setTitle(R.string.warning_title)
				.setMessage(R.string.warning)
				.setPositiveButton(R.string.agree, agreeListener)
				.setNegativeButton(R.string.disagree, disagreeListener)
				.setCancelable(false)
				.show();
		}

        manager = SmsManager.getDefault();
        
        registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				if (arg1.getAction().equals(SMS_SENT)) {
					switch (getResultCode()) {
						case Activity.RESULT_OK:
			            	current++;
			                progress.incrementProgressBy(1);
			                
			                if (current >= count) {
			                    progress.dismiss();
			                    progress = null;
			                }
			                else {
			                	handler.sendEmptyMessage(0);
			                }
			                
							return;
							
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
							break;
							
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							Toast.makeText(getBaseContext(), "No service.", Toast.LENGTH_SHORT).show();
							break;
							
						case SmsManager.RESULT_ERROR_NULL_PDU:
							Toast.makeText(getBaseContext(), "Null PDU.", Toast.LENGTH_SHORT).show();
							break;
							
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							Toast.makeText(getBaseContext(), "Radio off.", Toast.LENGTH_SHORT).show();
							break;
					}
					progress.dismiss();
				}
			}
		}, new IntentFilter(SMS_SENT));
    }
    @Override
    public void onStart() {
    	super.onStart();
        
        textCount = (TextView)findViewById(R.id.number_of_messages_count);
        seekCount = (SeekBar)findViewById(R.id.number_of_messages);
        seekCount.setOnSeekBarChangeListener(countListener);
        buttonSend = (Button)findViewById(R.id.send);
        buttonSend.setOnClickListener(sendListener);
        editMessage = (EditText)findViewById(R.id.message);
        reTo = (RecipientsEditor)findViewById(R.id.recipients_editor);
        reTo.setAdapter(new RecipientsAdapter(this));
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, MENU_SEND, 0, R.string.menu_send);
		menu.add(0, MENU_ABOUT, 0, R.string.menu_about);
		menu.add(0, MENU_UPDATE, 0, R.string.menu_update);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SEND:
				sendListener.onClick(null);
				return true;
				
			case MENU_ABOUT:
				startActivity(new Intent(this, About.class));
				return true;
				
			case MENU_UPDATE:
				checkUpdate.sendEmptyMessage(0);
				return true;
		}
		return false;
	}
	
	private Handler checkUpdate = new Handler() {
        @Override
        public void handleMessage(Message msg) {
			final int BUFFER_SIZE = 4;
			final String UPDATE_URL = "http://versioncheck.jakewharton.com/smsbarrage";
			
			InputStream in = null;
			try {
		        int response = -1;
		        
		        URL url = new URL(UPDATE_URL);
		        URLConnection conn = url.openConnection();
		        
		        if (!(conn instanceof HttpURLConnection))                     
		            throw new IOException("Not an HTTP connection");
		        
	            HttpURLConnection httpConn = (HttpURLConnection) conn;
	            httpConn.setAllowUserInteraction(false);
	            httpConn.setInstanceFollowRedirects(true);
	            httpConn.setRequestMethod("GET");
	            httpConn.connect(); 
	
	            response = httpConn.getResponseCode();                 
	            if (response == HttpURLConnection.HTTP_OK) {
	                in = httpConn.getInputStream();                                 
	            }                     
			} catch (IOException e1) {
				Toast.makeText(getBaseContext(), "Unable to open connection to server.", Toast.LENGTH_SHORT).show();
				return;
			}
			
			InputStreamReader isr = new InputStreamReader(in);
			int charRead;
			String str = "";
			char[] inputBuffer = new char[BUFFER_SIZE];
			try {
				while ((charRead = isr.read(inputBuffer)) > 0) {
					String readString = String.copyValueOf(inputBuffer, 0, charRead);
					str += readString;
					inputBuffer = new char[BUFFER_SIZE];
				}
				in.close();
			} catch (IOException e) {
				Toast.makeText(getBaseContext(), "Unable to locate newest version.", Toast.LENGTH_SHORT).show();
				return;
			}
			str = str.trim();
			
			final int serverVersion = Integer.parseInt(str);
			int localVersion;
			try {
				PackageManager manager = getBaseContext().getPackageManager();
				PackageInfo info = manager.getPackageInfo(getBaseContext().getPackageName(), 0);
				localVersion = info.versionCode;
			} catch (NameNotFoundException e) {
				Toast.makeText(getBaseContext(), "Unable to read local version.", Toast.LENGTH_SHORT).show();
				return;
			}
	
			if (serverVersion > localVersion) {
				new AlertDialog.Builder(SMSBarrage.this)
					.setTitle("New Version Available")
					.setMessage("A newer version of this package is available online. Would you like to go to the website to download it?")
					.setPositiveButton("Yes", updateListener)
					.setNegativeButton("No", null)
					.setCancelable(false)
					.show();
			}
			else {
				Toast.makeText(getBaseContext(), "This version is up to date.", Toast.LENGTH_SHORT).show();
			}
		}
	};
}