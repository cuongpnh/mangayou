package vn.dwork.mangayou.application;

import vn.dwork.mangayou.R;
import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class MangaYouApplication extends Application {
	private static DisplayImageOptions mDisplayImageOptionsForChapter = null;
	private static DisplayImageOptions mDisplayImageOptionsForHome = null;
	private static ImageLoader mImageLoader = null;

	@Override
	public void onCreate() {
		super.onCreate();
		initDataForImageLoader();
	}

	private void initDataForImageLoader() {
		initImageLoader();
		initDisplayImageOptions();
	}

	private void initImageLoader() {
		if (!ImageLoader.getInstance().isInited() || mImageLoader == null) {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
					.memoryCacheExtraOptions(480, 800)
					// default = device screen dimensions
					.diskCacheExtraOptions(480, 800, null).threadPoolSize(3).diskCacheSize(100 * 1024 * 1024)
					.diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();
			ImageLoader.getInstance().init(config);
		}
		mImageLoader = ImageLoader.getInstance();
	}

	private void initDisplayImageOptions() {
		initDisplayImageOptionsForChapter();
		initDisplayImageOptionsForHome();
	}

	@SuppressWarnings("deprecation")
	private void initDisplayImageOptionsForChapter() {
		if (mDisplayImageOptionsForChapter == null) {
			mDisplayImageOptionsForChapter = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.ic_thumbnail_placeholder)
					/*
					 * !IMPORTANT! Here, the SAVIOR showStubImage =))
					 */
					.showStubImage(R.drawable.ic_thumbnail_placeholder)
					.showImageForEmptyUri(R.drawable.ic_thumbnail_placeholder).cacheInMemory(true).cacheOnDisk(true)
					.build();
		}
	}

	@SuppressWarnings("deprecation")
	private void initDisplayImageOptionsForHome() {
		if (mDisplayImageOptionsForHome == null) {
			mDisplayImageOptionsForHome = new DisplayImageOptions.Builder()
					.showImageOnFail(R.drawable.ic_thumbnail_placeholder)
					/*
					 * !IMPORTANT! Here, the SAVIOR showStubImage =))
					 */
					.showStubImage(R.drawable.ic_thumbnail_placeholder)
					.showImageForEmptyUri(R.drawable.ic_thumbnail_placeholder).cacheInMemory(true).cacheOnDisk(true)
					.postProcessor(new BitmapProcessor() {

						@Override
						public Bitmap process(Bitmap bitmap) {
							int width = getResources().getDimensionPixelSize(R.dimen.info_thumbnail_width);
							return Bitmap.createScaledBitmap(bitmap, width, width, false);
						}
					}).build();
		}
	}

	public static DisplayImageOptions getDisplayImageOptionsForChapter() {

		return mDisplayImageOptionsForChapter;
	}

	public static DisplayImageOptions getDisplayImageOptionsForHome() {

		return mDisplayImageOptionsForHome;
	}

	public static ImageLoader getImageLoader() {
		return mImageLoader;
	}

}
