/**
 * 做皮肤效果的类
 */
package com.sinohydro.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class UiUtil {
	// 私有化
	public UiUtil() {
	}

	// 修改窗体的图标
	// Frame 是带有标题和边框的顶层窗口。
	// JFrame 是java.awt.Frame 的扩展版本
	public static void setFramerImage(JFrame jf) {

		// 创建工具类对象
		// public static Toolkit getDefaultToolkit():获取默认工具包。
		Toolkit t = Toolkit.getDefaultToolkit();

		// 创建图像类对象，并使用工具类根据路径获取图片
		Image i = t.getImage("src/com/sinohydro/source/lamp.jpg");

		// 更改窗体的图标
		jf.setIconImage(i);
	}

}