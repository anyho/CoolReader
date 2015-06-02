package com.anyho.coolreader.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolReaderSqliteDbHelper extends SQLiteOpenHelper
{
	public CoolReaderSqliteDbHelper(Context context)
	{
		this(context, DB.DB_NAME, DB.DB_VERSION);
	}
	
	public CoolReaderSqliteDbHelper(Context context, String name, int version)
	{
		this(context, name, null, version, null);
	}
	
	public CoolReaderSqliteDbHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler)
	{
		super(context, name, factory, version, errorHandler);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DB.Table_Book.SQL.CREATE_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		
	}
	
}
