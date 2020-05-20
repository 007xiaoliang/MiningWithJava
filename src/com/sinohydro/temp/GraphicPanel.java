package com.sinohydro.temp;
import java.awt.Color;

import javax.swing.JPanel;
public class GraphicPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GraphicsFrame gf;
	
	public GraphicPanel (GraphicsFrame _gf)
	{
		super();
		
		gf = _gf;
 
		this.setBackground(Color.black);
 
		
	}
	//将面板中内容保存为图片代码
	/*BufferedImage  bi = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
	Graphics2D  g2d = bi.createGraphics();
	frame.paint(g2d);
	ImageIO.write(bi, "PNG", new File("frame.png"));*/
}
