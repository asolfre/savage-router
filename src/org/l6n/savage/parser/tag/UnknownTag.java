package org.l6n.savage.parser.tag;

import android.graphics.drawable.Drawable;

class UnknownTag extends TagBase {

	private static final String DEBUG_TAG = UnknownTag.class.getSimpleName();

	final String namespace, name;

	protected UnknownTag(final TagBase pParent, final String pNamespace, final String pName) {
		super(pParent);
		namespace = pNamespace;
		name = pName;
	}

	@Override
	public void parseText(final String pText) {
		// ignore text of unknown tags
	}

	@Override
	public void parseAttribute(final String pNamespace, final String pName, final String pValue) {
		// ignore unknown attributes of unknown tags
	}

	@Override
	public void addChild(final TagBase pTag) {
		// don't add children to unknown tags
	}

	@Override
	protected Drawable getMyDrawable() {
		return null;
	}

	@Override
	public String toString() {
		return DEBUG_TAG + "(" + name + ")";
	}
}
