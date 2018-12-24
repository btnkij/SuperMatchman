package silenus;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

public class RoundButton extends JButton 
{
	private static final long serialVersionUID = 5158331560804761904L;
	
	private Shape shape;
	private Image image;

	public RoundButton(Image image)
	{
		this.setPreferredSize(new Dimension(image.getWidth(null),image.getHeight(null)));
		this.image=image;
		setContentAreaFilled(false);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
//		if(getModel().isArmed())
//		{
//			g.setColor(Color.LIGHT_GRAY);
//		}
//		else
//		{
//			g.setColor(getBackground());
//		}
//		g.setColor(Color.CYAN);
//		g.fillOval(0,0,getSize().width-1,getSize().height-1);
		g.drawImage(image, 0, 0, null);
		super.paintComponents(g);
	}
	
	@Override
	protected void paintBorder(Graphics g)
	{
//		g.setColor(Color.WHITE);
//		g.drawOval(0,0,getSize().width-1,getSize().height-1);
	}
		 
	@Override
	public boolean contains(int x,int y)
	{
		if((shape==null)||(!shape.getBounds().equals(getBounds())))
		{
			shape=new Ellipse2D.Float(0,0,getWidth(),getHeight());
		}
		return shape.contains(x,y);
	}
}
