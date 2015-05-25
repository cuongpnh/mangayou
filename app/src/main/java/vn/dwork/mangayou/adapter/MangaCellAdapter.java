package vn.dwork.mangayou.adapter;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.model.Manga;
import vn.dwork.mangayou.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

public class MangaCellAdapter extends CustomAdapter {
	private int mImageHeight = 0;
	private int mNumColumns = 0;
	private int mItemHeight = 0;
	private RelativeLayout.LayoutParams mImageViewLayoutParams = null;

	public MangaCellAdapter(Context context, ArrayList<Manga> items, int imageHeight) {
		super(context, items);
		mImageHeight = imageHeight;
		if (mImageHeight == 0) {

			mImageViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		} else {
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(mImageHeight, mImageHeight);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.item_manga_grid_thumbnail, parent, false);
			holder.item_thumbnail = (ImageView) convertView.findViewById(R.id.item_thumbnail);
			holder.item_status_thumb = (ImageView) convertView.findViewById(R.id.item_status_thumb);
			holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
			holder.item_sub_title = (TextView) convertView.findViewById(R.id.item_sub_title);
			holder.thumb_progress = (ProgressBar) convertView.findViewById(R.id.thumb_progress);
			/*
			 * Modify image height here
			 */
			holder.item_thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/*
		 * Re-initialize layout for image here. Don't call the line below in
		 * block 'convertView == null'
		 */
		holder.item_thumbnail.setLayoutParams(mImageViewLayoutParams);
		final View view = convertView;
		Manga manga = (Manga) this.mObjects.get(position);
		String thumbnail = manga.getThumbnailUrl();
		getImageLoader().id(holder.item_thumbnail).image(thumbnail, true, true,
				getContext().getResources().getDimensionPixelSize(R.dimen.info_thumbnail_width),
				R.drawable.ic_thumbnail_placeholder, new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
						super.callback(url, iv, bm, status);

						((ViewHolder) view.getTag()).thumb_progress.setVisibility(View.GONE);
						((ViewHolder) view.getTag()).item_status_thumb.setVisibility(View.VISIBLE);
					}
				});

		holder.item_title.setText(manga.getName());
		holder.item_sub_title.setText(manga.getAuthor());

		return convertView;
	}

	public void setItemHeight(int height) {
		if (height == mItemHeight) {
			return;
		}
		mItemHeight = height;
		mImageViewLayoutParams = new RelativeLayout.LayoutParams(mItemHeight, mItemHeight);
		notifyDataSetChanged();
	}

	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
	}

	public int getNumColumns() {
		return mNumColumns;
	}

	static class ViewHolder {
		ImageView item_thumbnail;
		ImageView item_status_thumb;
		TextView item_title;
		TextView item_sub_title;
		ProgressBar thumb_progress;

	}
}
