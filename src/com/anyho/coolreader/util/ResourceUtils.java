package com.anyho.coolreader.util;

import com.anyho.coolreader.CoolReaderApplication;

public abstract class ResourceUtils
{
	public static String getString(int resId)
	{
		return CoolReaderApplication.obtainInstance().getString(resId);
	}
	
}
