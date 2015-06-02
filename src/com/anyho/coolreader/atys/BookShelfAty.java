package com.anyho.coolreader.atys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyho.coolreader.R;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.text.Library;
import com.anyho.coolreader.util.UIUtils;

public class BookShelfAty extends Activity implements OnItemClickListener,
		OnClickListener
{
	private Button btnImport, btnNetlibrary;
	private GridView gvBookShelf;
	private List<Book> books;
	private GridViewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_book_shelf);
		books = new ArrayList<Book>();
		init();
	}
	
	private final String TAG = BookShelfAty.class.getSimpleName() + ">>>";
	
	private void init()
	{
		btnImport = (Button) findViewById(R.id.btnImport);
		btnNetlibrary = (Button) findViewById(R.id.btnNetlibrary);
		btnImport.setOnClickListener(this);
		btnNetlibrary.setOnClickListener(this);
		
		gvBookShelf = (GridView) findViewById(R.id.gvBookShelf);
		registerForContextMenu(gvBookShelf);
		adapter = new GridViewAdapter(this, R.layout.grid_view_cell);
		gvBookShelf.setAdapter(adapter);
		gvBookShelf.setOnItemClickListener(this);
		Library.obtainInstance().setOnLibraryChangedCallback(
				onLibraryChangedCallback);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		UIUtils.runWithMessage(this, "正在加载本地书库···", new Runnable() {
			@Override
			public void run()
			{
				loadBooks();
			}
		}, new Runnable() {
			@Override
			public void run()
			{
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void loadBooks()
	{
		List<Book> resentBooks = Library.obtainInstance().loadBooks(this);
//		Log.e(TAG, "resentBooks(): " + resentBooks.size());
		List<Book> assetBooks = Library.obtainInstance().loadAssetBooks();
//		Log.e(TAG, "assetBooks(): " + assetBooks.size());
		books.clear();
		if (resentBooks == null || assetBooks == null)
		{
			if (resentBooks != null)
			{
				books.addAll(resentBooks);
			}
			else
			{
				books.addAll(assetBooks);
			}
		}
		else
		{
			books.addAll(resentBooks);
			books.removeAll(assetBooks);
			books.addAll(assetBooks);
		}
		Log.e(TAG, "size(): " + books.size());
	}
	
	private class GridViewAdapter extends ArrayAdapter<Book>
	{
		private int resourceId;
		private Context mContext;
		
		public GridViewAdapter(Context context, int resource)
		{
			super(context, resource);
			mContext = context;
			resourceId = resource;
		}
		
		@Override
		public int getCount()
		{
			return books.size();
		}
		
		@Override
		public Book getItem(int position)
		{
			return books.get(position);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			ViewHolder holder;
			if (view == null)
			{
				view = LayoutInflater.from(mContext).inflate(resourceId, null);
				ImageView cover = (ImageView) view.findViewById(R.id.imgCover);
				TextView title = (TextView) view.findViewById(R.id.tvTitle);
				holder = new ViewHolder();
				holder.bookCover = cover;
				holder.bookTitle = title;
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}
			Book book = getItem(position);
			holder.bookTitle.setText(book.getName());
			return view;
		}
		
		class ViewHolder
		{
			ImageView bookCover;
			TextView bookTitle;
		}
		
	}
	
	private Book selectedBook;
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		selectedBook = adapter.getItem(position);
		Intent intent = new Intent(this, CoolReaderAty.class);
		intent.putExtra(CoolReaderAty.EXTRA_BOOK, selectedBook);
		startActivity(intent);
	}
	
	private final int ID_ITEM_REMOVE = 1;
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		menu.add(0, ID_ITEM_REMOVE, 0, "移除");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = menuInfo.position;
		if (item.getItemId() == ID_ITEM_REMOVE)
		{
			Book selectedBook = adapter.getItem(position);
			if (!selectedBook.isDefault())
			{
				Library.obtainInstance().removeBook(this, selectedBook);
				books.remove(position);
				adapter.notifyDataSetChanged();
			}
			else
			{
				UIUtils.showToastMessage(this, "默认书籍不可移除！");
			}
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btnImport)
		{
			Intent intent = new Intent(this, FileListAty.class);
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(this, NetLibraryAty.class);
			startActivity(intent);
		}
	}
	
	private Library.OnLibraryChangedCallback onLibraryChangedCallback = new Library.OnLibraryChangedCallback() {
		
		@Override
		public void onLibraryChanged(Book book)
		{
			if (selectedBook != null)
			{
				selectedBook.setStartBlockIndex(book.getStartBlockIndex());
				selectedBook.setStartCharOffset(book.getStartCharOffset());
			}
		}
	};
	
}
