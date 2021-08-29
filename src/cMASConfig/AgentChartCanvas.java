package cMASConfig;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;

public class AgentChartCanvas extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7417720374336323794L;
	private static final int TIMER_DELAY = 1000/5;
	private static final int BOX_PADDING = 50;
	private static final int SPACING_TO_COLUMN = 200;
	private Agent agent;
	private AgentChartBox thisAgentBox;
	private List<AgentChartBox> compatibleAgentBoxes = new ArrayList<AgentChartBox>();
	private int verticalOffset;
	private Point lastDragPoint;
	private List<AgentChartListener> agentChartListeners = new ArrayList<AgentChartListener>();
	
	public AgentChartCanvas() {
		super();
		
		this.agent = null;
		this.setBackground(Color.WHITE);
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				lastDragPoint = new Point(0,0);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				lastDragPoint = e.getLocationOnScreen();
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				verticalOffset += e.getLocationOnScreen().y - lastDragPoint.y;
				lastDragPoint = e.getLocationOnScreen();
			}
		});
		
		Timer updateTimer = new Timer(TIMER_DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				validate();
				repaint();
			}
		});
		
		updateTimer.start();
		
	}
	
	public void addAgentChartListener(AgentChartListener listener) {
		agentChartListeners.add(listener);
	}
	
	
	/**
	 * Sets the root agent of the chart. If fireEvent is true an event will be fired to inform any
	 * listeners that the agent changed. When called from the tab after an agent
	 * has been selected with the combo box fireEvent must be false so that a loop does not 
	 * occur.
	 * @param agent
	 * @param fireEvent
	 */
	public void setAgent(Agent agent, boolean fireEvent) {
		this.agent = agent;
		
		populateBoxList();
		
		//System.out.println("setAgent: Agent was set. fireEvent: " + String.valueOf(fireEvent));
		
		if (fireEvent) {
			for (AgentChartListener listener : agentChartListeners) {
				listener.agentWasSet(agent);
			}
		}
	}
	
	
	public void resetVerticalOffset() {
		verticalOffset = 0;
	}
	
	/**
	 * Finds compatible agents
	 */
	public void populateBoxList() {
		
		compatibleAgentBoxes.clear();
		
		//Selected agent
		thisAgentBox = new AgentChartBox(agent, AgentChartBox.LineAttachSide.RIGHT);
		
		//Get compatible agents
		HashMap<Agent, List<String>> myMap = DataManager.getCompatibleAgents(agent);
		Iterator<Agent> myIterator = myMap.keySet().iterator();
		
		while (myIterator.hasNext()) {
			Agent myAgent = myIterator.next();
			AgentChartBox myBox = new AgentChartBox(myAgent, AgentChartBox.LineAttachSide.LEFT);
			thisAgentBox.addConnectedBox(myBox, myMap.get(myAgent));
			myBox.addConnectedBox(thisAgentBox, myMap.get(myAgent));
			compatibleAgentBoxes.add(myBox);
		}
		
	}

	public Agent getAgent() {
		return agent;
	}
	
	
	public void drawCanvas(Graphics g) {
		if (thisAgentBox == null)
			return;
		
		int boxColumnHeight = 0;
		
		for (AgentChartBox agentBox : compatibleAgentBoxes) {
			boxColumnHeight += BOX_PADDING;
			agentBox.setPosition(thisAgentBox.getWidth() + BOX_PADDING + SPACING_TO_COLUMN, boxColumnHeight + this.verticalOffset);
			agentBox.draw(g);
			boxColumnHeight += agentBox.getHeight();
		}
		
		if (boxColumnHeight > 0) {
			thisAgentBox.setPosition(BOX_PADDING, (int)(boxColumnHeight/2) - thisAgentBox.getHeight()/2 + this.verticalOffset);
		} 
		else {
			thisAgentBox.setPosition(BOX_PADDING, BOX_PADDING + verticalOffset);
		}
		
		thisAgentBox.draw(g);
		
		//Draw connecting lines
		int index = 0;
		
		for (AgentChartBox agentBox : compatibleAgentBoxes) {
			for (int i = 0; i < agentBox.getLineAttachmentPointCount(); i++) {
				
				int[] xPoints = {thisAgentBox.getLineAttachmentPoint(index).x,
						thisAgentBox.getLineAttachmentPoint(index).x + (int)(SPACING_TO_COLUMN/6),
						thisAgentBox.getLineAttachmentPoint(i).x + (int)(SPACING_TO_COLUMN/6) + (int)(SPACING_TO_COLUMN/5),
						agentBox.getLineAttachmentPoint(i).x};
				
				int[] yPoints = {thisAgentBox.getLineAttachmentPoint(index).y,
						thisAgentBox.getLineAttachmentPoint(index).y,
						agentBox.getLineAttachmentPoint(i).y,
						agentBox.getLineAttachmentPoint(i).y};
				
				g.drawPolyline(xPoints, yPoints, 4);
				
				g.drawString(agentBox.getConnectedBoxes().get(thisAgentBox).get(i), 
						thisAgentBox.getLineAttachmentPoint(i).x + (int)(SPACING_TO_COLUMN/6) + (int)(SPACING_TO_COLUMN/5) + 10, 
						agentBox.getLineAttachmentPoint(i).y);
				
				index += 1;
			}
		}
		
	}
	
	@Override
	public void paint(Graphics g) {
		drawCanvas(g);
	}
}
