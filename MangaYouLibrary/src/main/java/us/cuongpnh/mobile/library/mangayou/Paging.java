package us.cuongpnh.mobile.library.mangayou;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Paging<T> {
	@SerializedName("total")
	private Integer mTotal;

	@SerializedName("item")
	private List<T> mItem;

	public Integer getTotal() {
		return mTotal;
	}

	public void setTotal(Integer mTotal) {
		this.mTotal = mTotal;
	}

	public List<T> getItem() {
		return mItem;
	}

	public void setItem(List<T> mItem) {
		this.mItem = mItem;
	}

}
