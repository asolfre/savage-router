package org.l6n.savage.parser.tag;

import android.graphics.Color;

class GroupAttributes {

	public float scale = 1;

	public float translateX = 0;

	public float translateY = 0;

	public float floodOpacity = 1;

	public int floodColor = Color.BLACK;

	public GroupAttributes copy() {
		final GroupAttributes copy = new GroupAttributes();
		copy.scale = scale;
		copy.translateX = translateX;
		copy.translateY = translateY;
		copy.floodOpacity = floodOpacity;
		copy.floodColor = floodColor;
		return copy;
	}
}
