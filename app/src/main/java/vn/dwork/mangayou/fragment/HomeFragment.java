package vn.dwork.mangayou.fragment;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.Manga;
import us.cuongpnh.mobile.library.mangayou.model.MangaSource;
import us.cuongpnh.mobile.library.mangayou.utils.Common;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.adapter.MangaCellAdapter;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

public class HomeFragment extends BaseFragment {
	private View mView = null;
	private GridView mMangaCellGridView = null;
	private MangaCellAdapter mMangaCellAdapter = null;
	private ApiClient mApiClient = null;
	private View mFrameView = null;
	private int mNumColumns = 0;
	private int mImageThumbSize = 0;
	private int mImageThumbSpacing = 0;
	private boolean mIsloading = false;
	private boolean mIsDone = false;
	private int mPage = 0;
	private int mLimit = 0;
	private ArrayList<Manga> mMangaList = null;
	private final Handler mHandler = new Handler();
	private String mBackupMangaSourceId = null;
	private String mBackupMangaSourceSortBy = null;
	private String mQuery = null;
	private String mBackupQuery = null;
	private BroadcastReceiver mRefreshMangaListRecevier = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mQuery = null;
			forceGetMangaList();
		}
	};

	private BroadcastReceiver mSearchMangaListRecevier = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle == null) {
				mQuery = null;
			}
			mQuery = bundle.getString(Param.QUERY);
			Log.d("HomeFragment", mQuery + "");
			if (mQuery.isEmpty()) {
				mQuery = null;
			}

			forceGetMangaList();
		}
	};

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_home, container, false);
			initView();
			initViewEvent();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			parent.removeView(mView);
		}
		setTitle();
		return mView;
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("mMangaList", Common.toJson(mMangaList));

	}

	/*
	 * You can delete this block
	 */
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setRetainInstance(true);
	// }

	@Override
	public void onResume() {
		Log.d("onResume", "onResume");
		super.onResume();
		/*
		 * Using only for register broadcast
		 */
		initReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseReceiver();
	}

	private void releaseReceiver() {
		LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mRefreshMangaListRecevier);
		LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mSearchMangaListRecevier);
	}

	private void initReceiver() {
		LocalBroadcastManager.getInstance(mActivity).registerReceiver((mRefreshMangaListRecevier),
				new IntentFilter(Config.RECEIVER_REFRESH_MANGA_LIST));
		LocalBroadcastManager.getInstance(mActivity).registerReceiver((mSearchMangaListRecevier),
				new IntentFilter(Config.RECEIVER_SEARCH_MANGA_LIST));
	}

	@Override
	protected void setTitle() {
		super.setTitle();
		mActivity.setTitle(getString(R.string.common_home));
	}

	private void initView() {
		mMangaCellGridView = (GridView) mView.findViewById(R.id.gridView);
		mFrameView = (View) mView.findViewById(R.id.flGridview);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	private void initData(Bundle savedInstanceState) {
		Resources r = getResources();
		mLimit = Config.DEFAULT_LIMIT;
		int orientation = getResources().getConfiguration().orientation;
		mNumColumns = (orientation == Configuration.ORIENTATION_PORTRAIT) ? r.getInteger(R.integer.grid_num_columns)
				: r.getInteger(R.integer.grid_num_columns_big);
		mApiClient = new ApiClient(mActivity);
		mImageThumbSize = r.getDimensionPixelSize(R.dimen.grid_column_width);
		mImageThumbSpacing = r.getDimensionPixelSize(R.dimen.image_thumbnail_spacing);
		if (savedInstanceState != null) {
			/*
			 * Power off -> on
			 */
			String mangaList = savedInstanceState.getString("mMangaList");
			mMangaList = (ArrayList<Manga>) Common.fromJson(mangaList, new TypeToken<ArrayList<Manga>>() {
			}.getType());

		} else {
			if (mMangaCellGridView.getAdapter() != null) {
				/*
				 * Handler for press back button event
				 */
				mMangaCellAdapter = (MangaCellAdapter) mMangaCellGridView.getAdapter();
				mMangaList = (ArrayList<Manga>) mMangaCellAdapter.getItem();
				/*
				 * Checking manga source Id if user have changed manga source Id
				 * before. This case use when HomeFragment isn't visible =>
				 * receiver cannot receive event update manga source
				 */

				String mangaSourceId = Utils.getString(mActivity, Config.PREFS_MANGA_SOURCE_ID, null);
				String sortBy = Utils.getString(mActivity, Config.PREFS_SORT_BY, null);

				if ((mangaSourceId != null && mangaSourceId != mBackupMangaSourceId)
						|| (sortBy != null && sortBy != mBackupMangaSourceSortBy)) {
					resetMangaListInfo();
				} else {
					resizeMangaListGridView();
				}

			} else {
				/*
				 * First loading time
				 */
				mMangaList = new ArrayList<Manga>();
				mMangaCellAdapter = new MangaCellAdapter(mActivity, mMangaList, 0);
				mMangaCellGridView.setAdapter(mMangaCellAdapter);
			}
		}
		getMangaList();

	}

	private void forceGetMangaList() {
		resetMangaListInfo();
		getMangaList();
	}

	private void resetMangaListInfo() {
		if (mIsloading) {
			// Toast.makeText(mActivity,
			// mActivity.getString(R.string.common_dont_stress_just_rest),
			// Toast.LENGTH_LONG).show();
		} else {
			mPage = 0;
			mMangaList = new ArrayList<Manga>();
			mMangaCellAdapter = new MangaCellAdapter(mActivity, mMangaList, 0);
			mMangaCellGridView.setAdapter(mMangaCellAdapter);
			mIsDone = false;
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	private void getMangaList() {
		String mangaSourceId = Utils.getString(mActivity, Config.PREFS_MANGA_SOURCE_ID, null);
		if (mangaSourceId != null && mBackupMangaSourceId != null && mBackupMangaSourceId != mangaSourceId) {
			/*
			 * There is some change about manga source. We will reset all manga
			 * info now
			 */
			resetMangaListInfo();
		}
		/*
		 * Null that's mean there is no comic source is selected
		 */
		if (mangaSourceId == null) {
			String mangaSourceSlug = Utils.getString(mActivity, Config.PREFS_MANGA_SOURCE_SLUG,
					Config.PREFS_DEFAULT_MANGA_SOURCE_SLUG);
			RequestParams params = new RequestParams();
			params.put(Param.SLUG, mangaSourceSlug);
			mApiClient.getMangaSourceById(params, new IApiResponse<MangaSource>() {

				@Override
				public void onSuccess(MangaSource apiResponse) {
					Utils.storeMangaSource(mActivity, apiResponse);
					getMangaList(apiResponse.getId());
				}

				@Override
				public void onFailure(String message, int status) {
					Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
					mIsloading = false;
					Utils.hideProgress();
				}
			});
		} else {
			getMangaList(mangaSourceId);
		}
	}

	private boolean canLoadData() {
		/*
		 * Checking if we have enough condition to query data
		 */
		if (mIsDone == true || mIsloading == true) {
			return false;
		}

		/*
		 * !IMPORTANT! This block use to check if the current view have enough
		 * condition to load more content. Only load more when we reached to the
		 * end of list. We should load more content when max(0, list.count - X)
		 * with X is the number below the last visible position so that user
		 * will not have to waiting when load more data => UX
		 */

		/*
		 * BACKUP OLD Strategy if (mPage != 0 && mMangaCellGridView.getAdapter()
		 * != null) { if (mMangaCellGridView.getLastVisiblePosition() !=
		 * mMangaCellGridView.getChildCount() - 1 ||
		 * mMangaCellGridView.getChildAt
		 * (mMangaCellGridView.getAdapter().getCount() - 1).getBottom() >
		 * mMangaCellGridView .getHeight()) { return false; } }
		 */

		if (mPage != 0 && mMangaCellGridView.getAdapter() != null) {
			if (mMangaCellGridView.getLastVisiblePosition() < mMangaCellGridView.getAdapter().getCount()
					- Config.MIN_REMAINING_LOAD_MORE) {
				return false;
			}
		}
		return true;
	}

	private void getMangaList(String mangaSourceId) {
		if (!canLoadData()) {
			return;
		}
		String sortBy = Utils.getString(mActivity, Config.PREFS_SORT_BY, Config.PREFS_DEFAULT_SORT_BY);
		mBackupMangaSourceId = mangaSourceId;
		mBackupMangaSourceSortBy = sortBy;
		RequestParams params = new RequestParams();
		params.put(Param.SOURCE_ID, mangaSourceId);
		params.put(Param.PAGE, mPage);
		params.put(Param.LIMIT, mLimit);
		params.put(Param.SORT_BY, sortBy);
		if (mQuery != null) {
			mBackupQuery = mQuery;
			params.put(Param.QUERY, mQuery);
		}
		mIsloading = true;
		Utils.showProgress(mActivity);
		mApiClient.getMangaList(params, new IApiResponse<ArrayList<Manga>>() {

			@Override
			public void onSuccess(ArrayList<Manga> apiResponse) {
				updateMangaList(apiResponse);
				resizeMangaListGridView();
				if (mBackupQuery != null) {
					Log.d("mBackupQuery", mBackupQuery);
				}
				if (mQuery != null) {
					Log.d("mQuery", mQuery);
				}

				if (mQuery != null && mBackupQuery != null && mBackupQuery != mQuery) {
					forceGetMangaList();
				}
			}

			@Override
			public void onFailure(String message, int status) {
				processRequestFail(message, status);
			}
		});

	}

	private void processRequestFail(String message, int status) {
		Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
		Utils.hideProgress();
		resizeMangaListGridView();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mIsloading = false;
			}
		}, Config.HTTP_INTERVAL_REQUEST_FAIL * 1000);
	}

	private void resizeMangaListGridView() {
		if (mMangaCellAdapter == null) {
			resetMangaListInfo();
		}
		mMangaCellAdapter.setNumColumns(0);
		mMangaCellGridView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (mMangaCellAdapter.getNumColumns() == 0) {
							View f = mFrameView;
							Log.d("mFrameView", mFrameView.getWidth() + "");
							int shortestWidth = f.getWidth() - f.getPaddingLeft() - f.getPaddingRight();
							int usableWidth = shortestWidth - mNumColumns * mImageThumbSpacing;
							int columnWidth = usableWidth / mNumColumns;
							mImageThumbSize = columnWidth;
							int gridWidth = shortestWidth;
							mMangaCellGridView.setPadding(mImageThumbSpacing, mImageThumbSpacing, mImageThumbSpacing,
									mImageThumbSpacing);
							mMangaCellGridView.setColumnWidth(columnWidth);

							Log.d("gridWidth", "gridWidth: " + gridWidth);
							// Now that we have made all the extra adjustments,
							// resize the grid
							// and have it re-do its view one more time.
							LayoutParams lparams = new LinearLayout.LayoutParams(gridWidth,
									LinearLayout.LayoutParams.MATCH_PARENT);

							mMangaCellGridView.setLayoutParams(lparams);
							mMangaCellGridView.setNumColumns(mNumColumns);
							mMangaCellAdapter.setNumColumns(mNumColumns);
							mMangaCellAdapter.setItemHeight(mImageThumbSize);
						}
					}
				});
		initScrollForMangaList();
		mMangaCellGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putString(Param.SLUG, mMangaList.get(position).getSlug());
				Fragment mangaInfoFragment = new MangaInfoFragment();
				Utils.replaceFragment(getActivity(), mangaInfoFragment, R.id.content_frame, bundle);
			}
		});
	}

	private void initScrollForMangaList() {
		mMangaCellGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				getMangaList();
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/*
				 * Nothing to do here right now
				 */
			}
		});
	}

	private void initViewEvent() {

	}

	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		int orientation = getResources().getConfiguration().orientation;
		Resources r = getResources();
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			Log.d("onConfigurationChanged", "ORIENTATION_LANDSCAPE");
			mNumColumns = r.getInteger(R.integer.grid_num_columns_big);
		} else {
			Log.d("onConfigurationChanged", "ORIENTATION_PORTRAIT");
			mNumColumns = r.getInteger(R.integer.grid_num_columns);
		}

		mFrameView.post(new Runnable() {
			@Override
			public void run() {
				/*
				 * We use post to make sure that mFrameView have got new
				 * width/height
				 */
				resizeMangaListGridView();
			}
		});

	}

	private void updateMangaList(ArrayList<Manga> manga) {
		if (manga != null) {
			if (manga.size() < Config.DEFAULT_LIMIT) {
				mIsDone = true;
			}
			if (mMangaCellAdapter != null) {
				mMangaList.addAll(manga);
				mMangaCellAdapter.notifyDataSetChanged();
			}
			mPage++;
		} else {
			mIsDone = true;
		}
		mIsloading = false;
		Utils.hideProgress();
	}

}
