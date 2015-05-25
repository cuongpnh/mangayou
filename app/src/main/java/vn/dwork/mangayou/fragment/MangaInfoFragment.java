package vn.dwork.mangayou.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.Manga;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.activity.MainActivity;
import vn.dwork.mangayou.adapter.MangaInfoAdapter;
import vn.dwork.mangayou.utils.Utils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.loopj.android.http.RequestParams;

public class MangaInfoFragment extends BaseFragment implements TabListener {
	public static String TAG = MangaInfoFragment.class.toString();
	private String mSlug = null;
	private View mView = null;
	private ActionBar mActionBar = null;
	private ViewPager mViewPager = null;
	private PagerAdapter mViewPagerAdapter = null;
	private ArrayList<String> mTabs = null;
	private ApiClient mApiClient = null;
	private int mCurrentTab = 0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (!validate()) {
			Toast.makeText(mActivity, getString(R.string.error_invalid_parameter), Toast.LENGTH_LONG).show();
			return null;
		}
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_manga_info, container, false);
			initView();
			initViewEvent();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			parent.removeView(mView);
		}

		setTitle();
		return mView;
	}

	private void initData(Bundle savedInstanceState) {
		mApiClient = new ApiClient(mActivity);
		if (savedInstanceState != null) {
			mCurrentTab = savedInstanceState.getInt("mCurrentTab", 0);
			mViewPager.setCurrentItem(mCurrentTab);
		}
		getMangaInfo();
	}

	private void getMangaInfo() {
		RequestParams params = new RequestParams();
		params.put(Param.SLUG, mSlug);
		mApiClient.getMangaById(params, new IApiResponse<Manga>() {

			@Override
			public void onSuccess(Manga apiResponse) {
				mViewPagerAdapter = new MangaInfoAdapter(getFragmentManager(), apiResponse);
				mViewPager.setAdapter(mViewPagerAdapter);
				mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {

						super.onPageSelected(position);
						if (mActionBar == null) {
							if (getActivity() != null) {
								getActivity().invalidateOptionsMenu();
							}

						} else {
							mActionBar.setSelectedNavigationItem(position);
						}
					}
				});
				mActivity.setTitle(apiResponse.getName());
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void initView() {
		mViewPager = (ViewPager) mView.findViewById(R.id.vpManga);
	}

	private void initViewEvent() {

	}

	@Override
	public void onResume() {
		super.onResume();
		final int currentTab = Utils.getInt(mActivity, TAG + "_pager_index", mCurrentTab);
		mViewPager.postDelayed(new Runnable() {

			@Override
			public void run() {
				mViewPager.setCurrentItem(currentTab);
			}
			/*
			 * Little trick here. We will re-select current tab after fragment
			 * set viewpager's tab to 0
			 */
		}, 500);
		if (mActionBar == null) {
			mActionBar = ((MainActivity) getActivity()).getSupportActionBar();
			mTabs = new ArrayList<String>(Arrays.asList(getString(R.string.common_summary),
					getString(R.string.common_chappters)));
			if (mActionBar != null) {
				mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				for (String tab : mTabs) {
					Tab newTab = mActionBar.newTab().setText(tab).setTabListener(this);
					mActionBar.addTab(newTab);
				}
			}
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	@Override
	protected void setTitle() {
		super.setTitle();
		mActivity.setTitle(getString(R.string.manga_is_loading));
	}

	private boolean validate() {
		if (getArguments() == null) {
			return false;
		}
		if (!getArguments().containsKey(Param.SLUG)) {
			return false;
		}
		mSlug = getArguments().getString(Param.SLUG, null);
		if (mSlug == null) {
			return false;
		}
		return true;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mViewPager != null) {
			mViewPager.setCurrentItem(tab.getPosition());
			mCurrentTab = tab.getPosition();
			Utils.storeInt(mActivity, TAG + "_pager_index", mCurrentTab);
			Log.d("mCurrentTab", "mCurrentTab: " + mCurrentTab);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onStop() {
		super.onStop();
		if (mActionBar != null) {
			mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			mActionBar.removeAllTabs();
			mActionBar = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Utils.storeInt(mActivity, TAG + "_pager_index", 0);
	}

}
