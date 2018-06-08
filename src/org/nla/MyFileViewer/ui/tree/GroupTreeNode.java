
package org.nla.MyFileViewer.ui.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;

import org.nla.cobol.GenericActualElement;
import org.nla.cobol.Group;
import org.nla.cobol.IActualElement;
import org.nla.cobol.Variable;


public class GroupTreeNode extends GenericElementTreeNode {

	/** child nodes. */
	protected ArrayList<TreeNode> childTreeNodes = new ArrayList<TreeNode>(); 

	/**
	 * @param group
	 */
	protected GroupTreeNode(Group group) {
		super(group);
	}
	
	/**
	 * 
	 */
	protected void buildChildren() {
		Group group = (Group)element;
		for ( IActualElement actualElement : group.getChildElements() ) {
			if ( actualElement instanceof Variable ) {
				VariableTreeNode variableNode = new VariableTreeNode((GenericActualElement)actualElement);
				variableNode.setCopyBookTreeNode( copyBookTreeNode );
				addChildNode( variableNode );
			}
			else if ( actualElement instanceof Group ) {
				GroupTreeNode groupNode = new GroupTreeNode((Group)actualElement);
				groupNode.setCopyBookTreeNode( copyBookTreeNode );
				groupNode.buildChildren();
				addChildNode( groupNode );
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	@Override
	public Enumeration<TreeNode> children() {
		return Collections.enumeration(childTreeNodes);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	@Override
	public TreeNode getChildAt(int childIndex) {
		return childTreeNodes.get(childIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	@Override
	public int getChildCount() {
		return childTreeNodes.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	@Override
	public int getIndex(TreeNode node) {
		return childTreeNodes.indexOf(node);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	@Override
	public boolean isLeaf()	{
		return false;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return (Group)element;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(Group group) {
		element = group;
	}

	/**
	 * @return the childTreeNodes
	 */
	public ArrayList<TreeNode> getChildTreeNodes() {
		return childTreeNodes;
	}

	/**
	 * @param childTreeNodes the childTreeNodes to set
	 */
	public void setChildTreeNodes(ArrayList<TreeNode> childTreeNodes) {
		this.childTreeNodes = childTreeNodes;
	}
	
	/**
	 * 
	 * @param node
	 */
	public void addChildNode( GenericElementTreeNode node ) {
		childTreeNodes.add( node );
		node.setParent( this );
		if ( copyBookTreeNode != null ) { 
			copyBookTreeNode.addElementTreeNode( node );
		}
	}
}