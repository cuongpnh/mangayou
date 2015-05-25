package vn.dwork.mangayou.adapter;

import us.cuongpnh.mobile.library.mangayou.model.Manga;
import vn.dwork.mangayou.fragment.MangaInfoChapterFragment;
import vn.dwork.mangayou.fragment.MangaInfoSummaryFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MangaInfoAdapter extends FragmentStatePagerAdapter {
	final private static int MANGA_INFO_TYPE = 2;
	private Manga mManga = null;
	public MangaInfoAdapter(FragmentManager fm, Manga manga) {
		super(fm);
		mManga = manga;
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return MangaInfoSummaryFragment.newInstance(mManga);
		case 1:
			return MangaInfoChapterFragment.newInstance(mManga.getId());
		}
		return null;
	}

	@Override
	public int getCount() {
		return MANGA_INFO_TYPE;
	}

}
