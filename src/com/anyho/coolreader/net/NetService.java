package com.anyho.coolreader.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetService
{
	private String mUrl;
	
	public NetService(String url)
	{
		mUrl = url;
	}
	
	private final int ConnTimeOut = 8000;
	private final int ReadTimeOut = 5000;
	
	public String execute()
	{
		try
		{
			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(ConnTimeOut);
			conn.setReadTimeout(ReadTimeOut);
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String readLine = null;
			while ((readLine = br.readLine()) != null)
			{
				sb.append(readLine);
			}
			br.close();
			is.close();
			return sb.toString();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
