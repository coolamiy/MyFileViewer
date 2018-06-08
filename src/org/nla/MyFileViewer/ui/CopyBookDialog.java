package org.nla.MyFileViewer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Observable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import org.nla.cobol.CopyBook;
import org.nla.cobol.FlatFile;
import org.nla.cobol.xml.CopyBookToXmlGenerator;
import org.nla.MyFileViewer.ui.tree.CopyBookTree;
import org.nla.MyFileViewer.ui.tree.CopyBookTreeModel;
import org.nla.MyFileViewer.ui.tree.GenericElementTreeNode;

@SuppressWarnings("serial")
public class CopyBookDialog extends JDialog implements ClipboardOwner {

	/** attached copybook. */
	private CopyBook copyBook;

	/**  mapped flat file. */
	private FlatFile flatFile;

	/** flags indicating the mode state. */
	//private boolean isMappedToFlatFile, isDataMapped, isCopyBookLoaded, isCollapsed, isGroupValueDisplayed, isHexaDisplayed;
	private boolean isMappedToFlatFile, isCopyBookLoaded, isCollapsed;
	
	/** line number. */
	private int lineNumber, maxLineNumber;
	
	/** searched field. */
	private String searchedField;

	/** menu bar. */
	private JMenuBar menuBar;

	/** menus. */
	private JMenu optionsMenu, viewMenu;
	
	/** popup menu. */
	private JPopupMenu popupMenu;

	/** menu items. */
	private JMenuItem mapDataItem, unmapItem, switchToTreeItem, switchToTableItem, closeItem, exportItem, exportExtendedItem;

	/** checkbox menu items. */
	private JCheckBoxMenuItem hexaViewItem, displayGroupValuesItem;
	
	/** popup menu items. */
	private JMenuItem initializeItem, expandItem, collapseItem, copyItem, pasteItem, searchItem, searchNextItem;
	
	/** mapped file content navigation bar.*/
	private JPanel navigationBar;

	/** previous end next line button.*/
	private JButton nextLineButton, previousLineButton;

	/** current line index.*/
	private JFormattedTextField lineNumberField;

	/** actual component displaying the copybook. */
	//private JComponent copyBookComponent;
	private CopyBookTree copyBookTree;
	
	/** parent file aide frame. */
	private MyFileViewerFrame parentFrame;
	
//	/** treenode selected. */
//	private TreeNode selectedTreeNode;
	
	/** tree path selected */
	private TreePath selectedPath;
	
	/** observable. */
	private Observable observable;
	
	/** info panel. */
	private CopyBookDialogInfoPanel infoPanel;
	

	/**
	 * Create a copybook dialog using a copybook.
	 * @param copyBook
	 */
	public CopyBookDialog( CopyBook copyBook, File copyBookFile, MyFileViewerFrame parentFrame ) {
		
		this.copyBook = copyBook;
		this.parentFrame = parentFrame;
		this.lineNumber = 1;
		this.observable = new Observable();
//		this.isCollapsed = false;
//		this.isGroupValueDisplayed = true;
//		this.isHexaDisplayed = false;
		
		// components initialization
		initComponents();
		initTreeView();
		updateControls();

		// general dialog properties
		setTitle( copyBook.getName() + " [" + copyBookFile.getName() + "]" );
		setModal( false );
		setLocation( 600, 300 );
		getContentPane().setPreferredSize( new Dimension( 450, 300 ) );
		pack();
		setVisible( true );
	}

	/**
	 * Initialize all components.
	 */
	private void initComponents() {
		// menu bar
		menuBar = new JMenuBar();
		setJMenuBar( menuBar );

		// menus
		optionsMenu = new JMenu( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options" ) );
		viewMenu = new JMenu( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.view" ) );
		popupMenu = new JPopupMenu( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup" ) );
		menuBar.add( optionsMenu );
		menuBar.add( viewMenu );

		// options menu items
		mapDataItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.map" ) );
		mapDataItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_M, ActionEvent.ALT_MASK));

		unmapItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.unmap" ) );
		unmapItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_U, ActionEvent.ALT_MASK));

		switchToTreeItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.switchToTree" ) );
		switchToTreeItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_1, ActionEvent.ALT_MASK));

		switchToTableItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.switchToTable" ) );
		switchToTableItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_2, ActionEvent.ALT_MASK));

		exportItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.export" ) );
		exportItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_E, ActionEvent.ALT_MASK));

		exportExtendedItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.exportExtended" ) );
		exportExtendedItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Z, ActionEvent.ALT_MASK));
		
		closeItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.options.close" ) );
		closeItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.ALT_MASK));

		optionsMenu.add( mapDataItem );
		optionsMenu.add( unmapItem );
		optionsMenu.addSeparator();
		optionsMenu.add( switchToTreeItem );
		optionsMenu.add( switchToTableItem );
		optionsMenu.addSeparator();
		optionsMenu.add( exportItem );
		optionsMenu.add( exportExtendedItem );
		optionsMenu.addSeparator();
		optionsMenu.add( closeItem );

		// view menu items
		hexaViewItem = new JCheckBoxMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.view.hexaOn" ) );
		hexaViewItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_H, ActionEvent.ALT_MASK));
		
		displayGroupValuesItem = new JCheckBoxMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.view.displayGroupValues" ) );
		displayGroupValuesItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_G, ActionEvent.ALT_MASK));
		displayGroupValuesItem.setSelected( true );

		viewMenu.add( hexaViewItem );
		viewMenu.add( displayGroupValuesItem );

		// popup menu items
		expandItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.expand" ) );
		expandItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_E, ActionEvent.ALT_MASK));

		collapseItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.collapse" ) );
		collapseItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.ALT_MASK));

		initializeItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.initialize" ) );
		initializeItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_I, ActionEvent.CTRL_MASK));

		copyItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.copy" ) );
		copyItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		pasteItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.paste" ) );
		pasteItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_V, ActionEvent.CTRL_MASK));

		searchItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.search" ) );
		searchItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_F, ActionEvent.CTRL_MASK));

		searchNextItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.searchNext" ) );
		searchNextItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_F, ActionEvent.CTRL_MASK + ActionEvent.SHIFT_MASK ));
		
		popupMenu.add( expandItem );
		popupMenu.add( collapseItem );
		popupMenu.addSeparator();
		popupMenu.add( initializeItem );
		popupMenu.add( copyItem );
		popupMenu.add( pasteItem );
		popupMenu.addSeparator();
		popupMenu.add( searchItem );
		//popupMenu.add( searchNextItem );
		
		// navigation bar
		lineNumberField = new JFormattedTextField( NumberFormat.getIntegerInstance() );
		lineNumberField.setFocusLostBehavior( JFormattedTextField.REVERT );
		lineNumberField.setColumns( 5 );
		nextLineButton = new JButton( ">" );
		previousLineButton = new JButton( "<" );
		navigationBar = new JPanel();
		navigationBar.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		navigationBar.add( lineNumberField );
		navigationBar.add( previousLineButton );
		navigationBar.add( nextLineButton );

		// actions
		mapDataItem.addActionListener( new MapDataAction() );
		exportItem.addActionListener( new ExportAction() );
		exportExtendedItem.addActionListener( new ExportExtendedAction() );
		closeItem.addActionListener( new CloseAction() );
		nextLineButton.addActionListener( new NextLineAction() );
		previousLineButton.addActionListener( new PreviousLineAction() );
		lineNumberField.addFocusListener( new SpecificLineAction() );
		hexaViewItem.addActionListener( new HexaAction () );
		displayGroupValuesItem.addActionListener( new GroupViewAction() );
		expandItem.addActionListener( new ExpandAction() );
		collapseItem.addActionListener( new CollapseAction() );
		initializeItem.addActionListener( new InitializeAction() );
		copyItem.addActionListener( new CopyAction() );
		pasteItem.addActionListener( new PasteAction() );
		searchItem.addActionListener( new SearchAction() );
		searchNextItem.addActionListener( new SearchNextAction() );
		addWindowListener( new CloseDialogAction() ); 
		
	}

	/**
	 * Initialize tree view with copybook.
	 */
	private void initTreeView() {
		// tree
		copyBookTree = new CopyBookTree( copyBook );
		JScrollPane scrollPane = new JScrollPane( copyBookTree );

		// refresh screen : add tree
		getContentPane().removeAll();
		getContentPane().add( scrollPane, BorderLayout.CENTER );
		getContentPane().setPreferredSize( new Dimension( 450, 300 ) );
		pack();

		isCopyBookLoaded = true;
		
		copyBookTree.addMouseListener( new CopyBookTreeMouseListener() );
	}

	/**
	 * reload tree view.
	 */
	private void reloadTreeView() {
		// Completely reload the tree and explicitely expand all nodes previously expanded
		// This snippet is aimed to prevent the incorrect display of '...' at the end of each node
		// when a change of view occurs (for instance when hexa is set to on) and when the previously
		// displayed nodes aren't well reloaded. 
		//CopyBookTreeModel model = copyBookTree.getTreeModel();
		Enumeration<TreePath> tps = copyBookTree.getExpandedDescendants( copyBookTree.getPathForRow( 0 ) );
		copyBookTree.getTreeModel().reload();
		while( tps != null && tps.hasMoreElements() ) {
			copyBookTree.expandPath( tps.nextElement() );
		}
	}

	/**
	 * Modify states of current controls depending on the action.
	 */
	private void updateControls() {

		
		switchToTreeItem.setEnabled( false );
		switchToTableItem.setEnabled( false );

		// general operations 
		collapseItem.setSelected( isCollapsed );
		lineNumberField.setText( new Integer( lineNumber ).toString() );

		if ( isCopyBookLoaded && !isMappedToFlatFile ) {
			mapDataItem.setEnabled( true );
			unmapItem.setEnabled( false );
			collapseItem.setEnabled( true );
		}

		if ( !isCopyBookLoaded ) {
			mapDataItem.setEnabled( false );
		}

		if ( isMappedToFlatFile ) {
//			unmapItem.setEnabled( true );
			collapseItem.setEnabled( false );
		}

		if ( isMappedToFlatFile && lineNumber == 1 ){
			previousLineButton.setEnabled( false );
		}
		else if ( isMappedToFlatFile && lineNumber > 1 ){
			previousLineButton.setEnabled( true );
		}

		if ( isMappedToFlatFile && maxLineNumber == lineNumber ){
			nextLineButton.setEnabled( false );
		}
		else if ( isMappedToFlatFile && maxLineNumber != lineNumber ){
			nextLineButton.setEnabled( true );
		}

//		this.isCollapsed = false;
//		this.isGroupValueDisplayed = true;
//		this.isHexaDisplayed = false;

	}
	
