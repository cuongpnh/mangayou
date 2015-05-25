package vn.dwork.mangayou.fragment;

import vn.dwork.mangayou.R;
import vn.dwork.mangayou.adapter.MainMenuSlidePagerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainMenuFragment extends BaseFragment {
	private ViewPager mPager = null;
	private PagerAdapter mPagerAdapter = null;
	private View mView = null;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_menu, null);
		mPager = (ViewPager) mView.findViewById(R.id.vpMainMenu);
		mPagerAdapter = new MainMenuSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.

				/*
				 * !IMPORTANT! Only use this line below when you want to update
				 * the option menu when change
				 */

				// getActivity().invalidateOptionsMenu();
			}
		});
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}
