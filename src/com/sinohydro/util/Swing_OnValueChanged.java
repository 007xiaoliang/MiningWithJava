package com.sinohydro.util;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Swing_OnValueChanged implements DocumentListener {

	private JTextField fileText_0;
	
	public Swing_OnValueChanged(JTextField fileText_0) {
		super();
		this.fileText_0 = fileText_0;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		String text = null;
		try {
			text = e.getDocument().getText(e.getDocument().getStartPosition().getOffset(), e.getDocument().getLength());
			JTextField textField= (JTextField) e.getDocument().getProperty("owner");
			new ConnectAccess().accessOutputUtil(fileText_0.getText(), textField.getText(),textField);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub

	}

}
