package org.l6n.savage.parser;

import java.io.IOException;
import java.io.InputStream;

import org.l6n.savage.parser.tag.TagBase;
import org.l6n.savage.parser.tag.TagFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class Parser {

	private static final String TAG = Parser.class.getSimpleName();

	private final XmlPullParser xpp;

	private TagBase mDocumentTag;

	public Parser() throws XmlPullParserException {
		final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		xpp = factory.newPullParser();
	}

	public final void parse(final InputStream source) throws XmlPullParserException, IOException {
		xpp.setInput(source, "utf-8");

		final int eventType = xpp.getEventType();
		if (eventType == XmlPullParser.START_DOCUMENT) {
			parseDocument();
		} else {
			throw new IllegalStateException("Unknown event type: " + eventType);
		}
	}

	public Drawable getDrawable() {
		return mDocumentTag.getDrawable();
	}

	private void parseDocument() throws XmlPullParserException, IOException {
		int eventType = xpp.next();
		if (eventType == XmlPullParser.START_TAG) {
			mDocumentTag = TagFactory.newRootTag();
			parseInitialElement(mDocumentTag);
			parseTag(mDocumentTag);
		} else {
			throw new IllegalStateException("Unknown event type: " + eventType);
		}

		eventType = xpp.next();
		if (eventType != XmlPullParser.END_DOCUMENT) {
			throw new IllegalStateException("Unknown event type: " + eventType);
		}
	}

	private void parseTag(final TagBase pTag) throws XmlPullParserException, IOException {
		int eventType;
		while ((eventType = xpp.next()) != XmlPullParser.END_TAG) {
			final String tagNamespace = xpp.getNamespace();
			final String tagName = xpp.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				final TagBase newTag = TagFactory.newTag(tagNamespace, tagName, pTag);
				parseInitialElement(newTag);
				parseTag(newTag); // recurse
				pTag.addChild(newTag);
				break;
			case XmlPullParser.TEXT:
				final String text = xpp.getText().trim();
				if (text.length() > 0) {
					pTag.parseText(text);
				}
				break;
			default:
				throw new IllegalStateException("Unknown event type: " + eventType);
			}
		}
		// TODO when I get END_TAG make sure the namespace and name match
	}

	private void parseInitialElement(final TagBase pTag) {
		final int count = xpp.getAttributeCount();
		for(int i = 0; i < count; i++) {
			final String namespace = xpp.getAttributeNamespace(i);
			final String name = xpp.getAttributeName(i);
			final String value = xpp.getAttributeValue(i);
			// final String prefix = xpp.getAttributePrefix(i);
			// final String type = xpp.getAttributeType(i);
			try {
				pTag.parseAttribute(namespace, name, value);
			} catch(final Exception e) {
				Log.v(TAG, "Error parsing attribute " + name + " (" + namespace + ") = " + value);
			}
		}
	}
}
