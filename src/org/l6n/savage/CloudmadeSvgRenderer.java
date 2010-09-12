package org.l6n.savage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.tileprovider.CloudmadeException;
import org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCallback;
import org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCloudmadeTokenCallback;
import org.andnav.osm.tileprovider.OpenStreetMapTile;
import org.andnav.osm.views.util.OpenStreetMapRendererBase;
import org.l6n.savage.parser.Parser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class CloudmadeSvgRenderer extends OpenStreetMapRendererBase {

	private static final String TAG = CloudmadeSvgRenderer.class.getSimpleName();

	// TODO do svgz instead of svg - do later because it's handy to be able to see the text

	CloudmadeSvgRenderer() throws XmlPullParserException {
		// tile zoom of 9 gives tile size of 512
		//  - we should probably use a tile zoom that gives about 4 tiles for the device's screen resolution
		//  - although if it changes it will mean that the existing tiles will be invalid
		super("CloudmadeSVG", 0, 18, 9, ".svg", "http://alpha.vectors.cloudmade.com/%s/%d/%d/%d/%d/%d%s?clipped=true");
	}

	@Override
	public String localizedName(final ResourceProxy pProxy) {
		return name();
	}

	@Override
	public String getTileURLString(
			final OpenStreetMapTile aTile,
			final IOpenStreetMapTileProviderCallback aCallback,
			final IOpenStreetMapTileProviderCloudmadeTokenCallback aCloudmadeTokenCallback)
	throws CloudmadeException {

		final String key = aCallback.getCloudmadeKey();
		return String.format(getBaseUrl(), key, cloudmadeStyle, mMaptileSizePx, aTile.getZoomLevel(), aTile.getX(), aTile.getY(), mImageFilenameEnding);
	}

	@Override
	public Drawable getDrawable(final String pFilePath) {

		// XXX most time is spent in parseFloat
		// Debug.startMethodTracing("getDrawable");

		InputStream fileSource = null;
		Drawable drawable = null;
		try {
			fileSource = new FileInputStream(pFilePath);
			final InputStream source = mImageFilenameEnding.equals(".svgz") ? new GZIPInputStream(fileSource) : fileSource;
			final Parser parser = new Parser();
			parser.parse(source);
			drawable = parser.getDrawable();
		} catch (final Throwable e) {
			Log.e(TAG, "Error getting drawable from xml", e);
		} finally {
			// Debug.stopMethodTracing();
			if (fileSource != null) {
				try {
					fileSource.close();
				} catch(final IOException ignore) {}
			}
		}
		return drawable;
	}

}
