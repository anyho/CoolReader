package com.anyho.coolreader.view;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class PopupWindow extends LinearLayout
{
	protected Activity mActivity;
	
	protected PopupWindow(Activity activity, RelativeLayout root)
	{
		super(activity);
		mActivity = activity;
		createWindowView();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		root.addView(this, params);
		
		setVisibility(View.GONE);
	}
	
	protected abstract View createWindowView();
	
	public void toggle()
	{
		if (isShow)
		{
			hide();
			isShow = false;
		}
		else
		{
			show();
			isShow = true;
		}
	}
	
	private boolean isShow = false;
	
	protected void show()
	{
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				setVisibility(View.VISIBLE);
			}
		});
	}
	
	protected void hide()
	{
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run()
			{
				setVisibility(View.GONE);
			}
		});
	}
}
