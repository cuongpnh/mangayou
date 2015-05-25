package vn.dwork.mangayou.adapter;

import java.util.ArrayList;

import vn.dwork.mangayou.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

public class CustomAdapter extends BaseAdapter {

	protected Context mContext = null;
	protected LayoutInflater mLayoutInflater = null;
	protected ArrayList<?> mObjects = null;
	protected AQuery mAq = null;

	public CustomAdapter(Context context, ArrayList<?> items) {
		this.mAq = new AQuery(context);
		this.mContext = context;
		this.mObjects = items;
		this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}
	
	public Object getItem() {
		return mObjects;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if (view == null) {
			view = mLayoutInflater.inflate(R.layout.fragment_main_menu_left, parent, false);
		}
		return view;
	}

	public void remove(int index) {
		this.mObjects.remove(index);
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public void updateItemList(ArrayList<?> items) {
		this.mObjects = items;
	}

	protected AQuery getImageLoader() {
		if (mAq == null) {
			mAq = new AQuery(getContext());
		}
		return mAq;
	}
}