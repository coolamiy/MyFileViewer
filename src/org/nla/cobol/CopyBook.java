package org.nla.cobol;

import java.util.ArrayList;



public class CopyBook extends Group {
	
	private static final String COPYBOOK_STD_NAME = "UNDEFINED"; 

//	/** all element names. */
//	//private ArrayList<String> elementNames = new ArrayList<String>();
//
	/** unxtended version of the copybook.*/
	private CopyBook unextendedCopyBook;
	
	


	/**
	 *	Default constructor. Sets an artificial zero level.
	 */
	public CopyBook() {
		this( COPYBOOK_STD_NAME );
	}

	/**
	 * @param name
	 */
	public CopyBook( String name ) {
		super(name, 0, Boolean.FALSE, 0, null);
		dimension = 0;
		level = 1;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.GenericActualElement#getName()
	 */
	@Override
	public String getName() {
		//return childElements.get(0).getName();
		return name;
	}
	
	/**
	 * Tell whether name has been specified.
	 * @return
	 */
	public boolean isNameSpecified() {
		return ( name != null && !name.toUpperCase().equals( COPYBOOK_STD_NAME ) );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}


	public void buildCopyBookFromRawElements( ArrayList<IActualElement> rawElements) {
		//buildGroups(this, rawElements.listIterator());
		buildDimensions();
		computePositions(1);
	}
	
	/**
	 * 
	 */
	public void finalise() {
		buildDimensions();
		computePositions(1);
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.Group#mapToData(java.lang.String)
	 */
	@Override
	public void mapToDataGlobal(String data) {
		super.mapToDataGlobal(data);

		if ( data.length() > getSize() ) {
			mappingWarning = "WARNING:SIZE OF DATA ("+data.length()+") GREATER THAN SIZE OF COPYBOOK ("+getSize()+")";
		}
	}
	
	/**
	 * Return a non extended version of the copybook
	 * @return
	 */
	public CopyBook getUnextendedCopyBook() {
		return unextendedCopyBook;
	}
	
	/**
	 * Set the unextended CopyBook
	 * @param unextendedCopyBook
	 */
	public void setUnextendedCopyBook( CopyBook unextendedCopyBook ) {
		this.unextendedCopyBook = unextendedCopyBook;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#cloneElement()
	 */
	@Override
	public IActualElement cloneElement() {
		CopyBook clone = new CopyBook();
		clone.setName( name );
		clone.setLevel( level );
		clone.setStartPosition( startPosition );
		clone.setEndPosition( endPosition );


		for ( IActualElement childElement : childElements ) {
			clone.addChildElement( childElement.cloneElement() );
		}

		return clone;
	}
}
