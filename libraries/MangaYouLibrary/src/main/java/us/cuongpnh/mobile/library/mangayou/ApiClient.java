package us.cuongpnh.mobile.library.mangayou;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.cuongpnh.mobile.library.mangayou.interfaces.IApiResponse;
import us.cuongpnh.mobile.library.mangayou.model.Manga;
import us.cuongpnh.mobile.library.mangayou.model.MangaChapter;
import us.cuongpnh.mobile.library.mangayou.model.MangaSource;
import us.cuongpnh.mobile.library.mangayou.utils.Common;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiClient {

	public static int RESPONSE_CREATED = 201;
	public static int RESPONSE_NO_CONTENT = 204;
	public static int RESPONSE_BAD_REQUEST = 400;
	public static int RESPONSE_UNAUTHORIZED = 401;
	public static int RESPONSE_FORBIDDEN = 403;
	public static int RESPONSE_NOT_FOUND = 404;
	public static int RESPONSE_TOO_MANY_REQUESTS = 429;
	public static int RESPONSE_INTERNAL_SERVER_ERROR = 500;
	public static int RESPONSE_SERVICE_UNAVAILABLE = 503;

	public static String TAG = ApiClient.class.toString();
	private Activity mActivity = null;

	public ApiClient(Activity activity) {
		this.mActivity = activity;

	}

	public Context getContext() {
		return mActivity;
	}

	private boolean validateParams(RequestParams params, ArrayList<String> paramKey) {
		if (params == null) {
			Toast.makeText(getContext(), mActivity.getString(R.string.invalid_parameter), Toast.LENGTH_LONG).show();
			return false;
		}
		for (String key : paramKey) {
			if (!params.has(key)) {
				Toast.makeText(getContext(), key + " " + mActivity.getString(R.string.is_missing), Toast.LENGTH_LONG)
						.show();
				return false;
			}
		}
		return true;
	}

	public void getMangaSourceList(IApiResponse<ArrayList<MangaSource>> onComplete) {
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_SOURCE, null), null,
				new TypeToken<ArrayList<MangaSource>>() {
				}.getType(), onComplete);

	}

	public void getMangaList(RequestParams params, IApiResponse<ArrayList<Manga>> onComplete) {
		ArrayList<String> paramKey = new ArrayList<String>();
		paramKey.add(Param.SOURCE_ID);
		if (!validateParams(params, paramKey)) {
			return;
		}
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_COMIC, null), params,
				new TypeToken<ArrayList<Manga>>() {
				}.getType(), onComplete);
	}

	/*
	 * Set params null here to receive error => handler error
	 */
	public void getMangaChapterList(RequestParams params, IApiResponse<ArrayList<MangaChapter>> onComplete) {
		ArrayList<String> paramKey = new ArrayList<String>();
		paramKey.add(Param.COMIC_ID);
		if (!validateParams(params, paramKey)) {
			return;
		}
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_CHAPTER, null), params,
				new TypeToken<ArrayList<MangaChapter>>() {
				}.getType(), onComplete);
	}

	public void getMangaSourceById(RequestParams params, IApiResponse<MangaSource> onComplete) {
		ArrayList<String> paramKey = new ArrayList<String>();
		paramKey.add(Param.SLUG);
		if (!validateParams(params, paramKey)) {
			return;
		}
		String call = params.getUrlParam(Param.SLUG);
		params.remove(Param.SLUG);
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_SOURCE, call), null, new TypeToken<MangaSource>() {
		}.getType(), onComplete);

	}

	public void getMangaById(RequestParams params, IApiResponse<Manga> onComplete) {
		ArrayList<String> paramKey = new ArrayList<String>();
		paramKey.add(Param.SLUG);
		if (!validateParams(params, paramKey)) {
			return;
		}
		String call = params.getUrlParam(Param.SLUG);
		params.remove(Param.SLUG);
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_COMIC, call), null, new TypeToken<Manga>() {
		}.getType(), onComplete);
	}

	public void getMangaChapterById(RequestParams params, IApiResponse<MangaChapter> onComplete) {
		ArrayList<String> paramKey = new ArrayList<String>();
		paramKey.add(Param.SLUG);
		if (!validateParams(params, paramKey)) {
			return;
		}
		String call = params.getUrlParam(Param.SLUG);
		params.remove(Param.SLUG);
		processRequest(ApiRequest.getRequest(ApiRequest.Manga.GET_CHAPTER, call), null, new TypeToken<MangaChapter>() {
		}.getType(), onComplete);
	}

	private <T> void processRequest(String requestName, RequestParams params, final Type type,
			final IApiResponse<T> onComplete) {
		CustomHttpClient.get(requestName, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				handlerResponseWhenReturnObject(response, onComplete, type);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				handlerResponseWhenReturnArray(response, onComplete, type);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				Log.d(TAG, responseString);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				String errorMessage = null;
				int status = RESPONSE_SERVICE_UNAVAILABLE;
				if (errorResponse != null) {
					JSONObject responseObj = null;
					try {
						responseObj = errorResponse.getJSONObject(Param.NAME_VALUE_PAIRS);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (responseObj != null) {
						try {
							errorMessage = responseObj.getString(Param.ERROR);
							status = responseObj.getInt(Param.STATUS);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				if (errorMessage == null && throwable != null) {
					errorMessage = throwable.getMessage();
				}
				/*
				 * Set default error message if there is no error response from
				 * server.
				 */
				if (errorMessage == null) {
					errorMessage = mActivity.getString(R.string.error_connection_timeout);
				}
				onComplete.onFailure(errorMessage, status);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				String errorString = Common.toJson(errorResponse);
				Log.d(TAG, errorString);
			}
		});
	}

	@SuppressWarnings({ "unchecked" })
	private <T> void handlerResponseWhenReturnObject(JSONObject response, IApiResponse<T> onComplete, Type type) {
		Log.d(TAG, response.toString());
		T apiResponse = null;
		apiResponse = (T) Common.fromJson(response.toString(), type);
		onCompleteHandler(apiResponse, onComplete);
	}

	@SuppressWarnings("unchecked")
	private <T> void handlerResponseWhenReturnArray(JSONArray response, IApiResponse<T> onComplete, Type type) {
		Log.d(TAG, response.toString());
		T apiResponse = null;
		apiResponse = (T) Common.fromJson(response.toString(), type);
		onCompleteHandler(apiResponse, onComplete);
	}

	private <T> void onCompleteHandler(T apiResponse, IApiResponse<T> onComplete) {
		if (onComplete != null) {
			if (apiResponse != null && apiResponse != "") {
				onComplete.onSuccess(apiResponse);
			} else {
				Toast.makeText(getContext(),
						getContext().getString(R.string.there_something_happened_when_process_this_request),
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
