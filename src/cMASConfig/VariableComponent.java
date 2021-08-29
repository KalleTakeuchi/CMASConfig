package cMASConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import net.miginfocom.swing.MigLayout;

public class VariableComponent extends JPanel {

	private ArrayList<VariableChangeListener> variableChangeListeners;
	private JFormattedTextField numValueField;
	private JTextField txtValueField;
	private JTextArea txtAreaValue;
	private JLabel lblValue = new JLabel("String value:");
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZ;
	private JLabel lblRx;
	private JLabel lblRy;
	private JLabel lblRz;
	private JLabel lblUpperBound;
	private JLabel lblLowerBound;
	private JFormattedTextField numX;
	private JFormattedTextField numY;
	private JFormattedTextField numZ;
	private JFormattedTextField numRx;
	private JFormattedTextField numRy;
	private JFormattedTextField numRz;
	private JFormattedTextField numLowerBound;
	private JFormattedTextField numUpperBound;
	private JLabel lblTypeOfVariable;
	private JComboBox<String> cmbTypeOfVariable;
	private JButton btnRemove;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5431562450278011419L;
	
	private Variable variable;
	
	public VariableComponent(Variable variable) {
		this.variableChangeListeners = new ArrayList<VariableChangeListener>();
		
		this.variable = variable;
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.setLayout(new MigLayout("","[shrink] [grow]", ""));
		
		/*
		 * Remove button
		 */
		btnRemove = new JButton("-");
		btnRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendRemoveRequest();
			}
		});
		
		this.add(btnRemove, "cell 1 0, shrink, align right, wrap");
		
		addEntityComponents();	
		addVariableComponents();
		
	}
	
	public void deactivateChangeType() {
		this.remove(lblTypeOfVariable);
		this.remove(cmbTypeOfVariable);
	}
	
	public void deactivateRemove() {
		this.remove(btnRemove);
	}
	
	/**
	 * Adds Location specific components. 
	 * It is necessary to clean up old components before calling this 
	 * method since it only adds components.
	 * This method is normally called by the setValueInput method
	 * which handles cleanup. 
	 */
	private void addLocationComponents() {
		
		Location myLocation;
		
		try {
			myLocation = (Location) variable;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		float[] coordinateArray = new float[6];
		coordinateArray[0] = Float.parseFloat(myLocation.x);
		coordinateArray[1] = Float.parseFloat(myLocation.y);
		coordinateArray[2] = Float.parseFloat(myLocation.z);
		coordinateArray[3] = Float.parseFloat(myLocation.rx);
		coordinateArray[4] = Float.parseFloat(myLocation.ry);
		coordinateArray[5] = Float.parseFloat(myLocation.rz);
		
		/*
		 * Setup coordinate components
		 */
		createCoordinateComponents("x", coordinateArray[0]);
		createCoordinateComponents("y", coordinateArray[1]);
		createCoordinateComponents("z", coordinateArray[2]);
		createCoordinateComponents("rx", coordinateArray[3]);
		createCoordinateComponents("ry", coordinateArray[4]);
		createCoordinateComponents("rz", coordinateArray[5]);
		
	}
	
	public void removeLocationComponents() {
		if (Arrays.asList(this.getComponents()).contains(lblX))
			this.remove(lblX);
		
		if (Arrays.asList(this.getComponents()).contains(lblY))
			this.remove(lblY);
		
		if (Arrays.asList(this.getComponents()).contains(lblZ))
			this.remove(lblZ);
		
		if (Arrays.asList(this.getComponents()).contains(lblRx))
			this.remove(lblRx);
		
		if (Arrays.asList(this.getComponents()).contains(lblRy))
			this.remove(lblRy);
		
		if (Arrays.asList(this.getComponents()).contains(lblRz))
			this.remove(lblRz);
		
		if (Arrays.asList(this.getComponents()).contains(numX))
			this.remove(numX);
		
		if (Arrays.asList(this.getComponents()).contains(numY))
			this.remove(numY);
		
		if (Arrays.asList(this.getComponents()).contains(numZ))
			this.remove(numZ);
		
		if (Arrays.asList(this.getComponents()).contains(numRx))
			this.remove(numRx);
		
		if (Arrays.asList(this.getComponents()).contains(numRy))
			this.remove(numRy);
		
		if (Arrays.asList(this.getComponents()).contains(numRz))
			this.remove(numRz);
	}
	
	private void addVariableComponents() {
		lblTypeOfVariable = new JLabel("Type of variable:");
		this.add(lblTypeOfVariable, "align right");
		String[] variableTypes = new String[Variable.VariableType.values().length];
		
		for (int i = 0; i < variableTypes.length; i++) {
			variableTypes[i] = Variable.VariableType.values()[i].toString();
		}
		
		cmbTypeOfVariable = new JComboBox<String>(variableTypes);
		cmbTypeOfVariable.setEditable(true);
		cmbTypeOfVariable.setSelectedItem(variable.TypeOfVariable);
		cmbTypeOfVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> txt = (JComboBox<String>) e.getSource();
				setTypeOfValue(txt.getSelectedItem().toString());	
			}
		});
		
		this.add(cmbTypeOfVariable, "growx, wrap");
		
		JCheckBox chkReadOnly = new JCheckBox("Read only");
		chkReadOnly.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox chk = (JCheckBox) e.getSource();
				
				setReadOnly(chk.isSelected());
				
			}
		});
		
		chkReadOnly.setSelected(variable.ReadOnly);
		this.add(chkReadOnly, "wrap");
		
		if (Variable.VariableType.belongsToEnum(variable.TypeOfVariable.toString())) {
			createValueInputComponents(Variable.VariableType.valueOf(variable.TypeOfVariable.toString()));
		} else {
			createValueInputComponents(Variable.VariableType.STRING);
		}
		
	}
	
	private void setReadOnly(boolean state) {
		variable.ReadOnly = state;
	}
	
	private void setStringValue(Value value, String text) {
		value.StringValue = text;
	}

	/**
	 * Checks if the argument corresponds to a variable type. If it does,
	 * setValueInput is called to update the controls. Also, if
	 * the variable goes from being a Value to a Location or vice versa
	 * the variable is reinitialized.
	 * @param text
	 */
	private void setTypeOfValue(String text) {
		
		if (Variable.VariableType.belongsToEnum(text)) {
			
			Variable.VariableType myVal = Variable.VariableType.valueOf(text);
			
			if (!variable.TypeOfVariable.equals(Variable.VariableType.LOCATION)
					&& myVal.equals(Variable.VariableType.LOCATION)) {
				variable = new Location(variable);
				
				for (VariableChangeListener listener : variableChangeListeners) {
					listener.variableReinitialized(variable);
				}
				
			}
			else if (variable.TypeOfVariable.equals(Variable.VariableType.LOCATION)
					&& !myVal.equals(Variable.VariableType.LOCATION)) {
				variable = new Value(variable);
				
				for (VariableChangeListener listener : variableChangeListeners) {
					listener.variableReinitialized(variable);
				}
			}
					
			variable.TypeOfVariable = myVal;
			
			createValueInputComponents(myVal);
		}
		else {
			createValueInputComponents(Variable.VariableType.STRING);
		}

	}

	/**
	 * 
	 * @param coordinate the coordinate in lower case. x, rx, y, ry, z, rz
	 */
	private void createCoordinateComponents(String coordinate, float value) {
		
		if (!coordinate.equals("x") && 
				!coordinate.equals("rx") &&
				!coordinate.equals("y") &&
				!coordinate.equals("ry") &&
				!coordinate.equals("z") &&
				!coordinate.equals("rz"))
			return;
		
		
		JLabel lblCoordinate = new JLabel(coordinate + ":");
		NumberFormat coordinateFormat = NumberFormat.getNumberInstance(getLocale());
		JFormattedTextField txtCoordinate = new JFormattedTextField(coordinateFormat);
		txtCoordinate.setValue(0);
		txtCoordinate.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				JFormattedTextField txt = (JFormattedTextField) e.getSource();
				String myCoordinate = coordinate;
				
				try {
					txt.commitEdit();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				Number myNum = (Number) txt.getValue();
				
				setCoordinateValue(myCoordinate, myNum.toString());
				
			}
			
		});
		
		txtCoordinate.setValue(value);
		this.add(lblCoordinate, "align right");
		this.add(txtCoordinate, "growx, wrap");
		
		switch (coordinate) {
		case "x":
			lblX = lblCoordinate;
			numX = txtCoordinate;
			break;
			
		case "y":
			lblY = lblCoordinate;
			numY = txtCoordinate;
			break;
		
		case "z":
			lblZ = lblCoordinate;
			numZ = txtCoordinate;
			break;
			
		case "rx":
			lblRx = lblCoordinate;
			numRx = txtCoordinate;
			break;
		
		case "ry":
			lblRy = lblCoordinate;
			numRy = txtCoordinate;
			break;
		
		case "rz":
			lblRz = lblCoordinate;
			numRz = txtCoordinate;
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Sets a coordinate field to the passed value
	 * @param coordinate
	 * @param value
	 */
	protected void setCoordinateValue(String coordinate, String value) {
		
		Location myLocation;
		
		try {
			myLocation = (Location) variable;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		switch (coordinate) {
		case "x":
			myLocation.x = value;
			break;
			
		case "y":
			myLocation.y = value;
			break;
		
		case "z":
			myLocation.z = value;
			break;
			
		case "rx":
			myLocation.rx = value;
			break;
		
		case "ry":
			myLocation.ry = value;
			break;
		
		case "rz":
			myLocation.rz = value;
			break;
			
		default:
			break;
		}
		
	}
	
	
	/**
	 * Sets up value input controls based on the variable type
	 * Note that the Value class stores its value in the StringValue field, no matter its
	 * variable type. The location class has specific fields for its coordinates.
	 * @param variableType
	 */
	private void createValueInputComponents(Variable.VariableType variableType) {
		
		if(Arrays.asList(this.getComponents()).contains(txtValueField))
			this.remove(txtValueField);
		if(Arrays.asList(this.getComponents()).contains(numValueField))
			this.remove(numValueField);
		if(Arrays.asList(this.getComponents()).contains(lblValue))
			this.remove(lblValue);
		if(Arrays.asList(this.getComponents()).contains(txtAreaValue))
			this.remove(txtAreaValue);
		if(Arrays.asList(this.getComponents()).contains(lblLowerBound))
			this.remove(lblLowerBound);
		if(Arrays.asList(this.getComponents()).contains(lblUpperBound))
			this.remove(lblUpperBound);
		if(Arrays.asList(this.getComponents()).contains(numLowerBound))
			this.remove(numLowerBound);
		if(Arrays.asList(this.getComponents()).contains(numUpperBound))
			this.remove(numUpperBound);
		
		removeLocationComponents();
		
		if (variableType.equals(Variable.VariableType.INTEGER)) {
			
			//Cast the variable as Value type.
			Value myValue; 
			try {
				myValue = (Value) variable;
			} 
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			
			lblValue = new JLabel("Integer value:");
			this.add(lblValue, "align right");
			
			NumberFormat format = NumberFormat.getIntegerInstance(getLocale());
			numValueField = new JFormattedTextField(format);
			numValueField.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					
					JFormattedTextField txt = (JFormattedTextField) e.getSource();
					
					try {
						txt.commitEdit();
					} catch (ParseException e1) {
						return;
					}
					
					Number myNum = (Number) txt.getValue();
					
					setStringValue(myValue, myNum.toString());
					
				}
			

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			this.add(numValueField, "growx, wrap");
			
			try {
				numValueField.setValue(Integer.parseInt(myValue.StringValue));
			}
			catch (NumberFormatException e) {
				myValue.StringValue = "0";
				numValueField.setValue(0);
			}
			
			addBoundsComponents(myValue);
		}
		else if (variableType.equals(Variable.VariableType.REAL)) {
			
			//Cast the variable as Value type.
			Value myValue; 
			try {
				myValue = (Value) variable;
			} 
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			lblValue = new JLabel("Real value:");
			this.add(lblValue, "align right");
			NumberFormat format = NumberFormat.getNumberInstance(getLocale());
			numValueField = new JFormattedTextField(format);
			numValueField.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					
					JFormattedTextField txt = (JFormattedTextField) e.getSource();
					
					try {
						txt.commitEdit();
					} catch (ParseException e1) {
						return;
					}
					
					Number myNum = (Number) txt.getValue();
					
					setStringValue(myValue, myNum.toString());
					
				}
			

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			this.add(numValueField, "growx, wrap");
			try {
				numValueField.setValue(Double.parseDouble(myValue.StringValue));
			}
			catch (NumberFormatException e) {
				myValue.StringValue = "0";
				numValueField.setValue(0);
			}
			
			addBoundsComponents(myValue);
		}
		else if(variableType.equals(Variable.VariableType.STRING)) {
			
			//Cast the variable as Value type.
			Value myValue; 
			try {
				myValue = (Value) variable;
			} 
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			lblValue = new JLabel("String value:");
			this.add(lblValue, "align right");
			txtAreaValue = new JTextArea();
			txtAreaValue.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					
					JTextArea txt = (JTextArea) e.getSource();
					
					setStringValue(myValue, txt.getText());
					
				}
			

				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			this.add(txtAreaValue, "growx, growy, wrap");
			txtAreaValue.setText(myValue.StringValue);
			
			addBoundsComponents(myValue);
		}
		else if (variableType.equals(Variable.VariableType.LOCATION)) {
			lblValue = new JLabel("Location:");
			this.add(lblValue, "wrap");
			addLocationComponents();
		}
		
		this.revalidate();
		this.repaint();
		
	}
	
	private void addBoundsComponents(Value value) {
		lblUpperBound = new JLabel("Upper bound:");
		this.add(lblUpperBound, "align right");
		
		numUpperBound = new JFormattedTextField(NumberFormat.getNumberInstance(getLocale()));
		numUpperBound.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				JFormattedTextField txt = (JFormattedTextField) e.getSource();
				
				Value myValue = value;
				
				try {
					txt.commitEdit(); 
				}
				catch (ParseException ex) {
					return;
				}
				
				Number myNum = (Number) txt.getValue();
				
				setUpperBound(myValue, myNum.toString());
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(numUpperBound, "growx, wrap");
		
		lblLowerBound = new JLabel("Lower bound");
		this.add(lblLowerBound, "align right");
		
		numLowerBound = new JFormattedTextField(NumberFormat.getNumberInstance(getLocale()));
		numLowerBound.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				JFormattedTextField txt = (JFormattedTextField) e.getSource();
				
				Value myValue = value;
				
				try {
					txt.commitEdit(); 
				}
				catch (ParseException ex) {
					return;
				}
				
				Number myNum = (Number) txt.getValue();
				
				setLowerBound(myValue, myNum.toString());
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(numLowerBound, "growx, wrap");
		
	}
	
	private void setUpperBound(Value value, String text) {
		value.UpperBound = text;
	}
	
	private void setLowerBound(Value value, String text) {
		value.LowerBound = text;
	}
	
	/**
	 * Adds name and entity ID form components
	 */
	private void addEntityComponents() {
	
		this.add(new JLabel("Entity ID:"), "align right");
		JTextField txtID = new JTextField();
		txtID.setText(variable.ID);
		txtID.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				JTextField txt = (JTextField) e.getSource();
				
				if (!variable.ID.equals(txt.getText())) {
					
					String oldID = variable.ID;
					
					if (DataManager.entityIDExists(txt.getText())) {
						txt.setText(oldID);
						return;
					}

					variable.ID = txt.getText();
					
					NavigationTreeNode newNode = new NavigationTreeNode(variable.ID, variable.Name, NavigationTreeNode.NodeType.VARIABLE);
					TreeManager.changeNode(oldID, newNode);
					
					for (VariableChangeListener listener : variableChangeListeners) {
						listener.variableIDChanged(oldID, variable);
					}
					
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(txtID, "growx, wrap");
		
		this.add(new JLabel("Name:"), "align right");
		JTextField txtName = new JTextField();
		txtName.setText(variable.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
						
				if (!txt.getText().equals(variable.Name)) {
					
					variable.Name = txt.getText();
					
					NavigationTreeNode newNode = new NavigationTreeNode(variable.ID, variable.Name, NavigationTreeNode.NodeType.VARIABLE);
					TreeManager.changeNode(variable.ID, newNode);
					
					for (VariableChangeListener listener : variableChangeListeners) {
						listener.variableNameChanged(variable);
					}
					
				}
				
			}
			
		});
		
		this.add(txtName, "growx, wrap");
	}
	
	private void sendRemoveRequest() {
		for (VariableChangeListener listener : variableChangeListeners) {
			listener.variableRemoveRequest(this);
		}
	}
	
	public Variable getVariable() {
		return this.variable;
	}
	
	public void addVariableChangeListener(VariableChangeListener listener) {
		variableChangeListeners.add(listener);
	}
}
