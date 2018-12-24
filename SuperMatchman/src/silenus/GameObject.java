package silenus;

import java.awt.Graphics;
import java.awt.Image;

public abstract class GameObject {

	public int getTag()
	{
		return 0;
	}
	public static final int TAG_MARIO=0x00000001;
	public static final int TAG_BRICK=0x00000002;
	public static final int TAG_TRAP=0x00000004;
	public static final int TAG_STAR=0x00000008;
	
	protected static Rect2D viewPort;
	
	protected boolean visible=true;
	public boolean getVisibility()
	{
		return visible;
	}
	public void setVisibility(boolean value)
	{
		visible=value;
	}
	
	protected Rect2D bounds=new Rect2D();
	public Rect2D getBounds()
	{
		return bounds;
	}
//	public Vector2D getPosition()
//	{
//		return bound;
//	}
//	public void setX(int value)
//	{
//		bound.x=value;
//	}
//	public void setY(int value)
//	{
//		position.x=value;
//	}
//	public void setPosition(int x,int y)
//	{
//		position.x=x;
//		position.y=y;
//	}
//	public void setPosition(Vector2D value)
//	{
//		position=value;
//	}
	protected Image[] img;
	public Image getFrame()
	{
		return img[0];
	}

	public GameObject(Prefab prefab) 
	{
		bounds.setWidth(prefab.width);
		bounds.setHeight(prefab.height);
		this.img=prefab.img;
	}

	public void paint(Graphics g)
	{
//		if(viewPort==null)
//		{
//			viewPort=Canvas.instance().getViewPort();
//		}
//		viewPort=Canvas.instance().getViewPort();
		g.drawImage(
				getFrame(),
				bounds.getX()-viewPort.getX(),
				bounds.getY()-viewPort.getY(),
				null);
	}
}
