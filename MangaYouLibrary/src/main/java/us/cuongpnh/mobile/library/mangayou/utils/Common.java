package us.cuongpnh.mobile.library.mangayou.utils;

import java.lang.reflect.Type;
import com.google.gson.Gson;

public class Common {

	public static String toJson(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	public static Object fromJson(String json, Type type) {
		Gson gson = new Gson();
		return gson.fromJson(json, type);

	}
}
