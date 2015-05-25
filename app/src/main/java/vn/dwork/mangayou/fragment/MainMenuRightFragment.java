package vn.dwork.mangayou.fragment;

import vn.dwork.mangayou.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainMenuRightFragment extends BaseFragment {
	private View mView = null;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_main_menu_right, null);
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.flMainMenuRight, new SearchMangaSourceFragment())
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
