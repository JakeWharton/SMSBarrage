package com.jakewharton.smsbarrage.ui;

import com.jakewharton.smsbarrage.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

public class BarrageList extends ListActivity {
	private static final String TAG="BarrageList";
	
	//Options Menu
	private static final int MENU_COMPOSE_NEW_BARRAGE = 0;
	private static final int MENU_PAUSE_ALL = 1;
	private static final int MENU_DELETE_ALL = 2;
	private static final int MENU_PREFERENCES = 3;
	private static final int MENU_ABOUT = 4;
	
	//Context Menu
	private static final int MENU_EDIT = 0;
	private static final int MENU_PAUSE = 1;
	private static final int MENU_DELETE = 2;
	
	private Cursor mCursor;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.barrage_list);
        ListView listView = getListView();
        LayoutInflater inflater = LayoutInflater.from(this);
        BarrageHeaderView headerView = (BarrageHeaderView)inflater.inflate(R.layout.barrage_header_view, listView, false);
        headerView.bind(getString(R.string.menu_new_barrage), getString(R.string.create_new_barrage));
        listView.addHeaderView(headerView, null, true);
        
        //UI Preview
        /*BarrageHeaderView item1 = (BarrageHeaderView)inflater.inflate(R.layout.barrage_header_view, listView, false);
        item1.bind(this, new BarrageHeader("Testing 1, 2, 3...", BarrageHeader.STATUS_RUNNING, 43, 12));
        listView.addHeaderView(item1, null, true);
        BarrageHeaderView item4 = (BarrageHeaderView)inflater.inflate(R.layout.barrage_header_view, listView, false);
        item4.bind(this, new BarrageHeader("Another Barrage", BarrageHeader.STATUS_PAUSED, 123, 64));
        listView.addHeaderView(item4, null, true);
        BarrageHeaderView item2 = (BarrageHeaderView)inflater.inflate(R.layout.barrage_header_view, listView, false);
        item2.bind(this, new BarrageHeader("Pie Day", BarrageHeader.STATUS_QUEUED, 43, 12));
        listView.addHeaderView(item2, null, true);
        BarrageHeaderView item3 = (BarrageHeaderView)inflater.inflate(R.layout.barrage_header_view, listView, false);
        item3.bind(this, new BarrageHeader("Some Name", BarrageHeader.STATUS_DRAFT, 160, 12));
        listView.addHeaderView(item3, null, true);*/
        
        setListAdapter(new BarrageListAdapter(this, null, true));
        
        if (savedInstanceState != null) {
        	//do something
        }
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (position == 0) {
			composeNewBarrage();
		}
		else if (v instanceof BarrageHeaderView) {
			BarrageHeaderView bhv = (BarrageHeaderView)v;
			BarrageHeader bh = bhv.getHeader();
			
			editBarrage(bh.getID());
		}
	}

	private void editBarrage(int ID) {
		Intent edit = new Intent(this, Compose.class);
		edit.putExtra("id", ID);
		startActivity(edit);
	}

	private void composeNewBarrage() {
		Intent composeBarrage = new Intent(this, Compose.class);
		startActivity(composeBarrage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		
		menu.add(0, MENU_COMPOSE_NEW_BARRAGE, 0, R.string.menu_new_barrage).setIcon(R.drawable.compose);
		if ((mCursor != null) && (mCursor.getCount() > 0)) {
			menu.add(0, MENU_PAUSE_ALL, 0, R.string.menu_pause_all);//.setIcon(...)
			menu.add(0, MENU_DELETE_ALL, 0, R.string.menu_delete_all).setIcon(R.drawable.delete);
		}
		menu.add(0, MENU_PREFERENCES, 0, R.string.menu_preferences).setIcon(R.drawable.preferences);
		menu.add(0, MENU_ABOUT, 0, R.string.menu_about);//.setIcon(...)
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_COMPOSE_NEW_BARRAGE:
				composeNewBarrage();
				break;
			case MENU_PAUSE_ALL:
				doPauseAll();
				break;
			case MENU_DELETE_ALL:
				confirmDeleteDialog(new DeleteThreadListener(-1), true);
				break;
			case MENU_PREFERENCES:
				Intent preferences = new Intent(this, Preferences.class);
				startActivityIfNeeded(preferences, -1);
				break;
			case MENU_ABOUT:
				Intent about = new Intent(this, About.class);
				startActivityIfNeeded(about, -1);
				break;
			default:
				return true;
		}
		return false;
	}
	private void doPauseAll() {
		//TODO: pause all
		return;
	}

	private void confirmDeleteDialog(OnClickListener listener, boolean deleteAll) {
		new AlertDialog.Builder(this)
			.setTitle(R.string.confirm_delete_title)
			.setCancelable(true)
			.setPositiveButton(R.string.yes, listener)
			.setNegativeButton(R.string.no, null)
			.setMessage(deleteAll ? R.string.confirm_delete_all : R.string.confirm_delete)
			.show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.clear();
		
		menu.add(0, MENU_EDIT, 0, R.string.menu_edit);
		menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
		menu.add(0, MENU_DELETE, 0, R.string.menu_delete);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_EDIT:
				break;
			case MENU_PAUSE:
				break;
			case MENU_DELETE:
				break;
			default:
				return true;
		}
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	private class DeleteThreadListener implements OnClickListener {
		private long mID;
		
		public DeleteThreadListener(long ID) {
			mID = ID;
			
			if (ID != -1) {
				
			}
			else {
				
			}
		}
		
		public void onClick(DialogInterface dialog, int whichButton) {
			
		}
	}
}
