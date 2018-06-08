
package org.nla.MyFileViewer.ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Observable;
import java.util.Observer;

import org.nla.cobol.CopyBook;
import org.nla.cobol.parsing.CopyBookParser;
import org.nla.MyFileViewer.MyFileViewer;



@SuppressWarnings({ "serial", "unused" })
public class MyFileViewerFrame extends JFrame implements ActionListener {

	/** menu bar. */
	private JMenuBar menuBar;

	/** file menu. */
	private JMenu fileMenu;

	/** open copybook option.*/
	private JMenuItem openCopyBookItem;
	
	/** help menu. */
	private JMenu helpMenu;

	/** give feedback item.*/
	private JMenuItem feedbackItem;

	/** display about dialog box option.*/
	private JMenuItem aboutItem;

	/** only instance of file chooser. */
	JFileChooser fileChooser;

	/**
	 * Default Constructor. Creates frame, initialize its components and initialize windows look and feel.
	 */
	public MyFileViewerFrame()
	{
		super( "MyFileViewer" );
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initLookAndFeel();
		initComponents();
		fileChooser = new JFileChooser( MyFileViewer.getProperties().getWorkingDirectory() );
	}

	/**
	 * Initialize windows look and feel.
	 */
	protected void initLookAndFeel() {
		try 	{
			String lookAndFeelClassName = MyFileViewer.getProperties().getLookAndFeelClassName();
			UIManager.setLookAndFeel( lookAndFeelClassName );
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize internal components.
	 */
	protected void initComponents()
	{
		// menu bar
		menuBar = new JMenuBar();
		fileMenu = new JMenu( MyFileViewerUIResourceBundle.getString( "main.menu.file" ) );
		openCopyBookItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "main.menu.file.open" ) );
		openCopyBookItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.ALT_MASK));
		fileMenu.add( openCopyBookItem );

		helpMenu = new JMenu( MyFileViewerUIResourceBundle.getString( "main.menu.help" ) );
		feedbackItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "main.menu.help.feedback" ) );
		feedbackItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F, ActionEvent.ALT_MASK));
		aboutItem = new JMenuItem( MyFileViewerUIResourceBundle.getString( "main.menu.help.about" ) );
		aboutItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.ALT_MASK));
		helpMenu.add( aboutItem );
		helpMenu.add( feedbackItem );

		menuBar.add( fileMenu );
		menuBar.add( helpMenu );
		setJMenuBar( menuBar );

		// listeners
		openCopyBookItem.addActionListener(this);
		feedbackItem.addActionListener(this);
		aboutItem.addActionListener(this);


		// general properties
		setSize( 250, 200 );
		setLocation( 500, 250 );
		setVisible( true );
		getContentPane().setLayout( new BoxLayout(getContentPane(), BoxLayout.Y_AXIS ) );
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// open copybok
		if ( e.getSource() == openCopyBookItem )
		{
			int action = fileChooser.showOpenDialog(this);

			if ( action == JFileChooser.APPROVE_OPTION ) {
				try {
					File copyBookFile = fileChooser.getSelectedFile();
					try {
						CopyBook newCopyBook = CopyBookParser.buildCopyBook( copyBookFile.getAbsolutePath() );
						CopyBookDialog dialog = new CopyBookDialog( newCopyBook, copyBookFile, this );
						
						CopyBookDialogInfoPanel infoPanel = new CopyBookDialogInfoPanel( this, dialog );
						getContentPane().add( infoPanel );
						dialog.setInfoPanel( infoPanel );
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						
					}
	
					validate();
					
				}
				catch( Exception exc ) {
					displayExceptionStack( exc );
					//JOptionPane.showMessageDialog(this,exc.getMessage(),JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if ( e.getSource() == aboutItem ) {
			JOptionPane.showMessageDialog(
					this,
					"MyFileViewer\n Ankita Singhal",
					MyFileViewerUIResourceBundle.getString( "helpDialog.title" ), 
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if ( e.getSource() == feedbackItem ) {
			JOptionPane.showMessageDialog(
					this,
					MyFileViewerUIResourceBundle.getString( "feedbackDialog.content" ),
					MyFileViewerUIResourceBundle.getString( "feedbackDialog.title" ), 
					JOptionPane.INFORMATION_MESSAGE);
		}
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
	 * @return the fileChooser
	 */
	public JFileChooser getFileChooser() {
		return fileChooser;
	}
}
