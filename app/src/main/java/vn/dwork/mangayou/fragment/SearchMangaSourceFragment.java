package vn.dwork.mangayou.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import us.cuongpnh.mobile.library.mangayou.ApiClient;
import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.MangaSource;
import vn.dwork.mangayou.R;
import vn.dwork.mangayou.adapter.MangaSourceAdapter;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchMangaSourceFragment extends BaseFragment {
	private View mView = null;
	private ViewPager mPager = null;
	private AutoCompleteTextView actvSearchInput = null;
	private ArrayAdapter<String> actvSearchInputAdapter = null;
	private Button btnBackMainMenu = null;
	private static int PAGE_INDEX = 0;
	private ArrayList<String> mMangaSourceName = null;
	private HashMap<String, Integer> mProvideHolder = null;
	private TextView tvCurrentLocationMainMenu = null;
	private ApiClient mApiClient = null;
	private MangaSourceAdapter mMangaSourceAdapter = null;
	private ArrayList<MangaSource> mMangaSource = null;
	private LocalBroadcastManager mBroadcastManager = null;
	private ListView mLvMangaSource = null;
	private OnClickListener btnBackMainMenuListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(PAGE_INDEX, true);
		}
	};

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_search_manga_source, null);
		initView();
		initViewEvent();
		initData();
		return mView;
	}

	private void processChangeMangaSource(int position) {
		Utils.storeMangaSource(mActivity, mMangaSource.get(position));
		String mangaSourceName = mMangaSource.get(position).getName();
		tvCurrentLocationMainMenu.setText(mangaSourceName);
		Toast.makeText(mActivity,
				getString(R.string.frag_search_manga_source_select_manga_source) + ": " + mangaSourceName,
				Toast.LENGTH_LONG).show();
		/*
		 * Send broadcast update update comic list
		 */
		sendRefreshMangaListBroadcast();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	private void initView() {
		mPager = (ViewPager) mActivity.findViewById(R.id.vpMainMenu);
		mLvMangaSource = (ListView) mView.findViewById(R.id.lvMangaSource);
		btnBackMainMenu = (Button) mView.findViewById(R.id.btnBackMainMenu);
		actvSearchInput = (AutoCompleteTextView) mView.findViewById(R.id.actvSearchInput);
		tvCurrentLocationMainMenu = (TextView) mActivity.findViewById(R.id.tvCurrentMangaSourceMainMenu);
	}

	private void initViewEvent() {
		btnBackMainMenu.setOnClickListener(btnBackMainMenuListener);
	}

	private void initData() {
		mBroadcastManager = LocalBroadcastManager.getInstance(mActivity);
		mProvideHolder = new HashMap<String, Integer>();
		mMangaSourceName = new ArrayList<String>();
		mApiClient = new ApiClient(mActivity);
		mApiClient.getMangaSourceList(new IApiResponse<ArrayList<MangaSource>>() {

			@Override
			public void onSuccess(ArrayList<MangaSource> apiResponse) {
				mMangaSource = apiResponse;
				mMangaSourceAdapter = new MangaSourceAdapter(mActivity, mMangaSource);
				mLvMangaSource.setAdapter(mMangaSourceAdapter);

				for (int i = 0; i < mMangaSource.size(); i++) {
					mProvideHolder.put(mMangaSource.get(i).getName(), i);
					mMangaSourceName.add(mMangaSource.get(i).getName());
				}

				actvSearchInputAdapter = new ArrayAdapter<String>(mActivity,
						android.R.layout.simple_dropdown_item_1line, mMangaSourceName);
				actvSearchInput.setAdapter(actvSearchInputAdapter);
				actvSearchInput.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

						/*
						 * Clear text
						 */
						actvSearchInput.clearFocus();
						actvSearchInput.setText("");
						/*
						 * Hide keyboard
						 */
						Utils.hideKeybroad(mActivity, actvSearchInput);

						/*
						 * Call process when change manga source
						 */
						processChangeMangaSource(position);
					}
				});
			}

			@Override
			public void onFailure(String message, int status) {
				Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
			}
		});
		mLvMangaSource.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				processChangeMangaSource(position);
			}
		});
	}

	private void sendRefreshMangaListBroadcast() {
		Intent intent = new Intent(Config.RECEIVER_REFRESH_MANGA_LIST);
		mBroadcastManager.sendBroadcast(intent);
	}

}
