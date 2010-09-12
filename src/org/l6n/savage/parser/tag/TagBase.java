package org.l6n.savage.parser.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

public abstract class TagBase {

	private static final String DEBUG_TAG = TagBase.class.getSimpleName();

	protected final List<TagBase> mChildren = new ArrayList<TagBase>();

	protected final RootAttributes mRootAttributes;
	protected final GroupAttributes mGroupAttributes;

	private int mZOrder;

	/**
	 * Use z-order in preference to layer, ie if we've got z-order then ignore layer.
	 */
	boolean mHasZOrder = false;

	protected TagBase(final RootAttributes pRootAttributes, final GroupAttributes pGroupAttributes) {
		mRootAttributes = pRootAttributes;
		mGroupAttributes = pGroupAttributes;
	}

	protected TagBase(final TagBase pParent) {
		mRootAttributes = pParent.mRootAttributes;
		mGroupAttributes = pParent.mGroupAttributes;
	}

	final public Drawable getDrawable() {
		final ArrayList<Drawable> list = new ArrayList<Drawable>();
		final Drawable mine = getMyDrawable();
		if (mine != null) {
			list.add(mine);
		}

		// sort tags by z-order
		Collections.sort(mChildren, new Comparator<TagBase>() {
			@Override
			public int compare(final TagBase pObject1, final TagBase pObject2) {
				try {
					return pObject1.mZOrder - pObject2.mZOrder;
				} catch(final Throwable e) {
					Log.e(DEBUG_TAG, "Error in compare", e);
					return 0;
				}
			}
		});

		for(final TagBase child : mChildren) {
			final Drawable d = child.getDrawable();
			if (d != null) {
				list.add(d);
			}
		}
		final int s = list.size();
		if (s == 0) {
			return null;
		} else if (s == 1) {
			return list.get(0);
		} else {
			final Drawable[] x = new Drawable[list.size()];
			list.toArray(x);
			return new LayerDrawable(x);
		}
	}


	public void setZOrder(final int pZOrder) {
		mZOrder = pZOrder;
		mHasZOrder = true;
	}

	public void setLayer(final int pLayer) {
		if (!mHasZOrder) {
			mZOrder = 10 * pLayer;
		}
	}

	public abstract void parseText(String pText);

	public abstract void parseAttribute(String pNamespace, String pName, String pValue);

	public abstract void addChild(TagBase pTag);

	protected abstract Drawable getMyDrawable();

	protected int parseColor(final String pValue) {
		// handle grey explicitly because CloudMade spells it differently
		if ("grey".equals(pValue)) {
			return Color.GRAY;
		} else {
			return Color.parseColor(pValue);
		}
	}
}
