package org.nla.cobol.parsing;

import java.util.ArrayList;


public class LexemeSequence extends ArrayList<Lexeme> {

	/** serial version uid. */
	private static final long serialVersionUID = 1887533046333116879L;
	
	/** current index*/
	private int currentIndex = 0;

	/**
	 * TODO
	 * @return
	 */
	public Lexeme currentLexeme() {
		if ( currentIndex != size() ) {
			return get( currentIndex );
		}
		else {
			return Lexeme.eosLexem;
		}
	}

	/**
	 * TODO
	 * @return
	 */
	public Lexeme nextLexeme() {
		if ( currentIndex != size() ) {
			return get( currentIndex + 1 );
		}
		else {
			return null;
		}
	}

	/**
	 * TODO
	 * @return
	 */
	public Lexeme previousLexeme() {
		if ( currentIndex > 0 ) {
			return get( currentIndex - 1 );
		}
		else {
			return null;
		}
	}

	/**
	 * Consume the current lexeme and assert its nature. If its nature is differtent from the specified nature,
	 * a ParsingException is thrown.
	 * @param nature
	 */
	public Lexeme consumeLexeme( LexemeNature nature ) {
		Lexeme currentLexeme = currentLexeme(); 
		if ( currentLexeme().getNature() != nature ) {
//			System.out.println( "Sequence :" );
//			System.out.println( this );
			throw new ParsingException( "Line " + currentLexeme().getLineNumber() + ", " + currentLexeme().getNature() + "(" + currentLexeme().getValue() +") was found but " + nature + " was expected" );
		}
		currentIndex += 1;
		//System.out.println("  Current Lexeme: " + currentLexeme().getNature() + "(" + currentLexeme().getValue() + ")" );
//		System.out.println( "Consumed " + currentLexeme );
		return currentLexeme;
	}

	/**
	 * Consume the potential current lexeme.
	 * @param nature
	 */
	public Lexeme consumePotentialLexeme( LexemeNature nature ) {
		Lexeme currentLexeme = currentLexeme(); 
		if ( currentLexeme().getNature() == nature ) {
			consumeLexeme( nature );
			//currentIndex += 1;
			//System.out.println("  Current Lexeme: " + currentLexeme().getNature() );
		}
		return currentLexeme;
	}
	
	/**
	 * Consume the current lexeme and assert its nature and value. If its nature or is value is differtent from the 
	 * specified ones, a ParsingException is thrown. The case is ignored for the assertion on the value.
	 * @param nature
	 * @param value
	 */
	public Lexeme consumeLexeme( LexemeNature nature, String value ) {
		Lexeme currentLexeme = currentLexeme(); 
		if ( currentLexeme().getNature() != nature || !currentLexeme().getValue().equalsIgnoreCase( value ) ) {
			throw new ParsingException( "(" + nature + "|" + value + ") was expected, but (" + currentLexeme().getNature() + "|" + currentLexeme().getValue() + ") was found" );
		}
		currentIndex += 1;
		//System.out.println("  Current Lexeme: " + currentLexeme().getNature() );
		return currentLexeme;
	}

	/**
	 * TODO
	 * Consume the current lexeme.
	 * @param nature
	 */
	public Lexeme consumeLexeme() {
		if ( currentIndex != size() ) {
			Lexeme currentLexeme = currentLexeme();
			currentIndex += 1;
		//	System.out.println("  Current Lexeme: " + currentLexeme().getNature() );
			return currentLexeme;			
		}
		else {
			return null;
		}		
	}
	
	/**
	 * Tell whether an element is a variable, that is 'PIC' or 'PICTURE' is found before next '.'.
	 * @return	true if the element is a variable
	 */
	public boolean isElementAVariable() {
		boolean goOnScanning = true;
		for ( int i = 1; goOnScanning; ++i ) {
			Lexeme lex = lookAheadForLexeme( i );
			
			// if 'PIC' or 'PICTURE' is found, return ok
			if ( lex.getNature() == LexemeNature.picture ) {
				return true;
			}
			
			// if either '.' or eos is found, stop scanning 
			goOnScanning = lex.getNature() != LexemeNature.dot && lex.getNature() != LexemeNature.eos ; 
		}
			
		return false;
	}
	
	/**
	 * Tell whether the next 3 lexemes define a usage clause
	 * @return	true if the next 3 lexemes define a usage clause
	 */
	public boolean isUsageClause() {
		Lexeme currentLexeme = currentLexeme();
		Lexeme nextLexeme = nextLexeme();
		
		if ( 		currentLexeme.getNature() == LexemeNature.usage 
				||	( 
							nextLexeme.getNature() == LexemeNature.binary
						|| 	nextLexeme.getNature() == LexemeNature.comp1
						|| 	nextLexeme.getNature() == LexemeNature.comp2
						|| 	nextLexeme.getNature() == LexemeNature.comp3
						|| 	nextLexeme.getNature() == LexemeNature.comp4	
						|| 	nextLexeme.getNature() == LexemeNature.display
						|| 	nextLexeme.getNature() == LexemeNature.display1
						|| 	nextLexeme.getNature() == LexemeNature.index
						|| 	nextLexeme.getNature() == LexemeNature.packed
						|| 	nextLexeme.getNature() == LexemeNature.pointer
					)
			) {
			return true;
		}
		return false;
	}
	
	/**
	 * TODO
	 * @param position
	 * @return
	 */
	public Lexeme lookAheadForLexeme( int position ) {
		if ( currentIndex + position < size() ) {
			return get( currentIndex + position );
		}
		else {
			return null;
		}
	}
	
	/**
	 * TODO
	 * @param lex
	 */
	public void insertLexeme( Lexeme lex ) {
		add( currentIndex, lex );
	}
	
	/**
	 * Set the line number on each lexeme, and remove all lexeme of nature lineNumber.   
	 */
	public void refactor() {
		for ( int i = 0; i < size(); ++i ) {
			if ( get( i ).getNature() == LexemeNature.lineNumber ) {
				
				Lexeme lineNumberLexeme = get( i );
				int lineNumber = lineNumberLexeme.getValueAsInt();
				remove( lineNumberLexeme );
				--i;
				
				for( int j = i + 1; j < size() && get( j ).getNature() != LexemeNature.lineNumber ; ++j ) {
					Lexeme lex = get( j );
					lex.setLineNumber( lineNumber );
					////System.out.println( j + " - Line " + lineNumber + " set for Lexeme " + lex );
					++i;
				}

			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		String toReturn = new String();
		for ( Lexeme lex : this ) {
			toReturn += ( lex.toString() + "\n" );
		}
		return toReturn;
	}
}
