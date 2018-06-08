package org.nla.MyFileViewer.ui.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.EventObject;

import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.nla.cobol.IActualElement;
import org.nla.cobol.Picture;
import org.nla.cobol.Variable;

public class CopyBookTreeCellEditor extends DefaultTreeCellEditor implements ActionListener {

	/** field allowing to input new variable value. */
	protected JTextField field;

	/** format to.  */
	protected NumberFormat numberFormat;
	
	/** tree on which the editor is associated. */
	protected JTree tree;
	
	/** edited variable. */
	protected Variable variable;

	/**
	 * @param tree
	 * @param renderer
	 */
	public CopyBookTreeCellEditor(JTree tree, CopyBookTreeCellRenderer renderer) {
		super(tree, renderer);
		this.tree = tree;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		  if ( value instanceof VariableTreeNode ) {
			  VariableTreeNode treeNode = (VariableTreeNode)value;
			  variable = treeNode.getVariable();

			  if ( variable.getType().getPicture() == Picture.Pic9 ) {
				  numberFormat = NumberFormat.getNumberInstance();
				  numberFormat.setMinimumIntegerDigits( variable.getType().getIntegerSize() );
				  numberFormat.setMinimumFractionDigits( variable.getType().getDecimalSize() );
				  numberFormat.setMaximumIntegerDigits( variable.getType().getIntegerSize() );
				  numberFormat.setMaximumFractionDigits( variable.getType().getDecimalSize() );
				  field = new JFormattedTextField(numberFormat);
			  }
			  else  if ( variable.getType().getPicture() == Picture.PicX ) {
				  field = new JTextField();
			  }

			  field.addActionListener(this);
			  field.setToolTipText(variable.getType().toString());
			  field.setText(variable.getMappedValue());
			  field.setColumns(30);

			  return field;
		  }
		  return null;
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject event) {
		JTree tree = (JTree)event.getSource();
	      if (event instanceof MouseEvent) {
	          MouseEvent mouseEvent = (MouseEvent) event;
	          TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	          TreeNode node = (TreeNode) path.getLastPathComponent();

			  IActualElement element = null;
			  if ( node instanceof VariableTreeNode ) {
				  VariableTreeNode treeNode = (VariableTreeNode)node;
				  element = treeNode.getVariable();
			  }
			  else {
				  GroupTreeNode treeNode = (GroupTreeNode)node;
				  element = treeNode.getGroup();
			  }
			  return element.isLeaf();
	      }
	      return false;
	}



	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellEditor#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0){
		variable.setMappedValue( field.getText() );
	}
}
