package vn.dwork.mangayou.utils;

public class Config {
	/*
	 * Shared preference
	 */
	public static String PREFS_MANGA_SOURCE_SLUG = "manga_source_slug";
	public static String PREFS_MANGA_SOURCE_NAME = "manga_source_name";
	public static String PREFS_MANGA_SOURCE_ID = "manga_source_id";
	public static String PREFS_DEFAULT_MANGA_SOURCE_SLUG = "mangareader";
	public static String PREFS_DEFAULT_MANGA_SOURCE_NAME = "MangaReader";
	public static String PREFS_DEFAULT_SORT_BY = "rank";
	public static String PREFS_SORT_BY = "sort_by";

	/*
	 * IntentService
	 */
	public static final String RECEIVER_REFRESH_MANGA_LIST = "us.cuongpnh.mobile.mangayou.REFRESH_MANGA_LIST";
	public static final String RECEIVER_SEARCH_MANGA_LIST = "us.cuongpnh.mobile.mangayou.SEARCH_MANGA_LIST";

	/*
	 * Comic loading
	 */
	public static int DEFAULT_LIMIT = 50;
	public static String DEFAULT_SORT = "rank";

	/*
	 * HTTP request
	 */
	public static int HTTP_REQUEST_SUCCESS = 200;
	public static int HTTP_INTERVAL_REQUEST_FAIL = 10;

	/*
	 * Other
	 */
	public static int MIN_REMAINING_LOAD_MORE = 20;
	public static String ABOUT_LINK = "https://plus.google.com/u/0/+C%C6%B0%E1%BB%9DngPh%E1%BA%A1mNguy%E1%BB%85n/";
	public static String POSITION = "position";
	public static String IMAGE_PATHS = "image_paths";
	

	/*
	 * Handler
	 */
	public static int HANDLER_DELAY_SEARCH_REQUEST = 300;
}
