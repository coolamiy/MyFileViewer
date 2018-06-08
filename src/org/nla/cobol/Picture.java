package org.nla.cobol;

/**
 * Represents an enumeration of all possible picture types. 
 
 */
public enum Picture
{
	PicX,
	Pic9;

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		if ( equals(Pic9) )
		{
			return "9";
		}
		else
		{
			return "X";
		}
	}
}
