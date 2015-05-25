package vn.dwork.mangayou.adapter;

import java.util.ArrayList;

import vn.dwork.mangayou.R;
import vn.dwork.mangayou.model.MainMenuModel;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends CustomAdapter {

	public MainMenuAdapter(Context context, ArrayList<MainMenuModel> items) {
		super(context, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.item_main_menu, parent,false);
			holder.main_menu_row_icon = (ImageView) view.findViewById(R.id.main_menu_row_icon);
			holder.main_menu_row_title = (TextView) view.findViewById(R.id.main_menu_row_title);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}
		MainMenuModel menu = (MainMenuModel) this.mObjects.get(position);
		holder.main_menu_row_icon.setImageResource(menu.getIconRes());
		holder.main_menu_row_title.setText(String.valueOf(menu.getTitle()));
		return view;
	}
	static class ViewHolder
	{
		ImageView main_menu_row_icon;
		TextView main_menu_row_title;
		
	}
}
