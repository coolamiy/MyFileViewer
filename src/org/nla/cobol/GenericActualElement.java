package org.nla.cobol;

import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a generic implementation of cobol element in a copybook, variable or group. It still is abstract,
 * but it provides implementations for a few methods. 
 * 

 */

public abstract class GenericActualElement implements IActualElement {

	/** unique identifier. */
	protected String uuid;

	/** name attribute. */
	protected String name;

	/** level attribute. */
	protected int level;

	/** occursed flag attribute. */
	protected boolean isOccursed;

	/** occurs count. */
	protected int occursCount;

	/** start and end positions. */
	protected int startPosition, endPosition;

	/** flag to indicate whether value has been mapped. */
	protected boolean isMappedToValue;

//	/** mapped value. */
//	protected String mappedValue;
	
	/** mapped bytes. */
	protected byte[] mappedBytes;

	/** mapping problems and errors. */
	protected Object mappingError, mappingWarning;

	/** parent element. */
	protected IActualElement parentElement;

	/** parent copybook. */
	protected CopyBook copyBook;

	/** has occursed element been extended. */
	protected boolean hasBeenExtended;

	/** dimension. */
	protected int dimension;

	/** indices. */
	protected int[] indices;

	/** usage clause. */
	protected Usage usage;
	
	/** is the occursed element extended. */
	protected boolean isExtended;
	


	/**
	 * Create a GenericActualElement using all fields.
	 * @param name
	 * @param level
	 * @param isOccursed
	 * @param occursCount
	 * @param parentElement
	 */
	protected GenericActualElement(String name, int level, boolean isOccursed, int occursCount, IActualElement parentElement) {
		super();
		this.name = name;
		this.level = level;
		this.isOccursed = isOccursed;
		this.occursCount = occursCount;
		this.startPosition = Integer.MIN_VALUE;
		this.endPosition = Integer.MIN_VALUE;
		this.hasBeenExtended = Boolean.FALSE;
		this.parentElement = parentElement;
		this.uuid = UUID.randomUUID().toString();
		this.indices = new int[0];
		this.isMappedToValue = false;
		this.isExtended = false;
	}

