package com.sinohydro.mainWindow;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinohydro.util.UiUtil;
import java.awt.Font;

public class HandleUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button = new JButton("����");
	private JButton button_1 = new JButton("���㱬��Ʒλ");
    private JButton button_2 = new JButton("����Ȧ���ļ�");

	public HandleUI() {
		this.setTitle("���ݴ���");
		UiUtil.setFramerImage(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 4; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 4; // ��ȡ��Ļ�ĸ�
		int height = this.getHeight();
		int width = this.getWidth();
		this.setLocation(screenWidth - width / 2, screenHeight - height / 2);
		this.setSize(400, 300);
		this.setResizable(false);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(111, 52, 161, 37);
		getContentPane().add(panel);

		button_1.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		panel.add(button_1);
		button_1.addActionListener(this);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(111, 117, 161, 37);
		getContentPane().add(panel_1);

		button_2.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		panel_1.add(button_2);
		button_2.addActionListener(this);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(110, 180, 162, 37);
		getContentPane().add(panel_2);

		button.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		panel_2.add(button);
		button.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			this.dispose();
			new MainUI();
		}else if(e.getSource() == button_1) {
			this.dispose();
			new GradeUI();
		}else if(e.getSource() == button_2) {
			this.dispose();
			new OutputUI();
		}
	}
}
