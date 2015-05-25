package vn.dwork.mangayou.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.model.Manga;
import us.cuongpnh.mobile.library.mangayou.model.MangaCategory;
import us.cuongpnh.mobile.library.mangayou.utils.Common;
import vn.dwork.mangayou.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.gson.reflect.TypeToken;

public class MangaInfoSummaryFragment extends BaseFragment {

	private View mView = null;
	private Manga mManga = null;
	public static HashMap<String, Integer> mMainMenuHolder = null;
	private TextView tvMangaInfoSummaryAuthor = null;
	private TextView tvMangaInfoSummaryCategories = null;
	private TextView tvMangaInfoSummaryDescription = null;
	@SuppressWarnings("unused")
	private TextView tvMangaInfoSummaryNumChapter = null;
	private TextView tvMangaInfoSummaryRanking = null;
	private TextView tvMangaInfoSummaryStatus = null;
	private ImageView ivMangaInfoSummaryThumb = null;
	private ProgressBar pbMangaInfoSummaryThumbProgress = null;
	private RelativeLayout rlMangaInfoSummaryloading = null;
	private AQuery mAq = null;

	public static MangaInfoSummaryFragment newInstance(Manga manga) {
		MangaInfoSummaryFragment fragment = new MangaInfoSummaryFragment();
		Bundle args = new Bundle();
		args.putString(Param.MANGA, Common.toJson(manga));
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
			mView = inflater.inflate(R.layout.fragment_manga_info_summary, container, false);
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
		if (!getArguments().containsKey(Param.MANGA)) {
			return false;
		}
		String mangaStr = getArguments().getString(Param.MANGA, null);
		if (mangaStr == null) {
			return false;
		}

		mManga = (Manga) Common.fromJson(mangaStr, new TypeToken<Manga>() {
		}.getType());

		if (mManga == null) {
			return false;
		}
		return true;
	}

	private void initView() {
		tvMangaInfoSummaryAuthor = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryAuthor);
		tvMangaInfoSummaryCategories = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryCategories);
		tvMangaInfoSummaryDescription = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryDescription);
		tvMangaInfoSummaryNumChapter = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryNumChapter);
		tvMangaInfoSummaryRanking = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryRanking);
		tvMangaInfoSummaryStatus = (TextView) mView.findViewById(R.id.tvMangaInfoSummaryStatus);
		ivMangaInfoSummaryThumb = (ImageView) mView.findViewById(R.id.ivMangaInfoSummaryThumb);
		pbMangaInfoSummaryThumbProgress = (ProgressBar) mView.findViewById(R.id.pbMangaInfoSummaryThumbProgress);
		rlMangaInfoSummaryloading = (RelativeLayout) mView.findViewById(R.id.rlMangaInfoSummaryloading);
	}

	private void initViewEvent() {

	}

	private void initData(Bundle savedInstanceState) {
		mAq = new AQuery(mActivity);
		String status = mManga.isCompleted() ? getString(R.string.manga_info_completed)
				: getString(R.string.manga_info_ongoing);
		String categoryStr = "";
		ArrayList<MangaCategory> categories = mManga.getCategories();
		if (categories.size() > 0) {
			for (int i = 0; i < categories.size(); i++) {
				categoryStr += categories.get(i).getName();
				if (i != categories.size() - 1) {
					categoryStr += ", ";
				}
			}
		}
		tvMangaInfoSummaryAuthor.setText(mManga.getAuthor());
		tvMangaInfoSummaryDescription.setText(mManga.getDescription());
		tvMangaInfoSummaryRanking.setText(String.valueOf(mManga.getRank()));
		tvMangaInfoSummaryCategories.setText(categoryStr);
		tvMangaInfoSummaryStatus.setText(status);
		final String name = mManga.getName();
		final int imageWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.info_thumbnail_width);
		
		mAq.id(ivMangaInfoSummaryThumb).image(mManga.getThumbnailUrl(), true, true, imageWidth, R.drawable.ic_launcher,
				new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
						float scale;
						int newSize;
						Bitmap scaleBitmap;
						scale = (float) bm.getHeight() / bm.getWidth();
						newSize = Math.round(imageWidth * scale);
						Log.d("scale", name + ": " + bm.getWidth() + " - " + bm.getHeight());
						Log.d("scale", name + ": " + scale + " - " + newSize);
						scaleBitmap = Bitmap.createScaledBitmap(bm, imageWidth, newSize, true);
						bm = scaleBitmap;
						iv.setImageBitmap(scaleBitmap);
						// super.callback(url, iv, scaleBitmap, status);
						pbMangaInfoSummaryThumbProgress.setVisibility(View.GONE);
						rlMangaInfoSummaryloading.setVisibility(View.GONE);
					}
				});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d("MangaInfoSummaryFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}

}
