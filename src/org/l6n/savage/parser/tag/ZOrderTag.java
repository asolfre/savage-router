package org.l6n.savage.parser.tag;

import android.graphics.drawable.Drawable;
import android.util.Log;

class ZOrderTag extends TagBase {

	private static final String DEBUG_TAG = ZOrderTag.class.getSimpleName();

	private final TagBase mParent;

	protected ZOrderTag(final TagBase pParent) {
		super(pParent);
		mParent = pParent;
	}

	@Override
	public void parseText(final String pText) {
		final int zOrder = Integer.parseInt(pText);
		mParent.setZOrder(zOrder);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		Log.v(DEBUG_TAG, "Unknown attribute: " + pNamespace + ":" + pName + "=" + pValue);
	}

	@Override
	public void addChild(final TagBase pTag) {
		Log.v(DEBUG_TAG, "Unknown child: " + pTag);
	}

	@Override
	protected Drawable getMyDrawable() {
		return null;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}
}
