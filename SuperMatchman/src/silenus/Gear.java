package silenus;

import java.awt.Image;

public class Gear extends Spirit {

	private int iframe=0;
	private Rect2D collider;
	private static final float MARGIN=3;
	
	private int x1,y1,x2,y2;
	public void setRoute(int x1,int y1,int x2,int y2)
	{
		if(x1>x2 || (x1==x2 && y1>y2))
		{
			int t=x1; x1=x2; x2=t;
			t=y1; y1=y2; y2=t;
		}
		this.x1=x1; this.y1=y1; this.x2=x2; this.y2=y2;
	}
	
	public Gear() {
		super(Prefab.GEAR);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCollide(GameObject arg, int side) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rect2D getCollider() {
		if(collider==null)
		{
			collider=new Rect2D();
			collider.width=bounds.width-MARGIN*2;
			collider.height=bounds.height-MARGIN*2;
		}
		collider.x=bounds.x+MARGIN;
		collider.y=bounds.y+MARGIN;
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
		iframe=(iframe+1)%img.length;
		return img[iframe];
	}
	
	@Override
	public void update()
	{
		if((x1==x2 && (bounds.getCanvasY()<y1 && velocity.y<0 || bounds.getCanvasY()>=y2 && velocity.y>0))
			|| (x1!=x2 && (bounds.getCanvasX()<x1 && velocity.x<0 || bounds.getCanvasX()>=x2 && velocity.x>0)))
			velocity.negate();
		super.update();
	}

}
