
package org.nla.cobol;

/**
 * Represents a Cobol element that has a proper existence in a CopyBook. It can either be :
 *  - a Variable, for example 15 MYVAR PIC X(6)  VALUE '123456'.
 *  - a Group, for example 10 ALL-VARS OCCURS 3.

 */
public interface IActualElement extends IElement {

	/** hexadecimal.  */
	public static final int HEXA_VIEW = 1;
	
	/** standard. */
	public static final int STD_VIEW = 2;
	
	/** no value.  */
	public static final int NO_VALUE_VIEW = 3;

	/**
	 * @return	size of the element, including occurs attributes.
	 */
	public int getSize();

	/**
	 * @return	boolean indicating whether the element is occursed.
	 */
	public boolean isOccursed();

	/**
	 * @return	occurs count, 1 if not occursed.
	 */
	public int getOccursCount();

	/**
	 * @return	dimension of element.
	 */
	public int getDimension();

	/**
	 * set dimension of element.
	 */
	public void setDimension(int dimension);

	/**
	 * @return	indices of element, its size depending on dimension.
	 */
	public int[] getIndices();

	/**
	 * set indices of element.
	 */
	public void setIndices(int[] indices);

	/**
	 * @return	start position of the element in the copy.
	 */
	public int getStartPosition();

	/**
	 * @return	end position of the element in the copy.
	 */
	public int getEndPosition();

	/**
	 * @param startPosition the startPosition to set
	 */	
	public void setStartPosition( int startPosition );

	/**
	 * @param endPosition the endPosition to set
	 */
	public void setEndPosition( int endPosition );
	
	/**
	 * Tells whether a value has been mapped to this element.
	 * @return	whether the element is mapped to a value.
	 */
	public boolean isMappedToValue();

	/**
	 * @return	value of the element if mapped with data.
	 */
	public String getMappedValue();

	/**
	 * @return	value of the element if mapped with data.
	 */
	public void setMappedValue(String value);

	/**
	 * TODO
	 */
	public void mapToDataGlobal(String value);

	/**
	 * TODO
	 */
	public void mapToDataLocal(String value);

	/**
	 * @return	value of the element if mapped with data.
	 */
	public String getHexaMappedValue();

	/**
	 * @return	displayable value
	 */
	public String getDisplayableValue( int mode );

	/**
	 * @return	mapping warning
	 */
	public Object getMappingWarning();

	/**
	 * @return	mapping error
	 */
	public Object getMappingError();

	/**
	 * set the parent element
	 * @param parentElement the parent element 
	 */
	public void setParentElement( IActualElement parentElement );

	/**
	 * @return	whether the element is a leaf
	 */
	public boolean isLeaf();
	
	/**
	 * initialize element.
	 * @return	initialized value
	 */
	public String initialize();
	
	/**
	 * @return	cloned element
	 */
	public IActualElement cloneElement();
	
	public boolean isExtended();
	
	public void setExtended( boolean extended );
}
