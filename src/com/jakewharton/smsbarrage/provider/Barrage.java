package com.jakewharton.smsbarrage.provider;

import android.net.Uri;

public class Barrage {
	private static final String TAG = "Barrage";
	
	public interface Columns {
		public static final String TYPE = "type";
		
		public static final int TYPE_ALL = 0;
		public static final int TYPE_RUNNING = 1;
		public static final int TYPE_QUEUED = 2;
		public static final int TYPE_DRAFT = 3;
		public static final int TYPE_TEMPLATE = 4;
		
		public static final String BARRAGE_ID = "barrage_id";
		public static final String ADDRESS = "address";
		public static final String BODY = "body";
		public static final String DATE = "date";
	}
	
	public static final Uri CONTENT_URI = Uri.parse("content://barrage");
}