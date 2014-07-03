package infinitec.eleventh.remindme.activities;

import java.util.ArrayList;
import java.util.List;

import infinitec.eleventh.remindme.R;
import infinitec.eleventh.remindme.utils.AppConstants;
import infinitec.eleventh.remindme.utils.Logger;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// String testString =
		// "Namaste! Your bill dated 12 /Apr/ 2314  of Rs 1589  for your airtel fixedline 08042132154  is due on 01-May-14. ignore if paid.";
		// String testString =
		// "You have 1 missed call(s) from +918867770562 .Last call: 21/ 05/5614 20:17 .Block unwanted calls ! Dial  *323# TollFree";

		// RegExUtils.isDatePresentInText(testString);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	public void processShare(View v) {

		String appShareUrl = getString(R.string.app_share_message).concat(
				AppConstants.PLAY_STORE_LINK);

		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		// Native email client doesn't currently support HTML, but it
		// doesn't hurt to try in case they fix it
		emailIntent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
		emailIntent.setType("message/rfc822");

		PackageManager mPacketManager = getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("text/plain");

		Intent openInChooser = Intent.createChooser(emailIntent, "Choose");

		List<ResolveInfo> resInfo = mPacketManager.queryIntentActivities(
				sendIntent, 0);
		List<LabeledIntent> mIntentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a
			// LabeledIntent
			ResolveInfo mResInf = resInfo.get(i);
			String packageName = mResInf.activityInfo.packageName;
			if (packageName.contains("android.email")) {
				emailIntent.setPackage(packageName);
			} else if (packageName.contains("twitter")
					|| packageName.contains("whatsapp")
					|| packageName.contains("facebook")
					|| packageName.contains("mms")
					|| packageName.contains("android.gm")) {
				Intent intent = new Intent();
				intent.setComponent(new ComponentName(packageName,
						mResInf.activityInfo.name));
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				if (packageName.contains("twitter")) {
					intent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
				} else if (packageName.contains("facebook")) {
					intent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
				} else if (packageName.contains("whatsapp")) {
					intent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
				} else if (packageName.contains("mms")) {
					intent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
				} else if (packageName.contains("android.gm")) {
					intent.putExtra(Intent.EXTRA_TEXT, appShareUrl);
					intent.putExtra(Intent.EXTRA_SUBJECT,
							getString(R.string.subject));
					intent.setType("message/rfc822");
				}

				mIntentList.add(new LabeledIntent(intent, packageName, mResInf
						.loadLabel(mPacketManager), mResInf.icon));
			}
		}

		LabeledIntent[] extraIntents = mIntentList
				.toArray(new LabeledIntent[mIntentList.size()]);
		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);
	}

	public void processDone(View v) {
		finish();
	}

	public void processCustomizeDays(View v) {
		TextView customizeOption = (Button) findViewById(R.id.customizeOption);
		int dayCount = AppConstants.days;
		dayCount++;
		int maxLimit = AppConstants.maxLimitDays;
		String textString = "";
		if (dayCount >= maxLimit || dayCount <= 0) {
			textString = String.format(getResources().getString(
					R.string.default_save_day_text));
			dayCount = 0;
		} else {
			textString = String.format(
					getResources().getString(R.string.save_day_text), dayCount);

		}
		AppConstants.days = dayCount;
		customizeOption.setText(textString);

	}
}
