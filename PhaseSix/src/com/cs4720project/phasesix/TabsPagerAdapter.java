package com.cs4720project.phasesix;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			//My Projects fragment
			return new ProjectsFragment();
		case 1:
			//My Schedule/Courses fragment
			return new ScheduleFragment();
		case 2:
//			//My Meetings fragment
			return new MeetingsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
