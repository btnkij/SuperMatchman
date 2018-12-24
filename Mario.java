package silenus;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class Mario extends Spirit implements KeyListener {
	
	private static final int SPEED_X=3;
	private static final int SPEED_Y=8;
	private static final int MARGIN=3;
	
	private int iframe=0;
	private int face=1;
	private boolean onGround2=false;
	private boolean leftKey=false,rightKey=false,upKey=false,downKey=false;
	private int jump=0; //0: none, 1: first jump, 2: end first jump, 3: double jump
	private int jumpTimer=0;
	
	private boolean die=false;
	public boolean isDie() { return die; }
	
	private Rect2D collider=null;

	public Mario()
	{
		super(Prefab.MARIO);
	}
	
	@Override
	public int getTag()
	{
		return GameObject.TAG_MARIO;
	}
	
	@Override
	public void update()
	{
		if(leftKey)
		{
			face=-1;
			velocity.x=-SPEED_X;
		}
		else if(rightKey)
		{
			face=1;
			velocity.x=SPEED_X;
		}
		else
		{
			velocity.x=0;
		}
		if(upKey)
		{
			if(onGround)
			{
				jump=1;
				jumpTimer=0;
			}
			else if(jump==2)
			{
				jump=3;
			}
		}
		else
		{
			if(jump==1)jump=2;
			else if(jump==3)jump=0;
		}
		if(downKey)
		{
			
		}
		if(jump==1 && jumpTimer<7)
		{
			velocity.y=-SPEED_Y;
			jumpTimer++;
		}
		else if(jump==3 && jumpTimer<14)
		{
			velocity.y=-SPEED_Y;
			jumpTimer++;
		}
		else if(!onGround && velocity.y<10)
		{
			velocity.y+=1;
		}
		super.update();
		onGround2=onGround;
		onGround=false;
	}
	
//	private boolean paused=false;
	@Override
	public void keyPressed(KeyEvent arg) {
//		System.out.println(arg.getKeyCode());
		switch(arg.getKeyCode())
		{
		case KeyEvent.VK_LEFT: leftKey=true; break;
		case KeyEvent.VK_RIGHT: rightKey=true; break;
		case KeyEvent.VK_UP: upKey=true; break;
		case KeyEvent.VK_DOWN: downKey=true; break;
/**
 * Pause : uncompleted function
 */
//		case KeyEvent.VK_ESCAPE:
//			// WARNING: not recommend to write hear
//			if(paused)break;
//			if(GameManager.instance().getGameStatus()==GameManager.RUNNING)
//			{
//				paused=true;
//				GameManager.instance().pause();
//				while(GameManager.instance().getGameStatus()!=GameManager.RUNNING)
//				{
//					try {
//						Thread.sleep(20);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//				paused=false;
//			}
//			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg) {
		switch(arg.getKeyCode())
		{
		case KeyEvent.VK_LEFT: leftKey=false; break;
		case KeyEvent.VK_RIGHT: rightKey=false; break;
		case KeyEvent.VK_UP: upKey=false; break;
		case KeyEvent.VK_DOWN: downKey=false; break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) 
	{
		
	}

	@Override
	public Rect2D getCollider() 
	{
		if(collider==null)
		{
			collider=new Rect2D();
			collider.width=bounds.width-MARGIN*2;
			collider.height=bounds.height;
		}
		collider.x=bounds.x+MARGIN;
		collider.y=bounds.y;
		return collider;
	}

	@Override
	public void onCollide(GameObject arg, int side) {
		if(arg==null)
		{
			setVisibility(false);
		}
		else if((arg.getTag()&GameObject.TAG_BRICK)!=0)
		{
			if(side==1)
			{
				velocity.x=0;
			}
			else if(side==3)
			{
				velocity.x=0;
			}
			else if(side==0)
			{
				velocity.y=0;
			}
			else
			{
				//there is a bug that walls at directions numbered 4 and 6 will be misrecognised as ground
				//see also Canvas.java CollisionTest()
				jump=0;
				velocity.y=0;
				onGround=true;
			}
		}
		else if((arg.getTag()&GameObject.TAG_TRAP)!=0)
		{
			die=true;
			GameManager.instance().fail();
		}
		else if((arg.getTag()&GameObject.TAG_STAR)!=0)
		{
			GameManager.instance().win();
		}
	}
	
	private static final int DEV=6;
	@Override
	public Image getFrame()
	{
		if(die)
		{
			return img[7];
		}
		else if(!onGround2)
		{
			if(face==1)
			{
				return img[5];
			}
			else
			{
				return img[6];
			}
		}
		else if(velocity.x>0)
		{
			iframe=(iframe+1)%(DEV<<1);
			return img[(iframe/DEV)%2+1];
		}
		else if(velocity.x<0)
		{
			iframe=(iframe+1)%(DEV<<1);
			return img[(iframe/DEV)%2+3];
		}
		else
		{
			return img[0];
		}
	}

}
