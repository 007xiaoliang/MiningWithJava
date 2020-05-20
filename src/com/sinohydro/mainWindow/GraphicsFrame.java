package com.sinohydro.mainWindow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.util.Core;
import com.sinohydro.util.DrawOreLine;
import com.sinohydro.util.ReadAndWriteExcel;
import com.sinohydro.util.UiUtil;

public class GraphicsFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControlPanel cp;
	private JButton btn1 = new JButton("保存为图片");
	private JButton btnexcel = new JButton("生成excel文件");
	private JButton btn4 = new JButton("返回");
	private JTextField textField_1;
	private JTextField textField_2;
	private String blastName;
	private List<List<CircumcenterCoordinate>> allOreLines;

	public GraphicsFrame(List<CircumcenterCoordinate> list, List<List<CircumcenterCoordinate>> allOreLines,
			String blastName) {
		getContentPane().setBackground(Color.WHITE);
		this.blastName = blastName;
		this.allOreLines = allOreLines;
		setTitle("爆区圈矿图形显示");
		// 控制全屏
		/*
		 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); Rectangle
		 * bounds = new Rectangle(screenSize); this.setBounds(bounds);
		 */
		this.setSize(800, 1000);
		UiUtil.setFramerImage(this);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);

		cp = new ControlPanel(list, allOreLines);
		cp.setBounds(0, 13, 782, 839);
		this.getContentPane().add(cp);

		JPanel panel = new JPanel();
		panel.setBounds(0, 899, 782, 41);
		getContentPane().add(panel);
		panel.setLayout(null);

		btn1.setBounds(62, 0, 181, 41);
		panel.add(btn1);
		btn1.addActionListener(this);

		btnexcel.setBounds(315, 0, 175, 41);
		panel.add(btnexcel);
		btnexcel.addActionListener(this);

		btn4.setBounds(554, 0, 182, 41);
		panel.add(btn4);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 852, 782, 48);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblM = new JLabel("m³");
		lblM.setBounds(332, 14, 16, 22);
		panel_1.add(lblM);

		JLabel label_1 = new JLabel("矿石总量：");
		label_1.setBounds(411, 14, 75, 22);
		panel_1.add(label_1);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(485, 13, 100, 24);
		panel_1.add(textField_1);

		JLabel label_2 = new JLabel("m³");
		label_2.setBounds(599, 14, 16, 22);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel("爆区总量：");
		label_3.setBounds(143, 14, 75, 22);
		panel_1.add(label_3);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(218, 13, 100, 24);
		panel_1.add(textField_2);
		setVisible(true);
		btn4.addActionListener(this);
		textField_2.setText(getData(allOreLines.get(allOreLines.size() - 1)));
		double temp = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		for (int i = 0; i < allOreLines.size() - 1; i++) {
			if (allOreLines.get(i) != null)
				temp += Double.parseDouble(getData(allOreLines.get(i)));
		}
		textField_1.setText(df.format(temp) + "");

		JLabel label = new JLabel("黑色为边界线，绿色为矿石区域线；红点为矿石，蓝点为废石");
		label.setBounds(377, 783, 405, 18);
		getContentPane().add(label);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn4) {
			this.dispose();
		} else if (e.getSource() == btn1) {
			BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight() - 70, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bi.createGraphics();
			this.paint(g2d);
			try {
				ImageIO.write(bi, "PNG", new File(Core.desktopPath + File.separator + blastName + ".png"));
				new WarningUI("成功导出图片");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == btnexcel) {
			try {
				new ReadAndWriteExcel().writeOutput(allOreLines, blastName);
				new WarningUI("成功导出数据到excel");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 计算闭合多边形的体积（高按15m计算）
	 * 
	 * @param list
	 * @return
	 */
	private String getData(List<CircumcenterCoordinate> list) {
		String caculate = new DrawOreLine().getData(list);
		return caculate;
	}
}