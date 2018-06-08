
package org.nla.MyFileViewer.ui.tree;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.Map;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.nla.cobol.Choice;
import org.nla.cobol.ChoiceList;
import org.nla.cobol.GenericActualElement;
import org.nla.cobol.Group;
import org.nla.cobol.IActualElement;
import org.nla.MyFileViewer.ui.MyFileViewerUIResourceBundle;


public class CopyBookTreeCellRenderer  extends DefaultTreeCellRenderer {
	
	/** serial version uid. */	
	private static final long serialVersionUID = -5173753239824266504L;

	/** hexa vision of element values. */
	private boolean isHexa;

	/** display group values. */
	private boolean displayGroupValues;

	public CopyBookTreeCellRenderer() {
		isHexa = false;
		displayGroupValues = true;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)	{
		  Component rc = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		  // render a variable tree node and its tool tip
		  if ( value instanceof VariableTreeNode ) {
			  VariableTreeNode treeNode = (VariableTreeNode)value;
			  GenericActualElement variable = treeNode.getVariable();
			  

			  if ( isHexa ) {
				  setText( variable.getDisplayableValue( IActualElement.HEXA_VIEW ) );
			  }
			  else if ( !isHexa ) {
				  setText( variable.getDisplayableValue( IActualElement.STD_VIEW ) );
			  }
			  String toolTipText = MyFileViewerUIResourceBundle.getString( "copyBookDialog.variableTreeNode.tooltip" );
			  Map<String, Object> mapAttributes = variable.getAttributes();

			  // TODO : use proper API to render the list in the formatter
			  ChoiceList choiceList = (ChoiceList)mapAttributes.get( "choiceList" );
			  if ( choiceList != null && choiceList.size() > 0 ) {
				  StringBuffer choiceListAsHtml = new StringBuffer();
				  
				  for ( Choice choice : choiceList.values() ) {
					  choiceListAsHtml.append( "&nbsp;&nbsp;" );
					  choiceListAsHtml.append( choice.getName() );
					  choiceListAsHtml.append( "&nbsp;=&nbsp;" );
					  choiceListAsHtml.append( choice.getChoiceValue() );
					  choiceListAsHtml.append( "<br>" );
				  }
				  mapAttributes.remove( "choiceList" );
				  mapAttributes.put( "choiceList", choiceListAsHtml.toString() );
			  }
			  else {
				  mapAttributes.remove( "choiceList" );
				  mapAttributes.put( "choiceList", "&nbsp;&nbsp;none" );
			  }

			  Object[] attributes = mapAttributes.values().toArray();
			  setToolTipText( MessageFormat.format(toolTipText, attributes) );
		  }
		  // render a group tree node and its tool tip
		  else if ( value instanceof GroupTreeNode ) {
			  GroupTreeNode treeNode = (GroupTreeNode)value;
			  Group group = treeNode.getGroup();

			  if ( displayGroupValues && isHexa ) {
				  setText( group.getDisplayableValue( IActualElement.HEXA_VIEW ) );
			  }
			  else if ( displayGroupValues && !isHexa ) {
				  setText( group.getDisplayableValue( IActualElement.STD_VIEW ) );
			  }
			  else {
				  setText( group.getDisplayableValue( IActualElement.NO_VALUE_VIEW ) );
			  }
			  String toolTipText = MyFileViewerUIResourceBundle.getString( "copyBookDialog.groupTreeNode.tooltip" );
			  Object[] attributes = group.getAttributes().values().toArray();
			  setToolTipText( MessageFormat.format(toolTipText, attributes) );
		  }

		  repaint();

		  return rc;
	  }

	/**
	 * @return the isHexa
	 */
	public boolean isHexa() {
		return isHexa;
	}

	/**
	 * @return the displayGroupValues
	 */
	public boolean isDisplayGroupValues() {
		return displayGroupValues;
	}

	/**
	 * @param isHexa the isHexa to set
	 */
	public void setHexa(boolean isHexa) {
		this.isHexa = isHexa;
	}

	/**
	 * @param displayGroupValues the displayGroupValues to set
	 */
	public void setDisplayGroupValues(boolean displayGroupValues) {
		this.displayGroupValues = displayGroupValues;
	}
}