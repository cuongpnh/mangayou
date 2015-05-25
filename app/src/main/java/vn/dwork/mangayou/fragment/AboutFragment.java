package vn.dwork.mangayou.fragment;

import vn.dwork.mangayou.R;
import vn.dwork.mangayou.utils.Config;
import vn.dwork.mangayou.utils.Utils;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutFragment extends BaseFragment {
	private View mView = null;
	private WebView wvIntroduction = null;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_introduction, null);
		setTitle();
		initView();
		initData();
		return mView;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		Utils.showProgress(getActivity());
		wvIntroduction.clearCache(true);
		wvIntroduction.clearHistory();
		wvIntroduction.getSettings().setJavaScriptEnabled(true);
		wvIntroduction.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wvIntroduction.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// Handle the error here!
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		wvIntroduction.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					Utils.hideProgress();
				}
			}
		});
		wvIntroduction.loadUrl(Config.ABOUT_LINK);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void setTitle() {
		super.setTitle();
		getActivity().setTitle(getString(R.string.common_about));
	}

	private void initView() {
		wvIntroduction = (WebView) mView.findViewById(R.id.wvFragmentAbout);
	}

	@Override
	public void onStop() {
		Utils.hideProgress();
		super.onStop();
	}
}
