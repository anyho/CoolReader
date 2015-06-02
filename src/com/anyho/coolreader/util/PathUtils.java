package com.anyho.coolreader.util;

import android.os.Environment;

public abstract class PathUtils
{
	public static String cardDirectory()
	{
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	public static String cardCachedDirectory()
	{
		return cardBaseDirectory() + "/cached";
	}
	
	public static String cardBaseDirectory()
	{
		return cardDirectory() + "/coolreader";
	}
	
	public static String fileSaveDirectory()
	{
		return cardBaseDirectory() + "/files";
	}
}
