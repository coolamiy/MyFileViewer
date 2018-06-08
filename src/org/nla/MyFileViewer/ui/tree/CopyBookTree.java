package org.nla.MyFileViewer.ui.tree;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import org.nla.cobol.CopyBook;


public final class CopyBookTree extends JTree {

	/** auto genarated serial version uid. */
	private static final long serialVersionUID = 8577605256859907725L;

	/** associated copybook. */
	private CopyBook copyBook;

	/** root tree node. */
	private CopyBookTreeNode rootNode;

	/** tree model. */
	private CopyBookTreeModel treeModel;

	/** renderer. */
	private CopyBookTreeCellRenderer renderer;

	/**
	 * @param copyBook
	 */
	public CopyBookTree( CopyBook copyBook )/* throws Exception*/ {
		this.copyBook = copyBook;
		initTree();
	}

	private void initTree() {
		rootNode = new CopyBookTreeNode( copyBook );
		treeModel = new CopyBookTreeModel( rootNode );
		renderer = new CopyBookTreeCellRenderer();
		setModel( treeModel );
		setEditable( true );
		setRootVisible( true );
		setCellRenderer( renderer );
		setCellEditor( new CopyBookTreeCellEditor(this, new CopyBookTreeCellRenderer()) );
		ToolTipManager.sharedInstance().registerComponent(this);
	}

	/**
	 * @return the treeModel
	 */
	public CopyBookTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * Set the renderer in hexa mode.
	 * @param	isHexaView	indicates whether the renderer is in hexa mode.
	 */
	public void setHexaView( boolean isHexaView ) {
		renderer.setHexa( isHexaView );
	}


	/**
	 * Set the renderer not to display group values.
	 * @param	displayGroupValues	indicates whether the renderer displays group values.
	 */
	public void setDisplayGroupValue( boolean displayGroupValues ) {
		renderer.setDisplayGroupValues( displayGroupValues );
	}
}