//	/**
//	 * Add an observer.
//	 * @param o	an observer.
//	 */
//	public void addObserver( Observer o ) {
//		observable.addObserver( o );
//	}
	
	

	/**
	 * @param infoPanel the infoPanel to set
	 */
	public void setInfoPanel(CopyBookDialogInfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	/**
	 * @return the copyBook
	 */
	public CopyBook getCopyBook() {
		return copyBook;
	}

	/**
	 * @param copyBook the copyBook to set
	 */
	public void setCopyBook(CopyBook copyBook) {
		this.copyBook = copyBook;
	}

	/**
	 * Display the stack of an exception in a dialog box. 
	 * @param exc	Exception whose stack is to be displayed.
	 */
	private void displayExceptionStack( Exception exc ) {
		StringWriter sw = new StringWriter();
		exc.printStackTrace(new PrintWriter(sw));
		JOptionPane.showMessageDialog(
				this,
				sw.toString(),
				MyFileViewerUIResourceBundle.getString( "global.error" ),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Map copybook to flat file action 
	 */
	protected class MapDataAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			int action = parentFrame.getFileChooser().showOpenDialog( parentFrame );
			if ( action == JFileChooser.APPROVE_OPTION ) {
				try
				{
					File file = parentFrame.getFileChooser().getSelectedFile();
					flatFile = new FlatFile( file );
					isMappedToFlatFile = true;
					lineNumber = 1;
					maxLineNumber = flatFile.getLineCount();
					
					// format title
					getContentPane().add( navigationBar, BorderLayout.SOUTH );
					String title = MyFileViewerUIResourceBundle.getString( "copyBookDialog.title.mapped" );
					Object arguments[] = new Object[]{ copyBook.getName(), file.getName(), maxLineNumber };
					setTitle( MessageFormat.format(title, arguments) );

					// map line 1 content to  
					copyBook.mapToDataGlobal( flatFile.get( lineNumber-1 ) );
					
					// reload controls
					pack();
					reloadTreeView();
					updateControls();
				}
				catch( Exception exc ) {
					displayExceptionStack( exc );
				}
			}
		}
	}

	/**
	 * Map copybook to flat file action 
	 */
	protected class CloseAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			dispose();
		}
	}

	/**
	 * Map copybook to next line of the flat file
	 */
	protected class NextLineAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( lineNumber < maxLineNumber ) {
				lineNumber += 1;
			}
			copyBook.mapToDataGlobal( flatFile.get( lineNumber-1 ) );
			reloadTreeView();
			updateControls();
		}
	}

	/**
	 * Map copybook to previous line of the flat file
	 */
	protected class PreviousLineAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( lineNumber > 0 ) {
				lineNumber -= 1;
			}
			copyBook.mapToDataGlobal( flatFile.get( lineNumber-1 ) );
			reloadTreeView();
			updateControls();
		}
	}

	/**
	 * Map copybook to a specific line of the flat file
	 */
	protected class SpecificLineAction extends FocusAdapter {
		/* (non-Javadoc)
		 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
		 */
		@Override
		public void focusLost(FocusEvent e) {
			try {
				int inputLineNumber = (int)Long.parseLong( lineNumberField.getText() );
				if ( inputLineNumber < maxLineNumber ) 	{
					lineNumber = inputLineNumber;
					copyBook.mapToDataGlobal( flatFile.get( lineNumber-1 ) );
					reloadTreeView();
					updateControls();
				}
			}
			catch( Exception exc ) {
			}
		}
	}

	/**
	 * Collapse the copybook into its non extended shape.
	 */
	protected class CollapseAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( selectedPath != null ) {
				copyBookTree.collapsePath( selectedPath );
			}
		}
	}

	/**
	 * Swicth from hexadecimal to standard view.
	 */
	protected class HexaAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			copyBookTree.setHexaView( hexaViewItem.isSelected() );
			reloadTreeView();
			updateControls();
		}
	}

	/**
	 * Display or hide group values.  
	 */
	protected class GroupViewAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			copyBookTree.setDisplayGroupValue( displayGroupValuesItem.isSelected() );
			reloadTreeView();
			updateControls();
		}
	}

	/**
	 * TODO  
	 */
	protected class ExpandAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( selectedPath != null ) {
				copyBookTree.expandPath( selectedPath );
			}
		}
	}

	/**
	 * TODO  
	 */
	protected class InitializeAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( selectedPath != null ) {
				GenericElementTreeNode node = (GenericElementTreeNode)selectedPath.getLastPathComponent();
				node.getElement().initialize();
				//System.out.println(("node " + node.getElement().getName() + " initialized" );
				reloadTreeView();
				updateControls();
			}
		}
	}

	/**
	 * TODO
	 */
	protected class CopyAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( selectedPath != null ) {
				Clipboard systemClipboard = getToolkit ().getSystemClipboard ();
				GenericElementTreeNode node = (GenericElementTreeNode)selectedPath.getLastPathComponent();
				systemClipboard.setContents( new StringSelection(node.getElement().getMappedValue()) , CopyBookDialog.this);
				//System.out.println(("copied to clipboard: " + node.getElement().getMappedValue());
			}
		}
	}

	/**
	 * TODO
	 */
	protected class PasteAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( selectedPath != null ) {
				Clipboard systemClipboard = getToolkit ().getSystemClipboard ();
				GenericElementTreeNode node = (GenericElementTreeNode)selectedPath.getLastPathComponent();

				try {
					Object copiedString = systemClipboard.getData(DataFlavor.stringFlavor);
					node.getElement().mapToDataLocal(copiedString.toString());
					//System.out.println(("pasted from clipboard: " + node.getElement().getMappedValue());
				}
				catch(Exception e) {
					//System.out.println(("nothing pasted from clipboard...");
				}
				reloadTreeView();
				updateControls();
			}
		}
	}
	
	/**
	 * TODO
	 */
	protected class SearchAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			searchedField = JOptionPane.showInputDialog(
								null,
								MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.search.input" ),
								MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.search.title" ),
								JOptionPane.QUESTION_MESSAGE );
			
			if ( searchedField != null ) {
				CopyBookTreeModel copyBookModel = copyBookTree.getTreeModel();
				GenericElementTreeNode foundNode = copyBookModel.findNodeForName( searchedField, 0 );
				
				if ( foundNode != null ) {
					TreePath path = new TreePath( foundNode.getPath() );

					copyBookTree.setSelectionPath( path );
					copyBookTree.scrollPathToVisible( path );
					copyBookTree.setExpandsSelectedPaths( true );
				}
				else {
					JOptionPane.showMessageDialog(
							null, 
							MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.search.notFoundMessage" ), 
							MyFileViewerUIResourceBundle.getString( "copyBookDialog.menu.popup.search.notFoundTitle" ), 
							JOptionPane.INFORMATION_MESSAGE );
				}
			}
		}
	}
	
	/**
	 * TODO
	 */
	protected class SearchNextAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			if ( searchedField != null ) {
			}
		}
	}
	
	/**
	 * TODO
	 */
	protected class ExportExtendedAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			CopyBookToXmlGenerator xmlGenerator = new CopyBookToXmlGenerator( copyBook );
			String extendedXml = xmlGenerator.transformCopyBookToDom();
			
			File fileChooserDirectory = parentFrame.getFileChooser().getSelectedFile().getParentFile();
			parentFrame.getFileChooser().setSelectedFile( new File( fileChooserDirectory, copyBook.getName().toLowerCase() + "-extended.xml" ) );
			int action = parentFrame.getFileChooser().showSaveDialog( parentFrame );
			if ( action == JFileChooser.APPROVE_OPTION ) {
				try
				{
					File file = parentFrame.getFileChooser().getSelectedFile();
					
					PrintStream xmlOutputStream = new PrintStream( new FileOutputStream( file ) );
					xmlOutputStream.println( extendedXml );
					xmlOutputStream.flush();
					xmlOutputStream.close();
				}
				catch( Exception exc ) {
					displayExceptionStack( exc );
				}
			}
		}
	}

	/**
	 * TODO
	 */
	protected class ExportAction extends AbstractAction {

		@Override
		public void actionPerformed( ActionEvent event ) {
			CopyBookToXmlGenerator xmlGenerator = new CopyBookToXmlGenerator( copyBook.getUnextendedCopyBook() );
			String unExtendedXml = xmlGenerator.transformCopyBookToDom();
			
			File fileChooserDirectory = parentFrame.getFileChooser().getSelectedFile().getParentFile();
			parentFrame.getFileChooser().setSelectedFile( new File( fileChooserDirectory, copyBook.getName().toLowerCase() + ".xml" ) );
			int action = parentFrame.getFileChooser().showSaveDialog( parentFrame );
			if ( action == JFileChooser.APPROVE_OPTION ) {
				try
				{
					File file = parentFrame.getFileChooser().getSelectedFile();
					
					PrintStream xmlOutputStream = new PrintStream( new FileOutputStream( file ) );
					xmlOutputStream.println( unExtendedXml );
					xmlOutputStream.flush();
					xmlOutputStream.close();
				}
				catch( Exception exc ) {
					displayExceptionStack( exc );
				}
			}
		}
	}
	
	/**
	 * TODO
	 */
	protected class CloseDialogAction extends WindowAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			dispose();
			//observable.setChanged();
			infoPanel.update();
			
		}
	}

	
	/**
	 * TODO
	 */
	class CopyBookTreeMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			if ( SwingUtilities.isRightMouseButton (e) ) {
				selectedPath = copyBookTree.getPathForLocation(e.getX(), e.getY());
				copyBookTree.setSelectionPath( selectedPath );
				//popupMenu.show( copyBookTree, e.getX(), e.getY() );
				showPopupMenu( e.getX(), e.getY() );
				
			}
		}
	}
	
	/**
	 * Enable and disable popup menu-items then show popup menu.
	 * @param x
	 * @param y
	 */
	private void showPopupMenu( int x, int y ) {
		
		if ( selectedPath == null ) {
			expandItem.setEnabled( false );
			collapseItem.setEnabled( false );
			initializeItem.setEnabled( false );
			copyItem.setEnabled( false );
			pasteItem.setEnabled( false );
		}
		else {
			expandItem.setEnabled( true );
			collapseItem.setEnabled( true );
			initializeItem.setEnabled( true );
			copyItem.setEnabled( true );
			pasteItem.setEnabled( true );
		}
		popupMenu.show( copyBookTree, x, y );
	}
	
	

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable value) {
		// do nothing
	}
}
