package org.nla.MyFileViewer.ui.tree;

import java.util.ArrayList;

//import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.nla.cobol.IActualElement;



//@SuppressWarnings("serial")
public abstract class GenericElementTreeNode implements TreeNode  {
	/** element. */
	protected IActualElement element;

	/** parent node. */
	protected TreeNode parentNode;
	
	/** copybook super parent tree node. */
	protected CopyBookTreeNode copyBookTreeNode; 


	/**
	 * TODO
	 * @param element
	 */
	protected GenericElementTreeNode(IActualElement element) {
		this.element = element;
	}

	
	/**
	 * TODO
	 * @return
	 */
	public IActualElement getElement() {
		return element;
	}

	/**
	 * TODO
	 * @param element
	 */
	public void setElement(IActualElement element) {
		this.element = element;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	@Override
	public TreeNode getParent() {
		return parentNode;
	}

	/**
	 * TODO
	 * @param parentNode
	 */
	public void setParent(TreeNode parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * @return the copyBookNode
	 */
	public CopyBookTreeNode getCopyBookTreeNode() {
		return copyBookTreeNode;
	}

	/**
	 * @param copyBookNode the copyBookNode to set
	 */
	public void setCopyBookTreeNode(CopyBookTreeNode copyBookNode) {
		this.copyBookTreeNode = copyBookNode;
	}



    /**
     * Returns the TreePath used to designate this node in the tree structure.
     */
    public GenericElementTreeNode[] getPath() {
        ArrayList<GenericElementTreeNode> nodes = new ArrayList<GenericElementTreeNode>();
        GenericElementTreeNode node = this;
        while  (node != null) {
        	//String name = node.getElement().getName();
            // Adds the new node at the beginning of the list.
            nodes.add(0, node);
            node = (GenericElementTreeNode) node.getParent();
        }
        // The "new FileTreeNode[0]" parameter is only used to
        // tell the toArray method which runtime type the returned
        // array should have.
        return nodes.toArray(new GenericElementTreeNode[0]);
    }

	
	
   
   
}
