package org.nla.MyFileViewer.ui.tree;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

import org.nla.cobol.GenericActualElement;
import org.nla.cobol.Variable;



public class VariableTreeNode extends GenericElementTreeNode {
	
	/**
	 * TODO
	 * @param variable
	 */
	public VariableTreeNode(GenericActualElement variable) {
		super( variable );
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	@Override
	public final Enumeration<TreeNode> children() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	@Override
	public TreeNode getChildAt(int arg0) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	@Override
	public int getChildCount() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	@Override
	public int getIndex(TreeNode arg0) {
		return -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	@Override
	public boolean isLeaf()	{
		return true;
	}

	/**
	 * @return the variable
	 */
	public Variable getVariable() {
		return (Variable)element;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(GenericActualElement variable) {
		element = variable;
	}
}
