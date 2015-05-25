package us.cuongpnh.mobile.library.mangayou.interfaces;

public interface IApiResponse<T> {
	public void onSuccess(T apiResponse);
	public void onFailure(String message, int status);
}
