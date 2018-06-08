
package org.nla.cobol.parsing;

import org.nla.cobol.IElement;
import org.nla.cobol.IActualElement;
import org.nla.cobol.CopyBook;
import org.nla.cobol.Group;
import org.nla.cobol.Usage;
import org.nla.cobol.Variable;
import org.nla.MyFileViewer.ui.MyFileViewerFrame;
import org.nla.cobol.Choice;
import org.nla.cobol.ChoiceList;
import org.nla.cobol.Redefinition;
import org.nla.cobol.Type;
import org.nla.cobol.Picture;
import java.io.LineNumberReader;

import javax.swing.JOptionPane;

import java.io.FileReader;
import java.io.IOException;

/**
 * Parses a cobol copybook and loads it into a org.nla.cobol.CopyBook instance  

 */
public final class CopyBookParser {

	/** position of comments in copybook */
	private static int INT_POS_COMMENTS = 7;

	/** position of end of line in copybook */
	private static int INT_POS_EOL = 72;

	/**  reader. */
	private LineNumberReader _reader;


	/** copybook. */
	private CopyBook copyBook;

	/** raw stream. */
	private StringBuffer stream = new StringBuffer();

	/** lexeme sequence. */
	private LexemeSequence sequence;
	
	/** execution mode : extended or unextended. */
	private boolean extendOccurs;


	/**
	 * Constructs a with the copybook path file. This constructor is not to be called by package users.
	 * @param	path	path to the copybook file
	 * @throws	IOException, if a problem is encountered with the file
	 */
	private CopyBookParser(String path) throws IOException {
		_reader = new LineNumberReader( new FileReader( path ) );
		//_mapLines = new LinkedHashMap<Integer, String>();
	}

	/**
	 * Returns a copybook created with the file designed by <i>path</i>
	 * @param	path	path to the copybook file
	 * @throws	IOException, if a problem is encountered with the file
	 */
	public static CopyBook buildCopyBook( String path ) throws IOException {
		CopyBookParser parser = new CopyBookParser( path );
		return parser.buildCopyBook(); 
	}


	/**
	 * Returns a copybook created with the file created by the constructor.
	 * @throws	IOException, if a problem is encountered with the file
	 */
	private CopyBook buildCopyBook() throws IOException
	{
		readFileContent();
		try {
			// analyze a first time to get an extended copybook
			extendOccurs = true;
			buildCopyBookFromStream();
			CopyBook extended = (CopyBook)copyBook.cloneElement();
			
			// analyze a second time to get an unextended copybook
			setExtendOccurs( false );
			buildCopyBookFromStream();
			CopyBook unExtended = (CopyBook)copyBook.cloneElement();
			extended.setUnextendedCopyBook( unExtended );
			
			
			return extended;
		} 
		catch (Exception e) {
			e.printStackTrace();
	
		}
		
		return new CopyBook();
	}

	/**
	 * Reads content of the file, line by line. Each line is added to the internal map, identified by its number.
	 * Commented and empty lines are left aside. 
	 * @throws	IOException, if a problem is encountered with the file
	 */
	private void readFileContent()  throws IOException {
		String strLine = null;
		
		for ( strLine = _reader.readLine(); strLine != null; strLine = _reader.readLine() ) {
			if ( 
					strLine.trim().length() != 0					 	// line is not empty
				 && strLine.charAt(INT_POS_COMMENTS - 1) != '*' ) {		// line is not commented
				
				//System.out.println( "Raw line '" + strLine + "'" );
				
				// remove the first 8 characters
				try {
					strLine = strLine.substring(INT_POS_COMMENTS);
				}
				catch(Exception e) {
					// do nothing
				}

				// remove the characters behind position 80 if present
				try {
					strLine = strLine.substring(0, INT_POS_EOL - INT_POS_COMMENTS);
				}
				catch(Exception e) {
					// do nothing
				}

				// remove potential useless spaces
				strLine.trim();

				//_mapLines.put( _reader.getLineNumber(), strLine ); 
				stream.append( "#" );						// add line number between #
				stream.append( _reader.getLineNumber() );	// add line number between #
				stream.append( "# " );						// add line number between #
				stream.append( strLine );					// add trimed line to stream
				stream.append( " " );						// add single space character to ensure two words are 'glued' to each other
			}
		}
	}

	/**
	 * Builds CopyBook from stream.
	 * @return copyBook
	 * @throws ParsingException if the analysis encounters a problem
	 */
	private void buildCopyBookFromStream() throws Exception {
		sequence = new Scanner( stream.toString() ).scan();
		analyseCopyBook();
		copyBook.finalise();
	}

