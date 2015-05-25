package us.cuongpnh.mobile.library.mangayou.interfaces;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
	@SerializedName("code")
	private Integer responseCode;

	@SerializedName("data")
	private T responseData;

	@SerializedName("message")
	private List<String> responseMessage;

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public T getResponseData() {
		return responseData;
	}

	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}

	public String getErrorsAsString() {
		String msgStr = "";
		if (responseMessage != null) {
			for (String msg : responseMessage) {
				msgStr += msg + "\n";
			}
		}
		/*
		 * Remove \n character
		 */
		return msgStr.substring(0, msgStr.length() - 1);
	}

	public List<String> getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(List<String> responseMessage) {
		this.responseMessage = responseMessage;
	}

}
