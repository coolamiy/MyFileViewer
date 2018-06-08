package org.nla.cobol.parsing;


public class Scanner {

	/** lexeme stream. */
	private String stream;

	/** lexeme sequence. */
	private LexemeSequence sequence = new LexemeSequence();

	/** current index. */
	private int currentIndex = 0;
	
	/** current line. */
	private int currentLine = 0;

	/**
	 * @param stream
	 */
	public Scanner(String stream) {
		super();
		this.stream = stream;
		//System.out.println( "stream to scan: " + stream );
	}

	/** scan stream. */
	public LexemeSequence scan() {
		
		
		boolean goOnReading = true;
		while ( goOnReading ) {
			try {
				// read the next lexeme in stream
				Lexeme readLexeme = readLexeme();
				
				if ( readLexeme.getNature() == LexemeNature.lineNumber ) {
					currentLine = readLexeme.getValueAsInt(); 
				}
				
				else if ( readLexeme.getNature() != LexemeNature.eject ) {
					readLexeme.setLineNumber( currentLine );
					sequence.add( readLexeme );
				}
			}
			catch( Exception e ) {
				goOnReading = false;
			}
		}
		//System.out.println( "Toc scan " + System.currentTimeMillis() );
		return sequence;
	}

	/**
	 * 
	 * @return
	 */
	protected char currentChar() {
		return stream.charAt( currentIndex );
	}

	/**
	 *
	 * @return
	 */
	protected char previousChar() {
		return stream.charAt( currentIndex - 1 );
	}

	/**
	 *
	 * @return
	 */
	protected char nextChar() {
		return stream.charAt( currentIndex + 1 );
	}

	/**
	 *
	 */
	protected void consumeChar() {
		currentIndex += 1;
	}

	/**
	 *
	 */
	protected boolean eos() {
		return ( currentIndex == stream.length() - 1 );
	}