	/**
	 * Analyze lexeme sequence and builds a CopyBook from it by adding all its children.
	 * As for now, Redefinition elements are parsed but aren't taken in account in the copybook instance.
	 * @return
	 */
	private void analyseCopyBook() {
		
		// CopyBook creation 
		copyBook = new CopyBook();

		while ( sequence.currentLexeme().getNature() == LexemeNature.literal ) {
			try {
				IActualElement element = (IActualElement)element();
				
				// if element of level 01, it's a copybook 
				if ( element.getLevel() == 1 ) {
					copyBook = ((Group)element).buildCopyBookFromGroup();
					break;
				}
				
				// else, add element to copybook
				else if ( element instanceof Variable  || element instanceof Group ) {
					copyBook.addChildElement( element );
					
					// occurs extension
					if ( extendOccurs && element.isOccursed() ) {
						if ( extendOccurs ) {
							element.setExtended( true );
							for ( int i = 1; i < element.getOccursCount(); ++i ) {
								IActualElement clonedElement = element.cloneElement();
								copyBook.addChildElement( clonedElement );
							}
						}
						else {
							element.setExtended( false );
						}
					}
				}
			}
			catch (ParsingException pe) {
				System.out.println("Error in the copybook: " + pe.getMessage());
			}
		}
		if ( sequence.currentLexeme().getNature() != LexemeNature.eos ) {
			System.out.println("Error in the copybook: there are remaining elements in or after line " + sequence.currentLexeme().getLineNumber() );
		}
	}

	/**
	 * Analyze any type of cobol element, build it and return it so its parent can add it to its children list. 
	 *
	 * Element |=
	 * 		Group [RedefinitionList]
	 *  	Variable [RedefinitionList]
	 *
	 *  The choice between the two possibilities is made by a test on the presence of either keywords 'PIC' or
	 *  'PICTURE' before the next '.'.
	 *
	 * @return
	 */
	private IElement element() {
		IElement toReturn;

		if ( sequence.isElementAVariable() ) {
			toReturn = variable();
		}

		// group definition
		else {
			toReturn = group();
		}
		
		// potential redefinitions of the element, not yet taken in account
		redefinitionList();

		return toReturn;
	}


	/**
	 * Group |= literal(level) literal(name) [Occurs] '.' [elementList]
	 * @return	A group
	 */
	private Group group() {
		Group toReturn = new Group();
		toReturn.setCopybook( copyBook );

		Lexeme levelLexeme = sequence.consumeLexeme( LexemeNature.literal ); 		// level 
		Lexeme nameLexeme = sequence.consumeLexeme( LexemeNature.literal );			// element name
		
		//System.out.println( "Analyze GROUP " + nameLexeme.getValue() );

		toReturn.setLevel( levelLexeme.getValueAsInt() );
		toReturn.setName( nameLexeme.getValue() );

		// Loop over all possible clauses ;  max 3 times : 3 sub clauses are possible, hence no more than 4 loop executions should be necessary  
		int maxLoopCount = 3;
		int loopCount = 0;
		while ( sequence.currentLexeme().getNature() != LexemeNature.dot && loopCount < maxLoopCount ) {

			// set potential usage
			Usage tempUsage = usageClause();										// usage clause
			if ( tempUsage != null ) {
				toReturn.setUsage( tempUsage );
			}
				
			// set potential occurs count
			int tempOccursCount = occursClause();									// occurs clause
			if ( tempOccursCount > 0 ) {
				toReturn.setOccursCount( tempOccursCount );
			}
			
			// set potential value			
			String tempValue = valueClause();										// value clause
			if ( tempValue != null ) {
				//toReturn.setDefaultValue( tempValue );
			}
			
			// loop count increase
			loopCount += 1;
		}
		sequence.consumeLexeme( LexemeNature.dot );									// '.'
		
		// keep scanning elements and adding them to its kids : 
		//		   if there seems to be more in the sequence (current lexeme is a literal)
		//	   and if the level of the element is above this group level
		while ( 	sequence.currentLexeme().getNature() == LexemeNature.literal 
				&&	toReturn.getLevel() < sequence.currentLexeme().getValueAsInt() ) {

			IElement element;
			
			try {
				// scan next nested element : this can throw a ParsingException
				element = element();
				
				// add it to its kids
				if ( element instanceof Variable || element instanceof Group ) {
					IActualElement actualElement = (IActualElement)element;
					toReturn.addChildElement( actualElement );
	
					// occurs extenstion
					if ( actualElement.isOccursed() ) {
						if ( extendOccurs ) {
							actualElement.setExtended( true );
							for ( int i = 1; i < actualElement.getOccursCount(); ++i ) {
								IActualElement clonedElement = actualElement.cloneElement();
								toReturn.addChildElement( clonedElement );
							}
						}
						else {
							actualElement.setExtended( false );
						}
					}
				}
			}

			// a problem occured in the sequence
			// we're gonna try to get back in business by reaching consuming every lexeme till we reach the next element
			catch ( ParsingException pe ) {
//				System.out.println( "Exception in group " + toReturn.getName() + ": " + pe.getMessage() );
//				pe.printStackTrace();
//				throw pe;

				
				// TODO
				// next '.' lookup doesn't work yet. to be done later
				
				// consume every lexeme till a dot followed by literal is found or till eos is found
				while ( sequence.currentLexeme().getNature() != LexemeNature.dot && sequence.currentLexeme().getNature() != LexemeNature.eos ) {
//					String lxName = sequence.currentLexeme().getValue();
					sequence.consumeLexeme();
				}
				// consume potential dot
				if ( sequence.currentLexeme().getNature() == LexemeNature.dot ) {
					sequence.consumeLexeme( LexemeNature.dot );
				}

				// Case 1
				// The current lexeme is a literal, and it should describe a level
				//  - if this level is strictly above the group's, we're back in business
				//  - else, the element is at the same level as the group   
				// Case 2
				// The current lexeme is eos, the analysis is over
				
				// => we simply go back to the loop, the restart of the analysis will be handled properly
				//break;
			}
		}

		return toReturn;
	}

