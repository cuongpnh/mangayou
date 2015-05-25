package vn.dwork.mangayou.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import vn.dwork.mangayou.R;
import vn.dwork.mangayou.application.MangaYouApplication;
import vn.dwork.mangayou.customview.TouchImageView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

public class FullScreenImageAdapter extends PagerAdapter {
	/*
	 * !IMPORTANT! Don't use a global variable when using PagerAdapter, using
	 * final View instead. Especially, for progress you MUST use FINAL +
	 * showStubImage set on in DisplayImageOptions so that ImageLoader can
	 * replace that stub with new image load from server
	 */
	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mDisplayImageOptions;
	private Activity mActivity = null;
	private ArrayList<String> mImagePaths = null;
	private LayoutInflater mInflater = null;
	private static final List<String> mDisplayedImages = Collections.synchronizedList(new LinkedList<String>());

	public FullScreenImageAdapter(Activity activity, ArrayList<String> imagePaths) {
		/*
		 * Using Universal Image Loader have because it can update view in
		 * PageAdapter which AQuery can't
		 */
		this.mActivity = activity;
		this.mImagePaths = imagePaths;
		mImageLoader = MangaYouApplication.getImageLoader();
		mDisplayImageOptions = MangaYouApplication.getDisplayImageOptionsForChapter();

	}

	@Override
	public int getCount() {
		return this.mImagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = mInflater.inflate(R.layout.fragment_photo_slider_row, container, false);
		final TouchImageView ivMangaChapterItem = (TouchImageView) view.findViewById(R.id.ivMangaChapterItem);
		ivMangaChapterItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mActivity.getActionBar().isShowing()) {
					mActivity.getActionBar().hide();
				} else {
					mActivity.getActionBar().show();
				}
			}
		});
		final RelativeLayout rlMangaChapterItemLoading = (RelativeLayout) view
				.findViewById(R.id.rlMangaChapterItemLoading);
		final ProgressBar pbMangaChapterItemLoading = (ProgressBar) view.findViewById(R.id.pbMangaChapterItemLoading);
		mImageLoader.displayImage((mImagePaths.get(position)), ivMangaChapterItem, mDisplayImageOptions,
		/*
		 * Universal Image Loader: Can I use cache but also refresh it?
		 * http://stackoverflow.com/questions/19054192/
		 */

		new SimpleImageLoadingListener() {
			boolean cacheFound;

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				rlMangaChapterItemLoading.setVisibility(View.VISIBLE);
				pbMangaChapterItemLoading.setProgress(0);
				List<String> memCache = MemoryCacheUtils.findCacheKeysForImageUri(imageUri,
						mImageLoader.getMemoryCache());
				cacheFound = !memCache.isEmpty();
				if (!cacheFound) {
					File discCache = DiskCacheUtils.findInCache(imageUri, mImageLoader.getDiskCache());
					if (discCache != null) {
						cacheFound = discCache.exists();
					}
				}
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				if (loadedImage != null) {
					ImageView imageView = (ImageView) view;
					boolean firstDisplay = !mDisplayedImages.contains(imageUri);
					if (firstDisplay) {
						FadeInBitmapDisplayer.animate(imageView, 500);
						mDisplayedImages.add(imageUri);
					}
					if (cacheFound) {
						MemoryCacheUtils.removeFromCache(imageUri, mImageLoader.getMemoryCache());
						DiskCacheUtils.removeFromCache(imageUri, mImageLoader.getDiskCache());
						mImageLoader.displayImage(imageUri, (ImageView) view);
					}
					rlMangaChapterItemLoading.setVisibility(View.GONE);
				}

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = mActivity.getString(R.string.error_input_output_error);
					break;
				case DECODING_ERROR:
					message = mActivity.getString(R.string.error_image_cant_be_decoded);
					break;
				case NETWORK_DENIED:
					message = mActivity.getString(R.string.error_downloads_are_denied);
					break;
				case OUT_OF_MEMORY:
					message = mActivity.getString(R.string.error_out_of_memory_error);
					break;
				case UNKNOWN:
					message = mActivity.getString(R.string.error_unknown_error);
					break;
				}
				Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
				rlMangaChapterItemLoading.setVisibility(View.GONE);
			}

		}, new ImageLoadingProgressListener() {

			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				pbMangaChapterItemLoading.setProgress(Math.round(100.0f * current / total));
			}
		});

		((ViewPager) container).addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

}
