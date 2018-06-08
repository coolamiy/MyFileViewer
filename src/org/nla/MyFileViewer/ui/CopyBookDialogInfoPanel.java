package org.nla.MyFileViewer.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

/**
 * Panel aimed to be inserted in the main frame, in order to inform the user about the dialog state. 

 */
@SuppressWarnings("serial")
class CopyBookDialogInfoPanel extends JLabel implements MouseListener {
	
	/** dialog it refers to. */
	private CopyBookDialog dialog;
	
	/** main frame. */
	private MyFileViewerFrame frame;
	
	/** is dialog alive. */
	private boolean isDialogAlive;
	
	/** link font. */
	private final Font linkFont = new Font( "arial", Font.BOLD, getFont().getSize() ); 

	/** std font. */
	private final Font stdFont = new Font( "arial", Font.PLAIN, getFont().getSize() );
	
	/** message. */
	private String message;
	

	
	/**
	 * @param dialog
	 */
	public CopyBookDialogInfoPanel( MyFileViewerFrame frame, CopyBookDialog dialog ) {
		super();
		this.frame = frame;
		this.dialog = dialog;
		this.isDialogAlive = true;
		
		message = " " + dialog.getCopyBook().getName().toUpperCase() + " - " + dialog.getCopyBook().getSize() + " octets";
		setText( message );
		
		addMouseListener( this );
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("DialogInfoPanel.mouseClicked()");
		dialog.toFront();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		if ( isDialogAlive ) {
			setFont( linkFont );
			setForeground( Color.blue );
			validate();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		if ( isDialogAlive ) {
			setFont( stdFont );
			setForeground( Color.black );
			validate();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// do nothing		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// do nothing
	}

	public void update() {
		isDialogAlive = false;
		setText( message + " - Closed" );
		setFont( stdFont );
		setForeground( Color.gray );
		frame.validate();
	}
}