	/**
	 * Variable |= literal(level) literal(name) 'PIC' Type [Value] [Occurs] '.' [ChoiceList]
	 * @return
	 */
	private IActualElement variable() {
		Variable toReturn = new Variable();
		toReturn.setCopybook( copyBook );

		Lexeme levelLexeme = sequence.consumeLexeme( LexemeNature.literal ); 		// level
		Lexeme nameLexeme = sequence.consumeLexeme( LexemeNature.literal );			// element name

		//System.out.println( "Analyze VARIABLE " + nameLexeme.getValue() );
		
		// Loop over all possible clauses ;  max 4 times : 4 sub clauses are possible, hence no more than 4 loop executions should be necessary  
		int maxLoopCount = 4;
		int loopCount = 0;
		while ( sequence.currentLexeme().getNature() != LexemeNature.dot && loopCount < maxLoopCount ) {

			// set potential usage
			Usage tempUsage = usageClause();										// usage clause
			if ( tempUsage != null ) {
				toReturn.setUsage( tempUsage );
			}
			
			// set potential type
			Type tempType = typeClause();											// type
			if ( tempType != null ) {
				toReturn.setType( tempType );
			}

			// set potential occurs count
			int tempOccursCount = occursClause();									// occurs clause
			if ( tempOccursCount > 0 ) {
				toReturn.setOccursCount( tempOccursCount );
			}
			
			// set potential value			
			String tempValue = valueClause();										// value clause
			if ( tempValue != null ) {
				toReturn.setDefaultValue( tempValue );
			}
			
			// increase loop count
			loopCount += 1;
		}

		// dot
		sequence.consumeLexeme( LexemeNature.dot );									// '.'
			
		// potential 88 clauses
		toReturn.setChoiceList( choiceList() );


		// set variable properties
		toReturn.setLevel( levelLexeme.getValueAsInt() );
		toReturn.setName( nameLexeme.getValue() );
		
		return toReturn;
	}
	
	/**
	 * BlankWhenZeroClause |= 'BLANK' ['WHEN'] 'ZERO'/'ZEROS'/'ZEROES'
	 */
	public void blankWhenZeroClause() {
		if ( sequence.currentLexeme().getNature() == LexemeNature.blank ) {
			sequence.consumeLexeme( LexemeNature.blank );
			sequence.consumePotentialLexeme( LexemeNature.when );
			
			if ( sequence.currentLexeme().getNature() == LexemeNature.zero ) {
				sequence.consumeLexeme( LexemeNature.zero );
			}

			else if ( sequence.currentLexeme().getNature() == LexemeNature.zeroes ) {
				sequence.consumeLexeme( LexemeNature.zeroes );
			}
		}
	}
	
	/**
	 * JustifiedClause |= 'JUST'/'JUSTIFIED' [RIGHT]
	 */
	public void justifiedClause() {
		if ( sequence.currentLexeme().getNature() == LexemeNature.justified ) {
			sequence.consumeLexeme( LexemeNature.justified );
			sequence.consumePotentialLexeme( LexemeNature.right );
		}
	}
	
	
	
	/**
	 * RedefinitionList |= Redefinition [Redefinition]
	 */
	private void redefinitionList() {
		// potential 'REDEFINES' is 2 Lexemes ahead from current Lexeme ('.')
		while ( sequence.lookAheadForLexeme( 2 ) != null && sequence.lookAheadForLexeme( 2 ).getNature() == LexemeNature.redefines ) {
			redefinition();
		}
	}

