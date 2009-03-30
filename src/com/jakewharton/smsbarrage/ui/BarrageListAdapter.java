package com.jakewharton.smsbarrage.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class BarrageListAdapter extends CursorAdapter {
	private static final String TAG = "BarrageListAdapter";
	
	private static final String[] PROJECTION = new String[] {
		
	};
	
	private boolean mSimple;
	private LayoutInflater mFactory;

	public BarrageListAdapter(Context context, Cursor cursor, boolean simple) {
		super(context, cursor);
		mSimple = simple;
		mFactory = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view instanceof BarrageHeaderView) {
			//todo
			BarrageHeader bh = new BarrageHeader();
			((BarrageHeaderView)view).bind(context, bh);
		}
	}
	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return null;
	}
}
