package vn.dwork.mangayou.adapter;

import vn.dwork.mangayou.fragment.MainMenuLeftFragment;
import vn.dwork.mangayou.fragment.MainMenuRightFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainMenuSlidePagerAdapter extends FragmentStatePagerAdapter {
	private static int NUM_PAGES = 2;
	public MainMenuSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return new MainMenuLeftFragment();
		case 1:
			return new MainMenuRightFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}
}