	/**
	 * Redefinition |=
	 * 		literal|level literal|name 'REDEFINES' RedefinedGroup
	 * 		literal|level literal|name 'REDEFINES' RedefinedVariable
	 *  
	 *  The choice between the two possibilities is made by a test on the presence of either keywords 'PIC' or
	 *  'PICTURE' before the next '.'.
	 * @return
	 */
	private Redefinition redefinition() {
		Redefinition toReturn = new Redefinition();
		Lexeme level = sequence.consumeLexeme( LexemeNature.literal ); 			// level
		Lexeme name = sequence.consumeLexeme( LexemeNature.literal );			// element name
		sequence.consumeLexeme( LexemeNature.redefines );						// 'REDEFINES'
		
		//System.out.println( "Analyze REDEFINE " + name.getValue() );

		// variable definition
		if ( sequence.isElementAVariable() ) {
			redefinedVariable();
		}

		// group definition
		else {
			redefinedGroup( level.getValueAsInt() );
		}
		return toReturn;
	}
	
	/**
	 * RedefinedGroup |= literal(name) '.' [ElementList]
	 * @param groupLevel
	 */
	private void redefinedGroup( int groupLevel ) {
		sequence.consumeLexeme( LexemeNature.literal );				// name

		// Loop over all possible clauses ;  max 3 times : 3 sub clauses are possible, hence no more than 4 loop executions should be necessary  
		int maxLoopCount = 3;
		int loopCount = 0;
		while ( sequence.currentLexeme().getNature() != LexemeNature.dot && loopCount < maxLoopCount ) {

			// set potential usage
			Usage tempUsage = usageClause();										// usage clause
			if ( tempUsage != null ) {
				//toReturn.setUsage( tempUsage );
			}
			
			// set potential occurs count
			int tempOccursCount = occursClause();									// occurs clause
			if ( tempOccursCount > 0 ) {
				//toReturn.setOccursCount( tempOccursCount );
			}
			
			// set potential value			
			String tempValue = valueClause();										// value clause
			if ( tempValue != null ) {
				//toReturn.setDefaultValue( tempValue );
			}
			
			// loop count increase
			loopCount += 1;
		}
		sequence.consumeLexeme( LexemeNature.dot );									// '.'
		
		// add potential element to redefined group.
		while ( 	sequence.currentLexeme().getNature() == LexemeNature.literal 
				&&	groupLevel < sequence.currentLexeme().getValueAsInt() ) {
			element();
		}
	}

	/**
	 * RedefinedVariable |= literal(name) 'PIC' Type '.'
	 */
	private void redefinedVariable() {
		sequence.consumeLexeme( LexemeNature.literal );								// name

		// Loop over all possible clauses ;  max 4 times : 4 sub clauses are possible, hence no more than 4 loop executions should be necessary  
		int maxLoopCount = 4;
		int loopCount = 0;
		while ( sequence.currentLexeme().getNature() != LexemeNature.dot && loopCount < maxLoopCount ) {

			// set potential usage clause
			Usage tempUsage = usageClause();										// usage clause
			if ( tempUsage != null ) {
				//toReturn.setUsage( tempUsage );
			}
			
			// set potential type
			Type tempType = typeClause();											// type
			if ( tempType != null ) {
				//toReturn.setType( tempType );
			}

			// set potential occurs count
			int tempOccursCount = occursClause();									// occurs clause
			if ( tempOccursCount > 0 ) {
				//toReturn.setOccursCount( tempOccursCount );
			}
			
			// set potential value			
			String tempValue = valueClause();										// value clause
			if ( tempValue != null ) {
				//toReturn.setDefaultValue( tempValue );
			}
			
			// loop count increase
			loopCount += 1;
		}
		
		sequence.consumeLexeme( LexemeNature.dot );									// '.'
		
		// potential 88 clauses
		choiceList();

	}

	/**
	 * Value |= 'VALUE' ['IS'] literal(value)*
	 * @return the value carried by the literal. If the value is surrounded by quotes, the quotes are removed. 
	 */
	private String valueClause() {
		
		// string to return 
		String toReturn = null;
		
		// scan and identifie value if kw 'VALUE' is found
		if ( sequence.currentLexeme().getNature() == LexemeNature.value ) {
			sequence.consumeLexeme( LexemeNature.value );										// 'VALUE'
			sequence.consumePotentialLexeme( LexemeNature.is );									// 'IS'
			transformAllSingleLettersToLiteral();

			// loop on all possible values
			while ( sequence.currentLexeme().getNature() == LexemeNature.literal ) {
				Lexeme valueLexeme = sequence.consumeLexeme( LexemeNature.literal );			// literal
				String value = valueLexeme.getValue();

				// setup return value only for the first found value
				if ( toReturn == null ) {
					if ( valueLexeme.isQuotedLiteral() ) {
						toReturn = value.substring( 1, value.length() - 1 );
					}
					else {
						toReturn = value;
					}
				}
			}
		}
		
		return toReturn;
	}

