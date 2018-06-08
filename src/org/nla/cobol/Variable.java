
package org.nla.cobol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.nla.util.ByteUtil;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


@SuppressWarnings("unused")
public class Variable extends GenericActualElement {

	/** type. */
	protected Type type;

	/** default value. */
	protected String defaultValue;
	
	/** choice list. */
	protected ChoiceList choiceList;
	
	/**
	 * @param name
	 * @param level
	 * @param isOccursed
	 * @param occursCount
	 * @param type
	 */
	public Variable(String name, int level, boolean isOccursed, int occursCount, String defaultValue, Type type, Usage usage, IActualElement parentElement) {
		super( name, level, isOccursed, occursCount, parentElement );
		this.type = type;
		this.defaultValue = defaultValue;
		this.choiceList = new ChoiceList();
		this.usage = usage;
	}

	/**
	 * Default constructor
	 */
	public Variable() {
		this( null, Integer.MIN_VALUE, Boolean.FALSE, Integer.MIN_VALUE, null, null, null, null );
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getSize()
	 */
	@Override
	public int getSize(){
		int size = type.getSize();
		
		// if packed element, divide by 2, add 1, cast to int
		if ( usage == Usage.comp3 )
		{
			size = ( size / 2 ) + 1; 
		}
		
		// if non extended occursed element, multiply its size by occurs count
		if ( isOccursed && !isExtended ) {
			size *= occursCount;
		}
		
		return size;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
		initializeDefaultValue();
	}

	/**
	 * Return the default value.
	 * @return	the default value.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Initialize default value of the variable if none is already defined.
	 */
	public void initializeDefaultValue() {
		if ( defaultValue == null ) {
			StringBuffer buf = new StringBuffer();

			for ( int i = 0; i < getSize(); ++i ) {
				buf.append( ( type.getPicture() == Picture.Pic9 ? "0" : " " ) );
			}

			defaultValue = buf.toString();
		}
	}

	/**
	 * TODO
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the choiceList
	 */
	public ChoiceList getChoiceList() {
		return choiceList;
	}

	/**
	 * TODO
	 * @param choiceList the choiceList to set
	 */
	public void setChoiceList(ChoiceList choiceList) {
		this.choiceList = choiceList;
	}
	
	
	/**
	 * TODO
	 * @param choiceList the choiceList to set
	 */
	public void addChoice(Choice choice) {
		if ( choiceList != null ) {
			choiceList.addChoice( choice );
		}
	}

	/**
	 * TODO
	 * Tell whether one or several choices have been defined on that variable.
	 * @return
	 */
	public boolean hasChoices() {
		return( choiceList != null && choiceList.size() > 0 );
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public String initialize() {
		mapToDataLocal( defaultValue );
		return defaultValue;
	}

	/** 
	 * TODO
	 */
	@Override
	public void mapToDataLocal( String data ) {
		
		if ( usage == Usage.comp3 ) {
			byte[] packedData = ByteUtil.packDigits( data );
			
			if ( packedData.length > getSize() ) {
				//System.out.println( "data to map too large, we're gonna loose data" );
				//mappedValue = data.substring( 0, getSize() );
				isMappedToValue = true;
				mappedBytes = new byte[getSize()];

			}
			else if ( packedData.length < getSize() ) {
				//System.out.println( "data to map too short, it's gonna cause problem" );
				//mappedValue = data.toString();
				isMappedToValue = true;
				mappedBytes = new byte[getSize()];
			}
			else {
				//System.out.println( "data to map perfectly fits!!!" );
				//mappedValue = data.toString();
				mappedBytes = packedData;
				isMappedToValue = true;
			}
		}
		else {
			super.mapToDataLocal( data );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#cloneElement()
	 */
	@Override
	public IActualElement cloneElement() {
		Variable clone = new Variable();
		clone.setName( name );
		clone.setLevel( level );
		clone.setOccursed( isOccursed );
		clone.setOccursCount( occursCount );
		clone.setParentElement( parentElement );
		clone.setType( type );
		clone.setDimension( dimension );
		clone.setUsage( usage );
		clone.setExtended( isExtended );
		clone.setStartPosition( startPosition );
		clone.setEndPosition( endPosition );
		
		int[] clonedIndices = new int[indices.length];
		System.arraycopy( indices, 0, clonedIndices, 0, indices.length );
		clone.setIndices( clonedIndices );
		
		return clone;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getDisplayableValue(int)
	 */
	@Override
	public String getDisplayableValue(int mode) {
		if ( mode != STD_VIEW && mode != HEXA_VIEW )
		{
			throw ( new IllegalArgumentException() );
		}
		StringBuffer buf = new StringBuffer();
		buf.append( super.toString() );
		buf.append( " " );
		buf.append( type );
		if ( usage != null ) {
			buf.append( " " );
			buf.append( usage.toString().toUpperCase() );
		}
		//if ( mappedValue != null ) {
		if ( mappedBytes != null ) {
			buf.append(" [");
			buf.append( ( mode == STD_VIEW && usage != Usage.comp3 ? getMappedValue() : GenericActualElement.stringToHex( getMappedValue() ) ) );
			buf.append("]");
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getAttributesNew()
	 */
	@Override
	public HashMap<String, Object> getAttributes() {
		LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>( 14 );

		// generic
		attributes.put( "uuid", getUuid() );
		attributes.put( "level", getLevel() + "" );
		attributes.put( "name", getName() );
		attributes.put( "type", getType().toString() );
//		attributes.put( "isPacked", ( getType().isPacked() ? "true" : "false" ) );
		attributes.put( "isPacked", "false" );
		attributes.put( "size", getSize() + "" );
		attributes.put( "isOccursed", ( isOccursed() ? getOccursCount() + "" : "none" ) );
		attributes.put( "default", getDefaultValue() == null ? "none" : "'" + getDefaultValue() + "'" );
		attributes.put( "parent", getParentElement() == null ? "none" : getParentElement().getName() );
				
		// specific
		attributes.put( "indices", getIndicesAsString() );
		attributes.put( "startPosition", getStartPosition() + "" );
		attributes.put( "endPosition", getEndPosition() + "" );
		attributes.put( "value", ( getMappedValue() == null ? "none" : getMappedValue() ) );

		// choice list
		attributes.put( "choiceList", getChoiceList() );
		
		return attributes;
	}
	

}