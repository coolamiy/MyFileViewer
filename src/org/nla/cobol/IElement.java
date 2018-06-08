package org.nla.cobol;

import java.util.HashMap;

/**
 * Represents the most abstract element in a Cobol CopyBook. It can either be :
 *  - An IActualElement representing either a Variable or a Group.
 *  - An IVirtualElement, representing either a redefine clause or an 88 clause.

 */
public interface IElement {

	/**
	 * @return	unique identifier of the element.
	 */
	public String getUuid();

	/**
	 * @return	name of the element.
	 */
	public String getName();

	/**
	 * @return	level of the element in the copybook.
	 */
	public int getLevel();

	/**
	 * @return	parent element. null if <i>this</i> is a copybook.
	 */
	public IActualElement getParentElement();

	/**
	 * @return	copybook this element belongs to
	 */
	public CopyBook getCopyBook();


	/**
	 * @return	custom attributes
	 */
	public HashMap<String, Object> getAttributes();	
}
