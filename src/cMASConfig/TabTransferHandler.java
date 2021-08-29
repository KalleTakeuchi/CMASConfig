package cMASConfig;

import java.awt.datatransfer.DataFlavor;
import javax.swing.TransferHandler;

public class TabTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921872713742417469L;

	public boolean canImport(TransferHandler.TransferSupport supp) {
		
		if (supp.isDataFlavorSupported(DataFlavor.stringFlavor))
			return true;
		
		return false;
	}
	/*
	protected Transferable createTransferable(JComponent c) {
		Tab myTab = (Tab) c;
	}*/
}
