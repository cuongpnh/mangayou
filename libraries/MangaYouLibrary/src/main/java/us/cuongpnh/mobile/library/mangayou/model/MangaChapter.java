package us.cuongpnh.mobile.library.mangayou.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MangaChapter {
	@SerializedName("_id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("order")
	private int order;

	@SerializedName("created_at")
	private String createdAt;
	
	@SerializedName("pages")
	private ArrayList<String> pages;

	public String getId() {
		return id;
	}

	public ArrayList<String> getPages() {
		return pages;
	}

	public void setPages(ArrayList<String> pages) {
		this.pages = pages;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
