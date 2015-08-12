package com.xweisoft.wx.family.ui.sliding;

import com.xweisoft.wx.family.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity
{
	private SlidingMenu mMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);

	}

	public void toggleMenu(View view)
	{
		mMenu.toggle();
	}
}
