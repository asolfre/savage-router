package org.l6n.savage.parser.tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.Log;

class PathTag extends TagBase {

	private static final String DEBUG_TAG = PathTag.class.getSimpleName();

	private String mClass;
	private int mFill = Color.BLACK;
	private int mStroke = Color.BLACK;
	private float mStrokeWidth = 1f;
	private float[] mStrokeDashArray;

	private final Path mPath = new Path();

	protected PathTag(final TagBase pParent) {
		super(pParent);
	}

	@Override
	public void parseText(final String pText) {
		Log.v(DEBUG_TAG, "Unexpected text: " + pText);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		if ("class".equals(pName)) {
			mClass = pValue;
		} else if ("d".equals(pName)) {
			parsePathDataAttribute(pValue);
		} else if ("fill".equals(pName)) {
			if (!"none".equals(pValue)) {
				mFill = parseColor(pValue);
			}
		} else if ("stroke".equals(pName)) {
			mStroke = parseColor(pValue);
		} else if ("stroke-width".equals(pName)) {
			parseStrokeWidthAttribute(pValue);
		} else if ("stroke-dasharray".equals(pName)) {
			parseStrokeDashArrayAttribute(pValue);
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
		final ShapeDrawable path = new ShapeDrawable(new PathShape(mPath, mRootAttributes.width, mRootAttributes.height));

		path.setBounds(0, 0, mRootAttributes.width, mRootAttributes.height);
		// XXX note that OpenStreetMapTilesOverlay calls setBounds which will overwrite this
		//  - this only works because the bounds are always the full size of the tile

		final Paint paint = path.getPaint();
		paint.setColor("line".equals(mClass) ? mStroke : mFill);
		if ("line".equals(mClass)) {
			paint.setStyle(Paint.Style.STROKE);
			if (mStrokeDashArray != null) {
				paint.setPathEffect(new DashPathEffect(mStrokeDashArray, 0));
			}
			paint.setStrokeWidth(mStrokeWidth);
		}

		return path;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}

	private void parseStrokeWidthAttribute(final String pValue) {
		if (pValue.endsWith("px")) {
			final String s = pValue.substring(0, pValue.length() - 2);
			mStrokeWidth = Float.parseFloat(s) * mGroupAttributes.scale;
		} else {
			mStrokeWidth = Float.parseFloat(pValue) * mGroupAttributes.scale;
		}
	}

	private void parseStrokeDashArrayAttribute(final String pValue) {
		final ArrayList<Float> x = new ArrayList<Float>();
		final String[] intervals = pValue.split(",");
		for(final String s : intervals) {
			x.add(Float.parseFloat(s));
		}

		// now copy to array
		mStrokeDashArray = new float[x.size()];
		int i = 0;
		for(final Float f : x) {
			mStrokeDashArray[i++] = f;
		}
	}

	private void parsePathDataAttribute(final String pValue) {

		// http://www.w3.org/TR/SVG/paths.html#PathData

		final StringTokenizer st = new StringTokenizer(pValue, "MLZ ", true);
		// don't bother with lowercase or H or V commands because CloudMade doesn't use them

		PathCommand command = PathCommand.Move;
		float x = Float.NaN;
		float y = Float.NaN;
		while (st.hasMoreTokens()) {
			final String token = st.nextToken().trim();
			if (token.length() == 0) {
				// ignore white space
			} else if ("M".equals(token)) {
				command = PathCommand.Move;
				x = Float.NaN;
				y = Float.NaN;
			} else if ("L".equals(token)) {
				command = PathCommand.Line;
				x = Float.NaN;
				y = Float.NaN;
			} else if ("Z".equals(token)) {
				command = PathCommand.Line; // default next command after close
				x = Float.NaN;
				y = Float.NaN;
				mPath.close();
			} else {
				// we've got a coordinate
				if (Float.compare(x, Float.NaN) == 0) {
					x = Float.parseFloat(token);
				} else if (Float.compare(y, Float.NaN) == 0) {
					y = Float.parseFloat(token);
					x = (x + mGroupAttributes.translateX) * mGroupAttributes.scale;
					y = (y + mGroupAttributes.translateY) * mGroupAttributes.scale;
					switch(command) {
					case Move:
						// Log.v(TAG, "move to " + x + "," + y);
						mPath.moveTo(x, y);
						break;
					case Line:
						// Log.v(TAG, "line to " + x + "," + y);
						mPath.lineTo(x, y);
						break;
					default:
						// this can't happen
					}
					x = Float.NaN;
					y = Float.NaN;
				} else {
					// this can't happen
				}
			}
		}
	}

	private enum PathCommand {
		Move, Line
	}

	// explicitly list the attributes I know I'm ignoring
	private static final HashSet<String> ignoredAttributes = new HashSet<String>();
	static {
		ignoredAttributes.add("access");
		ignoredAttributes.add("addr_housenumber");
		ignoredAttributes.add("amenity");
		ignoredAttributes.add("area");
		ignoredAttributes.add("bicycle");
		ignoredAttributes.add("bridge");
		ignoredAttributes.add("building");
		ignoredAttributes.add("cm_id");
		ignoredAttributes.add("cycleway");
		ignoredAttributes.add("foot");
		ignoredAttributes.add("highway");
		ignoredAttributes.add("historic");
		ignoredAttributes.add("is_in");
		ignoredAttributes.add("junction");
		ignoredAttributes.add("landuse");
		ignoredAttributes.add("leisure");
		ignoredAttributes.add("manmade");
		ignoredAttributes.add("motorcar");
		ignoredAttributes.add("name");
		ignoredAttributes.add("natural");
		ignoredAttributes.add("oneway");
		ignoredAttributes.add("railway");
		ignoredAttributes.add("ref");
		ignoredAttributes.add("religion");
		ignoredAttributes.add("shop");
		ignoredAttributes.add("sport");
		ignoredAttributes.add("timestamp");
		ignoredAttributes.add("tourism");
		ignoredAttributes.add("tunnel");
		ignoredAttributes.add("waterway");
		ignoredAttributes.add("way_area");
	}
}
