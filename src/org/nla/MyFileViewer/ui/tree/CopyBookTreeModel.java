
package org.nla.MyFileViewer.ui.tree;

import javax.swing.tree.DefaultTreeModel;


public class CopyBookTreeModel extends DefaultTreeModel {
	
	/** serial version uid. */
	private static final long serialVersionUID = 7925585493676533129L;
	
	/** copybook tree node. */
	protected CopyBookTreeNode rootNode;

	/**
	 * Build tree model from copybook tree node
	 * @param rootNode
	 */
	public CopyBookTreeModel(CopyBookTreeNode rootNode) {
		super(rootNode);
		this.rootNode = rootNode;
	}

	/**
	 * @return the rootNode
	 */
	public CopyBookTreeNode getRootNode() {
		return rootNode;
	}

	/**
	 * @param rootNode the rootNode to set
	 */
	public void setRootNode(CopyBookTreeNode rootNode) {
		this.rootNode = rootNode;
	}
	
	/**
	 * 
	 * @param elementNameToFind
	 * @param fromPosition
	 * @return
	 */
	public GenericElementTreeNode findNodeForName( String elementNameToFind, int fromPosition ) {
		return rootNode.findNodeForName(elementNameToFind, fromPosition);
	}
}