package vn.dwork.mangayou.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.MangaChapter;
import us.cuongpnh.mobile.library.mangayou.utils.Common;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.activity.MangaChapterViewerActivity;
import vn.dwork.mangayou.adapter.MangaChapterAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;

public class MangaInfoChapterFragment extends BaseFragment {

	private View mView = null;
	private String mComicId = null;
	public static HashMap<String, Integer> mMainMenuHolder = null;
	private ListView lvMangaChapter = null;
	private ApiClient mApiClient = null;
	private MangaChapterAdapter mMangaChapterAdapter = null;
	private ArrayList<MangaChapter> mMangaChapterList = null;
	private RelativeLayout rlMangaInfoChapterLoading = null;
	private TextView tvMangaInfoSummaryNumChapter = null;

	public static MangaInfoChapterFragment newInstance(String sourceId) {
		MangaInfoChapterFragment fragment = new MangaInfoChapterFragment();
		Bundle args = new Bundle();
		args.putString(Param.COMIC_ID, sourceId);
		fragment.setArguments(args);
		return fragment;

	}

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (!validate()) {
			Toast.makeText(mActivity, getString(R.string.error_invalid_parameter), Toast.LENGTH_LONG).show();
			return null;
		}
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_manga_info_chapter, container, false);
			initView();
			initViewEvent();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			parent.removeView(mView);
		}

		return mView;
	}

	private boolean validate() {
		if (getArguments() == null) {
			return false;
		}
		if (!getArguments().containsKey(Param.COMIC_ID)) {
			return false;
		}
		mComicId = getArguments().getString(Param.COMIC_ID, null);
		if (mComicId == null) {
			return false;
		}
		return true;
	}

	private void initView() {
		lvMangaChapter = (ListView) mView.findViewById(R.id.lvMangaChapter);
		rlMangaInfoChapterLoading = (RelativeLayout) mView.findViewById(R.id.rlMangaInfoChapterLoading);
		tvMangaInfoSummaryNumChapter = (TextView) mActivity.findViewById(R.id.tvMangaInfoSummaryNumChapter);
	}

	private void initViewEvent() {
		// It will be shown only if listview total height is 4x or more bigger
		// than listview's visible height
		lvMangaChapter.setFastScrollEnabled(true);
		lvMangaChapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String mangaChapterListJson = Common.toJson(mMangaChapterList);
				Intent i = new Intent(mActivity, MangaChapterViewerActivity.class);
				i.putExtra(Param.SLUG, mMangaChapterList.get(position).getId());
				i.putExtra(Param.CHAPTER_LIST, mangaChapterListJson);
				i.putExtra(Param.CHAPTER_CURRENT_INDEX, position);
				mActivity.startActivity(i);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initData(Bundle savedInstanceState) {
		mApiClient = new ApiClient(mActivity);

		if (savedInstanceState != null) {
			/*
			 * Power off -> on
			 */
			String mangaChapterList = savedInstanceState.getString("mMangaChapterList");
			mMangaChapterList = (ArrayList<MangaChapter>) Common.fromJson(mangaChapterList,
					new TypeToken<ArrayList<MangaChapter>>() {
					}.getType());

		} else {
			if (lvMangaChapter.getAdapter() != null) {
				/*
				 * Handler for press back button event
				 */
				mMangaChapterAdapter = (MangaChapterAdapter) lvMangaChapter.getAdapter();
				mMangaChapterList = (ArrayList<MangaChapter>) mMangaChapterAdapter.getItem();
			} else {
				/*
				 * First loading time
				 */
				mMangaChapterList = new ArrayList<MangaChapter>();
				mMangaChapterAdapter = new MangaChapterAdapter(mActivity, mMangaChapterList);
				lvMangaChapter.setAdapter(mMangaChapterAdapter);
			}
		}

		getChapterList();
	}

	private void getChapterList() {
		RequestParams params = new RequestParams();
		params.put(Param.COMIC_ID, mComicId);
		mApiClient.getMangaChapterList(params, new IApiResponse<ArrayList<MangaChapter>>() {

			@Override
			public void onSuccess(ArrayList<MangaChapter> apiResponse) {
				rlMangaInfoChapterLoading.setVisibility(View.GONE);
				if (mMangaChapterAdapter == null || mMangaChapterList == null) {
					return;
				}
				mMangaChapterList.addAll(apiResponse);
				mMangaChapterAdapter.notifyDataSetChanged();

				if (tvMangaInfoSummaryNumChapter != null) {
					tvMangaInfoSummaryNumChapter.setText(String.valueOf(apiResponse.size()));
				}
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
				rlMangaInfoChapterLoading.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("mMangaChapterList", Common.toJson(mMangaChapterList));
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

}
