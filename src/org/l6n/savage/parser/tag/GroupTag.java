package org.l6n.savage.parser.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.drawable.Drawable;
import android.util.Log;

class GroupTag extends TagBase {

	private static final String DEBUG_TAG = GroupTag.class.getSimpleName();

	private final Pattern SCALE_PATTERN = Pattern.compile("^scale\\(([-\\d\\.]+)\\)$");

	private final Pattern TRANSLATE_PATTERN = Pattern.compile("^translate\\(([-\\d\\.]+)\\s*,\\s*([-\\d\\.]+)\\)$");

	protected GroupTag(final TagBase pParent) {
		super(pParent.mRootAttributes, pParent.mGroupAttributes.copy()); // copy of parent's group attributes
	}

	@Override
	public void parseText(final String pText) {
		Log.v(DEBUG_TAG, "Unexpected text: " + pText);
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		if ("transform".equals(pName)) {
			parseTransformAttribute(pValue);
		} else if ("flood-opacity".equals(pName)) {
			mGroupAttributes.floodOpacity = Float.parseFloat(pValue);
		} else if ("flood-color".equals(pName)) {
			mGroupAttributes.floodColor = parseColor(pValue);
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
		return null;
	}

	@Override
	public String toString() {
		return DEBUG_TAG;
	}

	private void parseTransformAttribute(final String pValue) {
		final Matcher scaleMatcher = SCALE_PATTERN.matcher(pValue);
		if (scaleMatcher.find()) {
			final String s1 = scaleMatcher.group(1);
			try {
				final float f = Float.parseFloat(s1);
				mGroupAttributes.scale = f;
			} catch(final NumberFormatException e) {
				Log.v(DEBUG_TAG, "Bad transform scale parameter: " + pValue);
			}
		} else {
			final Matcher translateMatcher = TRANSLATE_PATTERN.matcher(pValue);
			if (translateMatcher.find()) {
				final String s1 = translateMatcher.group(1);
				final String s2 = translateMatcher.group(2);
				try {
					final float f1 = Float.parseFloat(s1);
					final float f2 = Float.parseFloat(s2);
					mGroupAttributes.translateX = f1;
					mGroupAttributes.translateY = f2;
				} catch(final NumberFormatException e) {
					Log.v(DEBUG_TAG, "Bad transform translate parameter: " + pValue);
				}
			} else {
				Log.v(DEBUG_TAG, "Bad transform parameter: " + pValue);
			}
		}
	}
}
