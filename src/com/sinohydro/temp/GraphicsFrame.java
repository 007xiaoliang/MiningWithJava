package com.sinohydro.temp;
import java.awt.BorderLayout;
import javax.swing.JFrame;

import com.sinohydro.util.UiUtil;
public class GraphicsFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	public GraphicPanel gp;
	
	public ControlPanel cp;
 
	public GraphicsFrame (int height, int width)
	{
 
		setTitle("ÅÚÇøÈ¦¿óÍ¼ÐÎÏÔÊ¾");
 
		setSize(width, height);
		
		UiUtil.setFramerImage(this);
		
		setLocationRelativeTo(null);
 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
		setLayout(new BorderLayout(5, 5));
 
		setVisible(true);
		gp = new GraphicPanel(this);
		this.add(gp, BorderLayout.CENTER);
		
		cp = new ControlPanel(this);
		this.add(cp, BorderLayout.EAST);
 
		
	}
    
	public static void main (String[] args)
	{
		
		new GraphicsFrame(512, 768);
	}
}
