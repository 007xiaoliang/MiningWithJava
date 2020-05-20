package com.sinohydro.mainWindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.sinohydro.domain.Result;
import com.sinohydro.util.Core;
import com.sinohydro.util.UiUtil;

public class GradeUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String def[] = { "0.05", "0.01", "0.02", "0.03", "0.04", "0.06", "0.07", "0.08", "0.09", "010", "0.11",
			"0.12", "0.13", "0.14", "0.15" };
	String path = null;// �ļ�·��
	Result r;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	JButton button_1 = new JButton("����ó�ƽ��Ʒλ");
	private JButton button_4 = new JButton("����");
	private FileNameExtensionFilter filter = null;

	public GradeUI() {
		this.setTitle("�ظ�Ʒζ�������");
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
		this.setSize(447, 359);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(26, 33, 390, 37);
		getContentPane().add(panel);

		JLabel label = new JLabel("ѡ���ļ�");
		label.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(14, 4, 98, 29);
		panel.add(label);

		textField = new JTextField();
		textField.setEditable(true);
		textField.setBounds(126, 1, 173, 35);
		panel.add(textField);

		JButton button = new JButton("���");
		button.setBounds(301, 3, 93, 31);
		panel.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField);
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(26, 83, 390, 37);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblk = new JLabel("����kֵ��");
		lblk.setHorizontalAlignment(SwingConstants.CENTER);
		lblk.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		lblk.setBounds(14, 0, 98, 37);
		panel_1.add(lblk);

		JComboBox comboBox = new JComboBox(def);
		comboBox.setEditable(true);
		comboBox.setBounds(126, 1, 163, 35);
		panel_1.add(comboBox);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(26, 133, 390, 37);
		getContentPane().add(panel_2);

		button_1.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		button_1.setBounds(9, 6, 161, 27);
		panel_2.add(button_1);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(184, 6, 153, 28);
		panel_2.add(textField_1);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(26, 183, 390, 37);
		getContentPane().add(panel_3);

		JButton button_2 = new JButton("ѭ������");
		button_2.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		button_2.setBounds(9, 6, 153, 27);
		panel_3.add(button_2);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(184, 6, 153, 28);
		panel_3.add(textField_2);
		button_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				double k = Double.parseDouble((String) comboBox.getSelectedItem());
				try {
					r = Core.geologicTest(path, k);
					// ����ƽ��Ʒζ
					double[] grade = r.getGrade();
					// ����doubleС�������λ
					DecimalFormat df = new DecimalFormat("0.0000");
					// ���ı�������ʾƽ��Ʒζ
					textField_1.setText(df.format((Core.dataSum(grade) / grade.length)));
					textField_2.setText(r.getNum() + "");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(26, 246, 390, 37);
		getContentPane().add(panel_4);

		JButton button_3 = new JButton("����鿴����Ʒλ");
		button_3.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		button_3.setBounds(33, 6, 169, 27);
		panel_4.add(button_3);
		button_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ��������ɵ�excel�ļ�
				try {
					Runtime.getRuntime().exec("cmd /c start " + Core.excelPath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		button_4.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		button_4.setBounds(250, 6, 102, 27);
		panel_4.add(button_4);

		JSeparator separator = new JSeparator();
		separator.setBounds(26, 233, 390, 2);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(26, 128, 390, 2);
		getContentPane().add(separator_1);
		button_4.addActionListener(this);
		
	}

	private void chooseFile(JTextField fileText) {
		int result = 0;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		filter = new FileNameExtensionFilter(".xls", "xls");// �ļ���������
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("��ѡ��Ҫ������excel�ļ�...");
		fileChooser.setFileFilter(filter);// ���ļ�ѡ���������ļ�������
		fileChooser.setApproveButtonText("ȷ��");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		result = fileChooser.showOpenDialog(this);
		if (JFileChooser.APPROVE_OPTION == result) {
			path = fileChooser.getSelectedFile().getPath();
			// �ļ�·���õ�֮�����ı�������ʾ����
			fileText.setText(path);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_4) {
			this.dispose();
			new HandleUI();
		}
	}
}
