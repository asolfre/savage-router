package org.l6n.savage.parser.tag;

import android.graphics.drawable.Drawable;
import android.util.Log;

class RootTag extends TagBase {

	private static final String DEBUG_TAG = RootTag.class.getSimpleName();

	protected RootTag(final RootAttributes pRootAttributes, final GroupAttributes pGroupAttributes) {
		super(pRootAttributes, pGroupAttributes);
	}

	@Override
	public void parseText(final String pText) {
		Log.v(DEBUG_TAG, "Unexpected text: " + pText);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		if ("width".equals(pName)) {
			mRootAttributes.width = Integer.parseInt(pValue);
		} else if ("height".equals(pName)) {
			mRootAttributes.height = Integer.parseInt(pValue);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}
}
