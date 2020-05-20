package com.sinohydro.temp;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
public class ControlPanel extends JPanel {
 
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public GraphicsFrame gf;
	public GraphicPanel gp;
 
 
	public ControlPanel(GraphicsFrame _gf) {
		super();
		gf = _gf;
		gp = gf.gp;
 
 
		ButtonGroup bg = new ButtonGroup();
		final JRadioButton pointsButton = new JRadioButton("Points", false);
		final JRadioButton linesButton = new JRadioButton("Lines", false);
		final JRadioButton trianglesButton = new JRadioButton("Triangles", false);
		final JRadioButton polygonsButton = new JRadioButton("Polygons", false);
 
 
		bg.add(pointsButton);
		bg.add(linesButton);
		bg.add(trianglesButton);
		bg.add(polygonsButton);
 
 
		setLayout(new GridLayout(20, 1, 2, 2));
 
 
		add(pointsButton);
		add(linesButton);
		add(trianglesButton);
		add(polygonsButton);
		Graphics g = gf.getGraphics();
		g.setColor(Color.red);
		for (int i = 0; i < 10; i++) {
			g.fillOval(i*5+7, i*5+30, 2, 2);
		}
		repaint();
		/*gp.addMouseListener(new MouseListener() {
 
 
			List<Point> li = new ArrayList<Point>();
			List<Point> tri = new ArrayList<Point>();
			List<Point> pol = new ArrayList<Point>();
			Graphics g;
 
 
			@Override
			public void mouseClicked(MouseEvent e) {
				if (pointsButton.isSelected()) {
					int x, y;
					x = e.getX();
					y = e.getY();
					g = gf.getGraphics();
					g.setColor(Color.red);
					g.fillOval(x+7, y+30, 2, 2);
				} else if (linesButton.isSelected()) {
					Point p1 = new Point();
					Point p2 = new Point();
					li.add(e.getPoint());
					if (li.size() % 2 == 0) {
						g = gf.getGraphics();
						g.setColor(Color.red);
						p1 = li.get(0);
						p2 = li.get(1);
						g.fillOval(e.getX()+7, e.getY()+30, 2, 2);
						g.drawLine(p1.x+7, p1.y+30, p2.x+7, p2.y+30);
						repaint();
						li.clear();
					} else {
						g = gf.getGraphics();
						g.setColor(Color.red);
						g.fillOval(e.getX()+7, e.getY()+30, 2, 2);
					}
				} else if (trianglesButton.isSelected()) {
					Point pt1 = new Point();
					Point pt2 = new Point();
					Point pt3 = new Point();
					tri.add(e.getPoint());
					if (tri.size() % 3 == 0) {
						g = gf.getGraphics();
						g.setColor(Color.red);
						pt1 = tri.get(0);
						pt2 = tri.get(1);
						pt3 = tri.get(2);
						g.fillOval(e.getX()+7, e.getY()+30, 2, 2);
						g.drawLine(pt1.x+7, pt1.y+30, pt2.x+7, pt2.y+30);
						g.drawLine(pt2.x+7, pt2.y+30, pt3.x+7, pt3.y+30);
						g.drawLine(pt3.x+7, pt3.y+30, pt1.x+7, pt1.y+30);
						repaint();;
						tri.clear();
					} else {
						g = gf.getGraphics();
						g.setColor(Color.red);
						g.fillOval(e.getX()+7, e.getY()+30, 2, 2);
					}
				} else if (polygonsButton.isSelected()) {
					pol.add(e.getPoint());
					g = gf.getGraphics();
					g.setColor(Color.red);
					g.fillOval(e.getX()+7, e.getY()+30, 2, 2);
					if (e.isMetaDown()) {
						g = gf.getGraphics();
						g.setColor(Color.red);
						for (int i = 0; i < pol.size() - 1; i++) {
							Point po1 = pol.get(i);
							Point po2 = pol.get(i + 1);
							g.drawLine(po1.x+7, po1.y+30, po2.x+7, po2.y+30);
						}
						g.drawLine(pol.get(0).x+7, pol.get(0).y+30, pol.get(pol.size() - 1).x+7, pol.get(pol.size() - 1).y+30);
						pol.clear();
					}
				}
 
 
			}
 
 
			@Override
			public void mouseReleased(MouseEvent e) {
			}
 
 
			@Override
			public void mousePressed(MouseEvent e) {
			}
 
 
			@Override
			public void mouseExited(MouseEvent e) {
			}
 
 
			@Override
			public void mouseEntered(MouseEvent e) {
			}
 
 
		});*/
	}
 
 
}
