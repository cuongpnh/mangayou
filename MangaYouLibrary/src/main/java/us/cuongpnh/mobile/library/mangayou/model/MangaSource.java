package us.cuongpnh.mobile.library.mangayou.model;

import com.google.gson.annotations.SerializedName;

public class MangaSource {
	@SerializedName("_id")
	private String id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("language")
	private String language;
	
	@SerializedName("slug")
	private String slug;

	public String getId() {
		return id;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
	
}
