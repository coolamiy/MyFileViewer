package org.nla.cobol.parsing;

import java.util.HashMap;
import java.util.UUID;
import org.nla.util.StringUtils;


public class Lexeme {

	/** opening bracket character. */
	public static final char chrOpeningBracket = '(';

	/** closing bracket character. */
	public static final char chrClosingBracket = ')';

	/** dot character. */
	public static final char chrDot = '.';

	/** space character. */
	public static final char chrSpace = ' ';

	/** quote character. */
	public static final char chrQuote = '\'';

	/** double-quote character. */
	public static final char chrDblQuote = '"';

	/** opening bracket character. */
	public static final String strOpeningBracket = "(";

	/** closing bracket character. */
	public static final String strClosingBracket = ")";

	/** dot character. */
	public static final String strDot = ".";

	/** space character. */
	public static final String strSpace = " ";

	/** quote character. */
	public static final String strQuote = "'";

	/** double-quote character. */
	public static final String strDblQuote = "\"";
	
	/** filler. */
	public static final String strFillerKW = "filler";
	
	/** pic keyword. */
	public static final String strPicKW = "pic";
	
	/** picture keyword. */
	public static final String strPictureKW = "picture";
	
	/** occurs keyword. */
	public static final String strOccursKW = "occurs";

	/** value keyword. */
	public static final String strValueKW = "value";

	/** values keyword. */
	public static final String strValuesKW = "values";

	/** redefine keyword. */
	public static final String strRedefineKW = "redefines";
	
	/** renames kw. */
	public static final String strRenamesKW = "renames";

	/** usage keyword. */
	public static final String strUsageKW = "usage";

	/** is keyword. */
	public static final String strIsKW = "is";

	/** are keyword. */
	public static final String strAreKW = "are";
	
	/** comp-1 keyword. */
	public static final String strComp1KW = "comp-1";
	
	/** computational-1 keyword. */
	public static final String strComputational1KW = "computational-1";
	
	/** comp-2 keyword. */
	public static final String strComp2KW = "comp-2";
	
	/** computational-2 keyword. */
	public static final String strComputational2KW = "computational-2";
	
	/** comp-3 keyword. */
	public static final String strComp3KW = "comp-3";
	
	/** computational-3 keyword. */
	public static final String strComputational3KW = "computational-3";
	
	/** comp-4 keyword. */
	public static final String strComp4KW = "comp-4";

	/** computational-4 keyword. */
	public static final String strComputational4KW = "computational-4";
	
	/** comp keyword. */
	public static final String strCompKW = "comp";
	
	/** binary keyword. */
	public static final String strBinaryKW = "binary";
	
	/** packed-decimal keyword. */
	public static final String strPackedKW = "packed-decimal";
	
	/** display keyword. */
	public static final String strDisplayKW = "display";
	
	/** display-1 keyword. */
	public static final String strDisplay1KW = "display-1";
	
	/** index keyword. */
	public static final String strIndexKW = "index";

	/** indexed kw. */
	public static final String strIndexedKW = "indexed";

	/** X picture. */
	public static final String strPicX = "x";

	/** 9 picture. */
	public static final String strPic9 = "9";

	/** virtual comma. */
	public static final String strV = "v";

	/** sign. */
	public static final String strSign = "s";

	/** 88 level. */
	public static final String strLevel88 = "88";
	
	/** 77 level. */
	public static final String strLevel77 = "77";
	
	/** eject kw. */
	public static final String strEjectKW = "eject";

	/** blank kw. */
	public static final String strBlankKW = "blank";

	/** when kw. */
	public static final String strWhenKW = "when";

	/** zero kw. */
	public static final String strZeroKW = "zero";

	/** zeroes kw. */
	public static final String strZeroesKW = "zeroes";
	
	/** zeroes kw. */
	public static final String strZerosKW = "zeros";
	
	/** justified kw. */
	public static final String strJustifiedKW = "justified";
	
	/** right kw. */
	public static final String strRightKW = "right";

