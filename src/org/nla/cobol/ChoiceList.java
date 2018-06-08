package org.nla.cobol;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ChoiceList extends LinkedHashMap<String, Choice> {

	/** serial version uid. */
	private static final long serialVersionUID = 2254002852369813382L;

	/**
	 * Add a choice to the list.
	 * @param c
	 */
	public void addChoice( Choice c ) {
		put( c.getName(), c );
	}

	/**
	 * Retrieve a choice from the list using its name.
	 * @param choiceName
	 * @return	the choice whose name corresponds to the specified String, null otherwise.
	 */
	public Choice getChoice( String choiceName ) {
		return get( choiceName );
	}

	/**
	 *	Return the choice list as an ArrayList 
	 */
	public ArrayList<Choice> getChoices() {
		return new ArrayList<Choice>( values() );
	}
}
