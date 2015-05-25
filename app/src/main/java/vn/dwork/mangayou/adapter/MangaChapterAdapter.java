package vn.dwork.mangayou.adapter;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.model.MangaChapter;
import vn.dwork.mangayou.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MangaChapterAdapter extends CustomAdapter {

	public MangaChapterAdapter(Context context, ArrayList<MangaChapter> items) {
		super(context, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.item_manga_chapter, parent,false);
			holder.item_title = (TextView) view.findViewById(R.id.item_title);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}
		MangaChapter chapter = (MangaChapter) this.mObjects.get(position);
		holder.item_title.setText(String.valueOf(chapter.getName()));
		return view;
	}
	static class ViewHolder
	{
		TextView item_title;
		
	}
}
