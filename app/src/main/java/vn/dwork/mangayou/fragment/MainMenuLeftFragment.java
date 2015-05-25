package vn.dwork.mangayou.fragment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import vn.dwork.mangayou.R;
import vn.dwork.mangayou.adapter.MainMenuAdapter;
import vn.dwork.mangayou.model.MainMenuModel;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainMenuLeftFragment extends ListFragment {
	public static int mCurrentMenu = 0;
	private View mView = null;
	private ViewPager mPager = null;
	private static int PAGE_INDEX = 1;
	private RelativeLayout rlLocationMainMenu = null;
	private ArrayList<MainMenuModel> mMainMenu = null;
	private TextView tvCurrentLocationMainMenu = null;
	public static HashMap<String, Integer> mMainMenuHolder = null;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_menu_left, null);
		mPager = (ViewPager) getActivity().findViewById(R.id.vpMainMenu);
		tvCurrentLocationMainMenu = (TextView) mView.findViewById(R.id.tvCurrentMangaSourceMainMenu);
		String mangaSourceName = Utils.getString(getActivity(), Config.PREFS_MANGA_SOURCE_NAME,
				Config.PREFS_DEFAULT_MANGA_SOURCE_NAME);
		tvCurrentLocationMainMenu.setText(mangaSourceName);
		rlLocationMainMenu = (RelativeLayout) mView.findViewById(R.id.rlLocationMainMenu);
		rlLocationMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(PAGE_INDEX, true);
			}
		});
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mMainMenu = new ArrayList<MainMenuModel>();
		mMainMenu.add(new MainMenuModel(getResources().getString(R.string.common_home), R.drawable.ic_home, "Home"));
		mMainMenu.add(new MainMenuModel(getResources().getString(R.string.common_about), R.drawable.ic_about, "About"));

		MainMenuAdapter adapter = new MainMenuAdapter(getActivity(), mMainMenu);
		setListAdapter(adapter);

		mMainMenuHolder = new HashMap<String, Integer>();
		for (int i = 0; i < mMainMenu.size(); i++) {
			mMainMenuHolder.put(mMainMenu.get(i).getClassName(), i);
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			/*
			 * Replace old fragment with the new one
			 */
			// Checking if we are at the current fragment
			if (mCurrentMenu != position) {
				Fragment newFragment = (Fragment) Utils.getFrament(getActivity(), mMainMenu.get(position)
						.getClassName());
				Utils.replaceFragment(getActivity(), newFragment, R.id.content_frame, null);

				/*
				 * Set the current menu = position after replaced fragment
				 */
				mCurrentMenu = position;
			}

		} catch (java.lang.InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		((SlidingFragmentActivity) getActivity()).getSlidingMenu().toggle();

	};

}
