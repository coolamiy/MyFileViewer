package org.nla.cobol;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * FlatFile represents a COBOL generated file. All records, or lines, in this file supposedly match a format 
 * described in a CopyBook.

 */
public class FlatFile extends ArrayList<String> {

	/** serial version uid. */
	private static final long serialVersionUID = 8194280332296021262L;
	
	/** flat file. */
	private File file;

	/**
	 * 
	 * @param file
	 * @throws Exception
	 */
	public FlatFile( File file ) throws Exception {
		super();
		if ( file.isDirectory() ) {
			throw ( new IllegalArgumentException( file + " is not a file" ) );
		}
		this.file = file;
		readFileContent();
	}

	
	public String getLine( int lineNumber ) {
		 return get( lineNumber );
	}


	public int getLineCount() {
		 return size();
	}

	
	private void readFileContent() throws Exception {
		BufferedReader reader = new BufferedReader( new FileReader( file ) );
		for ( String line = reader.readLine(); line != null; line = reader.readLine() ) {
			add( line );
		}
		reader.close();
	}
}
