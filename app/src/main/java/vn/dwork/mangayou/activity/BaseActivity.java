package vn.dwork.mangayou.activity;

import us.cuongpnh.mobile.library.mangayou.Param;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.fragment.MainMenuFragment;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.SearchView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity implements SearchView.OnQueryTextListener,
		SearchView.OnCloseListener {

	private int mTitleRes = 0;
	protected SherlockFragment mFrag = null;
	protected SharedPreferences mPrefs = null;
	protected LocalBroadcastManager mBroadcastManager = null;
	protected SearchView mSearchView = null;
	protected MenuItem mSearchItem = null;
	protected Handler mHanlder = new Handler();
	public String mQuery = null;

	public LocalBroadcastManager getBroadcastManager() {
		if (mBroadcastManager == null) {
			mBroadcastManager = LocalBroadcastManager.getInstance(this);
		}
		return mBroadcastManager;
	}

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);

		mSearchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) mSearchItem.getActionView();

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (null != searchManager) {
			mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}
		mSearchView.setIconifiedByDefault(false);
		setupSearchView();
		return true;
	}

	protected boolean isAlwaysExpanded() {
		return false;
	}

	private void setupSearchView() {

		if (isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
		} else {
			mSearchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
					| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		}

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}
		/*
		 * For opening applications in device. Can use in future
		 */
		// if (searchManager != null) {
		// List<SearchableInfo> searchables = searchManager
		// .getSearchablesInGlobalSearch();
		//
		// SearchableInfo info = searchManager
		// .getSearchableInfo(getComponentName());
		// for (SearchableInfo inf : searchables) {
		// if (inf.getSuggestAuthority() != null
		// && inf.getSuggestAuthority().startsWith("applications")) {
		// info = inf;
		// }
		// }
		// mSearchView.setSearchableInfo(info);
		// }

		mSearchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		String sortBy = Utils.getString(this, Config.PREFS_SORT_BY, Config.PREFS_DEFAULT_SORT_BY);
		switch (sortBy) {
		case "rank":
			menu.findItem(R.id.sort_by_rank).setChecked(true);
			return true;
		case "name":
			menu.findItem(R.id.sort_by_name).setChecked(true);
			return true;
		case "updated_time":
			menu.findItem(R.id.sort_by_updated_time).setChecked(true);
			return true;
		default:
			menu.findItem(R.id.sort_by_rank).setChecked(true);
			return true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utils.init(this);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		mBroadcastManager = LocalBroadcastManager.getInstance(this);
		setTitle(mTitleRes);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new MainMenuFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (SherlockFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public SharedPreferences getSharedPreferences() {
		return mPrefs;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sort_by_name:
			hanlderSettingSortByChanged("name");
			return true;
		case R.id.sort_by_rank:
			hanlderSettingSortByChanged("rank");
			return true;
		case R.id.sort_by_updated_time:
			hanlderSettingSortByChanged("updated_time");
			return true;
		case R.id.action_refresh:
			if (mSearchView != null) {
				mSearchItem.collapseActionView();
			}
			sendRefreshMangaListBroadcast();
			return true;
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void hanlderSettingSortByChanged(String sortBy) {
		Utils.storeString(this, Config.PREFS_SORT_BY, sortBy);
		Intent intent = new Intent(Config.RECEIVER_REFRESH_MANGA_LIST);
		mBroadcastManager.sendBroadcast(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		Utils.destroy();
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// Toast.makeText(this, "Submited:" + query, Toast.LENGTH_LONG).show();
		/*
		 * Replace fragment home => manga info now. But this case will never
		 * happen in this code=))
		 */
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!newText.isEmpty()) {
			/*
			 * Call broadcast to update whole manga list
			 */
			mQuery = newText;
			Log.d("sendQueryMangaListBroadcast", mQuery);
			sendQueryMangaListBroadcast(mQuery);
		}
		return false;
	}

	@Override
	public boolean onClose() {
		/*
		 * For future purpose
		 */
		// Toast.makeText(getApplication(), "onClose",
		// Toast.LENGTH_LONG).show();
		// sendQueryMangaListBroadcast(null);
		return false;
	}

	private void sendQueryMangaListBroadcast(String query) {
		Intent intent = new Intent(Config.RECEIVER_SEARCH_MANGA_LIST);
		if (query != null) {
			intent.putExtra(Param.QUERY, query);
		}
		getBroadcastManager().sendBroadcast(intent);
	}

	private void sendRefreshMangaListBroadcast() {
		Intent intent = new Intent(Config.RECEIVER_REFRESH_MANGA_LIST);
		mQuery = null;
		mBroadcastManager.sendBroadcast(intent);
	}

}
