package us.cuongpnh.mobile.library.mangayou;

public final class ApiRequest {
	public static final String PARAM_HOLDER = "/[#param#]";
	public static final String SLASH = "/";

	public static final class Manga {
		public static final String GET_SOURCE = "sources" + PARAM_HOLDER;
		public static final String GET_COMIC = "comics" + PARAM_HOLDER;
		public static final String GET_CHAPTER = "chapters" + PARAM_HOLDER;
	}

	public static String getRequest(String prefixRequest, String suffixRequest) {
		if (suffixRequest == null || suffixRequest.isEmpty()) {
			return prefixRequest.replace(PARAM_HOLDER, ""); 
		}
		return prefixRequest.replace(PARAM_HOLDER, SLASH + suffixRequest);

	}
}
