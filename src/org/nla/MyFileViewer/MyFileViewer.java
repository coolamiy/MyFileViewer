
package org.nla.MyFileViewer;

import org.nla.MyFileViewer.ui.MyFileViewerFrame;

public final class MyFileViewer {

	/** property file path. */
	private static String propertyFilePath = "MyFileViewer.config";

	/** application properties. */
	private static MyFileViewerProperties properties = MyFileViewerProperties.getMyFileViewerProperties( propertyFilePath );
	
	public static void main(String[] args) {
		// retrieve property file path if given
		if ( args.length == 1 ) {
			propertyFilePath = args[0];
			//System.out.println(propertyFilePath);
		}
		
		

		new MyFileViewerFrame();
	}

	/**
	 * Return properties.
	 * @return	properties
	 */
	public static MyFileViewerProperties getProperties() {
		return properties;
	}
}
