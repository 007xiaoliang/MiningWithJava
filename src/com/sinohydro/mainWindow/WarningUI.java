package com.sinohydro.mainWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sinohydro.util.UiUtil;
import javax.swing.SwingConstants;

public class WarningUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public WarningUI(String message) {
		super();
		this.message = message;
		this.setTitle("��ʾ");
		UiUtil.setFramerImage(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 2; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 2; // ��ȡ��Ļ�ĸ�
		int height = this.getHeight();
		int width = this.getWidth();
		this.setLocation(screenWidth - width / 2, screenHeight - height / 2);
		this.setSize(205, 112);
		this.setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel(message);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 13, 199, 48);
		getContentPane().add(lblNewLabel);
	}
	public static void main(String[] args) {
		new WarningUI("hehe");
	}
	

}
