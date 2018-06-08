
package org.nla.cobol.xml;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.nla.cobol.CopyBook;
import org.nla.cobol.Group;
import org.nla.cobol.IActualElement;
import org.nla.cobol.Picture;
import org.nla.cobol.Variable;


public class CopyBookToXmlGenerator {
	
	/** copybook to transform. */
	private CopyBook copyBook;
	
	/** xml document. */
	private Document copyBookDocument;
	
	
	
	/**
	 * Constructor using a copybook.
	 * @param copyBook
	 */
	public CopyBookToXmlGenerator(CopyBook copyBook) {
		super();
		this.copyBook = copyBook;
	}
	
	/**
	 * Generates a XML Document based upon the copybook.
	 */
	public String transformCopyBookToDom() {
		
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			copyBookDocument = docBuilder.newDocument();
			
			// xml generic properties
			copyBookDocument.setXmlVersion( "1.0" );
			copyBookDocument.setXmlStandalone( true );
			
			// xml elements
			copyBookDocument.appendChild( buildXmlElement( copyBook ) );
			
			
			// string transform
			return ( domToString( copyBookDocument ) );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		return null;
	}
	
	private Element buildXmlElement( IActualElement element ) {

		if ( element instanceof Group ) {
			return buildGroupElement( (Group)element );
		}
		else if ( element instanceof Variable ) {
			return buildFieldElement( (Variable)element );
		}
		else {
			return null;
		}
	}
	

	private Element buildGroupElement( Group group ) {
		
		// xml element
		Element xmlElement;
		
		if ( group instanceof CopyBook ) {
			xmlElement = copyBookDocument.createElement( "copybook" );
		}
		else /* if ( group instanceof Group ) */{
			xmlElement = copyBookDocument.createElement( "group" );
			xmlElement.setAttribute( "isOccursed" , group.isOccursed() + "" );
			if ( group.isOccursed() ) {
				xmlElement.setAttribute( "occursCount" , group.getOccursCount() + "" );
				
				if ( group.isExtended() ) {
					int[] indices = group.getIndices();
					xmlElement.setAttribute( "occursIndex", indices[indices.length-1] + "" );
				}

			}
		}
		xmlElement.setAttribute( "level" , group.getLevel() + "" );
		xmlElement.setAttribute( "name" , group.getName() );
		xmlElement.setAttribute( "size" , group.getSize() + "" );
		xmlElement.setAttribute( "startPosition" , group.getStartPosition() + "" );
		xmlElement.setAttribute( "endPosition" , group.getEndPosition() + "" );
		
		// xml child elements
		for ( IActualElement element : group.getChildElements() ) {
			Element childXmlElement = buildXmlElement( element );
			xmlElement.appendChild( childXmlElement );
		}
		
		return xmlElement;
	}

	private Element buildFieldElement( Variable variable ) {
		
		// xml element
		Element xmlElement = copyBookDocument.createElement( "field" );
		xmlElement.setAttribute( "level" , variable.getLevel() + "" );
		xmlElement.setAttribute( "name" , variable.getName() );
		xmlElement.setAttribute( "size" , variable.getSize()+ "" );
		xmlElement.setAttribute( "startPosition" , variable.getStartPosition() + "" );
		xmlElement.setAttribute( "endPosition" , variable.getEndPosition() + "" );

		xmlElement.setAttribute( "picture" , variable.getType().getPicture() + "" );
		
		if ( variable.getType().getPicture() == Picture.Pic9 ) {
			xmlElement.setAttribute( "isSigned" , variable.getType().isSigned() + "" );
			xmlElement.setAttribute( "integerSize" , variable.getType().getIntegerSize() + "" );
			xmlElement.setAttribute( "decimalSize" , variable.getType().getDecimalSize() + "" );
		}
		
		if ( variable.getUsage() != null ) {
			xmlElement.setAttribute( "usage" , variable.getUsage() + "" );
		}
		
		xmlElement.setAttribute( "isOccursed" , variable.isOccursed() + "" );
		if ( variable.isOccursed() ) {
			xmlElement.setAttribute( "occursCount" , variable.getOccursCount() + "" );
			
			if ( variable.isExtended() ) {
				int[] indices = variable.getIndices();
				xmlElement.setAttribute( "occursIndex", indices[indices.length-1] + "" );
			}
		}
		
		return xmlElement;
	}
	
	/**
	 * Transform a XML Document to its String representation 
	 * @param doc
	 * @return	String representation of the XML Document
	 * @throws Exception	If a TransformerConfigurationException or a TransformerException occurs
	 */
	private static String domToString( Document doc ) throws Exception {
		if ( doc == null ) {
			return null;
		}
		
		try {
			DOMSource domSource = new DOMSource( doc );
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult( writer );
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform( domSource, result);
			return ( writer.toString() );
		}
		catch (TransformerConfigurationException tfce) {
			throw new Exception( "CobolToXmlGenerator.domToString(): " + tfce.getMessage(), tfce );
		}
		catch (TransformerException te) {
			throw new Exception( "CobolToXmlGenerator.domToString(): " + te.getMessage(), te );
		}
	}
}
