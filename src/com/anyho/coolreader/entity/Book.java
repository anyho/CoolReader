package com.anyho.coolreader.entity;

import java.io.Serializable;

public class Book implements Serializable
{
	private int id;
	private String name;
	private String path;
	private int startBlockIndex;
	private int startCharOffset;
	private boolean isSaved;
	private boolean isDefault;
	
	public Book(String name, String path)
	{
		this(0, name, path, 0, 0, false);
	}
	
	public Book(int id, String name, String path, int startBlockIndex,
			int startCharOffset, boolean isSaved)
	{
		this.id = id;
		this.name = name;
		this.path = path;
		this.startBlockIndex = startBlockIndex;
		this.startCharOffset = startCharOffset;
		this.isSaved = isSaved;
		isDefault = false;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public int getStartBlockIndex()
	{
		return startBlockIndex;
	}
	
	public void setStartBlockIndex(int startBlockIndex)
	{
		this.startBlockIndex = startBlockIndex;
	}
	
	public int getStartCharOffset()
	{
		return startCharOffset;
	}
	
	public void setStartCharOffset(int startCharOffset)
	{
		this.startCharOffset = startCharOffset;
	}
	
	public boolean isSaved()
	{
		return isSaved;
	}
	
	public void setSaved(boolean isSaved)
	{
		this.isSaved = isSaved;
	}
	
	public boolean isDefault()
	{
		return isDefault;
	}
	
	public void setDefault(boolean isDefault)
	{
		this.isDefault = isDefault;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Book)
		{
			Book book = (Book) o;
			return book.getName().equals(name);
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return "Book [id=" + id + ", name=" + name + ", path=" + path
				+ ", startBlockIndex=" + startBlockIndex + ", startCharOffset="
				+ startCharOffset + ", isSaved=" + isSaved + "]";
	}
	
}
