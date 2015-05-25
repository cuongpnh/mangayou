package us.cuongpnh.mobile.library.mangayou.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Manga {
	@SerializedName("_id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("author")
	private String author;

	@SerializedName("source_id")
	private String sourceId;

	@SerializedName("rank")
	private int rank;

	@SerializedName("completed")
	private boolean completed;

	@SerializedName("slug")
	private String slug;

	@SerializedName("updated_at")
	private String updatedAd;

	@SerializedName("thumbnail_url")
	private String thumbnailUrl;

	@SerializedName("source")
	private MangaSource source;

	@SerializedName("newest_chapter")
	private MangaChapter newestChapter;

	@SerializedName("description")
	private String description;
	
	@SerializedName("categories")
	private ArrayList<MangaCategory> categories;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<MangaCategory> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<MangaCategory> categories) {
		this.categories = categories;
	}

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getUpdatedAd() {
		return updatedAd;
	}

	public void setUpdatedAd(String updatedAd) {
		this.updatedAd = updatedAd;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public MangaSource getSource() {
		return source;
	}

	public void setSource(MangaSource source) {
		this.source = source;
	}

	public MangaChapter getNewestChapter() {
		return newestChapter;
	}

	public void setNewestChapter(MangaChapter newestChapter) {
		this.newestChapter = newestChapter;
	}

}
