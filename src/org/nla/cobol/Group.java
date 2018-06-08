package org.nla.cobol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;

/**
 * Represents a cobol group containing child elements. 
 
 */
public class Group extends GenericActualElement {

	/** child elements. */
	protected ArrayList<IActualElement> childElements;

	/** temporary map, used to compute occursed element indices. */
	protected HashMap<String, Integer> currentIndexForOccursedElementMap;

	/** size computed for performance issues.  */
	protected int size;
	
//	/** unextended group. */
//	protected Group unextended;

	/**
	 * @param name
	 * @param level
	 * @param isOccursed
	 * @param occursCount
	 * @param parentElement
	 */
	public Group(String name, int level, boolean isOccursed, int occursCount, IActualElement parentElement) {
		super(name, level, isOccursed, occursCount, parentElement );
		childElements = new ArrayList<IActualElement>();
		size = Integer.MIN_VALUE;
	}

	/**
	 * Default constructor. 
	 */
	public Group() {
		this(null, Integer.MIN_VALUE, Boolean.FALSE, Integer.MIN_VALUE, null);
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getSize()
	 */
	@Override
	public int getSize() {
		// if never computed, compute size
		if ( size == Integer.MIN_VALUE ) {
			int totalSize = 0;
			for ( IActualElement element : childElements ) {
				totalSize += element.getSize();
			}
			size = totalSize;
			
			// if non extended occursed element, multiply its size by occurs count
			if ( isOccursed && !isExtended ) {
				size *= occursCount;
			}
		}
		return size;
	}

	/**
	 * @return the childElements
	 */
	public ArrayList<IActualElement> getChildElements() {
		return childElements;
	}

	/**
	 * @param childElements the childElements to set
	 */
	public void setChildElements(ArrayList<IActualElement> childElements) {
		this.childElements = childElements;
	}

	/**
	 * @return a specific child element given its position in the child array
	 */
	public IActualElement getChildElement(int index) {
		return childElements.get(index);
	}

	/**
	 * @param a specific child element to add at the end of the child array
	 */
	public void addChildElement(IActualElement childElement) {
		childElements.add( childElement );
		childElement.setParentElement( this );
	}

	/**
	 * @param a specific child element to add at the end of the child array
	 */
	public void addChildElements(ArrayList<IActualElement> childElements) {
		for ( IActualElement childElement : childElements ) {
			addChildElement ( childElement );
		}
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return false;
	}



	/**
	 * Organize raw elements into structured groups as represented in the original copybook.
	 * This method must be called only once, before buildGroups() and computePositions().
	 * @param rootElement	root group of the copybook.
	 * @param itElements	iterator of the elements previously read.
	 */
	protected void buildGroups( Group rootElement, ListIterator<IActualElement> elements ) {
//		
		while ( elements.hasNext() ) {
			IActualElement element = elements.next();

			// untested
			if ( element.getLevel() == 88 ) {
				return;
			}

			if ( element.getLevel() <= rootElement.getLevel() ) {
				elements.previous();
				return;
			}

			if ( element.isOccursed() ) {
				//element.setOccursDepth( rootElement.getOccursDepth() + 1 );
			}
			else {
				//element.setOccursDepth( rootElement.getOccursDepth() );
			}

			if ( element instanceof Group ) {
				buildGroups( (Group)element, elements);
			}

			//				
			rootElement.addChildElement( element );

			if ( element.isOccursed() )
			{
				for ( int i = 1; i < element.getOccursCount(); ++i )
				{
					IActualElement clonedElement = element.cloneElement();
//					
					rootElement.addChildElement( clonedElement );
				}
			}
		}
	}

	/** 
	 * Computes position of the group and its kids.
	 * @param startPosition
	 */
	@Override
	public void computePositions( int startPosition ) {
		// logger.info("Entering Group.computePosition()");
		super.computePositions( startPosition );

		int intermediaryStartPosition = startPosition;
		for ( IActualElement iElement : childElements ) {
			if ( iElement instanceof Redefinition) {
				break;
			}
			GenericActualElement element = (GenericActualElement)iElement;
			element.computePositions( intermediaryStartPosition );
			intermediaryStartPosition += element.getSize();
		}
		// logger.info("Exiting Group.computePosition()");
	}

	/**
	 * Computes and build the element dimensions, depending on the occurs. 
	 */
	protected void buildDimensions() {
//		logger.debug("Entering buildDimensions()");

		// build occursed element indices for root element
		buildIndexForOccursedElementsMap();

		ListIterator<IActualElement> elements = getChildElements().listIterator();
		while( elements.hasNext() ) {
			IActualElement element = elements.next();
			if ( element instanceof Redefinition) {
				break;
			}
			
//			logger.debug(element.getName());
			if ( element.isOccursed() )	{
				element.setDimension(getDimension() +1);
				element.setIndices( buildNextOccursedChildIndices(getIndices(), getNextOccursedChildIndex(element.getName())) );
			}
			else {
				element.setDimension( getDimension() );
				element.setIndices( getIndices() );
			}

			if ( element instanceof Group ) {
				( (Group)element ).buildDimensions();
			}
		}
//		logger.debug("Exiting buildDimensions()");
	}

	/**
	 * TODO
	 */
	protected void buildIndexForOccursedElementsMap() {
//		logger.debug("Entering buildindexForOccursedElementsMap(Group)");
		currentIndexForOccursedElementMap = new HashMap<String,Integer>();
		Iterator<IActualElement> it = getChildElements().iterator();
		while ( it.hasNext() ) {
			IActualElement iElement = it.next();
			if ( iElement instanceof Redefinition) {
				break;
			}
			if ( iElement.isOccursed() && !currentIndexForOccursedElementMap.keySet().contains(iElement.getName()) ) {
				currentIndexForOccursedElementMap.put(iElement.getName(), 1);
			}
		}
//		logger.debug("Exiting buildindexForOccursedElementsMap(Group)");
	}

	protected int getNextOccursedChildIndex(String elementName) {
		int nextOccursedChildIndex = currentIndexForOccursedElementMap.get( elementName );
		currentIndexForOccursedElementMap.put( elementName, nextOccursedChildIndex +1 );
		return nextOccursedChildIndex;
	}
	
	

	/**
	 * 
	 * @param rootElement	root group from which to start 
	 */
	protected int[] buildNextOccursedChildIndices( int[] parentIndices, int nextIndex )	{
		int[] nextOccursedChildIndices = new int[parentIndices.length+1];

		System.arraycopy(parentIndices, 0, nextOccursedChildIndices, 0, parentIndices.length);
		nextOccursedChildIndices[parentIndices.length] = nextIndex;

		return nextOccursedChildIndices;
	}

	/**
	 * 
	 * @param rootElement	root group from which to start. 
	 */
	protected int[] buildNextChildIndices(int elementDimension)	{
		int[] nextChildIndices = new int[elementDimension];

		for ( int i : indices )	{
			nextChildIndices[i] = indices[i];
		}
		return nextChildIndices;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.GenericActualElement#mapToDataGlobal(java.lang.String)
	 */
	@Override
	public void mapToDataGlobal(String data) {
		super.mapToDataGlobal(data);
		for ( IActualElement iElement : childElements ) {
			if ( iElement instanceof Redefinition ) {
				break;
			}
			GenericActualElement element = (GenericActualElement)iElement;
			element.mapToDataGlobal( data );
		}
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.GenericActualElement#mapToData(java.lang.String)
	 */
	@Override
	public void mapToDataLocal(String data)	{
		super.mapToDataLocal(data);
		int startPosition = 0;
		for ( IActualElement iElement : childElements ) {
			if ( iElement instanceof Redefinition ) {
				break;
			}
			GenericActualElement element = (GenericActualElement)iElement;
			element.mapToDataLocal( data.substring( startPosition ) );
			startPosition += element.getSize();
		}
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#cloneElement()
	 */
	@Override
	public IActualElement cloneElement() {
		Group clone = new Group();
		clone.setName( name );
		clone.setLevel( level );
		clone.setOccursed( isOccursed );
		clone.setOccursCount( occursCount );
		clone.setParentElement( parentElement );
		clone.setDimension( dimension );
		clone.setUsage( usage );
		clone.setExtended( isExtended );
		clone.setStartPosition( startPosition );
		clone.setEndPosition( endPosition );
		
		
		int[] clonedIndices = new int[indices.length];
		System.arraycopy(indices, 0, clonedIndices, 0, indices.length);
		clone.setIndices( clonedIndices );
		
		
//		if ( unextended != null ) {
//			clone.setUnextended( (Group)unextended.cloneElement() );
//		}

		for ( IActualElement childElement : childElements ) {
			clone.addChildElement( childElement.cloneElement() );
		}

		return clone;
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getDisplayableValue(int)
	 */
	@Override
	public String getDisplayableValue(int mode) {
		if ( mode != STD_VIEW && mode != HEXA_VIEW && mode != NO_VALUE_VIEW ) {
			throw ( new IllegalArgumentException() );
		}

		StringBuffer buf = new StringBuffer(); 
		buf.append( super.toString() );
		//if ( mode != NO_VALUE_VIEW && mappedValue != null )	{
		if ( mode != NO_VALUE_VIEW && mappedBytes != null )	{
			buf.append(" [");
			//buf.append( ( mode == STD_VIEW ? mappedValue : GenericActualElement.stringToHex( mappedValue ) ) );
			buf.append( ( mode == STD_VIEW ? new String( mappedBytes ) : GenericActualElement.stringToHex( getMappedValue() ) ) );
			buf.append("]");
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#initialize()
	 */
	@Override
	public String initialize() {
		StringBuffer groupDefaultValue = new StringBuffer();
		for ( IActualElement iElement : childElements ) {
			groupDefaultValue.append( iElement.initialize() );
		}
		mapToDataLocal( groupDefaultValue.toString() );
		
		return groupDefaultValue.toString();
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IActualElement#getAttributesNew()
	 */
	@Override
	public HashMap<String, Object> getAttributes() {
		LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>( 12 );

		// generic
		attributes.put( "uuid", getUuid() );
		attributes.put( "level", getLevel() + "" );
		attributes.put( "name", getName() );
		attributes.put( "size", getSize() + "" );
		attributes.put( "isOccursed", ( isOccursed() ? getOccursCount() + "" : "none" ) );
		attributes.put( "parent", getParentElement() == null ? "none" : getParentElement().getName() );
		
		// specific
		attributes.put( "indices", getIndicesAsString() );
		attributes.put( "startPosition", getStartPosition() + "" );
		attributes.put( "endPosition", getEndPosition() + "" );
		attributes.put( "value", ( getMappedValue() == null ? "none" : getMappedValue() ) );

		return attributes;
	}
	
	/**
	 * Build a copybook from this group
	 * @return
	 */
	public CopyBook buildCopyBookFromGroup() {
		CopyBook builtCopyBook = new CopyBook();
		
		builtCopyBook.setName( name );
		builtCopyBook.addChildElements( childElements );
		
		return builtCopyBook;
	}
}