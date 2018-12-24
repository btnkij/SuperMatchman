package silenus;

import java.awt.Point;

public class Vector2D
{
	public float x,y;
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public void setX(float value) { x=value; }
	public void setY(float value) { y=value; }
	public void setPosition(int x,int y) { this.x=x; this.y=y; }
	public void setPosition(float x,float y) { this.x=x; this.y=y; }
	
	public int getCanvasX() { return (int)(x/Canvas.BOX_WIDTH); }
	public int getCanvasY() { return (int)(y/Canvas.BOX_WIDTH); }
	
	public Vector2D() 
	{
		x=y=0;
	}

	public Vector2D(int x, int y) 
	{
		this.x=x;
		this.y=y;
	}
	
	public Vector2D(float x, float y) 
	{
		this.x=x;
		this.y=y;
	}
	
	public void add(Vector2D rhs)
	{
		x+=rhs.x;
		y+=rhs.y;
	}
	
	public void multiply(float rhs)
	{
		x*=rhs;
		y*=rhs;
	}
	
	public float multiply(Vector2D rhs)
	{
		return x*rhs.x+y*rhs.y;
	}

	public void normalize()
	{
		float mod=(float)Math.sqrt(x*x+y*y);
		x/=mod;
		y/=mod;
	}
	
	public void negate()
	{
		x=-x;
		y=-y;
	}
	
	public Point toPoint()
	{
		return new Point(getX(),getY());
	}
	
	@Override
	public String toString()
	{
		return String.format("Vector2D{x=%f y=%f", x,y);
	}
}
