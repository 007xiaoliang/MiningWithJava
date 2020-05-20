package com.sinohydro.mainWindow;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sinohydro.util.UiUtil;
import java.awt.Font;

public class MainUI extends JFrame implements ActionListener {

	private JButton jb1 = new JButton("¼������");
	private JButton jb2 = new JButton("��������");
	private JButton jb3 = new JButton("��ѯ����");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainUI() {
		this.setTitle("��ɽȦ����ϵͳ");
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
		JPanel p = new JPanel();
		p.setBounds(124, 54, 136, 145);
		p.setLayout(new GridLayout(3, 1, 50, 20));
		jb1.setFont(new Font("����", Font.PLAIN, 15));
		p.add(jb1);
		jb2.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		p.add(jb2);
		jb3.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		p.add(jb3);
		jb1.addActionListener(this);//����ť�������
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		getContentPane().setLayout(null);
		getContentPane().add(p);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new MainUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb1) {
			this.dispose();
			new InputUI();
		} else if (e.getSource() == jb2) {
			this.dispose();
			new HandleUI();
		} else if (e.getSource() == jb3) {
			this.dispose();
			new SelectUI();
		}
	}

}
