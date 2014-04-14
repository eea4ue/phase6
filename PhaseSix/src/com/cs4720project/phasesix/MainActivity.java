package com.cs4720project.phasesix;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
//import android.app.FragmentTransaction;
//import android.app.FragmentManager;
//import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
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
	private static String logoutURL="http://plato.cs.virginia.edu/~cs4720s14pepper/logout.php";
	// Tab titles

private String[] tabs = { "My Projects", "My Schedule", "My Meetings"};
	protected void onResumeFragments(){
		super.onResumeFragments();
		((MeetingsFragment) mAdapter.getItem(2)).setUser(userName);
		((ScheduleFragment) mAdapter.getItem(1)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setUser(userName);
		if(mAdapter.getCount()>=1){
			if(((ScheduleFragment)mAdapter.getItem(1)).adapter!=null)
				((ScheduleFragment)mAdapter.getItem(1)).adapter.notifyDataSetChanged();
		}
	}
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
		Log.e("Test2", "Test2: " + userName);

		if (fragmentName != null && fragmentName.equals("My Schedule")) {
			viewPager.setCurrentItem(1); // because 'My Schedule' is tab index 1
		}
		Log.d("onCreate", "fired");
		Log.d("MainActivity username",userName);
		if(userName!=null){
		((MeetingsFragment) mAdapter.getItem(2)).setUser(userName);
		((ScheduleFragment) mAdapter.getItem(1)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setUser(userName);
		((ProjectsFragment) mAdapter.getItem(0)).setActivity(this);
		}
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
		((MeetingsFragment) mAdapter.getItem(2)).setUser(userName);
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
		new LogoutTask().execute(logoutURL);
		Log.d("Finishing activity",this.toString());
		((ProjectsFragment)mAdapter.getItem(0)).tempWorkAround=false;
		((ScheduleFragment)mAdapter.getItem(1)).tempWorkAround=false;
		((MeetingsFragment)mAdapter.getItem(2)).tempWorkAround=false;
		((ProjectsFragment)mAdapter.getItem(0)).projectURL = "http://peppernode.azurewebsites.net/project/view/details/";
		((ScheduleFragment)mAdapter.getItem(1)).userViewCoursesURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/user/";
		((ScheduleFragment)mAdapter.getItem(1)).userDeleteCourseURL = "http://plato.cs.virginia.edu/~cs4720s14pepper/user/";
		//mAdapter.popBackStack(mAdapter.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentManager fm = getSupportFragmentManager();//.beginTransaction();
//		fm.remove(((Fragment)mAdapter.getItem(0))).commit();
//		fm.remove(((Fragment)mAdapter.getItem(1))).commit();
//		fm.remove(((Fragment)mAdapter.getItem(2))).commit();
		for(int i=0;i<fm.getBackStackEntryCount();i++)
		fm.popBackStack();
				super.finish();
		this.finish();
	}
	public class LogoutTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... URL) {

			
			for (String logoutURL : URL) {
				HttpClient httpClient = HTTPClients.getDefaultHttpClient();
				try {
					HttpGet httpGet = new HttpGet(logoutURL);
					HttpResponse response = httpClient.execute(httpGet);
					StatusLine searchStatus = response.getStatusLine();

					if (searchStatus.getStatusCode() == 200) {
					//HTTPClients.clearClient();
						
					} else
						Log.d("STATUS CODE ERROR", "!= 200");
				} catch (Exception e) {
					Log.d("Exception", "httpClient");
					e.printStackTrace();
				}

			}
			return "Completed";
		}
		protected void onPostExecute(String result){
			Intent intent = new Intent(MainActivity.this,
					Splash.class);
			
			HTTPClients.clearClient();
			
			//intent.putExtra("USER_ID", "blob");
			startActivity(intent);
			//MainActivity.this.finish();
			
		}
	}
	


}
