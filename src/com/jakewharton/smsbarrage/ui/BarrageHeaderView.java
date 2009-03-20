package com.jakewharton.smsbarrage.ui;

import com.jakewharton.smsbarrage.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BarrageHeaderView extends RelativeLayout {
	private static final String TAG="BarrageHeaderView";
	
	private BarrageHeader mBarrageHeader;
	private TextView mName;
	private TextView mStatus;
	private TextView mCounts;
	private TextView mPercent;
	private ProgressBar mProgress;
	
	public BarrageHeaderView(Context context) {
		super(context);
	}
	public BarrageHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mName     = (TextView)findViewById(R.id.name);
		mStatus   = (TextView)findViewById(R.id.status);
		mCounts   = (TextView)findViewById(R.id.counts);
		mPercent  = (TextView)findViewById(R.id.percent);
		mProgress = (ProgressBar)findViewById(R.id.progress);
	}
	
	public void bind(String name, String counts) {
		mName.setText(name);
		mStatus.setVisibility(INVISIBLE);
		mCounts.setText(counts);
		mProgress.setVisibility(INVISIBLE);
		mPercent.setVisibility(INVISIBLE);
	}
	public void bind(Context context, BarrageHeader header) {
		mBarrageHeader = header;
		
		mName.setText(header.getName());
		
		switch (header.getStatus()) {
			case BarrageHeader.STATUS_RUNNING:
				mStatus.setTextColor(Color.GREEN);
				mStatus.setText("Running");
				updateProgress(header.getCountCurrent(), header.getCountTotal());
				break;
			case BarrageHeader.STATUS_PAUSED:
				mStatus.setTextColor(Color.YELLOW);
				mStatus.setText("Paused");
				updateProgress(header.getCountCurrent(), header.getCountTotal());
				break;
			case BarrageHeader.STATUS_QUEUED:
				mStatus.setTextColor(Color.GRAY);
				mStatus.setText("Queued");
				//TODO: add time remaining
				mCounts.setText("Starts: 03/14/2009 03:14PM");
				mProgress.setVisibility(INVISIBLE);
				mPercent.setVisibility(INVISIBLE);
				break;
			case BarrageHeader.STATUS_DRAFT:
				mStatus.setTextColor(Color.RED);
				mStatus.setText("Draft");
				updateProgress(0, header.getCountTotal());
				break;
		}
	}
	private void updateProgress(int current, int total) {
		mCounts.setText(String.format("%1$d/%2$d", current, total));
		mPercent.setText(String.format("%1$2d%2$%", (current*100 / total)));
		mProgress.setMax(total);
		mProgress.setProgress(current);
	}
	public BarrageHeader getHeader() {
		return mBarrageHeader;
	}
}
