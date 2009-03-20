package com.jakewharton.smsbarrage.ui;

import com.android.mms.ui.RecipientsAdapter;
import com.android.mms.ui.RecipientsEditor;
import com.jakewharton.smsbarrage.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class Compose extends Activity {
	private static final String TAG="ComposeBarrage";
	
	//Options menu
	private static final int MENU_START_BARRAGE = 0;
	private static final int MENU_SAVE_DRAFT = 1;
	private static final int MENU_SAVE_AS_TEMPLATE = 2;
	private static final int MENU_CANCEL = 3;
	
	private long mID;
	
	private RecipientsEditor mRecipientEditor;
	private EditText mContents;
	private Button mSend;
	private Button mSave;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose);
		
		mRecipientEditor = (RecipientsEditor)findViewById(R.id.recipients_editor);
		mRecipientEditor.setAdapter(new RecipientsAdapter(this));
		mContents = (EditText)findViewById(R.id.message);
		mSend = (Button)findViewById(R.id.send);
		mSave = (Button)findViewById(R.id.save);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		
		if (true) {
			//TODO: don't display these until required fields are complete
			menu.add(0, MENU_START_BARRAGE, 0, R.string.menu_start_barrage);
			menu.add(0, MENU_SAVE_DRAFT, 0, R.string.menu_save_draft);
			menu.add(0, MENU_SAVE_AS_TEMPLATE, 0, R.string.menu_save_as_template);
		}
		menu.add(0, MENU_CANCEL, 0, R.string.menu_cancel);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_START_BARRAGE:
				return true;
			case MENU_SAVE_DRAFT:
				return true;
			case MENU_SAVE_AS_TEMPLATE:
				return true;
			case MENU_CANCEL:
				this.finish();
				return true;
		}
		return false;
	}	
}