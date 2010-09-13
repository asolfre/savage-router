package org.l6n.savage.parser.tag;

import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;

class CircleTag extends TagBase {

	private static final String DEBUG_TAG = CircleTag.class.getSimpleName();

	private final PointF mCenter = new PointF(0, 0);
	private int mFill = Color.BLACK;
	private float mRadius = 1;

	protected CircleTag(final TagBase pParent) {
		super(pParent);
	}

	@Override
	public void parseText(final String pText) {
		Log.v(DEBUG_TAG, "Unexpected text: " + pText);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		if ("class".equals(pName)) {
			// ignore
		} else if ("cx".equals(pName)) {
			mCenter.x = (int) ((Float.parseFloat(pValue) + mGroupAttributes.translateX) * mGroupAttributes.scale);
		} else if ("cy".equals(pName)) {
			mCenter.y = (int) ((Float.parseFloat(pValue) + mGroupAttributes.translateY) * mGroupAttributes.scale);
		} else if ("fill".equals(pName)) {
			mFill = parseColor(pValue);
			// don't use specified color
			mFill = Color.argb(200, 255, 128, 0); // slightly transparent orange
			// get POI colors from here:
			// http://code.google.com/p/osmand/source/browse/trunk/OsmAnd/src/net/osmand/render/OsmandRenderer.java
		} else if ("r".equals(pName)) {
			mRadius = Float.parseFloat(pValue) * mGroupAttributes.scale;
			// don't use specified radius
			mRadius = 10;
		} else {
			Log.v(DEBUG_TAG, "Unknown attribute: " + pNamespace + ":" + pName + "=" + pValue);
		}
	}

	@Override
	public void addChild(final TagBase pTag) {
		if (pTag instanceof UnknownTag) {
			final UnknownTag ut = (UnknownTag) pTag;
			if (ignoredAttributes.contains(ut.name)) {
				// do nothing
			} else {
				// log the rest
				Log.v(DEBUG_TAG, "Unknown child: " + pTag);
			}
		} else {
			mChildren.add(pTag);
		}
	}

	@Override
	protected Drawable getMyDrawable() {
		final OvalShape s = new MyCircleShape();
		final ShapeDrawable circle = new ShapeDrawable(s);
		return circle;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}

	private class MyCircleShape extends OvalShape {
		@Override
		public void draw(final Canvas pCanvas, final Paint pPaint) {
			pPaint.setColor(mFill);
			pCanvas.drawCircle(mCenter.x, mCenter.y, mRadius, pPaint);
		}
	}

	// explicitly list the attributes I know I'm ignoring
	private static final HashSet<String> ignoredAttributes = new HashSet<String>();
	static {
		ignoredAttributes.add("addr_housenumber");
		ignoredAttributes.add("amenity");
		ignoredAttributes.add("barrier");
		ignoredAttributes.add("building");
		ignoredAttributes.add("cm_id");
		ignoredAttributes.add("highway");
		ignoredAttributes.add("is_in");
		ignoredAttributes.add("leisure");
		ignoredAttributes.add("man_made");
		ignoredAttributes.add("name");
		ignoredAttributes.add("place");
		ignoredAttributes.add("railway");
		ignoredAttributes.add("religion");
		ignoredAttributes.add("shop");
		ignoredAttributes.add("sport");
		ignoredAttributes.add("timestamp");
		ignoredAttributes.add("tourism");
	}
}
