package vn.dwork.mangayou.activity;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.Param;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.Manga;
import us.cuongpnh.mobile.library.mangayou.model.MangaChapter;
import us.cuongpnh.mobile.library.mangayou.model.MangaSource;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.fragment.HomeFragment;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.RequestParams;

public class MainActivity extends BaseActivity {
	public MainActivity() {
		super(R.string.app_name);
	}

	private ApiClient mApiClient = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
		initEvent();
	}

	public void getManageSource() {
		mApiClient.getMangaSourceList(new IApiResponse<ArrayList<MangaSource>>() {

			@Override
			public void onSuccess(ArrayList<MangaSource> apiResponse) {
				Utils.showDialog(getContext(), "Manga source list", apiResponse.toString());

			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		});

	}

	public void getMangaSourceById() {
		RequestParams params = new RequestParams();
		params.put(Param.SLUG, Config.PREFS_DEFAULT_MANGA_SOURCE_SLUG);
		mApiClient.getMangaSourceById(params, new IApiResponse<MangaSource>() {

			@Override
			public void onSuccess(MangaSource apiResponse) {
				Utils.showDialog(getContext(), "Manga source", apiResponse.toString());
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void getMangaList() {
		RequestParams params = new RequestParams();
		params.put(Param.SOURCE_ID, "53b2e4854c65738212000000");
		mApiClient.getMangaList(params, new IApiResponse<ArrayList<Manga>>() {

			@Override
			public void onSuccess(ArrayList<Manga> apiResponse) {
				Utils.showDialog(getContext(), "Manga list", apiResponse.toString());
			}

			@Override
			public void onFailure(String apiResponse, int status) {
				Toast.makeText(getApplicationContext(), apiResponse, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void getMangaById() {
		RequestParams params = new RequestParams();
		params.put(Param.SLUG, "pandora-hearts-1");
		mApiClient.getMangaById(params, new IApiResponse<Manga>() {

			@Override
			public void onSuccess(Manga apiResponse) {
				Utils.showDialog(getContext(), "Manga", apiResponse.toString());
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void getMangaChapterList() {
		RequestParams params = new RequestParams();
		params.put(Param.COMIC_ID, "53b302e764653142dbea0d00");
		mApiClient.getMangaChapterList(params, new IApiResponse<ArrayList<MangaChapter>>() {

			@Override
			public void onSuccess(ArrayList<MangaChapter> apiResponse) {
				Utils.showDialog(getContext(), "Manga", apiResponse.toString());
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
		});
	}

	public void getMangaChapterById() {
		RequestParams params = new RequestParams();
		params.put(Param.SLUG, "5504df826465324e7a7a3000");
		mApiClient.getMangaChapterById(params, new IApiResponse<MangaChapter>() {

			@Override
			public void onSuccess(MangaChapter apiResponse) {
				Utils.showDialog(getContext(), "Manga", apiResponse.toString());
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

			}
		});
	}

	private void initData() {
		mApiClient = new ApiClient(this);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		setContentView(R.layout.fragment_main_content);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();
	}

	private void initView() {
		initEvent();
	}

	private void initEvent() {

	}

	private Activity getContext() {
		return this;
	}
}
