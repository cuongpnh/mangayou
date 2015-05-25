package vn.dwork.mangayou.fragment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import us.cuongpnh.mobile.library.mangayou.Param;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.customview.TouchImageView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class MangaChapterPageFragment extends BaseFragment {
	private View mView = null;
	private String mImage = null;
	private TouchImageView ivMangaChapterItem = null;
	private ProgressBar pbMangaChapterItemLoading = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions mDisplayImageoptions;
	private static final List<String> mDisplayedImages = Collections.synchronizedList(new LinkedList<String>());

	public static MangaChapterPageFragment newInstance(String image) {
		MangaChapterPageFragment fragment = new MangaChapterPageFragment();
		Bundle args = new Bundle();
		args.putString(Param.IMAGE, image);
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (!validate()) {
			Toast.makeText(mActivity, getString(R.string.error_invalid_parameter), Toast.LENGTH_LONG).show();
			return null;
		}
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_photo_slider_row, container, false);
			initView();
			mDisplayImageoptions = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.ic_thumbnail_placeholder)
					.showImageForEmptyUri(R.drawable.ic_thumbnail_placeholder).cacheInMemory(true).cacheOnDisk(true)
					.build();

			imageLoader = ImageLoader.getInstance();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			parent.removeView(mView);
		}
		return mView;
	}

	private void initView() {
		ivMangaChapterItem = (TouchImageView) mView.findViewById(R.id.ivMangaChapterItem);
		pbMangaChapterItemLoading = (ProgressBar) mView.findViewById(R.id.pbMangaChapterItemLoading);
	}

	private void initData(Bundle savedInstanceState) {

		ivMangaChapterItem = (TouchImageView) mView.findViewById(R.id.ivMangaChapterItem);
		pbMangaChapterItemLoading = (ProgressBar) mView.findViewById(R.id.pbMangaChapterItemLoading);
		imageLoader.displayImage(mImage, ivMangaChapterItem, mDisplayImageoptions, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage != null) {
					ImageView imageView = (ImageView) view;
					boolean firstDisplay = !mDisplayedImages.contains(imageUri);
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						mDisplayedImages.add(imageUri);
						pbMangaChapterItemLoading.setVisibility(View.GONE);
					}
				}
			}
		});

	}

	private boolean validate() {
		if (getArguments() == null) {
			return false;
		}
		if (!getArguments().containsKey(Param.IMAGE)) {
			return false;
		}
		mImage = getArguments().getString(Param.IMAGE, null);
		if (mImage == null) {
			return false;
		}
		return true;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData(savedInstanceState);
	}
}
