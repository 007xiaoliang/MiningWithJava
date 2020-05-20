package com.sinohydro.mainWindow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.util.DrawOreLine;

public class ControlPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CircumcenterCoordinate> list;
	private List<List<CircumcenterCoordinate>> allOreLines;
	private int x;// �µ�����ϵԭ��x����
	private int y;// �µ�����ϵԭ��y����
	private final int X = 200;
	private final int Y = 600;
	public ControlPanel(List<CircumcenterCoordinate> list, List<List<CircumcenterCoordinate>> allOreLines) {
		super();
		this.list = list;
		this.allOreLines = allOreLines;
		setLayout(null);
		// �ҵ�������������С�ĵ㣬��Ϊ����ԭ��
		CircumcenterCoordinate minPoint = new DrawOreLine().getMinPoint(list);
		x = (int) minPoint.getX();
		y = (int) minPoint.getY();
	}

	@Override
	public void paint(Graphics g) {
		// g.translate(-x, -y);
		g.setColor(Color.red);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCu() < 0.12)
				g.setColor(Color.blue);
			g.fillOval((int) (list.get(i).getX() - x) * 3 + X, (int) (list.get(i).getY() - y) * 3 + Y,5,5);
			g.setColor(Color.red);
		}
		// �������߽�
		for (int i = 0; i < allOreLines.size(); i++) {
			g.setColor(Color.green);
			if (i == allOreLines.size() - 1)
				g.setColor(Color.black);
			if (allOreLines.get(i) != null) {
				int[] xx = new int[allOreLines.get(i).size()];
				int[] yy = new int[allOreLines.get(i).size()];
				for (int j = 0; j < allOreLines.get(i).size(); j++) {
					xx[j] = (int) (allOreLines.get(i).get(j).getX() - x) * 3 + X;
					yy[j] = (int) (allOreLines.get(i).get(j).getY() - y) * 3 + Y;
				}
				g.drawPolygon(xx, yy, xx.length);
			}
		}
	}
}
