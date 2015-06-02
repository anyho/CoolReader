package com.anyho.coolreader.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anyho.coolreader.CoolReaderApp;
import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;
import com.anyho.coolreader.atys.UserPreferenceAty;

public class BottomMenuWindow extends PopupWindow implements
		View.OnClickListener
{
	private TextView tvSwitchStatus, tvChangeFont, tvBookmark, tvNavigation,
			tvMore;
	
	public BottomMenuWindow(Activity activity, RelativeLayout root)
	{
		super(activity, root);
	}
	
	private final String TAG = BottomMenuWindow.class.getSimpleName() + ">>>";
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.tvSwitchStatus:
				Log.e(TAG, "tvSwitchStatus");
				performSwitchStatus();
				break;
			case R.id.tvChangeFont:
				Log.e(TAG, "tvChangeFont");
				showToolWindow(BottomWindowManager.KEY_TOOL_FONT);
				break;
			case R.id.tvBookmark:
				Log.e(TAG, "tvBookmark");
				showToolWindow(BottomWindowManager.KEY_TOOL_BOOKMARK);
				break;
			case R.id.tvNavigation:
				Log.e(TAG, "tvNavigation");
				showToolWindow(BottomWindowManager.KEY_TOOL_NAVIGATION);
				break;
			case R.id.tvMore:
				Log.e(TAG, "tvMore");
				performMore();
				break;
			
			default:
				break;
		}
	}
	
	public void showToolWindow(String key)
	{
		final BottomWindowManager manager = BottomWindowManager
				.obtainInstance();
		manager.toggleWindow(key);
	}
	
	private void performSwitchStatus()
	{
		final OptionConfig config = OptionConfig.obtainInstance();
		if (config.DAY)
		{
			config.setDayOption(false);
			setBackgroundResource(R.drawable.tmall_bar_bg);
			tvSwitchStatus.setText(R.string.day);
		}
		else
		{
			config.setDayOption(true);
			setBackgroundResource(R.drawable.titlebar_big);
			tvSwitchStatus.setText(R.string.night);
		}
		postInvalidate();
		CoolReaderApp.obtainInstance().clearAndRepaint();
	}
	
	private void performMore()
	{
		Intent toSetting = new Intent(mActivity, UserPreferenceAty.class);
		mActivity.startActivity(toSetting);
	}
	
	@Override
	protected View createWindowView()
	{
		final LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.window_bottom_menu, this,
				true);
		tvSwitchStatus = (TextView) view.findViewById(R.id.tvSwitchStatus);
		tvChangeFont = (TextView) view.findViewById(R.id.tvChangeFont);
		tvBookmark = (TextView) view.findViewById(R.id.tvBookmark);
		tvNavigation = (TextView) view.findViewById(R.id.tvNavigation);
		tvMore = (TextView) view.findViewById(R.id.tvMore);
		tvSwitchStatus.setOnClickListener(this);
		tvChangeFont.setOnClickListener(this);
		tvBookmark.setOnClickListener(this);
		tvNavigation.setOnClickListener(this);
		tvMore.setOnClickListener(this);
		final OptionConfig config = OptionConfig.obtainInstance();
		if (config.DAY)
		{
			view.setBackgroundResource(R.drawable.titlebar_big);
			tvSwitchStatus.setText(R.string.night);
		}
		else
		{
			view.setBackgroundResource(R.drawable.tmall_bar_bg);
			tvSwitchStatus.setText(R.string.day);
		}
		return view;
	}
	
}
