package com.anyho.coolreader.net;

public class NetBook
{
	private String name;
	private String url;
	private boolean isDownload;
	
	public NetBook(String name, String url)
	{
		this.name = name;
		this.url = url;
		isDownload = false;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public boolean isDownload()
	{
		return isDownload;
	}
	
	public void setDownload(boolean isDownload)
	{
		this.isDownload = isDownload;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof NetBook)
		{
			return ((NetBook) o).getName().equals(name);
		}
		return super.equals(o);
	}
}
