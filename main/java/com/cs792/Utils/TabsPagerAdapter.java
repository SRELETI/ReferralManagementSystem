package com.cs792.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.cs792.DischargePlanner.Fragment_Assigned;
import com.cs792.DischargePlanner.Fragment_Completed;
import com.cs792.DischargePlanner.Fragment_unAck;

/**
 * Created by sudeelet on 3/18/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new Fragment_unAck();
			case 1:
				return new Fragment_Assigned();
			case 2:
				return new Fragment_Completed();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3; // Number of tabs
	}
}
