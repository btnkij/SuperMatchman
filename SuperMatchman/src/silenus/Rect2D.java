package silenus;

import java.awt.Rectangle;

public class Rect2D extends Vector2D
{
	public float width;
	public int getWidth() { return (int)width; }
	public void setWidth(float value) { width=value; }
	
	public float height;
	public int getHeight() { return (int)height; }
	public void setHeight(float value) { height=value; }
	
	public int getCenterX() { return (int)(x+width/2); }
	
	public int getCanvasCenterX() { return (int)((x+width/2)/Canvas.BOX_WIDTH); }
	
	public int getCenterY() { return (int)(y+height/2); }
	
	public int getCanvasCenterY() { return (int)((y+height/2)/Canvas.BOX_WIDTH); }
	
	public int getMaxX() { return (int)(x+width); }
	
	public int getMaxY() { return (int)(y+height); }
	
	public int getCanvasMaxX() { return (int)(x+(width/Canvas.BOX_WIDTH)); }
	
	public int getCanvasMaxY() { return (int)(y+(height/Canvas.BOX_WIDTH)); }
	
	public Rect2D()
	{
		x=y=width=height=0;
	}
	
	public Rect2D(int x,int y,int width,int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public Rect2D(float x,float y,float width,float height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	public Rectangle toRectangle()
	{
		return new Rectangle((int)x,(int)y,(int)width,(int)height);
	}
	
	public boolean intersects(Rect2D rhs)
	{
		return !(x+width<rhs.x || x>rhs.x+rhs.width || y+height<rhs.y || y>rhs.y+rhs.height);
	}
	
	public boolean contains(int x,int y)
	{
		return this.x<=x && x<this.getMaxX() && this.y<=y && y<this.getMaxY();
	}
	
	public boolean contains(float x,float y)
	{
		return this.x<=x && x<this.x+this.width && this.y<=y && y<this.y+this.height;
	}
	
	@Override
	public String toString()
	{
		return String.format("Rect2D{x=%f y=%f width=%f height=%f}", x,y,width,height);
	}
}