	/** left kw. */
	public static final String strLeftKW = "left";

	/** times kw. */
	public static final String strTimesKW = "times";

	/** ascending kw. */
	public static final String strAscendingKW = "ascending";

	/** descending kw. */
	public static final String strDescendingKW = "descending";


	/** key kw. */
	public static final String strKeyKW = "key";

	/** by keyword. */
	public static final String strByKW = "by";
	
	/** to keyword. */
	public static final String strToKW = "to";
	
	/** through keyword. */
	public static final String strThroughKW = "through";

	/** thru keyword. */
	public static final String strThruKW = "thru";

	/** depending kw. */
	public static final String strDependingKW = "depending";
	
	/** on kw. */
	public static final String strOnKW = "on";

	/** leading kw. */
	public static final String strLeadingKW = "leading";

	/** trailing kw. */
	public static final String strTrailingKW = "trailing";

	/** separate kw. */
	public static final String strSeparateKW = "separate";
	
	/** character keyword. */
	public static final String strCharacterKW = "character";
	
	/** sync kw. */
	public static final String strSyncKW = "sync";

	/** synchronized kw. */
	public static final String strSynchronizedKW = "synchronized";
	
	/** eos lexem : means the end of the stream. */
	public static final Lexeme eosLexem = new Lexeme( LexemeNature.eos );

	/** mapping between keywords and lexemes. */
	private static final HashMap<String, Lexeme> mapKeywordLexemes = new HashMap<String, Lexeme>();

