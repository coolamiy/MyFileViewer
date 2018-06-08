
package org.nla.cobol.parsing;


/**
 *	Exception thrown by CopyBookParser 

 */
public class ParsingException extends RuntimeException
{
	/** serial version uid. */
	private static final long serialVersionUID = -2195587537132283061L;

	/** exception message */
	protected String message; 
		
	/** inner exception */
	protected Exception innerException;

	/**
	 * @param message
	 */
	protected ParsingException(String message) {
		this.message = message;
	}

	/**
	 * @param innerException
	 */
	protected ParsingException(Exception innerException) {
		this.innerException = innerException;
	}

	/**
	 * @param message
	 * @param innerException
	 */
	protected ParsingException(String message, Exception innerException) {
		this.message = message;
		this.innerException = innerException;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParsingException [innerException=" + innerException
				+ ", message=" + message + "]";
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