	/**
	 * Occurs |= 'OCCURS' literal(count)
	 * TODO finish the rest of the grammar
	 * @return	the occurs count carried by the literal
	 */
	private int occursClause() {
		if ( sequence.currentLexeme().getNature() == LexemeNature.occurs ) {					
			sequence.consumeLexeme( LexemeNature.occurs );										// 'OCCURS'
			Lexeme occursCountLexeme = sequence.consumeLexeme( LexemeNature.literal );			// literal

			// value domain clause
			if ( sequence.currentLexeme().getNature() == LexemeNature.to ) {
				sequence.consumeLexeme( LexemeNature.to );										// 'TO'
				sequence.consumeLexeme( LexemeNature.literal );									// literal
			}
			sequence.consumePotentialLexeme( LexemeNature.times );								// 'TIMES'
			
			// depending on clauses
			if ( sequence.currentLexeme().getNature() == LexemeNature.depending ) {
				sequence.consumeLexeme( LexemeNature.depending );								// 'DEPENDING'
				sequence.consumePotentialLexeme( LexemeNature.on );								// 'ON'
				sequence.consumeLexeme( LexemeNature.literal );									// literal
			}
			
			// ascending/descending clauses
			while ( sequence.currentLexeme().getNature() == LexemeNature.descending ||
					sequence.currentLexeme().getNature() == LexemeNature.ascending ) {
				sequence.consumeLexeme();														// 'ASCENDING'/'DESCENDING'
				sequence.consumePotentialLexeme( LexemeNature.key );							// 'KEY'
				sequence.consumePotentialLexeme( LexemeNature.is );								// 'IS'
				sequence.consumePotentialLexeme( LexemeNature.literal );						// literal
			}
			
			// indexed by clause
			if ( sequence.currentLexeme().getNature() == LexemeNature.indexed ) {
				sequence.consumeLexeme( LexemeNature.indexed );								// 'INDEXED'
				sequence.consumePotentialLexeme( LexemeNature.by );							// 'BY'
				sequence.consumeLexeme( LexemeNature.literal );								// literal
			}

			return ( occursCountLexeme.getValueAsInt() );
		}
		return 0;
	}

	/**
	 *  ChoiceList |= Choice [ChoiceList]
	 */
	private ChoiceList choiceList() {

		if ( sequence.currentLexeme().getNature() == LexemeNature.literal && sequence.currentLexeme().isLevel88Literal() ) {
			ChoiceList list = new ChoiceList();
			while ( sequence.currentLexeme().getNature() == LexemeNature.literal && sequence.currentLexeme().isLevel88Literal() ) {
				list.addChoice( choice() );
			}
			return list;
		}
		return null;
	}

	/**
	 * Choice |= literal(88) literal(name) [Value] '.'
	 */
	private Choice choice() {
		Choice toReturn = new Choice();

		sequence.consumeLexeme( LexemeNature.literal, "88" );
		String choiceName = sequence.consumeLexeme( LexemeNature.literal ).getValue();
		//System.out.println( "Analyze CHOICE " + choiceName );
		String choiceValue = valueClause();
		sequence.consumeLexeme( LexemeNature.dot );

		toReturn.setName( choiceName );
		toReturn.setChoiceValue( choiceValue );

		return toReturn; 
	}

	/**
	 * Type |=
	 * 		[sign] 'PIC'/'PICTURE' ['IS'] StringType    => flaw not yet corrected 
	 *  	[sign] 'PIC'/'PICTURE' ['IS'] NumericType
	 */
	private Type typeClause() {
		// return a type if either 'PIC' or 'PICTURE' kw is found  
		if ( sequence.currentLexeme().getNature() == LexemeNature.picture ) {

			Type toReturn;
			boolean isSigned = false;
			
			sequence.consumeLexeme( LexemeNature.picture );			// 'PIC' / 'PICTURE'
			sequence.consumePotentialLexeme( LexemeNature.is );		// 'IS'
			
			// transform potential literal|Szzzz to lexems sign + literal|zzzz
			transformSignedLiteralToSignAndLiteral();
	
			if ( sequence.currentLexeme().getNature() == LexemeNature.sign ) {
				sequence.consumeLexeme( LexemeNature.sign );
				isSigned = true;
			}
	
//			// transform potential literal|XXX to picX|XXX or literal|999 to pic9|999 
//			transformLiteralToPic9OrPicX();
	
			// transform potential literal|999V99 to lexems pic9|999 + virtualComma|null + pic9|99
			transformLiteralToPic9AndVirtualAndPic9();
	
	
//			if ( sequence.currentLexeme().getNature() == LexemeNature.picX ) {
			if ( sequence.currentLexeme().isPicXLiteral() ) {
				toReturn = stringType();
			}
			else {
				toReturn = numericType();
			}
			toReturn.setSigned( isSigned );
			return toReturn;
		}
		
		return null;
	}

