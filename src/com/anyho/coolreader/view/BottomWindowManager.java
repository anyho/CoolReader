package com.anyho.coolreader.view;

import java.util.HashMap;

import android.util.Log;

public final class BottomWindowManager
{
	private static BottomWindowManager instance;
	
	public static BottomWindowManager obtainInstance()
	{
		return instance == null ? new BottomWindowManager() : instance;
	}
	
	private BottomWindowManager()
	{
		instance = this;
	}
	
	private HashMap<String, PopupWindow> windows = new HashMap<String, PopupWindow>();
	public static final String KEY_MENU = "menu";
	public static final String KEY_TOOL_FONT = "tool_font";
	public static final String KEY_TOOL_BOOKMARK = "tool_bookmark";
	public static final String KEY_TOOL_NAVIGATION = "tool_navigation";
	
	public void addWindow(String key, PopupWindow window)
	{
		windows.put(key, window);
	}
	
	private String currentKey = null;
	private final String TAG = BottomWindowManager.class.getSimpleName()
			+ ">>>";
	
	public void toggleWindow(String key)
	{
		Log.e(TAG, "currentKey: " + currentKey);
		Log.e(TAG, "key: " + key);
		if (currentKey != null && !currentKey.equals(key))
		{
			PopupWindow window = windows.get(currentKey);
			if (window == null)
			{
				currentKey = null;
				return;
			}
			window.toggle();
			currentKey = key;
		}
		else
		{
			currentKey = key;
		}
		final PopupWindow window = windows.get(key);
		if (window != null)
		{
			window.toggle();
		}
	}
}
