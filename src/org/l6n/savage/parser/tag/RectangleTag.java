package org.l6n.savage.parser.tag;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

class RectangleTag extends TagBase {

	private static final String DEBUG_TAG = RectangleTag.class.getSimpleName();

	int mWidth;
	int mHeight;
	int mFill = Color.BLACK;
	int mOpacity = 1;

	protected RectangleTag(final TagBase pParent) {
		super(pParent);
		// TODO perhaps I should set width and height to root width and height
		// and then don't refer to root attributes below
	}

	@Override
	public void parseText(final String pText) {
		Log.v(DEBUG_TAG, "Unexpected text: " + pText);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		if ("width".equals(pName)) {
			mWidth = parseRelativeWidth(pValue, mRootAttributes.width);
		} else if ("height".equals(pName)) {
			mHeight = parseRelativeWidth(pValue, mRootAttributes.height);
		} else if ("fill".equals(pName)) {
			mFill = parseColor(pValue);
		} else if ("opacity".equals(pName)) {
			mOpacity = Integer.parseInt(pValue);
		} else {
			Log.v(DEBUG_TAG, "Unknown attribute: " + pNamespace + ":" + pName + "=" + pValue);
		}
	}

	@Override
	public void addChild(final TagBase pTag) {
		if (pTag instanceof UnknownTag) {
			Log.v(DEBUG_TAG, "Unknown child: " + pTag);
		} else {
			mChildren.add(pTag);
		}
	}

	@Override
	protected Drawable getMyDrawable() {
		final ShapeDrawable rectangle = new ShapeDrawable(new RectShape());

		rectangle.setBounds(0, 0, mWidth, mHeight);
		// XXX note that OpenStreetMapTilesOverlay calls setBounds which will overwrite this
		//  - this only works because the bounds are always the full size of the tile

		rectangle.getPaint().setColor(mFill);

		return rectangle;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}

	private int parseRelativeWidth(final String pValue, final int pTotalAbsoluteWidth) {
		if (pValue.endsWith("%")) {
			final String s = pValue.substring(0, pValue.length() - 1);
			final int percentage = Integer.parseInt(s);
			final int absolute = percentage * pTotalAbsoluteWidth / 100;
			return absolute;
		} else {
			final int absolute = Integer.parseInt(pValue);
			return absolute;
		}
	}
}
