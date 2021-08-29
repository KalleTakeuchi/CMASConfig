package cMASConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Optional;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class MainWindow {
	
	class TreeViewRightClickMenu extends JPopupMenu {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3905437856269036140L;

		public TreeViewRightClickMenu(NavigationTreeNode node) {
			JMenuItem itemNewAgent = new JMenuItem("New agent");
			JMenuItem itemDeleteAgent = new JMenuItem("Delete");
			JMenuItem itemNewPlan = new JMenuItem("New plan");
			
			itemNewAgent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					createNewAgent(true);
				}
			});
			
			itemNewPlan.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					createNewPlan(true);
					
				}
			});
			
			itemDeleteAgent.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (node == null)
						return;
					if (node.getNodeType() == NavigationTreeNode.NodeType.ORGANIZATION)
						return;
					
					int res = JOptionPane.showConfirmDialog(window, 
							"Are you sure you want to delete the " + node.getNodeType().toString() +
							" entity " + node.toString(), 
							"Delete entity",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE);
					
					if (res == (JOptionPane.YES_OPTION)) {
						deleteEntity(node);
					}	
				}
			});
			
			this.add(itemNewAgent);
			this.add(itemNewPlan);
			this.add(itemDeleteAgent);
		}
	}
	
	public static final int TREE_VIEW_WIDTH = 250;

	
	private NavigationTreeNode selection;
	private JFrame window;
	private JLabel lblSelectionID;
	private TabbedPaneManager tabbedPaneManager;
	
	public MainWindow() {
		initialize();
	}
	
	private void initialize() {
		window = new JFrame();
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5,5,5,5));
		panel.setLayout(new BorderLayout(5, 5));
		window.setContentPane(panel);
		window.setTitle("CMAS configuration tool");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window.setSize((int)(screenSize.getWidth() * 0.8), 
				(int)(screenSize.getHeight() * 0.9));
		window.setLocationRelativeTo(null);
	
		
		//Create agent list
		DataManager.createTestAgents();
		
		//Create button bar
		window.add(createButtonBar(), BorderLayout.NORTH);
		
		//Create the side bar
		window.add(createSideBar(DataManager.getAgents(), DataManager.getPlans()), BorderLayout.WEST);
		
		//Create tab pane with welcome screen
		createTabbedPaneManager();
		window.add(tabbedPaneManager, BorderLayout.CENTER);
	}
	
	private void createTabbedPaneManager() {
		tabbedPaneManager = new TabbedPaneManager(createTabbedPane(createWelcomeTab()));
	}
	
	private JToolBar createButtonBar() {
		JToolBar myToolBar = new JToolBar();
		 //New agent
		JButton newAgentButton = new JButton("New agent");
		newAgentButton.setToolTipText("Create new agent");
		newAgentButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewAgent(true);
			}
		});	
		
		 //New plan
		JButton newPlanButton = new JButton("New plan");
		newPlanButton.setToolTipText("Create new plan");
		newPlanButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewPlan(true);
			}
		});	
		
		//new tabbed pane
		JButton splitViewButton = new JButton("Split");
		splitViewButton.setToolTipText("Split view");
		splitViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPaneManager.splitView(createTabbedPane());
			}
			
		});	
		
		JButton unsplitViewButton = new JButton("Unsplit");
		unsplitViewButton.setToolTipText("Unsplit view");
		unsplitViewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabbedPaneManager.unsplitView();								
			}
		});
		
		JButton agentChartButton = new JButton("New agent chart");
		agentChartButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createAgentChart();
			}
		});
		
		JButton importConfigurationButton = new JButton("Import");
		JButton exportConfigurationButton = new JButton("Export");
		
		exportConfigurationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fs = new JFileChooser();
				fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fs.showOpenDialog(window);
				
				if (r == JFileChooser.APPROVE_OPTION)
					DataManager.exportConfiguration(fs.getSelectedFile().getPath());
				
			}
		});
		
		importConfigurationButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fs = new JFileChooser();
				fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fs.showOpenDialog(window);
				
				if (r == JFileChooser.APPROVE_OPTION)
					DataManager.importConfiguration(fs.getSelectedFile().getPath());
				
			}
		});
		
		myToolBar.add(newAgentButton);
		myToolBar.add(newPlanButton);
		myToolBar.addSeparator();
		myToolBar.add(splitViewButton);
		myToolBar.add(unsplitViewButton);
		myToolBar.addSeparator();
		myToolBar.add(agentChartButton);
		myToolBar.addSeparator();
		myToolBar.add(importConfigurationButton);
		myToolBar.add(exportConfigurationButton);
		
		return myToolBar;
	}
	
	private void createAgentChart() {
		AgentChartTab myAgentChartTab; 
		
		if (selection != null) {
			Agent myAgent = DataManager.getAgent(selection.getID());
		
			if (myAgent != null) {
				myAgentChartTab = tabbedPaneManager.createAgentChartTab(myAgent);
			}
			else {
				myAgentChartTab = tabbedPaneManager.createAgentChartTab();
			}
		}
		else {
			
			myAgentChartTab = tabbedPaneManager.createAgentChartTab();
			
		}
			
		tabbedPaneManager.addTab(myAgentChartTab);
	}
	
	private void deleteEntity(NavigationTreeNode node) {
		if (node == null)
			return;
		
		
		if (node.getNodeType() == NavigationTreeNode.NodeType.AGENT) {
			Agent myAgent = DataManager.getAgent(node.getID());
			
			if(myAgent == null)
				return;
			
			tabbedPaneManager.deleteAgentTab(myAgent);
			
			DataManager.removeAgent(node.getID());
			TreeManager.removeNode(node.getID());
			
			callUpdateContent();
		}
		if (node.getNodeType() == NavigationTreeNode.NodeType.INTERFACE) {
			DataManager.removeInterface(node.getID());			
			callUpdateContent();
			TreeManager.removeNode(node.getID());
			
		}
		if (node.getNodeType() == NavigationTreeNode.NodeType.PLAN) {
			Plan myPlan = DataManager.getPlan(node.getID());
			
			if (myPlan == null)
				return;
			
			//TODO
			
			//tabbedPaneManager.dele
			DataManager.removePlan(node.getID());
			callUpdateContent();
			TreeManager.removeNode(node.getID());
			
		}
	}	
	
	public void callUpdateContent() {
		
		for (Tab tab : tabbedPaneManager.getTabs()) {
			tab.updateContent();
		}
		
	}

	private void filterTree(String str, boolean agents, boolean plans) {
		ArrayList<Agent> filteredAgentList = new ArrayList<Agent>();
		ArrayList<Plan> filteredPlanList = new ArrayList<Plan>();
		
		if (agents && plans) {
			filteredAgentList = DataManager.filterAgents(str);
			filteredPlanList = DataManager.filterPlans(str);

		}
		else if (agents && !plans) {
			filteredAgentList = DataManager.filterAgents(str);
		}
		else if (!agents && plans) {
			filteredPlanList = DataManager.filterPlans(str);
		}
		
		DefaultMutableTreeNode root = TreeManager.createTreeStructure(
				new AgentEnumeration(filteredAgentList), 
				new PlanEnumeration(filteredPlanList)
				);
		
		TreeManager.setTreeModel(new DefaultTreeModel(root));
	}
	
	private JPanel createSideBar(Enumeration<Agent> agents, Enumeration<Plan> plans) {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new MigLayout("fill"));
		myPanel.add(createSearchBoxLabel(), "north, gapy 0px 5px");
		myPanel.add(createSearchBox(), "north, gapy 0px 5px");
		myPanel.add(createTreeView(agents, plans), "center, growy");
		lblSelectionID = new JLabel("Selection ID: ");
		myPanel.add(lblSelectionID, "south");
		
		return myPanel;
	}
	
	private JLabel createSearchBoxLabel() {
		return new JLabel("Search");
	}
	
	/**
	 * Creates the search box and some checkboxes for filtering entities.
	 * @return
	 */
	private JPanel createSearchBox() {
		JPanel thisPanel = new JPanel();
		thisPanel.setLayout(new MigLayout("fill"));
		
		JTextField textBox = new JTextField();
		textBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				filterTree(e.getActionCommand(), true, true);				
			}
			
		});
		textBox.setToolTipText("Type in an agent, interface, or skill name to search");
		
		thisPanel.add(textBox, "growx, wrap");
		
		JCheckBox chkResources = new JCheckBox("Agents");
		JCheckBox chkComponents = new JCheckBox("Plans");
		JCheckBox chkFindInterfaces = new JCheckBox("Interfaces");
		JCheckBox chkFindSkills = new JCheckBox("Skills");
		JCheckBox chkFindGoals = new JCheckBox("Goals");
		JCheckBox chkFindVariables = new JCheckBox("Variables");
		
		chkResources.setSelected(true);
		chkComponents.setSelected(true);
		
		chkFindInterfaces.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JCheckBox source = (JCheckBox) e.getSource();
				
				if (source.isSelected()) {
					chkFindSkills.setSelected(false);
					chkFindGoals.setSelected(false);
					chkFindVariables.setSelected(false);
				}
				
			}
		});
		
		chkFindSkills.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JCheckBox source = (JCheckBox) e.getSource();
				
				if (source.isSelected()) {
					chkFindInterfaces.setSelected(false);
					chkFindGoals.setSelected(false);
					chkFindVariables.setSelected(false);
				}
				
			}
		});
		
		chkFindGoals.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JCheckBox source = (JCheckBox) e.getSource();
				
				if (source.isSelected()) {
					chkFindSkills.setSelected(false);
					chkFindInterfaces.setSelected(false);
					chkFindVariables.setSelected(false);
				}
				
			}
		});
		
		chkFindVariables.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JCheckBox source = (JCheckBox) e.getSource();
				
				if (source.isSelected()) {
					chkFindSkills.setSelected(false);
					chkFindGoals.setSelected(false);
					chkFindInterfaces.setSelected(false);
				}
				
			}
		});
		
		thisPanel.add(new JLabel("Show"), "wrap");
		thisPanel.add(chkComponents, "split 2");
		thisPanel.add(chkResources, "wrap");
		thisPanel.add(new JLabel("that contains matching"), "wrap");
		thisPanel.add(chkFindInterfaces, "split 2");
		thisPanel.add(chkFindSkills, "wrap");
		thisPanel.add(chkFindVariables, "split 2");
		thisPanel.add(chkFindGoals);
		
		return thisPanel;
	}
	
	private JPanel createTreeView(Enumeration<Agent> agents, Enumeration<Plan> plans) {
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BorderLayout());
		treePanel.setBackground(Color.BLUE);
		treePanel.setPreferredSize(new Dimension(TREE_VIEW_WIDTH,0));
		
		TreeManager.CreateNavigationTree(TreeManager.createTreeStructure(agents, plans));
		TreeManager.setDragEnabled(true);
		TreeManager.setTransferHandler(new NodeTransferHandler());
		TreeManager.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				selection = (NavigationTreeNode) arg0.getPath().getLastPathComponent();

				lblSelectionID.setText("Selection ID: " + selection.getID());				}
			
		});
		
		//Add mouse listener to detect double clicks
		TreeManager.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
			
				if (e.getClickCount() >= 2 && selection != null) {
					if (selection.getNodeType().equals(NavigationTreeNode.NodeType.AGENT)) {
						Agent myAgent = DataManager.getAgent(selection.getID());
						if (myAgent == null)
							return;
						//if (!tabTitles.containsKey(getTabOfAgent(myAgent)))
						tabbedPaneManager.createAgentTab(myAgent);
					}
					else if (selection.getNodeType().equals(NavigationTreeNode.NodeType.VARIABLE)) {
						
						NavigationTreeNode agentNode = (NavigationTreeNode) selection.getParent().getParent();
						Agent myAgent = DataManager.getAgent(agentNode.getID());
						if (myAgent == null)
							return;
						//if (!tabTitles.containsKey(getTabOfAgent(myAgent))) {
						AgentTab myTab = tabbedPaneManager.createAgentTab(myAgent);
						myTab.selectVariablesTab();
						
					}
					else if (selection.getNodeType().equals(NavigationTreeNode.NodeType.GOAL)) {
						
						NavigationTreeNode agentNode = (NavigationTreeNode) selection.getParent().getParent();
						Agent myAgent = DataManager.getAgent(agentNode.getID());
						if (myAgent == null)
							return;
						
						AgentTab myTab = tabbedPaneManager.createAgentTab(myAgent);
						myTab.selectGoalsTab();
					}
					else if (selection.getNodeType().equals(NavigationTreeNode.NodeType.PLAN)) {
						
						Plan myPlan = DataManager.getPlan(selection.getID());
						
						if (myPlan == null)
							return;
						
						tabbedPaneManager.createPlanTab(myPlan);
						
					}
					else if (selection.getNodeType().equals(NavigationTreeNode.NodeType.ORGANIZATION)) {
						
						if (selection.getID().equals(TreeManager.VARIABLES_NODE_STRING)) {
							NavigationTreeNode agentNode = (NavigationTreeNode) selection.getParent();
							Agent myAgent = DataManager.getAgent(agentNode.getID());
							
							if (myAgent == null) 
								return;
							
							//if (!tabTitles.containsKey(getTabOfAgent(myAgent))) {
							AgentTab myTab = tabbedPaneManager.createAgentTab(myAgent);
							myTab.selectVariablesTab();
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger() && selection != null) {
					TreeViewRightClickMenu rightClickMenu = new TreeViewRightClickMenu(selection);
					rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger() && selection != null) {
					TreeViewRightClickMenu rightClickMenu = new TreeViewRightClickMenu(selection);
					rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			
		});
		
		treePanel.add(new JScrollPane(TreeManager.getNavigationTree()), BorderLayout.CENTER);
		
		return treePanel;
	}

	private Tab createWelcomeTab() {
		WelcomeTab tab = new WelcomeTab();
		tab.setBackground(Color.WHITE);
		
		//Heading
		JLabel label = new JLabel("Welcome to CMAS Configurator!");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 24));
		
		//New agent button
		JButton btnNewAgent = new JButton("New agent");
		btnNewAgent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createNewAgent(true);
			}
			
		});
		/*ImageIcon agentIcon = new ImageIcon("SpyIcon.png");
		Image image = agentIcon.getImage();
		image = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
		agentIcon = new ImageIcon(image);*/
		btnNewAgent.setFont(new Font(btnNewAgent.getFont().getFontName(),
				Font.ITALIC, 24));	
		//btnNewAgent.setIcon(agentIcon);
		btnNewAgent.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnNewAgent.setBackground(Color.WHITE);
		btnNewAgent.setIconTextGap(10);

		
		//New plan
		JButton btnNewPlan = new JButton("New goal process plan");
		btnNewPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createNewPlan(true);
			}
		});
		btnNewPlan.setFont(new Font(btnNewAgent.getFont().getFontName(),
				Font.ITALIC, 24));
		btnNewPlan.setHorizontalTextPosition(SwingConstants.LEFT);
		btnNewPlan.setBackground(Color.WHITE);
		
		tab.setLayout(new MigLayout("fill"));
		
		tab.add(label, "align center, wrap");
		tab.add(btnNewAgent, "growx, wrap");
		tab.add(btnNewPlan, "growx");
		
		//Import
		JButton btnImport = new JButton("Import configuration");
		btnImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fs = new JFileChooser();
				fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = fs.showOpenDialog(window);
				
				if (r == JFileChooser.APPROVE_OPTION)
					DataManager.importConfiguration(fs.getSelectedFile().getPath());
			}
			
		});
		
		btnImport.setFont(new Font(btnNewAgent.getFont().getFontName(),
				Font.ITALIC, 24));
		btnImport.setHorizontalTextPosition(SwingConstants.LEFT);
		btnImport.setBackground(Color.WHITE);
		
		tab.setLayout(new MigLayout("fill"));
		
		tab.add(label, "align center, wrap");
		tab.add(btnNewAgent, "growx, wrap");
		tab.add(btnNewPlan, "growx, wrap");
		tab.add(btnImport, "growx"); 
		
		return tab;
	}
	
	/*Creates a new agent with a randomly generated unique ID
	 * and and opens it in a new tab if so desired*/
	private void createNewAgent(Boolean openTab) {
		
		Agent myAgent = new Agent(DataManager.createUniqueID());
		DataManager.addAgent(myAgent);
		
		TreeManager.addNode(myAgent.ID, myAgent.Name, NavigationTreeNode.NodeType.AGENT, Optional.ofNullable(null));
		
		if (openTab)
			tabbedPaneManager.createAgentTab(myAgent);
		
		callUpdateContent();
	}
	
	/*Creates a new plan with a randomly generated unique ID
	 * and and opens it in a new tab if so desired*/
	private void createNewPlan(Boolean openTab) {
		
		Plan myPlan = new Plan(DataManager.createUniqueID());
		DataManager.addPlan(myPlan);
		
		TreeManager.addNode(myPlan.ID, myPlan.Name, NavigationTreeNode.NodeType.PLAN, Optional.ofNullable(null));
		
		if (openTab)
			tabbedPaneManager.createPlanTab(myPlan);
	}
	
	


	/*private void addTab(JPanel tab) {
		//Add tab and get index
		String title;
		if (tabTitles.values().contains(tab.toString())) {
			Random rnd = new Random();
			title = Long.valueOf(rnd.nextLong()).toString();
			
			while (tabTitles.containsValue(title))
				title = Long.valueOf(rnd.nextLong()).toString();
			
		}
		else {
			title = tab.toString();
		}
		
		tabbedPane.add(title,tab);
		tabTitles.put(tab, title);
		
		//create tab component
		JPanel myComponent = new JPanel(new FlowLayout());
		myComponent.setOpaque(false);
		JLabel lblTitle = new JLabel(tab.toString());
		lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.add(lblTitle);// "cell 0 0");
		JButton btnClose = new JButton("X");
		btnClose.setFont(new Font(btnClose.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		btnClose.setPreferredSize(new Dimension(TAB_CLOSE_BUTTON_WIDTH, TAB_CLOSE_BUTTON_WIDTH));
		btnClose.addActionListener(new ActionListener() {
			private JPanel myTab = tab;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.remove(tabbedPane.indexOfTab(tabTitles.get(myTab)));
				tabTitles.remove(myTab);
			}
			
		});
		
		btnClose.setContentAreaFilled(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		myComponent.add(btnClose);
		tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(title), myComponent);
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(title));
		
	}*/
	
	private TabbedPane createTabbedPane(Tab startTab) {
		TabbedPane tabbedPane = new TabbedPane(this);
		tabbedPane.addNewTab(startTab);
		tabbedPane.setMinimumSize(new Dimension(100, 100));
		tabbedPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		return tabbedPane;
	}
	
	private TabbedPane createTabbedPane() {
		TabbedPane tabbedPane = new TabbedPane(this);
		tabbedPane.setMinimumSize(new Dimension(100, 100));
		tabbedPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		
		return tabbedPane;
	}
	
	public void show() {
		this.window.setVisible(true);
	}
	
	
	
}
 