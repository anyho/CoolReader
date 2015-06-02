package com.anyho.coolreader.tree;

import java.io.File;
import java.util.TreeSet;

/**
 * 文件树类，继承自AbsTree
 */
public class FileTree extends AbsTree
{
	private File mFile;
	private String title;
	private String summary;
	
	public FileTree(AbsTree parent, File mFile, String title, String summary)
	{
		super(parent);
		this.mFile = mFile;
		this.title = title;
		this.summary = summary;
		if (!mFile.exists())
		{
			mFile.mkdirs();
		}
	}
	
	public FileTree(AbsTree parent, File file)
	{
		this(parent, file, null, null);
	}
	
	public File getFile()
	{
		return mFile;
	}
	
	@Override
	public String getTreeTitle()
	{
		return title != null ? title : mFile.getName();
	}
	
	@Override
	public String getTreeSummary()
	{
		return summary != null ? summary : "";
	}
	
	@Override
	public Status getOpenStatus()
	{
		if (mFile != null)
		{
			if (mFile.canRead())
			{
				if (mFile.isDirectory())
				{
					return Status.READY_TO_OPEN_DIRECTORY;
				}
				else
				{
					return Status.READY_TO_OPEN_FILE;
				}
			}
			else
			{
				return Status.CAN_NOT_OPEN;
			}
		}
		return null;
	}
	
	
	@Override
	public void waitForOpenTree()
	{
		if(mFile != null)
		{
			TreeSet<File> set = new TreeSet<File>();
			File[] files = mFile.listFiles();
			for (File file : files)
			{
				if(file.isDirectory() || file.getName().endsWith(".txt"))
				{
					set.add(file);
				}
			}
			clear();
			for (File file : set)
			{
				new FileTree(this, file);
			}
		}
	}
}
