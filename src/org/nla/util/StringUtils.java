/*
    This file is part of the MyFileViewer application.

    MyFileViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    org.nla.cobol.parsing is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MyFileViewer.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.nla.util;


public final class StringUtils {

	/**
	 * Tell whether specified String contains only specified character. 
	 * This method ignores the case.
	 * TODO TO CHANGE BY A REGEXP
	 * @param s	String to check.
	 * @param character Character to check against the String. Must be exactly one character in length.
	 * @return true if s contains only character.
	 */
	public static final boolean containsOnly( String s, String character ) {
		if( s == null ) {
			throw new IllegalArgumentException( "StringUtils.containsOnly() string to check can't be null." );
		}
		
		if( character == null ) {
			throw new IllegalArgumentException( "StringUtils.containsOnly() character to check can't be null." );
		}

		if( character.length() != 1 ) {
			throw new IllegalArgumentException( "StringUtils.containsOnly() character length must be exactly one." );
		}
		
		char[] dest = new char[s.length()];
		s.toLowerCase().getChars(0, s.length(), dest, 0);
		for ( char c : dest ) {
			if ( c != character.toLowerCase().charAt( 0 ) ) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tell whether specified String contains only digits. 
	 * TODO TO CHANGE BY A REGEXP
	 * @param s
	 * @return
	 */
	public static final boolean isNumeric( String s ) {
		
		if( s == null ) {
			throw new IllegalArgumentException( "StringUtils.isNumeric() string to check can't be null." );
		}
		
		char[] dest = new char[s.length()];
		s.getChars(0, s.length(), dest, 0);
		for ( char c : dest ) {
			if ( !Character.isDigit( c ) ) {
				return false;
			}
		}
		return true;
	}
}