	/**
	 * StringType |=
	 * 		picX
	 * 		picX '(' literal(integerSize) ')'
	 * 		n x picX
	 */
	private Type stringType() {
		Type toReturn = new Type();
		toReturn.setPicture( Picture.PicX );
//		// lexeme is either picX|X or picX|XXX..., it's why getValue().length() is used to find the size
//		toReturn.setIntegerSize( sequence.consumeLexeme( LexemeNature.picX ).getValue().length() );
		
		// lexeme is either literal|X or literal|XXX..., it's why getValue().length() is used to find the size
		toReturn.setIntegerSize( sequence.consumeLexeme( LexemeNature.literal ).getValue().length() );

		if ( sequence.currentLexeme().getNature() == LexemeNature.openingBracket ) {
			sequence.consumeLexeme( LexemeNature.openingBracket );
//			// transform potential pic9 to literal
//			transformPic9OrPicXToLiteral();
			// here, size is carried by the actual value of the literal
			toReturn.setIntegerSize( sequence.consumeLexeme( LexemeNature.literal ).getValueAsInt() );
			sequence.consumeLexeme( LexemeNature.closingBracket );
		}

		return toReturn;
	}

	/**
	 * NumericType |=
	 * 		NumericIntegerSize Packed
	 * 		NumericIntegerSize virtualComma NumericDecimalSize Packed
	 */
	private Type numericType() {
		Type toReturn = new Type();
		toReturn.setPicture( Picture.Pic9 );

		toReturn.setIntegerSize( numericIntegerSize() );								// NumericIntegerSize

		// transform probable literal|V999 lexem to lexems : virtualComma|null and pic9|999
		transformLiteralToVirtualNumeric();

		if ( sequence.currentLexeme().getNature() == LexemeNature.virtualComma ) {
			sequence.consumeLexeme( LexemeNature.virtualComma );						// 'V'
			
			// some specific case allows no decimal after virtual comma ( example : 'PIC 9(3)V.')
			// in that case the decimal is zero and the rest of the sequence is skipped
			// that case is determined by a dot directly after the virtual comma
			if ( sequence.currentLexeme().getNature() == LexemeNature.dot ) {
				toReturn.setDecimalSize( 0 );											// no decimal size
				return toReturn;
			}
			else {
//				// transform potential literal|XXX to picX|XXX or literal|999 to pic9|999
//				transformLiteralToPic9OrPicX();
				toReturn.setDecimalSize( numericDecimalSize() );						// DecimalIntegerSize
			}
		}
		
		return toReturn;
	}
	
	/**
	 * Usage |= 'USAGE' 'IS'
	 */
	private Usage usageClause() {
		//System.out.println( "enter usage()" );
		// usage clause is present
		if ( sequence.currentLexeme().getNature() == LexemeNature.usage ) {
				sequence.consumeLexeme( LexemeNature.usage );							// 'USAGE'
				sequence.consumePotentialLexeme( LexemeNature.is );						// 'IS'
			}
		
		// comp / binary
		if ( sequence.currentLexeme().getNature() == LexemeNature.binary ) {			// 'COMP' / 'BINARY'
			sequence.consumeLexeme( LexemeNature.binary );
			return Usage.binary;
		}
		
		// comp-1
		else if ( sequence.currentLexeme().getNature() == LexemeNature.comp1 ) {		// 'COMP-1'
			sequence.consumeLexeme( LexemeNature.comp1 );
			return Usage.comp1;
		}
		
		// comp-2
		else if ( sequence.currentLexeme().getNature() == LexemeNature.comp2 ) {		// 'COMP-2'
			sequence.consumeLexeme( LexemeNature.comp2 );
			return Usage.comp2;
		}
		
		// comp-3
		else if ( sequence.currentLexeme().getNature() == LexemeNature.comp3 ) {		// 'COMP-3'
			sequence.consumeLexeme( LexemeNature.comp3 );
			return Usage.comp3;
			//type.setPacked( true );
			//System.out.println( "is comp3" );
		}
		
		// comp-4
		else if ( sequence.currentLexeme().getNature() == LexemeNature.comp4 ) {		// 'COMP-4'
			sequence.consumeLexeme( LexemeNature.comp4 );
			return Usage.comp4;
		}
		
		// display
		else if ( sequence.currentLexeme().getNature() == LexemeNature.display ) {		// 'DISPLAY'
			sequence.consumeLexeme( LexemeNature.display );
			return Usage.display;
		}
		
		// display
		else if ( sequence.currentLexeme().getNature() == LexemeNature.display1 ) {		// 'DISPLAY-1'
			sequence.consumeLexeme( LexemeNature.display1 );
			return Usage.display1;
		}
		
		// packed-decimal
		else if ( sequence.currentLexeme().getNature() == LexemeNature.packed ) {		// 'PACKED-DECIMAL'
			sequence.consumeLexeme( LexemeNature.packed );
			return Usage.packed;
			//type.setPacked( true );
			//System.out.println( "is packed" );
		}
		
		// pointer
		else if ( sequence.currentLexeme().getNature() == LexemeNature.pointer ) {		// 'POINTER'
			sequence.consumeLexeme( LexemeNature.pointer );
			return Usage.pointer;
			//type.setPacked( true );
			//System.out.println( "is packed" );
		}
		
		return null;
		//System.out.println( "exit usage()" );
	}
	
//	/**
//	 * Packed |= [comp-3/comp-4/binary/packed-decimal]
//	 * @return true if either of comp-3/comp-4/binary/packed-decimal is stated
//	 */
//	private boolean packed() {
//		
//		if ( 	sequence.currentLexeme().getNature() == LexemeNature.binary
//				 || sequence.currentLexeme().getNature() == LexemeNature.comp3
//				 || sequence.currentLexeme().getNature() == LexemeNature.packed ) {
//				sequence.currentLexeme().setNature( LexemeNature.comp3 );
//				sequence.consumeLexeme( LexemeNature.comp3 );
//				
//				return true;
//			}
//		return false;
//	}

