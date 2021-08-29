package cMASConfig;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JLabel;

public class AgentChartBox {
	
	public enum LineAttachSide {
		RIGHT,
		LEFT;
	}
		
	private static final int CORNER = 5;
	private static final Color FILL_COLOR = Color.BLUE;
	private static final int VERTICAL_PADDING = 10;
	private static final int HORIZONTAL_PADDING = 10;
	private static final int ATTACHMENT_POINT_SPACING = 10;
	
	private Font font;
	private LineAttachSide lineAttach = LineAttachSide.RIGHT;
	private Agent agent;
	private int x = 40;
	private int y = 50;
	private Map<AgentChartBox, List<String>> connectedBoxes = new HashMap<AgentChartBox, List<String>>();
	private int height = 0;
	private int width = 0;
	
	public AgentChartBox(Agent agent, LineAttachSide lineAttachSide) {
		this.agent = agent;
		this.lineAttach = lineAttachSide;
		//Get standard font from JLabel
		JLabel aLbl = new JLabel();
		this.font = new Font(aLbl.getFont().getName(),
				aLbl.getFont().getStyle(),
				aLbl.getFont().getSize());
	}
	
	public int getLineAttachmentPointCount() {
		return connectedBoxes.size();
	}
	
	public void addConnectedBox(AgentChartBox box, List<String> interfaces) {
		connectedBoxes.put(box, interfaces);
	}
	
	public void removeConnectedBox(AgentChartBox box) {
		if (connectedBoxes.containsKey(box))
			connectedBoxes.remove(box);
	}
	
	public Map<AgentChartBox, List<String>> getConnectedBoxes() {
		return connectedBoxes;
	}
	
	public Point getPosition() {
		return new Point(x,y);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setLineAttach(LineAttachSide lineAttachSide) {
		this.lineAttach = lineAttachSide;
	}
	
	public LineAttachSide getLineAttach() {
		return lineAttach;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void draw(Graphics g) {
		
		if (font == null || agent == null)
			return;
		
		g.setColor(FILL_COLOR);
		g.setFont(font);
		
		//Get size
		Rectangle2D sb;
		
		if (agent.Name == "") {
			sb = g.getFontMetrics(font).getStringBounds(agent.ID, g);
		}
		else {
			sb = g.getFontMetrics(font).getStringBounds(agent.Name, g);
		}
		
		int stringHeight = (int) sb.getHeight();		//The height needed to accommodate the string
		int pointHeight;								//The height needed to accommodate all attachment points
		
		if (getLineAttachmentPointCount() > 1) {
			pointHeight = (getLineAttachmentPointCount() - 1) * ATTACHMENT_POINT_SPACING;
		}
		else {
			pointHeight = 0;
		}
		
		if (stringHeight > pointHeight) {
			height = stringHeight + VERTICAL_PADDING *2;
		}
		else {
			height = pointHeight + VERTICAL_PADDING * 2;
		}
		
		width = (int) sb.getWidth() + HORIZONTAL_PADDING * 2;
		
		g.drawRoundRect(x, y, width, height, CORNER, CORNER);
		int ovalRad = 3;
		
		/*
		 * Draw attachment points
		 */
		for (int i = 0; i < getLineAttachmentPointCount(); i++) {
			
			Point lineAttachmentPoint = getLineAttachmentPoint(i);
			
			g.fillOval(lineAttachmentPoint.x - ovalRad, 
					lineAttachmentPoint.y - ovalRad, 
					ovalRad * 2, 
					ovalRad * 2);
		}
		
		int stringLocX = x + HORIZONTAL_PADDING;
		int stringLocY = y + (int)((sb.getHeight() + VERTICAL_PADDING * 2) / 2);
		
		g.drawString(getDisplayedString(), stringLocX, stringLocY);
	}
	
	/**
	 * Points are counted in decending order with index
	 * 0 being the one place closest to the top of the 
	 * canvas
	 * @param index
	 * @return
	 */
	public Point getLineAttachmentPoint(int index) {
		int pointX;
		
		if (lineAttach.equals(LineAttachSide.LEFT)) {
			pointX = x;
		} else {
			pointX = x + width;
		}
		
		int pointY = y + VERTICAL_PADDING + index * ATTACHMENT_POINT_SPACING;
		
		return new Point(pointX, pointY);
	}

	public String getDisplayedString() {
		if (agent.Name.equals("")) {
			return agent.ID;
		}
		else {
			return agent.Name;
		}
	}
	

}
