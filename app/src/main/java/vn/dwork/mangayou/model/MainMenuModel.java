package vn.dwork.mangayou.model;

public class MainMenuModel extends BaseModel {
	private String mTitle = null;
	private int mIconRes;
	private String mClassName = null;

	public MainMenuModel(String name, int iconRes, String className) {
		this.mTitle = name;
		this.mIconRes = iconRes;
		this.mClassName = className;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public int getIconRes() {
		return mIconRes;
	}

	public void setIconRes(int mIconRes) {
		this.mIconRes = mIconRes;
	}

	public String getClassName() {
		return mClassName;
	}

	public void setClassName(String mClassName) {
		this.mClassName = mClassName;
	}

}
