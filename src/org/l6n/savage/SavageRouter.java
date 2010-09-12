package org.l6n.savage;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class SavageRouter extends Activity {

	private static final String TAG = SavageRouter.class.getSimpleName();

	private OpenStreetMapView mOsmv;
	private MyLocationOverlay mLocationOverlay;

	private IOpenStreetMapRendererInfo mRenderer = null;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			mRenderer = new CloudmadeSvgRenderer();
		} catch (final XmlPullParserException e) {
			Log.e(TAG, "Error initialising SVG parser", e);
		}

		final RelativeLayout rl = new RelativeLayout(this);

		this.mOsmv = new OpenStreetMapView(this, mRenderer);
		this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(), this.mOsmv);
		this.mOsmv.setBuiltInZoomControls(true);
		this.mOsmv.setMultiTouchControls(true);
		this.mOsmv.getOverlays().add(this.mLocationOverlay);
		rl.addView(this.mOsmv, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setContentView(rl);

		mOsmv.getController().setZoom(16);

		mOsmv.setKeepScreenOn(true);
	}

	@Override
	protected void onPause() {
		this.mLocationOverlay.disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(0, Menu.FIRST, Menu.NONE, "My location").setIcon(android.R.drawable.ic_menu_mylocation);
		pMenu.add(0, Menu.FIRST + 1, Menu.NONE, "Trace"); // FIXME remove this
		return true;
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		switch(item.getItemId()) {
		case Menu.FIRST:
			this.mLocationOverlay.followLocation(true);
			this.mLocationOverlay.enableMyLocation();
			final Location lastFix = this.mLocationOverlay.getLastFix();
			if (lastFix != null) {
				this.mOsmv.getController().setCenter(new GeoPoint(lastFix));
			}
			return true;
		case Menu.FIRST + 1:
			trace();
			return true;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent event) {
		return this.mOsmv.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			this.mLocationOverlay.followLocation(false);
		}

		return super.onTouchEvent(event);
	}

	private void trace() {
		new Thread() {
			@Override
			public void run() {
				SystemClock.sleep(2000);
				Debug.startMethodTracing("SavageRouter");
				// Toast.makeText(SavageRouter.this, "Started tracing", Toast.LENGTH_SHORT).show();
				SystemClock.sleep(20000);
				Debug.stopMethodTracing();
				// Toast.makeText(SavageRouter.this, "Stopped tracing", Toast.LENGTH_SHORT).show();
			}
		}.start();
	}
}
