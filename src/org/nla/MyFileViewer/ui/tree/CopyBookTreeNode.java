
package org.nla.MyFileViewer.ui.tree;
import java.util.ArrayList;
import org.nla.cobol.CopyBook;



public class CopyBookTreeNode extends GroupTreeNode {

	/** list of element names. */
	private ArrayList<String> elementNames = new ArrayList<String>();
	
	/** list of nodes, accorded to elements names. */
	private ArrayList<GenericElementTreeNode> elementNodes = new ArrayList<GenericElementTreeNode>();
	
	
	/**
	 * @param group
	 * @param copyBook
	 */
	public CopyBookTreeNode(CopyBook copyBook) {
		super(copyBook);
		setCopyBookTreeNode( this );
		buildChildren();
	}

	/**
	 * @return the copyBook
	 */
	public CopyBook getCopyBook() {
		return (CopyBook)element;
	}

	/**
	 * @param copyBook the copyBook to set
	 */
	public void setCopyBook(CopyBook copyBook) {
		element = copyBook;
	}
	
	/**
	 * Add a node to the node mapping.
	 * @param node
	 */
	
	public void addElementTreeNode( GenericElementTreeNode node ) {
		elementNames.add( node.getElement().getName() );
		elementNodes.add( node );
	}
	
	protected GenericElementTreeNode findNodeForName( String elementNameToFind, int fromPosition ) {
		if ( fromPosition >= elementNames.size() ) {
			throw new IllegalArgumentException( "CopyBookTreeNode.findNodeForName() from position ("+fromPosition+")out of bounds ("+elementNames.size()+")" );
		}
		if ( elementNameToFind == null ) {
			throw new IllegalArgumentException( "CopyBookTreeNode.findNodeForName() element name can't be null" );
		}
		for ( int i = fromPosition; i < elementNames.size(); ++i ) {
			String elementName = elementNames.get( i );
			if ( elementName != null && elementName.equals( elementNameToFind ) ) {
				return elementNodes.get( i );
			}
		}
		return null;
	}
}