	/**
	 * Default constructor.
	 */
	protected GenericActualElement() {
		this(null, Integer.MIN_VALUE, Boolean.FALSE, Integer.MIN_VALUE, null );		
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getName()
	 */
	@Override
	public String getUuid() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getLevel()
	 */
	@Override
	public int getLevel() {
		return level;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#isOccursed()
	 */
	@Override
	public boolean isOccursed() {
		return isOccursed;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getOccursCount()
	 */
	@Override
	public int getOccursCount() {
		return occursCount;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getStartPosition()
	 */
	@Override
	public int getStartPosition() {
		return startPosition;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getEndPosition()
	 */
	@Override
	public int getEndPosition() {
		return endPosition;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getParentElement()
	 */
	@Override
	public IActualElement getParentElement() {
		return parentElement;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @param isOccursed the isOccursed to set
	 */
	public void setOccursed(boolean isOccursed) {
		this.isOccursed = isOccursed;
	}

	/**
	 * @param occursCount the occursCount to set
	 */
	public void setOccursCount(int occursCount) {
		this.occursCount = occursCount;
		
		if ( occursCount > 0 ) {
			isOccursed = true;
		}
		else {
			isOccursed = false;
		}
	}

	/**
	 * @param parentElement the parentElement to set
	 */
	@Override
	public void setParentElement(IActualElement parentElement) {
		this.parentElement = parentElement;
	}

	/**
	 * TODO
	 */
	@Override
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	/**
	 * TODO
	 */
	@Override
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	/**
	 * @return the hasBeenExtended
	 */
	public boolean hasBeenExtended() {
		return hasBeenExtended;
	}

	/**
	 * @param hasBeenExtended the hasBeenExtended to set
	 */
	public void setHasBeenExtended(boolean hasBeenExtended) {
		this.hasBeenExtended = hasBeenExtended;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getDimension()
	 */
	@Override
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * @param dimension the dimension to set
	 */
	@Override
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getIndices()
	 */
	@Override
	public int[] getIndices() {
		return indices;
	}

	/**
	 * @param indices the indices to set
	 */
	@Override
	public void setIndices(int[] indices) {
		this.indices = indices;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getMappedValue()
	 */
	@Override
	public String getMappedValue() {
		//return mappedValue;
		if ( mappedBytes != null ) {
			return new String( mappedBytes );
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getHexaMappedValue()
	 */
	@Override
	public String getHexaMappedValue() {
		//return stringToHex( mappedValue );
		if ( getMappedValue() != null ) {
			return stringToHex( getMappedValue() );
		}
		return null;
	}

	@Override
	public void setMappedValue(String value) {
		//mappedValue = value;
		mappedBytes = value.getBytes();
		isMappedToValue = true;
	}

	/** 
	 * TODO
	 */
	@Override
	public void mapToDataGlobal( String data ) {
		mappingWarning = "";
		mappingError = "";

		if ( data == null )	{
			mappingWarning = "ERROR:DATA NULL";
			//mappedValue = null;
			mappedBytes = null;
			return;
		}

		try	{
			//mappedValue = data.substring(startPosition - 1, endPosition - 1);
			mappedBytes = data.substring(startPosition - 1, endPosition - 1).getBytes();
			isMappedToValue = true;
		}
		catch( Exception e ){
			//mappedValue = null;
			mappedBytes = null;
			mappingError = "ERROR:UNABLE TO MAP DATA TO ELEMENT ("+e+")";
		}
	}

	/** 
	 * TODO
	 */
	@Override
	public void mapToDataLocal( String data ) {
		mappingWarning = "";
		mappingError = "";

		if ( data == null )	{
			mappingWarning = "ERROR:DATA NULL";
			//mappedValue = null;
			mappedBytes = null;
			return;
		}

		if ( data.length() > getSize() ) {
			//System.out.println( "data to map too large, we're gonna loose data" );
			//mappedValue = data.substring( 0, getSize() );
			mappedBytes = data.substring( 0, getSize() ).getBytes();
			isMappedToValue = true;
		}
		else if ( data.length() < getSize() ) {
			//System.out.println( "data to map too short, it's gonna cause problem" );
			//mappedValue = data.toString();
			mappedBytes = data.getBytes();
			isMappedToValue = true;
		}
		else {
			//System.out.println( "data to map perfectly fits!!!" );
			//mappedValue = data.toString();
			mappedBytes = data.getBytes();
			isMappedToValue = true;
		}
	}

	/**
	 * TODO
	 * @return the mappingError
	 */
	@Override
	public Object getMappingError() {
		return mappingError;
	}

	/**
	 * @return the mappingWarning
	 */
	@Override
	public Object getMappingWarning() {
		return mappingWarning;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#isMappedToValue()
	 */
	@Override
	public boolean isMappedToValue() {
		return isMappedToValue;
	}

	/** 
	 * Computes position of the object and its kids
	 */
	public void computePositions( int startPosition ) {
		this.startPosition = startPosition;
		this.endPosition = startPosition + getSize();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ( obj != null ) {
			GenericActualElement element = (GenericActualElement)obj;
			return (
					name.equals( element.getName() ) &&
					Arrays.equals( indices, element.getIndices() )
					);
		}
		return false;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return name.hashCode() + indices.hashCode();
	}

	/**
	 * @return level prefixed with zero if < 10 
	 */
	protected String formatLevel()
	{
		if ( level < 10 ) {
			return "0" + level;
		}
		else {
			return "" + level;
		}
	}

	/**
	 * @return the copybook
	 */
	@Override
	public CopyBook getCopyBook() {
		return copyBook;
	}

	/**
	 * @param copybook the copybook to set
	 */
	public void setCopybook(CopyBook copyBook) {
		this.copyBook = copyBook;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(formatLevel());
		buf.append(" ");
		buf.append(name);
		if ( dimension > 0 )
		{
			buf.append("[");
			StringBuffer indicesBuffer = new StringBuffer();
			for (int i : indices )
			{
				indicesBuffer.append(i);
				indicesBuffer.append(",");
			}

			buf.append( indicesBuffer.substring(0, indicesBuffer.length() - 1 ) );
			buf.append("]");
		}
		return buf.toString();
	}
	

	/**
	 * TODO
	 * @return
	 */
	protected String getIndicesAsString() {
		if ( indices.length == 0 ) {
			return "none";
		}
		else {
			String toReturn = new String(); 
			for ( int index : indices ) {
				toReturn += index + ",";
			}
			return toReturn.substring( 0, toReturn.length() - 1 );
		}
	}

	/**
	 * @return the usage
	 */
	public Usage getUsage() {
		return usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(Usage usage) {
		this.usage = usage;
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
	 * @return the isExtended
	 */
	@Override
	public boolean isExtended() {
		return isExtended;
	}

	/**
	 * @param isExtended the isExtended to set
	 */
	@Override
	public void setExtended(boolean isExtended) {
		this.isExtended = isExtended;
	}
}