	static {
		mapKeywordLexemes.put( strPictureKW, 		new Lexeme( LexemeNature.picture ) );
		mapKeywordLexemes.put( strPicKW, 			new Lexeme( LexemeNature.picture ) );
		mapKeywordLexemes.put( strFillerKW, 		new Lexeme( LexemeNature.literal, strFillerKW ) );
		mapKeywordLexemes.put( strOccursKW, 		new Lexeme( LexemeNature.occurs ) );
		mapKeywordLexemes.put( strValueKW, 			new Lexeme( LexemeNature.value ) );
		mapKeywordLexemes.put( strValuesKW, 		new Lexeme( LexemeNature.values ) );
		mapKeywordLexemes.put( strIndexKW, 			new Lexeme( LexemeNature.index ) );
		mapKeywordLexemes.put( strIndexedKW, 		new Lexeme( LexemeNature.indexed ) );
		mapKeywordLexemes.put( strRedefineKW, 		new Lexeme( LexemeNature.redefines ) );
		mapKeywordLexemes.put( strRenamesKW, 		new Lexeme( LexemeNature.renames ) );
		mapKeywordLexemes.put( strUsageKW, 			new Lexeme( LexemeNature.usage ) );
		mapKeywordLexemes.put( strIsKW, 			new Lexeme( LexemeNature.is ) );
		mapKeywordLexemes.put( strAreKW, 			new Lexeme( LexemeNature.are ) );
		mapKeywordLexemes.put( strComp1KW, 			new Lexeme( LexemeNature.comp1 ) );
		mapKeywordLexemes.put( strComputational1KW, new Lexeme( LexemeNature.comp1 ) );
		mapKeywordLexemes.put( strComp2KW, 			new Lexeme( LexemeNature.comp2 ) );
		mapKeywordLexemes.put( strComputational2KW, new Lexeme( LexemeNature.comp2 ) );
		mapKeywordLexemes.put( strComp3KW, 			new Lexeme( LexemeNature.comp3 ) );
		mapKeywordLexemes.put( strComputational3KW, new Lexeme( LexemeNature.comp3 ) );
		mapKeywordLexemes.put( strComp4KW, 			new Lexeme( LexemeNature.comp4 ) );
		mapKeywordLexemes.put( strComputational3KW, new Lexeme( LexemeNature.comp4 ) );
		mapKeywordLexemes.put( strBinaryKW, 		new Lexeme( LexemeNature.binary ) );
		mapKeywordLexemes.put( strCompKW, 			new Lexeme( LexemeNature.binary ) );
		mapKeywordLexemes.put( strPackedKW, 		new Lexeme( LexemeNature.packed ) );
		mapKeywordLexemes.put( strDisplayKW, 		new Lexeme( LexemeNature.display ) );
		mapKeywordLexemes.put( strDisplay1KW, 		new Lexeme( LexemeNature.display1 ) );
		mapKeywordLexemes.put( strEjectKW, 			new Lexeme( LexemeNature.eject ) );
		mapKeywordLexemes.put( strPicX, 			new Lexeme( LexemeNature.literal, strPicX ) );
		mapKeywordLexemes.put( strPic9, 			new Lexeme( LexemeNature.literal, strPic9 ) );
		mapKeywordLexemes.put( strV, 				new Lexeme( LexemeNature.virtualComma ) );
		mapKeywordLexemes.put( strSign, 			new Lexeme( LexemeNature.sign ) );
		mapKeywordLexemes.put( strOpeningBracket, 	new Lexeme( LexemeNature.openingBracket ) );
		mapKeywordLexemes.put( strClosingBracket, 	new Lexeme( LexemeNature.closingBracket ) );
		mapKeywordLexemes.put( strDot, 				new Lexeme( LexemeNature.dot ) );
		mapKeywordLexemes.put( strQuote, 			new Lexeme( LexemeNature.quote ) );
		
		mapKeywordLexemes.put( strBlankKW, 			new Lexeme( LexemeNature.blank ) );
		mapKeywordLexemes.put( strWhenKW, 			new Lexeme( LexemeNature.when ) );
		mapKeywordLexemes.put( strZeroKW,	 		new Lexeme( LexemeNature.zero ) );
		mapKeywordLexemes.put( strZeroesKW,			new Lexeme( LexemeNature.zeroes ) );
		mapKeywordLexemes.put( strZerosKW, 			new Lexeme( LexemeNature.zeroes ) );
		mapKeywordLexemes.put( strJustifiedKW, 		new Lexeme( LexemeNature.justified ) );
		mapKeywordLexemes.put( strRightKW,	 		new Lexeme( LexemeNature.right ) );
		mapKeywordLexemes.put( strLeftKW, 			new Lexeme( LexemeNature.left ) );
		mapKeywordLexemes.put( strTimesKW, 			new Lexeme( LexemeNature.times ) );
		mapKeywordLexemes.put( strAscendingKW,		new Lexeme( LexemeNature.ascending ) );
		mapKeywordLexemes.put( strDescendingKW,		new Lexeme( LexemeNature.descending ) );
		mapKeywordLexemes.put( strKeyKW, 			new Lexeme( LexemeNature.key ) );
		mapKeywordLexemes.put( strByKW,	 			new Lexeme( LexemeNature.by ) );
		mapKeywordLexemes.put( strToKW,			 	new Lexeme( LexemeNature.to ) );
		mapKeywordLexemes.put( strThroughKW,		new Lexeme( LexemeNature.through ) );
		mapKeywordLexemes.put( strThruKW, 			new Lexeme( LexemeNature.through ) );
		mapKeywordLexemes.put( strDependingKW,		new Lexeme( LexemeNature.depending ) );
		mapKeywordLexemes.put( strOnKW, 			new Lexeme( LexemeNature.on ) );
		mapKeywordLexemes.put( strTrailingKW,	 	new Lexeme( LexemeNature.trailing ) );
		mapKeywordLexemes.put( strLeadingKW,	 	new Lexeme( LexemeNature.leading ) );
		mapKeywordLexemes.put( strSeparateKW,		new Lexeme( LexemeNature.separate ) );
		mapKeywordLexemes.put( strCharacterKW,		new Lexeme( LexemeNature.character ) );
		mapKeywordLexemes.put( strSynchronizedKW,	new Lexeme( LexemeNature.sync ) );
		mapKeywordLexemes.put( strSyncKW, 			new Lexeme( LexemeNature.sync ) );
		
	}
	
	
	/** lexeme nature. */
	private LexemeNature nature;

