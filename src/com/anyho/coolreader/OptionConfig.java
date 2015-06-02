package com.anyho.coolreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.anyho.coolreader.text.TextModel.Animation;

public class OptionConfig
{
	private static OptionConfig instance;
	
	public static OptionConfig obtainInstance()
	{
		return instance;
	}
	
	private Context mContext;
	
	public OptionConfig(Context context)
	{
		instance = this;
		mContext = context;
		setDayOption(DAY);
		refreshConfig();
	}
	
	public Animation getAnimationType()
	{
		Animation animation = null;
		switch (AnimationType)
		{
			case 0:
				animation = Animation.none;
				break;
			case 1:
				animation = Animation.curl;
				break;
			case 2:
				animation = Animation.slide;
				break;
			case 3:
				animation = Animation.shift;
				break;
			default:
				animation = Animation.slide;
				break;
		}
		return animation;
	}
	
	public int getAnimationSpeed()
	{
		return AnimationSpeed;
	}
	
	public boolean AutoOrientation = false;
	public boolean ActiveMenuWithStatus = true;
	public byte FontSize = 20;
	public byte LineSpace = 10;
	public byte FontFamily = 10;
	public byte LeftMargin = 10;
	public byte TopMargin = 10;
	public byte RightMargin = 10;
	public byte BottomMargin = 10;
	public boolean ShowTimeAtFooter = true;
	public boolean ShowBatteryPercentAtFooter = true;
	public boolean ShowPageNumberAtFooter = true;
	public boolean ScrollingWithVolumnKey = false;
	public byte AnimationType = 0;
	public byte AnimationSpeed = 2;
	public boolean DAY = true;
	private SharedPreferences sp;
	
	public void refreshConfig()
	{
		if (sp == null)
		{
			sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		
		AutoOrientation = sp.getBoolean(
				getString(R.string.pref_autoorientation), false);
		ActiveMenuWithStatus = sp.getBoolean(
				getString(R.string.pref_active_menu_with_status), true);
		FontSize = Byte.parseByte(sp.getString(
				getString(R.string.pref_font_size), "20"));
		FontFamily = Byte.parseByte(sp.getString(
				getString(R.string.pref_font_family), "0"));
		LineSpace = Byte.parseByte(sp.getString(
				getString(R.string.pref_line_space), "10"));
		ShowTimeAtFooter = sp.getBoolean(getString(R.string.pref_show_time),
				true);
		ShowBatteryPercentAtFooter = sp.getBoolean(
				getString(R.string.pref_show_battery_percent), true);
		ShowPageNumberAtFooter = sp.getBoolean(
				getString(R.string.pref_show_page_number), true);
		ScrollingWithVolumnKey = sp.getBoolean(
				getString(R.string.pref_scrolling_with_volumn_key), false);
		AnimationType = Byte.parseByte(sp.getString(
				getString(R.string.pref_animaton), "2"));
		AnimationSpeed = Byte.parseByte(sp.getString(
				getString(R.string.pref_animaton_speed), "1"));
		DAY = sp.getBoolean(PREF_DAY, true);
	}
	
	private final String PREF_DAY = "pref_day";
	public final byte MAX_FONT_SIZE = 30;
	public final byte MIN_FONT_SIZE = 20;
	
	public byte setFontSizeOption(byte fontSize)
	{
		if (fontSize < MIN_FONT_SIZE)
		{
			fontSize = MIN_FONT_SIZE;
		}
		else
		{
			if (fontSize > MAX_FONT_SIZE)
			{
				fontSize = MAX_FONT_SIZE;
			}
		}
		setOptionConfig(getString(R.string.pref_font_size), fontSize + "");
		return fontSize;
	}
	
	public void setDayOption(boolean value)
	{
		DAY = value;
		setOptionConfig(PREF_DAY, value);
	}
	
	private void setOptionConfig(String key, boolean value)
	{
		if (sp == null)
		{
			sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private void setOptionConfig(String key, String value)
	{
		if (sp == null)
		{
			sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private String getString(int resId)
	{
		return mContext.getString(resId);
	}
}
