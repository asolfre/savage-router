package org.l6n.savage;

import java.io.InputStream;

import org.l6n.savage.parser.Parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class SvgDrawableView extends View {

	private static final String TAG = SvgDrawableView.class.getSimpleName();

	private final Matrix mMatrix = new Matrix();
	private Drawable mDrawable;

	private final Point mTopLeft = new Point();
	private final PointF mMouseDown = new PointF();

	private final GestureDetector mGestureDetector;

	private boolean mRotated = false;

	public SvgDrawableView(final Context context) {
		super(context);

		// TODO a more suitable initial bitmap
		final Bitmap initialBitmap = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.btn_star);
		mDrawable = new BitmapDrawable(initialBitmap);

		mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
			@Override
			public boolean onDoubleTap(final MotionEvent pEvent) {
				// I needed somewhere to reset the view
				mTopLeft.x = 0;
				mTopLeft.y = 0;
				return true;
			}
		});
	}

	@Override
	public boolean onTouchEvent(final MotionEvent pEvent) {
		// Log.v(TAG, "onTouchEvent(" + pEvent + ")");

		mGestureDetector.onTouchEvent(pEvent);

		final int action = pEvent.getAction();
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			mMouseDown.x = pEvent.getX();
			mMouseDown.y = pEvent.getY();
			// Log.v(TAG, "mMouseDown=" + mMouseDown);
			return true;
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			final int x = (int)(mTopLeft.x + mMouseDown.x - pEvent.getX());
			final int y = (int)(mTopLeft.y + mMouseDown.y - pEvent.getY());
			scrollTo(x, y);
			// Log.v(TAG, "x=" + x + " y=" + y + " mTopLeft=" + mTopLeft);
			if (action == MotionEvent.ACTION_UP) {
				mTopLeft.x = x;
				mTopLeft.y = y;
			}
			return true;
		default:
			return false;
		}
	}

	// private static int drawNumber = 0;
	@Override
	protected void onDraw(final Canvas canvas) {
		// XXX most time is spent in native draw
		// Debug.startMethodTracing("onDraw-" + ++drawNumber);

		if (mRotated) {
			// add perspective - copied from ApiDemos/PolyToPoly.java
			canvas.save();
			mMatrix.setPolyToPoly(
					new float[] { 200, 200,  400, 200,  200, 600,  400, 600 }, 0,
					new float[] { 225, 200,  375, 200,  175, 600,  425, 600 }, 0,
					4
			);
			canvas.setMatrix(mMatrix);
			mDrawable.draw(canvas);
			canvas.restore();
		} else {
			mDrawable.draw(canvas);
		}

		// Debug.stopMethodTracing();
	}

	public void setSource(final InputStream source) {

		// TODO draw more often while we're loading source

		// XXX most time is spent in parseFloat
		// Debug.startMethodTracing("setSource");

		try {
			final Parser parser = new Parser();
			parser.parse(source);
			mDrawable = parser.getDrawable();
		} catch (final Throwable e) {
			Log.e(TAG, "Error getting drawable from xml", e);
		}

		// Debug.stopMethodTracing();
	}

	public void rotate() {
		mRotated = true;
		invalidate();
	}
}
