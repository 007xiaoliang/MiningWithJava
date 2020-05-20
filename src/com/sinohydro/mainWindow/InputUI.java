package com.sinohydro.mainWindow;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.sinohydro.domain.BlastArea;
import com.sinohydro.util.ConnectAccess;
import com.sinohydro.util.DateChooser;
import com.sinohydro.util.ReadAndWriteExcel;
import com.sinohydro.util.Swing_OnValueChanged;
import com.sinohydro.util.UiUtil;

public class InputUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField fileText_0;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	FileNameExtensionFilter filter = null;
	private JButton button_3 = new JButton("确认导入");
	private JButton button_4 = new JButton("返回");

	public InputUI() {
		this.setTitle("录入数据");
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
		this.setSize(518, 496);
		this.setResizable(false);
		JPanel p1 = new JPanel();
		p1.setBounds(37, 15, 426, 37);
		getContentPane().setLayout(null);

		getContentPane().add(p1);
		p1.setLayout(null);
		// p.setLayout(new GridLayout(3, 1, 50, 20));
		// 顶部 选择文件
		JLabel fileLabel = new JLabel("\u9009\u62E9\u6570\u636E\u5E93\uFF1A");
		fileLabel.setBounds(21, 4, 99, 29);
		p1.add(fileLabel);
		fileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fileText_0 = new JTextField();
		fileText_0.setBounds(133, 1, 198, 35);
		p1.add(fileText_0);
		fileText_0.setEditable(true);
		JButton fileButton = new JButton("浏览");
		fileButton.setBounds(334, 3, 93, 31);
		p1.add(fileButton);

		JPanel panel = new JPanel();
		panel.setBounds(37, 58, 423, 38);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("\u8F93\u5165\u7206\u533A\u7F16\u53F7\uFF1A");
		label.setBounds(-5, 13, 134, 18);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(label);

		textField = new JTextField();
		textField.setBounds(134, 5, 251, 33);
		textField.setEditable(true);
		panel.add(textField);
		// 定义JTextField组件的文本监听
		// textField.getDocument().putProperty("owner", textField);
		// textField.getDocument().addDocumentListener (new Swing_OnValueChanged
		// (fileText_0));

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(38, 102, 423, 38);
		getContentPane().add(panel_1);

		JLabel label_1 = new JLabel("\u9009\u62E9\u65E5\u671F\uFF1A");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(-1, 13, 155, 18);
		panel_1.add(label_1);
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
		textField_1 = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		textField_1.setEditable(false);
		dateChooser1.register(textField_1);
		textField_1.setEditable(false);
		textField_1.setBounds(132, 6, 254, 33);
		panel_1.add(textField_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(36, 172, 426, 151);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel label_2 = new JLabel("\u9009\u62E9\u5BFC\u5165\u6570\u636E\u6E90");
		label_2.setBounds(8, 64, 123, 18);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(label_2);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(133, 11, 279, 135);
		panel_2.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(2, 2, 274, 27);
		panel_3.add(panel_4);
		panel_4.setLayout(null);

		JLabel label_3 = new JLabel("\u7F16\u5F55\u8868\uFF1A");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(9, 3, 63, 22);
		panel_4.add(label_3);

		textField_2 = new JTextField();
		textField_2.setEditable(true);
		textField_2.setBounds(68, -1, 132, 30);
		panel_4.add(textField_2);

		JButton button = new JButton("浏览");
		button.setBounds(203, 0, 69, 27);
		panel_4.add(button);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBounds(3, 52, 274, 27);
		panel_3.add(panel_5);

		JLabel label_4 = new JLabel("\u5316\u9A8C\u8868\uFF1A");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(9, 3, 63, 22);
		panel_5.add(label_4);

		textField_3 = new JTextField();
		textField_3.setEditable(true);
		textField_3.setBounds(68, -1, 132, 30);
		panel_5.add(textField_3);

		JButton button_1 = new JButton("浏览");
		button_1.setBounds(203, 0, 69, 27);
		panel_5.add(button_1);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBounds(3, 102, 274, 27);
		panel_3.add(panel_6);

		JLabel label_5 = new JLabel("\u5750\u6807\u8868\uFF1A");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(9, 3, 63, 22);
		panel_6.add(label_5);

		textField_4 = new JTextField();
		textField_4.setEditable(true);
		textField_4.setBounds(68, -1, 132, 30);
		panel_6.add(textField_4);

		JButton button_2 = new JButton("浏览");
		button_2.setBounds(203, 0, 69, 27);
		panel_6.add(button_2);

		JPanel panel_7 = new JPanel();
		panel_7.setBounds(122, 356, 256, 29);
		getContentPane().add(panel_7);
		panel_7.setLayout(null);

		button_3.setBounds(8, 2, 113, 27);
		panel_7.add(button_3);
		button_3.addActionListener(this);

		button_4.setBounds(146, 2, 111, 27);
		panel_7.add(button_4);
		button_4.addActionListener(this);

		JSeparator separator = new JSeparator();
		separator.setBounds(37, 153, 426, 6);
		getContentPane().add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(37, 337, 426, 6);
		getContentPane().add(separator_1);

		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(fileText_0);
			}
		});
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_2);
			}
		});
		button_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_3);
			}
		});
		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile(textField_4);
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void chooseFile(JTextField textFile) {
		int result = 0;
		String path = null;// 文件路径
		JFileChooser fileChooser = new JFileChooser();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		if (textFile == fileText_0) {
			fileChooser.setDialogTitle("请选择数据库...");
			filter = new FileNameExtensionFilter(".mdb", "mdb");// 文件名过滤器
		}
		if (textFile == textField_2) {
			fileChooser.setDialogTitle("请选择编录表...");
			filter = new FileNameExtensionFilter(".xlsx", "xlsx");// 文件名过滤器
		}
		if (textFile == textField_3) {
			fileChooser.setDialogTitle("请选择化验表...");
			filter = new FileNameExtensionFilter(".xlsx", "xlsx");// 文件名过滤器
		}
		if (textFile == textField_4) {
			fileChooser.setDialogTitle("请选择坐标表...");
			filter = new FileNameExtensionFilter(".csv", "csv");// 文件名过滤器
		}
		fileChooser.setFileFilter(filter);// 给文件选择器加入文件过滤器
		fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
		fileChooser.setApproveButtonText("确定");
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		result = fileChooser.showOpenDialog(this);
		if (JFileChooser.APPROVE_OPTION == result) {
			path = fileChooser.getSelectedFile().getPath();
			// 文件路径拿到之后，在文本框中显示出来
			textFile.setText(path);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_3) {
			List<BlastArea> list = new ReadAndWriteExcel().dataInput(textField_2.getText(), textField_3.getText(),
					textField_4.getText());
			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(textField_1.getText());
				new ConnectAccess().accessInputUtil(fileText_0.getText(), textField.getText(), date, list);
				//执行完之后，将list清空
				for (BlastArea blastArea : list) {
					list.remove(blastArea);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == button_4) {
			this.dispose();
			new MainUI();
		}

	}
}
