package com.anyho.coolreader.atys;

import java.io.File;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.anyho.coolreader.R;
import com.anyho.coolreader.adapter.FileTreeListAdapter;
import com.anyho.coolreader.entity.Book;
import com.anyho.coolreader.tree.AbsTree;
import com.anyho.coolreader.tree.FileTree;
import com.anyho.coolreader.tree.RootTree;
import com.anyho.coolreader.util.PathUtils;
import com.anyho.coolreader.util.UIUtils;

public class FileListAty extends ListActivity
{
	private RootTree rootTree;
	private AbsTree currentTree;
	private FileTreeListAdapter fileAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootTree = new RootTree();
		addChild(PathUtils.fileSaveDirectory(),
				getString(R.string.file_tree_coolreader_title),
				getString(R.string.file_tree_coolreader_summary));
		addChild("/", getString(R.string.file_tree_root_title),
				getString(R.string.file_tree_root_summary));
		addChild(PathUtils.cardDirectory(),
				getString(R.string.file_tree_sdcard_title),
				getString(R.string.file_tree_sdcard_summary));
		
		fileAdapter = new FileTreeListAdapter(this);
		fileAdapter.replaceAll(rootTree.getSubTrees());
		
	}
	
	/**
	 * ���ڵ�����ӽڵ�
	 * 
	 * @param path
	 *            �ļ�·��
	 * @param title
	 *            �ӽڵ�ı���
	 * @param summary
	 *            �ӽڵ�ļ��
	 */
	private void addChild(String path, String title, String summary)
	{
		new FileTree(rootTree, new File(path), title, summary);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		FileTree selected = (FileTree) fileAdapter.getItem(position);
		switch (selected.getOpenStatus())
		{
			case CAN_NOT_OPEN:
				UIUtils.showToastMessage(this, R.string.permission_denied);
				break;
			case READY_TO_OPEN_DIRECTORY:
				openInternalTree(selected);
				break;
			case READY_TO_OPEN_FILE:
				openFile(selected.getFile());
				break;
			
			default:
				break;
		}
	}
	
	private void openInternalTree(final FileTree tree)
	{
		currentTree = tree;
		UIUtils.runWithMessage(this, R.string.openning, new Runnable() {
			@Override
			public void run()
			{
				tree.waitForOpenTree();
			}
		}, new Runnable() {
			@Override
			public void run()
			{
				fileAdapter.replaceAll(tree.getSubTrees());
			}
		});
	}
	
	private void openFile(File file)
	{
		String path = "file:" + file.getPath();
		String fileName = file.getName();
		String name = fileName.substring(0, fileName.indexOf("."));
		Book book = new Book(name, path);
		Intent toRead = new Intent(this, CoolReaderAty.class);
		toRead.putExtra(CoolReaderAty.EXTRA_BOOK, book);
		startActivity(toRead);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (currentTree != null)
			{
				AbsTree parent = currentTree.getParent();
				if (parent != null)
				{
					fileAdapter.replaceAll(parent.getSubTrees());
					currentTree = parent;
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
