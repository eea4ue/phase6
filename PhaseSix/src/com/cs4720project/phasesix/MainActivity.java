package com.cs4720project.phasesix;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//User Projects, Courses, and Tasks can be viewed and managed from here.
// works with activity_my_home

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String userName;
	// Tab titles
	private String[] tabs = { "My Projects", "My Schedule" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Init
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		Intent i = getIntent();
		String fragmentName = i.getStringExtra("fragment");

		userName = i.getStringExtra("USER_ID");
		// Log.e("Test1", "Test1" + fragmentName);
		Log.e("Test2", "Test2" + userName);

		if (fragmentName != null && fragmentName.equals("My Schedule")) {
			viewPager.setCurrentItem(1); // because 'My Schedule' is tab index 1
		}
		Log.d("onCreate", "fired");
		((ScheduleFragment) mAdapter.getItem(1)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.about:
			about();
			return true;
		case R.id.logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		Intent i = getIntent();
		userName = i.getStringExtra("USER_ID");
		Log.d("onTabSelected", "fired");
		viewPager.setCurrentItem(tab.getPosition());
		((ScheduleFragment) mAdapter.getItem(1)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setActivity(this);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void onImageButtonClick(View v) {
		if (viewPager.getCurrentItem() == 1)
			((ScheduleFragment) mAdapter.getItem(viewPager.getCurrentItem()))
					.onImageButtonClick(v);
	}

	public void onProjectClick(View v) {
		if (viewPager.getCurrentItem() == 0)
			((ProjectsFragment) mAdapter.getItem(viewPager.getCurrentItem()))
					.onProjectClick(v);
	}

	public void deleteProjectButton(View v) {
		if (viewPager.getCurrentItem() == 0)
			((ProjectsFragment) mAdapter.getItem(viewPager.getCurrentItem()))
					.deleteProjectButton(v);
	}

	public void about() {
		Toast.makeText(getApplicationContext(), "Pepper DevTeam: Amas, Larsen, Seid",
				Toast.LENGTH_LONG).show();
	}

	public void logout() {
		//set UserID to ""
		userName = "";
		//launch Splash screen
		Intent intent = new Intent(MainActivity.this,
				Splash.class);
		intent.putExtra("USER_ID", userName);
		startActivity(intent);
		
	}

}
