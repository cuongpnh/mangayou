package vn.dwork.mangayou.fragment;

import android.app.Activity;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment {
	public static String TRACK_MENU = "trackMenu";
	protected boolean mTrackMenu = true;
	protected Activity mActivity = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		Log.d("BaseFragment", "onAttach");
		super.onAttach(activity);
		mActivity = activity;
	}

	protected void setTitle() {
		/*
		 * IMPORTANT! For tracking fragment history.
		 */
		if (getArguments() != null) {
			if (getArguments().containsKey(BaseFragment.TRACK_MENU)) {
				mTrackMenu = getArguments().getBoolean(BaseFragment.TRACK_MENU);
			}
		}
		if (mTrackMenu) {
			if (MainMenuLeftFragment.mMainMenuHolder != null) {
				String menuName = getClass().getSimpleName().toString().replace("Fragment", "");
				if (MainMenuLeftFragment.mMainMenuHolder.containsKey(menuName)) {
					int index = MainMenuLeftFragment.mMainMenuHolder.get(menuName);
					MainMenuLeftFragment.mCurrentMenu = index;
				} else {
					MainMenuLeftFragment.mCurrentMenu = -1;
				}
			}
		}
	}
}
