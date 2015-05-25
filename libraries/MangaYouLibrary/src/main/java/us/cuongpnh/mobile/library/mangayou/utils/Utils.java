package us.cuongpnh.mobile.library.mangayou.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import us.cuongpnh.mobile.library.mangayou.R;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class Utils {
	public static Context mContext = null;
	public static ProgressBar mProgressBar = null;
	public static RelativeLayout mOverlay = null;
	public static String LOG_FOLDER = "hector-log";
	public static String BASE_FRAGMENT_PATH = "us.cuongpnh.mobile.yummy.fragment.";

	public static void init(Context context) {
		mContext = context;
	}

	@SuppressLint("NewApi")
	public static String getDeviceId() {
		String deviceId;
		final TelephonyManager mTelephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		if (mTelephony.getDeviceId() != null) {
			deviceId = mTelephony.getDeviceId();
		} else {
			deviceId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		}
		return deviceId;
	}

	@SuppressLint("NewApi")
	public static String getDeviceName() {
		return android.os.Build.MODEL + "," + android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
	}

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("http://github.com/jfeinstein10/slidingmenu");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}

	public static void setContext(Context context) {
		if (mContext != null && mContext != context) {
			mProgressBar = null;
			mOverlay = null;
		}
		mContext = context;
	}

	public static Context getContext() {
		return mContext;
	}

	public static void showMessage(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	public static Object getFrament(String fragmentName) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		fragmentName = fragmentName.replace(" ", "") + "Fragment";
		String fullFragmentName = BASE_FRAGMENT_PATH + fragmentName;
		Object object = Class.forName(fullFragmentName).getConstructor().newInstance();
		return object;
	}

	public static String capitalize(String name) {
		String result = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		return result;
	}

	public static String prepareResource(String text) {
		return text.toLowerCase().replace(" ", "_");
	}

	public static int getIdFromResourceName(Context context, String resourceType, String name) {
		return context.getResources()
				.getIdentifier(Utils.prepareResource(name), resourceType, context.getPackageName());
	}

	@SuppressLint("NewApi")
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	public static int randomInt(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min + 1) + min;
	}

	public static float randomFloat(int min, int max) {
		int tmpMax = max * 10;
		Random r = new Random();
		return r.nextInt(tmpMax) / 10f;
	}

	public static void replaceFragment(FragmentActivity activity, Fragment fragment, int id, Bundle bundle) {
		String backStackName = fragment.getClass().getName();
		FragmentManager fm = activity.getSupportFragmentManager();
		boolean fragmentPopped = fm.popBackStackImmediate(backStackName, 0);
		FragmentTransaction ft = fm.beginTransaction();

		/*
		 * Set setArguments for new fragment
		 */
		if (bundle != null) {
			fragment.setArguments(bundle);
		}

		if (!fragmentPopped && fm.findFragmentByTag(backStackName) == null) {
			ft.replace(id, fragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			ft.addToBackStack(backStackName);
			ft.commit();
		}
	}

	public static int dp2px(Context context, int dp) {
		// Get the screen's density scale
		final float scale = context.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (dp * scale + 0.5f);
	}

	public static boolean isGoogleMapsInstalled(Context context) {
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
			if (info != null) {
				return true;
			}
			return false;

		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	@SuppressLint("InlinedApi")
	public static void addProgress(Context context) {
		/*
		 * android.R.id.content = full screen including ActionBar
		 */
		ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
		mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
		mProgressBar.setIndeterminate(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout rl = new RelativeLayout(context);
		rl.setGravity(Gravity.CENTER);
		rl.addView(mProgressBar);
		layout.addView(rl, params);
	}

	@SuppressLint("InlinedApi")
	public static void addOverlay(Context context) {
		/*
		 * android.R.id.content = full screen including ActionBar
		 */
		ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		mOverlay = new RelativeLayout(context);
		mOverlay.setBackgroundColor(context.getResources().getColor(R.color.overlay));
		mOverlay.setClickable(true); // For disable click, touch of child view
		mOverlay.setGravity(Gravity.CENTER);
		layout.addView(mOverlay, params);
	}

	public static void showOverlay(Context context) {
		if (mOverlay == null) {
			addOverlay(context);
			showOverlay(context);
		} else {
			mOverlay.setVisibility(View.VISIBLE);
		}
	}

	public static void showProgress(Context context) {
		if (mProgressBar == null) {
			addProgress(context);
			showProgress(context);
		} else {
			mProgressBar.setVisibility(View.VISIBLE);
		}
	}

	public static void hideOverlay() {
		if (mOverlay != null) {
			mOverlay.setVisibility(View.INVISIBLE);
		}
	}

	public static void hideProgress() {
		if (mProgressBar != null) {
			mProgressBar.setVisibility(View.INVISIBLE);
		}
	}

	public static void showFullProgress(Context context) {
		showOverlay(context);
		showProgress(context);
	}

	public static void hideFullProgress() {
		hideOverlay();
		hideProgress();
	}

	public static void showDialog(Activity activity, String title, String content) {
		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setTitle(title);
		dialog.setMessage(content);
		dialog.show();
	}

	public static String getFileExtension(String fileName) {
		int start = fileName.lastIndexOf(".");
		if (start == -1) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	@SuppressLint("NewApi")
	public static void hideSoftKeyboard(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
	}

	public static void writeNewFile(String fileName, String text) {
		File mapFolder = new File(Environment.getExternalStorageDirectory(), LOG_FOLDER + "/");
		if (!mapFolder.exists()) {
			/*
			 * Create a new one if map folder is not exists
			 */
			mapFolder.mkdirs();
		}
		try {
			/*
			 * Write text to file
			 */

			File mapFile = new File(mapFolder, fileName);
			if (!mapFile.exists())
				mapFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(mapFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			osw.append(text);
			osw.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
