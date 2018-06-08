package org.nla.MyFileViewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.Properties;
import javax.swing.UIManager;

public class MyFileViewerProperties extends Properties {

	/** serial version uid. */
	private static final long serialVersionUID = -878823727157651446L;

	/** tells whether properties were properly loaded. */
	private boolean isProperlyLoaded;

	/** unique instance. */
	private static MyFileViewerProperties singleton;

	/**
	 * Creates object using given path, and stores its content.
	 * @param filePath
	 * @throws Exception if a problem occurs
	 */
	private MyFileViewerProperties(String filePath) throws Exception {
	/*	String path=MyFileViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = URLDecoder.decode(
				    path, 
				    "UTF-8");*/
		
		//String PropertiesPath=jarpath.getParentFile().getAbsolutePath();
		//System.out.println(PropertiesPath);
		File file = new File( filePath );
		if ( file.isDirectory() ) {
			throw new IllegalArgumentException("MyFileViewerProperties.MyFileViewerProperties(String) File " +filePath + " is a directory." );
		}
		if ( ! file.exists() )  {
			throw new IllegalArgumentException("MyFileViewerProperties.MyFileViewerProperties(String) File " +filePath + " doesn't exist." );
		}

		try {
			FileInputStream fis = new FileInputStream( filePath);
			//System.out.println(fis.read());
			//InputStream fis = ( MyFileViewerProperties.class.getClass().getResourceAsStream(filePath));
			//System.out.println(file.getAbsolutePath());
			loadFromXML( fis );
			
			//fis.close();
			isProperlyLoaded = true;
		}
		catch( Exception e ) {
			isProperlyLoaded = false;
			throw e;
		}
	}

	/**
	 * Default Constructor.
	 */
	private MyFileViewerProperties() {
		isProperlyLoaded = false;
		setProperty("LookAndFeelClass", UIManager.getSystemLookAndFeelClassName());
		setProperty("Locale", Locale.getDefault().toString());
		setProperty("WorkingDirectory", new File(".").getAbsolutePath());
	}

	/**
	 * Static method to call, that delivers the singleton.
	 */
	protected static MyFileViewerProperties getMyFileViewerProperties(String filePath) {
		if ( singleton == null ) {
			try {
				singleton = new MyFileViewerProperties( filePath );
			}
			catch (Exception e) {
				e.printStackTrace();
				singleton = new MyFileViewerProperties();
			}
		}
		return singleton;
	}

	/**
	 * Return the look and feel class.
	 * @return	Return the look and feel class.
	 */
	public String getLookAndFeelClassName() {
		return getProperty( "LookAndFeelClass", UIManager.getSystemLookAndFeelClassName() );
	}

	/**
	 * Return the locale.
	 * @return	Return the locale.
	 */
	public Locale getLocale() {
		String localeName = getProperty( "Locale", Locale.getDefault().toString() );
		System.out.println(localeName);
		return new Locale( localeName );
	}

	/**
	 * Return the working directory.
	 * @return	Return the working directory.
	 */
	public String getWorkingDirectory() {
		return getProperty( "WorkingDirectory", new File(".").getAbsolutePath() );
	}

	/**
	 * Tells whether properties were properly loaded from XML file.
	 * @return the isProperlyLoaded
	 */
	public boolean isProperlyLoaded() {
		return isProperlyLoaded;
	}
}
