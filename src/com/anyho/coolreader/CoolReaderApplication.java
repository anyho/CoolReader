package com.anyho.coolreader;

import android.app.Application;

/**
 * class CoolReaderApplication extends Application
 */
public class CoolReaderApplication extends Application
{
	private static CoolReaderApplication mApplication;
	
	public static CoolReaderApplication obtainInstance()
	{
		return mApplication;
	}
	
	public CoolReaderApplication()
	{
		mApplication = this;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		new OptionConfig(this);
	}
}
