package com.sinohydro.mainWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinohydro.util.UiUtil;

public class SelectUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton button = new JButton("返回");

	public SelectUI() {
		this.setTitle("查找数据");
		UiUtil.setFramerImage(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 4; // 获取屏幕的宽
		int screenHeight = screenSize.height / 4; // 获取屏幕的高
		int height = this.getHeight();
		int width = this.getWidth();
		this.setLocation(screenWidth - width / 2, screenHeight - height / 2);
		this.setSize(400, 300);
		this.setResizable(false);
		getContentPane().setLayout(null);

		button.setBounds(130, 126, 136, 27);
		getContentPane().add(button);
		button.addActionListener(this);
	}

	public static void main(String[] args) {
		new SelectUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			this.dispose();
			new MainUI();

		}
	}
}
