package org.nla.MyFileViewer.ui;

//import java.util.Locale;
import java.util.ResourceBundle;

import org.nla.MyFileViewer.MyFileViewer;

public class MyFileViewerUIResourceBundle {

	/** resource name. */
	private static final String ressource = "messages";
	
	/** singleton. */
	private static MyFileViewerUIResourceBundle singleton;
	
	/** resource bundle. */
	private ResourceBundle bundle;


	/**
	 * 
	 * @param resource
	 */
	private MyFileViewerUIResourceBundle( String resource ) {
		//Locale l = MyFileViewer.getProperties().getLocale();
		bundle = ResourceBundle.getBundle( resource, MyFileViewer.getProperties().getLocale() );
	
	}

	/**
	 * Static method to call, that delivers the singleton.
	 * @param resource
	 */
	public static String getString(String key) {
		if ( singleton == null ) {
			singleton = new MyFileViewerUIResourceBundle( ressource );
		}
		
		try {
			return singleton.bundle.getString(key);
		}
		catch( Exception e ) {
			return key;
		}
	}
}
