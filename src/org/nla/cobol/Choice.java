
package org.nla.cobol;

import java.util.HashMap;


/**
 * Represents a 88 level clause attached to a variable.
 * @author S573642
 */
public class Choice extends GenericVirtualElement {

	/** choice value. */
	protected String choiceValue;

	/**
	 * Default constructor.
	 */
	public Choice() {
		super();
		level = 88;
		choiceValue = null;
	}

	/**
	 * @return the choiceValue
	 */
	public String getChoiceValue() {
		return choiceValue;
	}

	/**
	 * @param choiceValue the choiceValue to set
	 */
	public void setChoiceValue(String choiceValue) {
		this.choiceValue = choiceValue;
	}

	/**
	 *	Clone this Choice element.
	 *	@return	A cloned version of this Choice.
	 */
	public Choice cloneChoice() {
		Choice clonedChoice = new Choice();
		clonedChoice.setCopyBook( copyBook );
		clonedChoice.setLevel( level );
		clonedChoice.setParentElement( parentElement );
		clonedChoice.setName( name );
		clonedChoice.setChoiceValue( choiceValue );

		return clonedChoice;
	}

	/* (non-Javadoc)
	 * @see org.nla.cobol.IElement#getAttributes()
	 */
	@Override
	public HashMap<String, Object> getAttributes() {
		HashMap<String, Object> attributes = super.getAttributes();
		attributes.put( "choiceValue", getChoiceValue() );
		return attributes;
	}
}
