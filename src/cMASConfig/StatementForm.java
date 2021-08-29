package cMASConfig;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public abstract class StatementForm extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6977899153908422272L;

	public enum TypeOfStatementForm {
		SKILL_CALL,
		IF,
		WHILE,
		FOR,
		ASSIGNMENT,
		DECLARATION;
	}
	
	List<StatementFormListener> statementFormListeners;
	TypeOfStatementForm type;
	/**
	 * For use with listeners
	 */
	StatementForm reference = this;
	
	public StatementForm(TypeOfStatementForm type) {
		statementFormListeners = new ArrayList<StatementFormListener>();
		
		this.type = type;
		
		this.setLayout(new MigLayout("fillx",
				"[shrink] [grow]",
				""));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
	}
	
	public void addStatementFormListener(StatementFormListener listener) {
		statementFormListeners.add(listener);
	}

	/**
	 * Contains buttons for moving and deleting the form and the title label.
	 */
	protected void createTitleRow(String title) {
		
		this.add(new JLabel(title), "cell 0 0");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		JButton btnArrowDown = new JButton("↓");
		btnArrowDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				for (StatementFormListener listener : statementFormListeners) {
					listener.moveDown(reference);
				}
				
			}
		});
		
		buttonPanel.add(btnArrowDown);
		
		JButton btnArrowUp = new JButton("↑");
		btnArrowDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				for (StatementFormListener listener : statementFormListeners) {
					listener.moveUp(reference);
				}
				
			}
		});
		
		buttonPanel.add(btnArrowUp);
		
		JButton btnRemove = new JButton("-");
		btnRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				for (StatementFormListener listener : statementFormListeners) {
					listener.deleteRequest(reference);
				}
				
			}
		});
		
		buttonPanel.add(btnRemove);
		
		this.add(buttonPanel, "cell 1 0, align right");
		
	}
	
}
