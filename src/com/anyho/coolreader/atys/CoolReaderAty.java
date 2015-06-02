package com.anyho.coolreader.atys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.text.Library;
import com.anyho.coolreader.util.UIUtils;
import com.anyho.coolreader.view.BottomMenuWindow;
import com.anyho.coolreader.view.BottomWindowManager;
import com.anyho.coolreader.view.FontToolWindow;
import com.anyho.coolreader.view.NavigationToolWindow;

public class CoolReaderAty extends Activity
{
	static final String TAG = CoolReaderAty.class.getSimpleName() + ">>>";
	public static final String EXTRA_FILE_PATH = "file_path";
	public static final String EXTRA_BOOK = "book";
	/**
	 * 电量
	 */
	private int batteryLevel = 0;
	/**
	 * 电量变化的接受器
	 */
	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context, Intent intent)
		{
			final int level = intent.getIntExtra("level", 100);
			batteryLevel = level;
		};
	};
	private RelativeLayout root;
	
	/**
	 * 获得电量
	 * 
	 * @return
	 */
	public int getBatteryLevel()
	{
		return batteryLevel;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver(batteryChangedReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(batteryChangedReceiver);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.aty_cool_reader);
		root = (RelativeLayout) findViewById(R.id.root);
		dealFileFromIntent(getIntent());
		final BottomWindowManager manager = BottomWindowManager
				.obtainInstance();
		manager.addWindow(BottomWindowManager.KEY_MENU, new BottomMenuWindow(
				this, root));
		manager.addWindow(BottomWindowManager.KEY_TOOL_FONT,
				new FontToolWindow(this, root));
		manager.addWindow(BottomWindowManager.KEY_TOOL_NAVIGATION,
				new NavigationToolWindow(this, root));
		
	}
	
	private Book book;
	
	private void dealFileFromIntent(Intent intent)
	{
		
		final Book selectedBook = (Book) intent
				.getSerializableExtra(EXTRA_BOOK);
		book = selectedBook;
		if (selectedBook != null)
		{
			final CoolReaderApp reader = new CoolReaderApp(this);
			UIUtils.runWithMessage(this, "wait for opening book",
					new Runnable() {
						@Override
						public void run()
						{
							reader.openBook(selectedBook);
						}
					}, new Runnable() {
						@Override
						public void run()
						{
							reader.repaint();
						}
					});
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		if (config.ActiveMenuWithStatus)
		{
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Library.obtainInstance().updateBookStatus(this,book);
		}
		else
		{
			if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
			{
				
			}
			else
			{
				if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				{
					
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
}
