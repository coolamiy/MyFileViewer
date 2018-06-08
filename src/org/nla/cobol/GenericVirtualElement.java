package org.nla.cobol;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;


public abstract class GenericVirtualElement implements IElement {

	/** unique identifier. */
	protected String uuid;

	/** name attribute. */
	protected String name;

	/** level attribute. */
	protected int level;

	/** parent element. */
	protected IActualElement parentElement;

	/** parent copybook. */
	protected CopyBook copyBook;

	/**
	 * Constructor using fields.
	 * @param name
	 * @param level
	 * @param parentElement
	 * @param copyBook
	 */
	public GenericVirtualElement( String name, int level, IActualElement parentElement, CopyBook copyBook ) {
		this.name = name;
		this.level = level;
		this.parentElement = parentElement;
		this.copyBook = copyBook;
		this.uuid = UUID.randomUUID().toString();
	}

	/**
	 * Default constructor.
	 */
	public GenericVirtualElement() {
		this( null, Integer.MIN_VALUE, null, null );
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getAttributes()
	 */
	@Override
	public HashMap<String, Object> getAttributes() {
		LinkedHashMap<String, Object> attributes = new LinkedHashMap<String, Object>( 3 );
		attributes.put( "uuid", getUuid() );
		attributes.put( "level", getLevel() + "" );
		attributes.put( "name", getName() );
		return attributes;
	}


	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getCopyBook()
	 */
	@Override
	public CopyBook getCopyBook() {
		return copyBook;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getLevel()
	 */
	@Override
	public int getLevel() {
		return level;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getParentElement()
	 */
	@Override
	public IActualElement getParentElement() {
		return parentElement;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getUuid()
	 */
	@Override
	public String getUuid() {
		return uuid;
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
	 * @param parentElement the parentElement to set
	 */
	public void setParentElement(IActualElement parentElement) {
		this.parentElement = parentElement;
	}

	/**
	 * @param copyBook the copyBook to set
	 */
	public void setCopyBook(CopyBook copyBook) {
		this.copyBook = copyBook;
	}
}
