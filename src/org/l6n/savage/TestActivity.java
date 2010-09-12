package org.l6n.savage;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TestActivity extends Activity {

	private static final String TAG = TestActivity.class.getSimpleName();

	private SvgDrawableView mSvgDrawableView;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSvgDrawableView = new SvgDrawableView(this);
		try {
			final String uri = "cm.1.svg";
			InputStream src = getAssets().open(uri);
			if (uri.endsWith(".svgz")) {
				// compressed svg
				src = new GZIPInputStream(src);
			}
			mSvgDrawableView.setSource(src);
		} catch (final IOException e) {
			Log.e(TAG, "Error loading source", e);
		}

		mSvgDrawableView.setKeepScreenOn(true);

		setContentView(mSvgDrawableView);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, Menu.FIRST, Menu.NONE, "Rotate");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch(item.getItemId()) {
		case Menu.FIRST:
			mSvgDrawableView.rotate();
			return true;
		default:
			break;
		}
		return false;
	}

}
