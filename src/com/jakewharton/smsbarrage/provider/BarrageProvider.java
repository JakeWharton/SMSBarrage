package com.jakewharton.smsbarrage.provider;

import com.google.android.mms.util.SqliteWrapper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class BarrageProvider {
	private static final String TAG = "BarrageProvider";
	
	//Folders
	public static final int TYPE_ALL      = 0;
	public static final int TYPE_RUNNING  = 1;
	public static final int TYPE_PAUSED   = 2;
	public static final int TYPE_QUEUED   = 3;
	public static final int TYPE_DRAFT    = 4;
	public static final int TYPE_TEMPLATE = 5;
	
	private static final String DEFAULT_SORT_ORDER = "priority DESC";
	public  static final Uri    CONTENT_URI        = Uri.parse("content://barrage");
	
	public interface TextBasedBarrageColumns {
		public static final String BARRAGE_ID = "barrage_id";
		public static final String TYPE       = "type";
		public static final String ADDRESS    = "address";
		public static final String BODY       = "body";
		public static final String DATE       = "date";
		public static final String COUNT      = "count";
		public static final String PRIORITY   = "priority";
	}
	
	public static final Cursor query(ContentResolver cr, String[] projection) {
		return cr.query(CONTENT_URI, projection, null, null, DEFAULT_SORT_ORDER);
	}
	public static final Cursor query(ContentResolver cr, String[] projection, String where, String orderBy) {
		return cr.query(CONTENT_URI, projection, where, null, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
	}
	public static Uri addBarrageToUri(ContentResolver cr, Uri uri, String address, String body, Long date, int count) {
		return addBarrageToUri(cr, uri, address, body, date, count, -1L);
	}
	public static Uri addBarrageToUri(ContentResolver cr, Uri uri, String address, String body, Long date, int count, long barrageId) {
		ContentValues values = new ContentValues(5);
		
		values.put(TextBasedBarrageColumns.ADDRESS, address);
		values.put(TextBasedBarrageColumns.BODY, body);
		if (date != null)
			values.put(TextBasedBarrageColumns.DATE, date);
		values.put(TextBasedBarrageColumns.COUNT, count);
		if (barrageId != -1L)
			values.put(TextBasedBarrageColumns.BARRAGE_ID, barrageId);
		
		return cr.insert(uri, values);
	}
	public static boolean moveBarrageToFolder(Context context, Uri uri, int folder) {
		if ((uri == null) || !validFolder(folder)) {
			return false;
		}
		
		ContentValues values = new ContentValues(1);
		values.put(TextBasedBarrageColumns.TYPE, folder);
		
		return (1 == SqliteWrapper.update(context, context.getContentResolver(), uri, values, null, null));
	}
	public static boolean validFolder(int folder) {
		return (
			(folder == TYPE_RUNNING)
			|| (folder == TYPE_QUEUED)
			|| (folder == TYPE_DRAFT)
			|| (folder == TYPE_PAUSED)
		);
	}
	
	public static final class Running implements BaseColumns, TextBasedBarrageColumns {
		public static final Uri    CONTENT_URI  = Uri.parse("content://barrage/running");
		public static final String DEFAULT_SORT = "date DESC";
		
		public static Uri addBarrage(ContentResolver cr, String address, String body, Long date, int count) {
			return addBarrageToUri(cr, CONTENT_URI, address, body, date, count);
		}
	}
	public static final class Paused implements BaseColumns, TextBasedBarrageColumns {
		public static final Uri    CONTENT_URI  = Uri.parse("content://barrage/paused");
		public static final String DEFAULT_SORT = "date DESC";
		
		public static Uri addBarrage(ContentResolver cr, String address, String body, Long date, int count) {
			return addBarrageToUri(cr, CONTENT_URI, address, body, date, count);
		}
	}
	public static final class Draft implements BaseColumns, TextBasedBarrageColumns {
		public static final Uri    CONTENT_URI  = Uri.parse("content://barrage/draft");
		public static final String DEFAULT_SORT = "date DESC";
		
		public static Uri addBarrage(ContentResolver cr, String address, String body, Long date, int count) {
			return addBarrageToUri(cr, CONTENT_URI, address, body, date, count);
		}
	}
	public static final class Queued implements BaseColumns, TextBasedBarrageColumns {
		public static final Uri    CONTENT_URI  = Uri.parse("content://barrage/queued");
		public static final String DEFAULT_SORT = "date DESC";
		
		public static Uri addBarrage(ContentResolver cr, String address, String body, Long date, int count) {
			return addBarrageToUri(cr, CONTENT_URI, address, body, date, count);
		}
	}
	public static final class All implements BaseColumns, TextBasedBarrageColumns {
		public static final Uri    CONTENT_URI  = Uri.parse("content://barrage/all");
		public static final String DEFAULT_SORT = "date DESC";
		
		public static Uri addBarrage(ContentResolver cr, String address, String body, Long date, int count) {
			return addBarrageToUri(cr, CONTENT_URI, address, body, date, count);
		}
	}
	
	public static final class Intents {
		public static final String BARRAGE_STARTED   = "com.jakewharton.smsbarrage.BARRAGE_STARTED";
		public static final String BARRAGE_PAUSED    = "com.jakewharton.smsbarrage.BARRAGE_PAUSED";
		public static final String BARRAGE_RESUMED   = "com.jakewharton.smsbarrage.BARRAGE_RESUMED";
		public static final String BARRAGE_COMPLETED = "com.jakewharton.smsbarrage.BARRAGE_COMPLETED";
		public static final String BARRAGE_QUEUED    = "com.jakewharton.smsbarrage.BARRAGE_QUEUED";
	}
}