package com.anyho.coolreader.net;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.anyho.coolreader.util.PathUtils;

public class NetBooksService
{
	private List<NetBook> books;
	
	public NetBooksService(List<NetBook> books)
	{
		this.books = books;
	}
	
	private final String Url_Book = "http://192.168.1.105:8080/coolreader/books";
	private final String Key_Data = "data";
	private final String Key_Name = "name";
	private final String Key_Url = "url";
	
	public void execute()
	{
		// {"data":
		// [{"name":"wish.txt","url":"http://localhost:8080/coolreader/books/wish.txt"},{"name":"xiaohua.txt","url":"http://localhost:8080/coolreader/books/xiaohua.txt"}]}}
		NetService service = new NetService(Url_Book);
		String result = service.execute();
		if (result == null)
			return;
		Log.e("NetBooksService", result);
		try
		{
			JSONObject jObj = new JSONObject(result);
			JSONArray jArray = jObj.getJSONArray(Key_Data);
			for (int i = 0; i < jArray.length(); i++)
			{
				JSONObject jItem = jArray.getJSONObject(i);
				String name = jItem.getString(Key_Name);
				String url = jItem.getString(Key_Url);
				NetBook book = new NetBook(name, url);
				if (existLocal(book))
				{
					book.setDownload(true);
				}
				books.add(book);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean existLocal(NetBook book)
	{
		String name = book.getName();
		File file = new File(PathUtils.fileSaveDirectory() + "/" + name);
		return file.exists();
	}
}