	/**
	 *
	 * @param startPosition
	 * @return
	 */
	protected Lexeme readLexeme() {

		// remove front spaces
		while( !eos() && currentChar() == Lexeme.chrSpace) {
			consumeChar();
		}

		// opening bracket
		if ( currentChar() == Lexeme.chrClosingBracket ) {
			consumeChar();
			return new Lexeme( LexemeNature.closingBracket );
		}

		// closing bracket
		else if ( currentChar() == Lexeme.chrOpeningBracket ) {
			consumeChar();
			return new Lexeme( LexemeNature.openingBracket );
		}

		// dot
		else if ( currentChar() == Lexeme.chrDot ) {
			consumeChar();
			return new Lexeme( LexemeNature.dot );
		}
		
		

		// quote
//		else if ( currentChar() == Lexeme.chrQuote ) {
//			consumeChar();
//			return new Lexeme( LexemeNature.quote );
//		}
		
		// complex lexeme
		else {
			StringBuffer lexemeValue = new StringBuffer();
			lexemeValue.append( currentChar() );
			consumeChar();

			boolean keepReading;

			// if lexeme starts with a dash, it's gonna be followed by the line number 
			if ( lexemeValue.toString().startsWith( "#" ) ) {
				keepReading = !eos() && currentChar() != '#';
			}

			// if lexeme starts with a quote, keep reading characters till next quote
			else if ( lexemeValue.toString().startsWith( Lexeme.strQuote ) ) {
				keepReading = !eos() && currentChar() != Lexeme.chrQuote;
			}
			
			// if lexeme starts with a double quote, keep reading characters till next double quote
			else if ( lexemeValue.toString().startsWith( Lexeme.strDblQuote ) ) {
				keepReading = !eos() && currentChar() != Lexeme.chrDblQuote;
			}
			
			// else normal condition
			else {
				keepReading = 		!eos()
								&&	currentChar() != Lexeme.chrDot
								&&	currentChar() != Lexeme.chrOpeningBracket
								&&	currentChar() != Lexeme.chrClosingBracket 
								&&	currentChar() != Lexeme.chrSpace 
								&&	currentChar() != Lexeme.chrQuote;
			}
	//		while ( 	!eos()
	//				&&	currentChar() != Lexeme.chrDot
	//				&&	currentChar() != Lexeme.chrOpeningBracket
	//				&&	currentChar() != Lexeme.chrClosingBracket 
	//				&&	currentChar() != Lexeme.chrSpace 
	//				&&	currentChar() != Lexeme.chrQuote ) {
			while ( keepReading ) {

				if ( currentChar() != '\t' && currentChar() != '\n' && currentChar() != '\r' ) {
					lexemeValue.append( currentChar() );
				}
				consumeChar();
	
				

				// if lexeme starts with a dash, keep reading characters till next dash
				if ( lexemeValue.toString().startsWith( "#" ) ) {
					keepReading = !eos() && currentChar() != '#';
				}

				// if lexeme starts with a quote, keep reading characters till next quote
				else if ( lexemeValue.toString().startsWith( Lexeme.strQuote ) ) {
					keepReading = !eos() && currentChar() != Lexeme.chrQuote;
				}

				// if lexeme starts with a double quote, keep reading characters till next double quote
				else if ( lexemeValue.toString().startsWith( Lexeme.strDblQuote ) ) {
					keepReading = !eos() && currentChar() != Lexeme.chrDblQuote;
				}
				
				// else normal condition
				else {
					keepReading = 		!eos()
									&&	currentChar() != Lexeme.chrDot
									&&	currentChar() != Lexeme.chrOpeningBracket
									&&	currentChar() != Lexeme.chrClosingBracket 
									&&	currentChar() != Lexeme.chrSpace 
									&&	currentChar() != Lexeme.chrQuote
									&&	currentChar() != Lexeme.chrDblQuote;
				}
			}
			
			String lexemeValueAsString = lexemeValue.toString();
			//System.out.println( "lexemeValueAsString: " + lexemeValueAsString );
			
//			
//			potential final characters to consume
//
			
			// if lexeme = eject, return null
			if ( lexemeValueAsString.equalsIgnoreCase( Lexeme.strEjectKW ) ) {
				return new Lexeme( LexemeNature.eject );
			}
			
			// if lexeme starts with dash and current char is a dash, consume it
			else if ( lexemeValueAsString.startsWith( "#" ) && currentChar() == '#' ) {
				consumeChar();
			}
			
			// if lexeme starts with quote and current char is a quote, consume it and add it to lexeme value
			else if ( lexemeValueAsString.startsWith( Lexeme.strQuote ) && currentChar() == Lexeme.chrQuote ) {
				lexemeValue.append( currentChar() );
				lexemeValueAsString = lexemeValue.toString();
				consumeChar();
			}
			
			// if lexeme starts with double quote and current char is a double quote, consume it and add it to lexeme value  
			else if ( lexemeValueAsString.startsWith( Lexeme.strDblQuote ) && currentChar() == Lexeme.chrDblQuote ) {
				lexemeValue.append( currentChar() );
				lexemeValueAsString = lexemeValue.toString();
				consumeChar();
			}

//			
//			lexemes to return
//
			
			// line number
			if ( lexemeValueAsString.startsWith( "#" ) ) {
				//currentLine = Integer.parseInt( lexemeValueAsString.substring( 1 ) );
				return new Lexeme( LexemeNature.lineNumber, lexemeValueAsString.substring( 1 ) );
			}
			
			// keyword
			else if ( Lexeme.keyWordExists( lexemeValueAsString ) ) {
				return Lexeme.getLexemeForKeyWord( lexemeValueAsString );
			}
			// literal
			else {
				return new Lexeme( LexemeNature.literal, lexemeValueAsString );
			}
		}
	}

//	public static void main(String[] args) {
//		String s = "88 888 '88' COMP-4 PIC '9' 'OCCURS' OCCURS 999V9999 )AAABBB 'REDEFINE' X COMP-3   REDEFINES VALUE 'test' ZZ  BINARY COMP -3  V PIC .  OCC(";
//		System.out.println( s );
//		Scanner scanner = new Scanner( s );
//		LexemeSequence seq = scanner.scan();
//
//		System.out.println( seq );
//	}
}
