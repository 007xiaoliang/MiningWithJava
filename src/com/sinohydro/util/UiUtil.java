/**
 * ��Ƥ��Ч������
 */
package com.sinohydro.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class UiUtil {
	// ˽�л�
	public UiUtil() {
	}

	// �޸Ĵ����ͼ��
	// Frame �Ǵ��б���ͱ߿�Ķ��㴰�ڡ�
	// JFrame ��java.awt.Frame ����չ�汾
	public static void setFramerImage(JFrame jf) {

		// �������������
		// public static Toolkit getDefaultToolkit():��ȡĬ�Ϲ��߰���
		Toolkit t = Toolkit.getDefaultToolkit();

		// ����ͼ������󣬲�ʹ�ù��������·����ȡͼƬ
		Image i = t.getImage("src/com/sinohydro/source/lamp.jpg");

		// ���Ĵ����ͼ��
		jf.setIconImage(i);
	}

}