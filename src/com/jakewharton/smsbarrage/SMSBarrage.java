package com.jakewharton.smsbarrage;

import android.app.Application;
import android.preference.PreferenceManager;

public class SMSBarrage extends Application {
	private static final String TAG = "SMSBarrage";
	
	@Override
	public void onCreate() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}
}

/*public class SMSBarrage extends Activity {
	private static final int PROGRESS_OFFSET = 2;
	private static final String TAG = "SMSBarrage";
	private static final String AGREED = "AGREED";
	
	//UI Elements
	private RecipientsEditor reTo;
	private TextView textCount;
	private SeekBar seekCount;
	private TextView textDelay;
	private SeekBar seekDelay;
	private EditText editMessage;
	private Button buttonSend;
	
	private SmsManager manager;
	private int count = 2;
	private int current = 0;
	private int delay = 1000;
	private String[] numbers;
	private String message;
	private ProgressDialog progress;
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            if (current >= count) {
                progress.dismiss();
                progress = null;
            }
            else {
            	try {
        			String number = numbers[current % numbers.length];
        			manager.sendTextMessage(number, null, message, null, null);
        			Log.i(TAG, "Sent message " + ((current / numbers.length) + 1) + " to " + number);
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
            	current++;
                progress.incrementProgressBy(1);
                handler.sendEmptyMessageDelayed(0, delay);
            }
        }
	};
	
	private OnClickListener sendListener = new OnClickListener() {
		public void onClick(View arg0) {
			current = 0;
			
			numbers = reTo.getRecipientList().getNumbers();
			
			count = seekCount.getProgress() + PROGRESS_OFFSET;
			count *= numbers.length;
			
			progress = new ProgressDialog(SMSBarrage.this);
			progress.setTitle(R.string.sending);
	        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        progress.setButton(getString(R.string.stop), cancelListener);

			progress.setMax(count);
			progress.setProgress(0);
			
			message = editMessage.getText().toString();
			if (message.length() == 0)
				return;
			
			progress.show();
			handler.sendEmptyMessage(0);
		}
	};
	private OnSeekBarChangeListener countListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			textCount.setText(Integer.toString(arg1 + PROGRESS_OFFSET));
		}
		public void onStartTrackingTouch(SeekBar arg0) {}
		public void onStopTrackingTouch(SeekBar arg0) {}
	};
	private OnSeekBarChangeListener delayListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			delay = arg1;
			textDelay.setText(Integer.toString(arg1));
		}
		public void onStartTrackingTouch(SeekBar arg0) {}
		public void onStopTrackingTouch(SeekBar arg0) {}
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	protected void onStart() {
		super.onStart();
		
		settings = getSharedPreferences(TAG, 0);
		
		if (!settings.getBoolean(AGREED, false)) {
			AlertDialog.Builder warning = new AlertDialog.Builder(SMSBarrage.this)
				.setTitle(R.string.warning_title)
				.setMessage(R.string.warning)
				.setPositiveButton(R.string.agree, agreeListener)
				.setNegativeButton(R.string.disagree, disagreeListener)
				.setCancelable(false);
        	warning.show();
		}

        manager = SmsManager.getDefault();
        
        textCount = (TextView)findViewById(R.id.number_of_messages_count);
        seekCount = (SeekBar)findViewById(R.id.number_of_messages);
        seekCount.setOnSeekBarChangeListener(countListener);
        seekCount.setProgress(2 - PROGRESS_OFFSET);
        textDelay = (TextView)findViewById(R.id.delay_count);
        seekDelay = (SeekBar)findViewById(R.id.delay);
        seekDelay.setOnSeekBarChangeListener(delayListener);
        seekDelay.setProgress(1000);
        buttonSend = (Button)findViewById(R.id.send);
        buttonSend.setOnClickListener(sendListener);
        editMessage = (EditText)findViewById(R.id.message);
        reTo = (RecipientsEditor)findViewById(R.id.recipients_editor);
        reTo.setAdapter(new RecipientsAdapter(this));
	}
}*/