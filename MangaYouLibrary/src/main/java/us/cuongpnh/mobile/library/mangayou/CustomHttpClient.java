package us.cuongpnh.mobile.library.mangayou;

import java.util.ArrayList;

import us.cuongpnh.mobile.library.mangayou.utils.Utils;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CustomHttpClient {
	private static String PARAM_HOLDER_WITHOUT_SLASH = "[#param#]";
	private static String TAG = "CustomHttpClient";
	private static final String BASE_URL = "http://mangayou.com/" + PARAM_HOLDER_WITHOUT_SLASH + ".json";
	private static ArrayList<String> sRequiredDevice = new ArrayList<String>();
	private static AsyncHttpClient client = new AsyncHttpClient();

	private static void init() {
		client.setURLEncodingEnabled(false);
	}

	public static void get(String call, RequestParams params, JsonHttpResponseHandler responseHandler) {
		params = CustomHttpClient.addExtraInfo(call, params);
		if (CustomHttpClient.validateParams(params)) {
			init();
			String requestUrl = BASE_URL.replace(PARAM_HOLDER_WITHOUT_SLASH, call);
			Log.d(TAG, CustomHttpClient.getRequestString(requestUrl, params));
			client.get(requestUrl, params, responseHandler);
		}
	}

	public static void post(String call, RequestParams params, JsonHttpResponseHandler responseHandler) {
		params = CustomHttpClient.addExtraInfo(call, params);
		if (CustomHttpClient.validateParams(params)) {
			init();
			String requestUrl = BASE_URL.replace(PARAM_HOLDER_WITHOUT_SLASH, call);
			Log.d(TAG, CustomHttpClient.getRequestString(requestUrl, params));
			client.post(requestUrl, params, responseHandler);
		}
	}

	public static String getRequestString(String requestUrl, RequestParams params) {
		return requestUrl + (params.getRequestString().isEmpty() ? "" : "?" + params.getRequestString());
	}

	@SuppressWarnings("unused")
	private static String getAbsoluteUrl(String requestUrl, String relativeUrl) {

		return requestUrl + relativeUrl;
	}

	public static boolean validateParams(RequestParams params) {
		if (params == null) {
			return false;
		}
//		if (!params.has(Param.CALL)) {
//			return false;
//		}
		return true;
	}

	public static RequestParams addExtraInfo(String call, RequestParams params) {

		if (call == null || call.equals("")) {
			return null;
		}
		if (params == null) {
			params = new RequestParams();
		}
		if (sRequiredDevice.contains(call)) {
			params.add(Param.DEVICE_HASH, Utils.getDeviceId());
			params.add(Param.DEVICE_NAME, Utils.getDeviceName());
		}
		//params.add(Param.CALL, call);
		return params;
	}

}