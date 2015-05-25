package vn.dwork.mangayou.activity;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.MangaChapter;
import us.cuongpnh.mobile.library.mangayou.utils.Common;
import us.cuongpnh.mobile.library.mangayou.utils.Utils;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.adapter.FullScreenImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

public class MangaChapterViewerActivity extends SherlockFragmentActivity {

	private PagerAdapter mAdapter = null;
	private String mChapterId = null;
	private ViewPager mViewPager = null;
	private ApiClient mApiClient = null;
	private ArrayList<String> mImagePaths = null;
	private ArrayList<MangaChapter> mMangaChapterList = null;
	private int mCurrentIndex = 0;
	private MenuItem mMenuChapterPrevious = null;
	private MenuItem mMenuChapterNext = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (!validate(intent)) {
			finish();
			return;
		}
		setContentView(R.layout.activity_manga_chapter_viewer);
		initView();
		initViewEvent();
		initData();

	}

	@SuppressWarnings("unchecked")
	private boolean validate(Intent intent) {
		if (intent == null) {
			Toast.makeText(this, getString(R.string.error_invalid_parameter), Toast.LENGTH_LONG).show();
			return false;
		}
		mCurrentIndex = intent.getIntExtra(Param.CHAPTER_CURRENT_INDEX, 0);
		mChapterId = intent.getStringExtra(Param.SLUG);
		if (mChapterId == null) {
			Toast.makeText(this, getString(R.string.error_chapter_not_found), Toast.LENGTH_LONG).show();
			return false;
		}

		String tmpMangaChapterList = intent.getStringExtra(Param.CHAPTER_LIST);
		if (tmpMangaChapterList == null) {
			Toast.makeText(this, getString(R.string.error_chapter_list_not_found), Toast.LENGTH_LONG).show();
			return false;
		}

		mMangaChapterList = (ArrayList<MangaChapter>) Common.fromJson(tmpMangaChapterList,
				new TypeToken<ArrayList<MangaChapter>>() {
				}.getType());
		if (mMangaChapterList == null) {
			Toast.makeText(this, getString(R.string.error_invalid_parameter), Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vpMangaChapter);
	}

	private void initViewEvent() {
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				getSupportActionBar().hide();
			}
		});
	}

	private void initData() {
		mApiClient = new ApiClient(getActivity());
		getMangaChapter();
	}

	private void getMangaChapter() {
		RequestParams params = new RequestParams();
		params.put(Param.SLUG, mChapterId);
		Utils.showProgress(this);
		mApiClient.getMangaChapterById(params, new IApiResponse<MangaChapter>() {

			@Override
			public void onSuccess(MangaChapter apiResponse) {
				mImagePaths = apiResponse.getPages();
				mAdapter = new FullScreenImageAdapter(getActivity(), mImagePaths);
				mViewPager.setAdapter(mAdapter);
				mViewPager.setOffscreenPageLimit(2);
				// mViewPager.setCurrentItem(0);
				getActivity().setTitle(apiResponse.getName());
				Utils.hideProgress();

			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				Utils.hideProgress();
				finish();
			}
		});
	}

	private Activity getActivity() {
		return this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.chapter, menu);
		initMenuButton(menu);
		return true;
	}

	private void initMenuButton(Menu menu) {

		initMenuView(menu);
		initMenuState();
	}

	private void initMenuView(Menu menu) {
		mMenuChapterPrevious = menu.findItem(R.id.action_chapter_previous);
		mMenuChapterNext = menu.findItem(R.id.action_chapter_next);
	}

	private void initMenuState() {
		if (mCurrentIndex == 0) {
			mMenuChapterNext.setEnabled(false);

		} else {
			mMenuChapterNext.setEnabled(true);
		}

		if (mCurrentIndex == (mMangaChapterList.size() - 1)) {
			mMenuChapterPrevious.setEnabled(false);
		} else {
			mMenuChapterPrevious.setEnabled(true);
		}
	}

	public void handlerForChapterNavigation(boolean isPrevious) {
		mCurrentIndex = isPrevious ? mCurrentIndex + 1 : mCurrentIndex - 1;
		mChapterId = mMangaChapterList.get(mCurrentIndex).getId();
		initMenuState();
		getMangaChapter();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_chapter_previous:
			handlerForChapterNavigation(true);
			return true;
		case R.id.action_chapter_next:
			handlerForChapterNavigation(false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
