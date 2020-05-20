package com.sinohydro.mainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.sinohydro.domain.CircumcenterCoordinate;
import com.sinohydro.domain.Ore;
import com.sinohydro.util.ConnectAccess;
import com.sinohydro.util.Core;
import com.sinohydro.util.DrawOreLine;
import com.sinohydro.util.ReadAndWriteExcel;
import com.sinohydro.util.UiUtil;

public class OutputUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JLabel label = new JLabel("���뱬����ţ�");
	private final JTextField textField = new JTextField();
	private final JLabel label_1 = new JLabel("ѡ������Դ");
	private JTextField textField_1;
	private JButton button_1 = new JButton("����");
	private JButton button_2 = new JButton("����");
	private JTextField textField_2;
	private JTextField textField_3;
	private ButtonGroup group = new ButtonGroup();
	private JCheckBox chckbxNewCheckBox = new JCheckBox("�����ݿ�ѡ��", true);
	private JCheckBox checkBox = new JCheckBox("���ļ�ѡ��");
	private FileNameExtensionFilter filter = null;
	String path = null;// �ļ�·��
	private List<CircumcenterCoordinate> list = new ArrayList<CircumcenterCoordinate>();

	public OutputUI() {
		this.setTitle("����Ȧ���ļ�");
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
		this.setSize(400, 561);
		this.setResizable(false);
		getContentPane().setLayout(null);
		panel.setForeground(Color.RED);
		panel.setBounds(56, 13, 298, 442);

		getContentPane().add(panel);
		panel.setLayout(null);
		label_1.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label_1.setBounds(14, 13, 106, 18);

		panel.add(label_1);

		chckbxNewCheckBox.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		chckbxNewCheckBox.setBounds(14, 94, 133, 27);
		panel.add(chckbxNewCheckBox);

		checkBox.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		checkBox.setBounds(14, 169, 133, 27);
		panel.add(checkBox);
		group.add(checkBox);
		group.add(chckbxNewCheckBox);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(14, 205, 254, 27);
		panel.add(panel_2);

		JLabel label_2 = new JLabel("�ļ�:");
		label_2.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(9, 3, 63, 22);
		panel_2.add(label_2);

		textField_1 = new JTextField();
		textField_1.setEditable(true);
		textField_1.setBounds(68, -1, 120, 30);
		panel_2.add(textField_1);

		JButton button = new JButton("���");
		button.setBounds(191, 1, 63, 27);
		panel_2.add(button);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBounds(12, 369, 256, 29);
		panel.add(panel_3);

		button_1.setBounds(14, 2, 93, 27);
		panel_3.add(button_1);
		button_1.addActionListener(this);

		button_2.setBounds(164, 2, 93, 27);
		panel_3.add(button_2);
		button_2.addActionListener(this);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 37, 254, 2);
		panel.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(14, 245, 254, 2);
		panel.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(14, 342, 254, 2);
		panel.add(separator_2);

		JLabel label_3 = new JLabel("�����߽�");
		label_3.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label_3.setBounds(14, 260, 72, 18);
		panel.add(label_3);

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBounds(14, 291, 254, 27);
		panel.add(panel_4);

		JLabel label_4 = new JLabel("�ļ�:");
		label_4.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(9, 3, 63, 22);
		panel_4.add(label_4);

		textField_2 = new JTextField();
		textField_2.setEditable(true);
		textField_2.setBounds(68, -1, 120, 30);
		panel_4.add(textField_2);

		JButton button_3 = new JButton("���");
		button_3.setBounds(191, 1, 63, 27);
		panel_4.add(button_3);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBounds(14, 130, 270, 30);
		panel.add(panel_5);

		JLabel label_5 = new JLabel("ѡ�����ݿ⣺");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label_5.setBounds(0, 3, 101, 22);
		panel_5.add(label_5);

		textField_3 = new JTextField();
		textField_3.setEditable(true);
		textField_3.setBounds(94, 1, 110, 27);
		panel_5.add(textField_3);

		JButton button_4 = new JButton("���");
		button_4.setBounds(207, 1, 63, 27);
		panel_5.add(button_4);
		textField.setBounds(105, 0, 144, 29);
		textField.setColumns(10);
		panel_1.setBounds(14, 55, 254, 30);
		panel.add(panel_1);
		panel_1.setLayout(null);
		label.setFont(new Font("���Ŀ���", Font.PLAIN, 15));
		label.setBounds(0, 0, 105, 30);
		panel_1.add(label);

		panel_1.add(textField);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		button_4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_3, ".mdb", "mdb");

			}
		});
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_1, ".xls", "xls");

			}
		});
		button_3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_2, ".xls", "xls");

			}
		});
	}

	private void chooseFile(JTextField fileText, String str1, String str2) {
		int result = 0;
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		filter = new FileNameExtensionFilter(str1, str2);// �ļ���������
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setDialogTitle("��ѡ�����ݿ�...");
		fileChooser.setFileFilter(filter);// ���ļ�ѡ���������ļ�������
		fileChooser.setApproveButtonText("ȷ��");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		result = fileChooser.showOpenDialog(this);
		if (JFileChooser.APPROVE_OPTION == result) {
			path = fileChooser.getSelectedFile().getPath();
			// �ļ�·���õ�֮�����ı�������ʾ����
			fileText.setText(path);
			// System.out.println("path: " + path);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_2) {
			this.dispose();
			new HandleUI();
		} else if (e.getSource() == button_1) {
			String blastName = textField.getText();
			if (chckbxNewCheckBox.isSelected()) {// �����ݿ�ѡ��Դ�ļ�
				String path = textField_3.getText();
				list = new ConnectAccess().coordinateSource(path, blastName);
				// ��֤����
				for (CircumcenterCoordinate cc : list) {
					System.out.println(cc.getX() + "," + cc.getY() + "," + cc.getZ());
				}
				System.out.println("****************************************************");

			} else {
				String path = textField_1.getText();
				List<Ore> readExcel;
				try {
					readExcel = Core.readExcel(path);
					for (Ore ore : readExcel) {
						CircumcenterCoordinate cc = new CircumcenterCoordinate();
						cc.setX(ore.getCoordinateX());
						cc.setY(ore.getCoordinateY());
						cc.setZ(ore.getCoordinateZ());
						cc.setCu(ore.getOrePercent());
						list.add(cc);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			try {
				if (list.size() != 0) {
					List<CircumcenterCoordinate> givedBorderList = null;
					if (textField_2 != null && textField_2.getText().length() != 0) {
						try {
							givedBorderList = new ReadAndWriteExcel().readExcel(textField_2.getText());
						} catch (IOException e1) {
							new WarningUI("��ѡ�߽��ļ���ʽ����");
							e1.printStackTrace();
						}
					}
					List<List<CircumcenterCoordinate>> allOreLines = new DrawOreLine().getAllOreLines(list,
							givedBorderList);
					for (List<CircumcenterCoordinate> list2 : allOreLines) {
						if (list2 != null) {
							for (CircumcenterCoordinate cc : list2) {
								System.out.println(cc.getX() + "," + cc.getY() + "," + 0.0);
							}
							System.out.println("--------------------------------------");
						}
					}
					new GraphicsFrame(list, allOreLines, blastName);
				} else {
					new WarningUI("��ѡ����������");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}
}