	/**
	 * NumericIntegerSize |=
	 * 		pic9|9
	 * 		pic9|999...
	 * 		pic9 '(' literal(integerSize) ')'
	 */
	private int numericIntegerSize() {
		
//		// lexeme is either pic9|9 or pic9|999..., it's why getValue().length() is used to find the size
//		int toReturn = sequence.consumeLexeme( LexemeNature.pic9 ).getValue().length();
		
		// lexeme is either literal|9 or literal|999..., it's why getValue().length() is used to find the size
		int toReturn = sequence.consumeLexeme( LexemeNature.literal ).getValue().length();
		
		if ( sequence.currentLexeme().getNature() == LexemeNature.openingBracket ) {
			sequence.consumeLexeme( LexemeNature.openingBracket );
//			// transform potentiel pic9 to literal
//			transformPic9OrPicXToLiteral();
			// here, size is carried by the actual value of the literal 
			toReturn = sequence.consumeLexeme( LexemeNature.literal ).getValueAsInt();
			sequence.consumeLexeme( LexemeNature.closingBracket );
		}
		return toReturn;
	}

	/**
	 * NumericDecimalSize |=
	 * 		pic9
	 * 		pic9 '(' literal(decimalSize) ')'
	 * 		n x pic9
	 */
	private int numericDecimalSize() {

//		// lexeme is either pic9|9 or pic9|999..., it's why getValue().length() is used to find the size
//		int toReturn = sequence.consumeLexeme( LexemeNature.pic9 ).getValue().length();

		// lexeme is either literal|9 or literal|999..., it's why getValue().length() is used to find the size
		int toReturn = sequence.consumeLexeme( LexemeNature.literal ).getValue().length();
		if ( sequence.currentLexeme().getNature() == LexemeNature.openingBracket ) {
			sequence.consumeLexeme( LexemeNature.openingBracket );
			toReturn = sequence.consumeLexeme( LexemeNature.literal ).getValueAsInt();
			sequence.consumeLexeme( LexemeNature.closingBracket );
		}
		return toReturn;
	}

//	/**
//	 * Transform potential literal|XXX to picX|XXX or literal|999 to pic9|999. It's meant to correct the sequence that's screwed by the raw scan. 
//	 * This method must be used only at some specific moment of the analysis. Hence, DO NOT USE if you don't grasp exactly its meaning.
//	 */
//	private void transformLiteralToPic9OrPicX() {
//		if ( sequence.currentLexeme().getNature() == LexemeNature.literal ) {
//			if ( StringUtils.containsOnly( sequence.currentLexeme().getValue(), Lexeme.strPicX ) ) {
//				sequence.currentLexeme().setNature( LexemeNature.picX );
//			}
//			else if ( StringUtils.containsOnly( sequence.currentLexeme().getValue(), Lexeme.strPic9 ) ) {
//				sequence.currentLexeme().setNature( LexemeNature.pic9 );
//			}
//		}
//	}

