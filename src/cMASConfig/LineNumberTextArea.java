package cMASConfig;

import java.awt.*;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;

/**
 * Thanks to Raja at tutorialspoint.com
 * (https://www.tutorialspoint.com/how-can-we-display-the-line-numbers-inside-a-jtextarea-in-java)
 * @author Kalle
 *
 */
public class LineNumberTextArea extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7800008487447359113L;
	private static JTextArea textArea;
	private static JTextArea lines;
	private JScrollPane jsp;
	
	public LineNumberTextArea() {
		jsp = new JScrollPane();
		
		textArea = new JTextArea();
		textArea.setFont(new Font("monospaced", Font.PLAIN, 16));	
		
		lines = new JTextArea("1");
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
		lines.setFont(new Font("monospaced", Font.PLAIN, 16));
		
		//  Code to implement line numbers inside the JTextArea
		textArea.getDocument().addDocumentListener(new DocumentListener() {
	         
			public String getText() {
	            int caretPosition = textArea.getDocument().getLength();
	            Element root = textArea.getDocument().getDefaultRootElement();
	            String text = "1" + System.getProperty("line.separator");
	               for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
	                  text += i + System.getProperty("line.separator");
	               }
	            return text;
	         }
	         @Override
	         public void changedUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	         @Override
	         public void insertUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	         @Override
	         public void removeUpdate(DocumentEvent de) {
	            lines.setText(getText());
	         }
	      });
		
		jsp.getViewport().add(textArea);
		jsp.setRowHeaderView(lines);
		
		this.setLayout(new BorderLayout());
		add(jsp, BorderLayout.CENTER);
	   }
	
	public String getText() {
		return textArea.getText();
	}
	
	public void setText(String text) {
		textArea.setText(text);
	}
	
	public void addFocusListener(FocusListener listener) {
		textArea.addFocusListener(listener);
	}
}
