package cMASConfig;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.TransferHandler;

public class NodeTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -510520462160579938L;

	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}
	
	 @Override
	 protected Transferable createTransferable(JComponent source) {
		 JTree sourceTree = (JTree) source;
		 NavigationTreeNode myNode = (NavigationTreeNode) sourceTree.getSelectionPath().getLastPathComponent();
		 
		 if (myNode == null)
			 return new StringSelection("Drag Drop Error");

		 String data = myNode.getID();
		 Transferable t = new StringSelection(data);
		 return t;
	}
	 
	 @Override
	 protected void exportDone(JComponent source, Transferable data, int action) {
		 
	 }
	 
	 @Override
	 public boolean canImport(TransferHandler.TransferSupport support) {
		 if (!support.isDrop())
			 return false;
		 
		 return support.isDataFlavorSupported(DataFlavor.stringFlavor);
	 }
	 
	 @Override
	 public boolean importData(TransferHandler.TransferSupport support) {
		 if (!this.canImport(support)) {
			 return false;
		 } 
		 
		 Transferable t = support.getTransferable();
		 String data = null;
		 
		 try {
			 data = (String) t.getTransferData(DataFlavor.stringFlavor);
			 if (data == null)
				 return false;
		 } catch (Exception e) {
			 e.printStackTrace();
			 return false;
		 }
		 
		 if (support.getComponent() instanceof JTextField) {
		 
			 JTextField txtData = (JTextField) support.getComponent();
		 
			if (txtData.getName() == "txtAttachedTo") {
			 		Interface myVar = DataManager.getInterface(data);
			 
			 		if (myVar == null)
			 			return false;
			 
			}
			
			txtData.setText(data);
		 } 
		 else if (support.getComponent() instanceof AgentChartCanvas){
			 
			 AgentChartCanvas chart = (AgentChartCanvas) support.getComponent();
			 Agent myAgent = DataManager.getAgent(data);
			 
			 if (myAgent == null)
				 return false;
			 
			 chart.setAgent(myAgent, true);
		 
		 }
			 
		 return true;
		 
	 }
}
