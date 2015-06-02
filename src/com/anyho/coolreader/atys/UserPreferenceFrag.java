package com.anyho.coolreader.atys;

import com.anyho.coolreader.OptionConfig;
import com.anyho.coolreader.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class UserPreferenceFrag extends PreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.userpreference);
	}
	@Override
	public void onPause()
	{
		super.onPause();
		OptionConfig.obtainInstance().refreshConfig();
	}
}
