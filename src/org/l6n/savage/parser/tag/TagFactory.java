package org.l6n.savage.parser.tag;

public class TagFactory {

	private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

	private static final String CLOUDMADE_NAMESPACE = "http://cloudmade.com/";

	public static TagBase newRootTag() {
		return new RootTag(new RootAttributes(), new GroupAttributes());
	}

	public static TagBase newTag(final String pNamespace, final String pName, final TagBase pParent) {
		final TagBase tag = pParent;
		if (SVG_NAMESPACE.equals(pNamespace)) {
			if ("path".equals(pName)) {
				return new PathTag(tag);
			} else if ("circle".equals(pName)) {
				return new CircleTag(tag);
			} else if ("rect".equals(pName)) {
				return new RectangleTag(tag);
			} else if ("g".equals(pName)) {
				return new GroupTag(tag);
			}
		} else if (CLOUDMADE_NAMESPACE.equals(pNamespace)) {
			if ("z_order".equals(pName)) {
				return new ZOrderTag(tag);
			} else if ("layer".equals(pName)) {
				return new LayerTag(tag);
			}
		}
		return new UnknownTag(tag, pNamespace, pName);
	}

}
