
package org.nla.cobol;

import java.util.UUID;



public final class Type {
	/** unique identifier */
	private String uuid;

	/** picture */
	private Picture picture;

	/** integer size */
	private int integerSize;

	/** decimal size */
	private int decimalSize;

	/** signed */
	private boolean isSigned;

	/**
	 * Constructor using all fields
	 * @param picture
	 * @param integerSize
	 * @param decimalSize
	 * @param isPacked
	 * @param isSigned
	 * @param defaultValue
	 */
	public Type(Picture picture, int integerSize, int decimalSize,boolean isPacked, boolean isSigned ) {
		this.picture = picture;
		this.integerSize = integerSize;
		this.decimalSize = decimalSize;
		this.isSigned = isSigned;
		this.uuid = UUID.randomUUID().toString();
	}

	/**
	 * Default constructor.
	 */
	public Type() {
		this(null, 0, 0, Boolean.FALSE, Boolean.FALSE );
	}

	/**
	 * @return the picture
	 */
	public Picture getPicture() {
		return picture;
	}

	/**
	 * @param picture the picture to set
	 */
	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	/**
	 * @return the integerSize
	 */
	public int getIntegerSize() {
		return integerSize;
	}

	/**
	 * @param integerSize the integerSize to set
	 */
	public void setIntegerSize(int integerSize) {
		this.integerSize = integerSize;
	}

	/**
	 * @return the decimalSize
	 */
	public int getDecimalSize() {
		return decimalSize;
	}

	/**
	 * @param decimalSize the decimalSize to set
	 */
	public void setDecimalSize(int decimalSize) {
		this.decimalSize = decimalSize;
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		int size = integerSize + decimalSize;
		return size;
	}
	

	/**
	 * @return the isSigned
	 */
	public boolean isSigned() {
		return isSigned;
	}

	/**
	 * @param isSigned the isSigned to set
	 */
	public void setSigned(boolean isSigned) {
		this.isSigned = isSigned;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getAttributes()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append( "PIC " );
		if ( picture.equals( Picture.Pic9 ) && isSigned ){
			buf.append( "S" );
		}

		buf.append( picture );
		buf.append( "(" );
		buf.append( integerSize );
		buf.append( ")" );

		if ( picture.equals( Picture.Pic9 ) ){
			if ( decimalSize > 0 )	{
				buf.append( "V(" );
				buf.append( decimalSize );
				buf.append( ")" );
			}
		}
		return buf.toString();
	}
}