	/** lexeme value. */
	private String value;
	
	/** uuid. */
	private UUID uuid;
	
	/** line number on which was found the lexeme. */
	private int lineNumber;

	/**
	 * @param nature
	 * @param value
	 */
	public Lexeme(LexemeNature nature, String value) {
		
		if( nature == null ) {
			throw ( new IllegalArgumentException( "nature can't be null" ) );
		}
		
		this.nature = nature;
		this.value = value;
		this.uuid = UUID.randomUUID();
	}

	/**
	 * @param nature
	 */
	public Lexeme(LexemeNature nature ) {
		this( nature, null );
	}

	/**
	 * @return the nature
	 */
	public LexemeNature getNature() {
		return nature;
	}

	/**
	 * @param nature the nature to set
	 */
	public void setNature(LexemeNature nature) {
		this.nature = nature;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @return the value casted as an integer, -1 if not possible 
	 */
	public int getValueAsInt() {
		try {
			return Integer.parseInt( value );
		}
		catch( Exception e ) {
			return -1;
		}
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		Lexeme lex = (Lexeme) object;
		return ( nature == lex.getNature() );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return nature.hashCode();
	}

	/**
	 *
	 * @param kw
	 * @return
	 */
	public static Lexeme getLexemeForKeyWord(String kw) {
		return mapKeywordLexemes.get( kw.toLowerCase() ).cloneLexeme();
	}

	/**
	 *
	 * @param kw
	 * @return
	 */
	public static boolean keyWordExists(String kw) {
		return mapKeywordLexemes.keySet().contains( kw.toLowerCase() );
	}

	/**
	 * Tell whether literal is surrounded by quotes or double quotes.
	 * @return true if literal is surrounded by quotes or double quotes
	 */
	public boolean isQuotedLiteral() {
		if ( value != null ) {
			if ( 
						value.startsWith( strQuote ) && value.endsWith( strQuote )
					||	value.startsWith( strDblQuote ) && value.endsWith( strDblQuote )
				) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tell whether literal represents a numeric value.
	 * @return true if literal represents a numeric value
	 */
	public boolean isNumericLiteral() {
		if ( value != null ) {
			return StringUtils.isNumeric( value );
		}
		return false;
	}

	/**
	 * Tell whether literal represents a 9* value.
	 * @return true if literal represents a 9* value.
	 */
	public boolean isPic9Literal() {
		if ( value != null ) {
			return StringUtils.containsOnly( value, strPic9 );
		}
		return false;
	}

	/**
	 * Tell whether literal represents a X* value.
	 * @return true if literal represents a X* value.
	 */
	public boolean isPicXLiteral() {
		if ( value != null ) {
			return StringUtils.containsOnly( value, strPicX );
		}
		return false;
	}

	/**
	 * Tell whether literal value equals "88".
	 * @return true if literal value equals 88
	 */
	public boolean isLevel88Literal() {
		if ( value != null ) {
			return value.equals( strLevel88 );
		}
		return false;
	}

	/**
	 * Tell whether literal value equals "S".
	 * @return true if literal value equals "S"
	 */
	public boolean isSignLiteral() {
		if ( value != null ) {
			return value.equalsIgnoreCase( "S" );
		}
		return false;
	}
	
	/**
	 * Tell whether literal value equals "V".
	 * @return true if literal value equals "V"
	 */
	public boolean isVirtualCommaLiteral() {
		if ( value != null ) {
			return value.equalsIgnoreCase( "V" );
		}
		return false;
	}

	/**
	 * Clone a lexeme, that is all its attributes except its uuid.
	 * @return
	 */
	public Lexeme cloneLexeme() {
		return new Lexeme( nature, ( value == null ? null : "" + getValue() ) );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String toReturn = "Lexeme <" + uuid.toString() + "> [" + nature.toString();
		if ( value != null ) {
			toReturn += "|" + value;
		}
		toReturn += "]" ;
		return toReturn;
	}
}
