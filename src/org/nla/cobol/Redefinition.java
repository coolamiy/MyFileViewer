
package org.nla.cobol;

import java.util.HashMap;

public class Redefinition extends GenericVirtualElement {

	/** redefined element. */
	protected IActualElement redefinedElement;

	/**
	 *	Clone this Redefinition element.
	 *	@return	A cloned version of this Redefinion.
	 */
	public Redefinition cloneRedefinition() {
		Redefinition clonedRedefinition = new Redefinition();
		clonedRedefinition.setCopyBook( copyBook );
		clonedRedefinition.setLevel( level );
		clonedRedefinition.setParentElement( parentElement );
		clonedRedefinition.setName( name );
		clonedRedefinition.setRedefinedElement( redefinedElement );

		return clonedRedefinition;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getAttributes()
	 */
	@Override
	public HashMap<String, Object> getAttributes() {
		HashMap<String, Object> attributes = super.getAttributes();
		attributes.put( "redefinedElement", getRedefinedElement().getName() );
		attributes.put( "redefinition", "yet to be implemented" );
		return attributes;
	}

	/**
	 * @return the redefinedElement
	 */
	public IActualElement getRedefinedElement() {
		return redefinedElement;
	}

	/**
	 * @param redefinedElement the redefinedElement to set
	 */
	public void setRedefinedElement(IActualElement redefinedElement) {
		this.redefinedElement = redefinedElement;
	}
}
