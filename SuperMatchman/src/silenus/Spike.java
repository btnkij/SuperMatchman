package silenus;

import java.awt.Image;

public class Spike extends StaticObject {

	private Rect2D collider=null;
	private int towards=0;
	
	public Spike(int towards) {
		super(Prefab.SPIKE);
		this.towards=towards;
	}

	@Override
	public Rect2D getCollider()
	{
		if(collider==null)
		{
			collider=new Rect2D();
			collider.width=bounds.width/2;
			collider.height=bounds.height/2;
			switch(towards)
			{
			case 0:
				collider.x=bounds.x+1.0F/4*bounds.width;
				collider.y=bounds.y;
				break;
			case 1:
				collider.x=bounds.x+1.0F/2*bounds.width;
				collider.y=bounds.y+1.0F/4*bounds.height;
				break;
			case 2:
				collider.x=bounds.x+1.0F/4*bounds.width;
				collider.y=bounds.y+1.0F/2*bounds.height;
				break;
			case 3:
				collider.x=bounds.x;
				collider.y=bounds.y+1.0F/4*bounds.height;
				break;
			}
		}
		return collider;
	}
	
	@Override
	public int getTag()
	{
		return GameObject.TAG_TRAP;
	}
	
	@Override
	public Image getFrame()
	{
		return img[towards];
	}
}
