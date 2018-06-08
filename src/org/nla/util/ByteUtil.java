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

//import java.util.HashMap;
import java.util.StringTokenizer;

/**

 *
 */
public class ByteUtil {

	/**
	 * Return an unpacked byte array of the specified packed digits.
	 * @param	packed digits, that is a hexadecimal representation of digits. For instance "12 7C" represents 127*
	 * @param	byteArraySize the returning byte array size
	 */
	public static byte[] packHexaString( String packedDigits ) {
		StringTokenizer spaceTokenizer = new StringTokenizer( packedDigits, " " );
		byte unpackedBytes[] = new byte[spaceTokenizer.countTokens()];
		int i = 0;
		while ( spaceTokenizer.hasMoreTokens() ) {
			String byteAsString = spaceTokenizer.nextToken();
			
			int leftNybble = Integer.parseInt( byteAsString.charAt( 0 ) + "" ); 
			int rightNybble = Integer.parseInt( byteAsString.charAt( 1 ) + "" );
			
			unpackedBytes[i] = (byte)(( leftNybble << 4 ) | rightNybble);  
			
			i += 1;
		}
		
		return unpackedBytes;
	}
	
	
	/**
	 * Return a packed-decimal version of the number represented by this string
	 * @param value
	 * @return	a packed version of this string
	 */
	public static byte[] packDigits( String digits ) {
		int valueAsInteger = Integer.parseInt( digits );
		int sign = 1;
		
		if ( digits.startsWith( "-" ) ) {
			sign = -1;
			digits = digits.substring( 1 ); 
		}

		else if ( digits.startsWith( "+" ) ) {
			sign = 1;
			digits = digits.substring( 1 ); 
		}

		int packedSize = ( digits.length() % 2 == 0 ? digits.length() / 2 : ( digits.length() + 1 ) / 2 );
		byte[] toReturn = new byte[packedSize];
		int j = packedSize-1;
		
		toReturn[j] = packDigitWithSign( Integer.parseInt( digits.charAt( digits.length() - 1 ) + "" ), sign );
		for ( int i = digits.length() - 2; i > 0; i = i - 2 ) {
			j -= 1;
			int rightDigit = Integer.parseInt( digits.charAt( i ) + "" );
			int leftDigit = 0;
			if ( i - 1 >= 0 ) {
				leftDigit = Integer.parseInt( digits.charAt( i - 1 ) + "" );
			}
			toReturn[j] = packTwoDigits( leftDigit, rightDigit );
		}
		
		return toReturn;
	}
	
	public static byte packDigitWithSign( int digit, int sign ) {
		//System.out.println( "packDigitWithSign(): digit " + digit + "; sign " + sign );
		
		// nybbles
		int rightNybble, leftNybble;
		
		// sign determination
		if ( sign == -1 ) {
			rightNybble = 0x0D;
		}
		else {
			rightNybble = 0x0C;
		}
		
		// digit
		leftNybble = ( digit << 4 );
		
		return (byte) ( ( leftNybble | rightNybble ) & 0x000000ff );
	}
	
	/**
	 * 
	 * @param digit1
	 * @param digit2
	 * @return
	 */
	public static byte packTwoDigits( int leftDigit, int rightDigit ) {
		
		//System.out.println( "packTwoDigits(): leftDigit " + leftDigit + "; rightDigit " + rightDigit );
		// nybbles
		byte rightNybble, leftNybble;
		
		// left nybble
		leftNybble = (byte)(leftDigit << 4);

		// right nybble
		rightNybble = (byte)rightDigit;

		
		return (byte) ( leftNybble | rightNybble );
	}
	
	/**
	 * Returns hexadecimal version of a String.
	 * Code found on www.java.happycodings.com, at http://www.java.happycodings.com/Core_Java/code11.html
	 * @param base string to translate
	 * @return
	 */
	public static String stringToHex( String base ) {
		StringBuffer buffer = new StringBuffer();
		int intValue;
		for( int x = 0; x < base.length(); x++ ) {
			int cursor = 0;
			intValue = base.charAt(x);
			String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
			for( int i = 0; i < binaryChar.length(); i++ ) {
            	if (binaryChar.charAt(i) == '1') {
            		cursor += 1;
            	}
			}
			if( (cursor % 2) > 0 ) {
				intValue += 128;
			}
			buffer.append(Integer.toHexString(intValue) + " ");
		}
	return buffer.toString();
	}
	
	/**
	 * Returns a hexadecimal representation of the specified byte. It does the same as Integer.toHexString(int) but it additionnaly removes all potential
	 * leading Fs if the integer is signed.  
	 * @param b
	 * @return
	 */
	public static String byteToHexString( byte b ) {
		String toReturn = Integer.toHexString( b );
		
		if ( toReturn.length() > 2 ) {
			toReturn = toReturn.substring( toReturn.length() - 2 );
		}
		
		return toReturn;
	}
}
