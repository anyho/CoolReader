package com.anyho.coolreader.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.anyho.coolreader.entity.Book;

public class BookDatabase
{
	private CoolReaderSqliteDbHelper helper;
	
	public BookDatabase(Context context)
	{
		helper = new CoolReaderSqliteDbHelper(context);
	}
	
	public List<Book> loadBooks()
	{
		ArrayList<Book> books = null;
		Cursor cursor = helper.getWritableDatabase().rawQuery(
				DB.Table_Book.SQL.QUERY_ALL_BOOK, null);
		if (cursor.moveToFirst())
		{
			books = new ArrayList<Book>();
			Book book = null;
			while (cursor.moveToNext())
			{
				int idIndex = cursor.getColumnIndex(DB.Table_Book.Field.ID);
				int nameIndex = cursor.getColumnIndex(DB.Table_Book.Field.NAME);
				int pathIndex = cursor.getColumnIndex(DB.Table_Book.Field.PATH);
				int startBlockIndex = cursor
						.getColumnIndex(DB.Table_Book.Field.START_BLOCK);
				int startCharIndex = cursor
						.getColumnIndex(DB.Table_Book.Field.START_CHAR);
				int isSavedIndex = cursor
						.getColumnIndex(DB.Table_Book.Field.ISSAVED);
				
				int id = cursor.getInt(idIndex);
				String name = cursor.getString(nameIndex);
				String path = cursor.getString(pathIndex);
				int startBlock = cursor.getInt(startBlockIndex);
				int startChar = cursor.getInt(startCharIndex);
				int savedValue = cursor.getInt(isSavedIndex);
				
				book = new Book(id, name, path, startBlock, startChar,
						savedValue == 1);
				books.add(book);
			}
		}
		return books;
	}
	
	public void updateBook(Book book)
	{
		String sql = DB.Table_Book.SQL.UPDATE_BOOK;
		sql = String.format(sql, book.getName(), book.getPath(),
				book.getStartBlockIndex(), book.getStartCharOffset(),
				book.getId());
		Log.e("updateBook(): ", sql);
		helper.getWritableDatabase().execSQL(sql);
	}
	
	public void addBook(Book book)
	{
		String sql = DB.Table_Book.SQL.ADD_NEW_BOOK;
		sql = String.format(sql, book.getName(), book.getPath(),
				book.getStartBlockIndex(), book.getStartCharOffset(), 1);
		Log.e("addBook(): ", sql);
		helper.getWritableDatabase().execSQL(sql);
	}
	
	public void removeBook(Book book)
	{
		String sql = DB.Table_Book.SQL.REMOVE_BOOK;
		sql = String.format(sql, book.getName());
		Log.e("addBook(): ", sql);
		helper.getWritableDatabase().execSQL(sql);
	}
	
}
