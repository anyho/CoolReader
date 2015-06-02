package com.anyho.coolreader.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.anyho.coolreader.CoolReaderApplication;
import com.anyho.coolreader.db.BookDatabase;
import com.anyho.coolreader.entity.Book;

public class Library
{
	private static Library instance;
	
	public static Library obtainInstance()
	{
		return instance == null ? new Library() : instance;
	}
	
	private Library()
	{
		instance = this;
	}
	
	public List<Book> loadAssetBooks()
	{
		AssetManager am = CoolReaderApplication.obtainInstance().getAssets();
		ArrayList<Book> books = null;
		try
		{
			String[] bookStrings = am.list("book");
			if (bookStrings != null && bookStrings.length > 0)
			{
				books = new ArrayList<Book>();
				Book book = null;
				for (String string : bookStrings)
				{
					String name = string.substring(0, string.indexOf("."));
					book = new Book(name, "asset:book/" + string);
					book.setDefault(true);
					books.add(book);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return books;
	}
	
	public List<Book> loadBooks(Context context)
	{
		BookDatabase bookDatabase = new BookDatabase(context);
		return bookDatabase.loadBooks();
	}
	
	public void updateBookStatus(Context context, Book book)
	{
		if (onLibraryChangedCallback != null)
		{
			onLibraryChangedCallback.onLibraryChanged(book);
		}
		BookDatabase bookDatabase = new BookDatabase(context);
		if (book.isSaved())
		{
			bookDatabase.updateBook(book);
		}
		else
		{
			bookDatabase.addBook(book);
		}
	}
	
	public void removeBook(Context context, Book book)
	{
		BookDatabase bookDatabase = new BookDatabase(context);
		bookDatabase.removeBook(book);
	}
	
	private OnLibraryChangedCallback onLibraryChangedCallback;
	
	public void setOnLibraryChangedCallback(
			OnLibraryChangedCallback onLibraryChangedCallback)
	{
		this.onLibraryChangedCallback = onLibraryChangedCallback;
	}
	
	public interface OnLibraryChangedCallback
	{
		void onLibraryChanged(Book book);
	}
}