	/**
	 * Transform potential literal|999V9999 to : pic9|999 followed by virtualComma followed by pic9|9999. It's meant to correct the sequence that's screwed by the raw scan.
	 * This method must be used only at some specific moment of the analysis. Hence, DO NOT USE if you don't grasp exactly its meaning.
	 */
	private void transformLiteralToPic9AndVirtualAndPic9() {
		if ( sequence.currentLexeme().getValue() != null && sequence.currentLexeme().getValue().toLowerCase().contains( Lexeme.strPic9 + Lexeme.strV + Lexeme.strPic9 ) ) {
			
			String lexemeValue = sequence.currentLexeme().getValue().toLowerCase();
			String integerSize = lexemeValue.substring( 0, lexemeValue.indexOf( Lexeme.strV ) ); 
			String decimalSize = lexemeValue.substring( lexemeValue.indexOf( Lexeme.strV ) + 1 );
			
			Lexeme virtualCommaLexeme = Lexeme.getLexemeForKeyWord( Lexeme.strV );
			Lexeme integerSizeLexeme = Lexeme.getLexemeForKeyWord( Lexeme.strPic9 );
			Lexeme decimalSizeLexeme = sequence.currentLexeme();
//			decimalSizeLexeme.setNature( LexemeNature.pic9 );
			decimalSizeLexeme.setNature( LexemeNature.literal );
			decimalSizeLexeme.setValue( decimalSize );
			integerSizeLexeme.setValue( integerSize );

			sequence.insertLexeme( virtualCommaLexeme );
			sequence.insertLexeme( integerSizeLexeme );
		}
	}

	/**
	 * Transform potential literal|V999 to : virtualComma followed by literal|999. It's meant to correct the sequence that's screwed by the raw scan.
	 * This method must be used only at some specific moment of the analysis. Hence, DO NOT USE if you don't grasp exactly its meaning.
	 */
	private void transformLiteralToVirtualNumeric() {
		if ( sequence.currentLexeme().getNature() == LexemeNature.literal && sequence.currentLexeme().getValue().toLowerCase().startsWith( Lexeme.strV + Lexeme.strPic9 ) ) {
			String lexemeValue = sequence.currentLexeme().getValue();
			sequence.currentLexeme().setValue( lexemeValue.substring( 1 ) );
			sequence.insertLexeme( Lexeme.getLexemeForKeyWord( Lexeme.strV ) );
		}
	}

//	/**
//	 * Transform potential pic9 to literal|9 or picX to literal|X. It's meant to correct the sequence that's screwed by the raw scan.
//	 * This method must be used only at some specific moment of the analysis. Hence, DO NOT USE if you don't grasp exactly its meaning.
//	 */
//	private void transformPic9OrPicXToLiteral() {
//		if ( sequence.currentLexeme().getNature() == LexemeNature.pic9 || sequence.currentLexeme().getNature() == LexemeNature.picX ) {
//			sequence.currentLexeme().setNature( LexemeNature.literal );
//		}
//	}


	/**
	 * Transform potential literal|S999 to : sign followed by literal|999. It's meant to correct the sequence that's screwed by the raw scan.
	 * This method must be used only at some specific moment of the analysis. Hence, DO NOT USE if you don't grasp exactly its meaning.
	 */
	private void transformSignedLiteralToSignAndLiteral() {
		if ( sequence.currentLexeme().getNature() == LexemeNature.literal && sequence.currentLexeme().getValue().toLowerCase().startsWith( Lexeme.strSign ) ) {
			String lexemeValue = sequence.currentLexeme().getValue();
			sequence.currentLexeme().setValue( lexemeValue.substring( 1 ) );
			sequence.insertLexeme( Lexeme.getLexemeForKeyWord( Lexeme.strSign ) );
		}
	}

	/**
	 *
	 */
	private void transformAllSingleLettersToLiteral() {
		if ( 	sequence.currentLexeme().getNature() == LexemeNature.closingBracket
			 || sequence.currentLexeme().getNature() == LexemeNature.dot
			 || sequence.currentLexeme().getNature() == LexemeNature.openingBracket
//			 || sequence.currentLexeme().getNature() == LexemeNature.pic9
//			 || sequence.currentLexeme().getNature() == LexemeNature.picX
			 || sequence.currentLexeme().getNature() == LexemeNature.sign
			 || sequence.currentLexeme().getNature() == LexemeNature.virtualComma ) {
			String lexemeValue = sequence.currentLexeme().getNature().toString();
			sequence.currentLexeme().setNature( LexemeNature.literal );
			sequence.currentLexeme().setValue( lexemeValue );
		}
	}
//
//	public static void main(String[] args) throws Exception {
//		//String stream = "COMP-4 PIC 9 OCCURS )AAABBB REDEFINE X COMP-3   REDEFINES VALUE 'coucou' ZZ  BINARY COMP -3  V PIC .  OCC(";
//		CopyBookParser parser = new CopyBookParser( "g:\\MyFileViewer_workspace\\MyFileViewer\\res\\test.txt" );
//		parser.buildCopyBook();
//	}

	/**
	 * @return the extendOccurs
	 */
	public boolean isExtendOccurs() {
		return extendOccurs;
	}

	/**
	 * @param extendOccurs the extendOccurs to set
	 */
	public void setExtendOccurs(boolean extendOccurs) {
		this.extendOccurs = extendOccurs;
	}
}
