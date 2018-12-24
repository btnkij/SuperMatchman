package silenus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;

public class Canvas extends JPanel implements Runnable {
	private static final long serialVersionUID = 4483475349606753250L;

	/**
	 * Warning: GameObject should not move more than BOX_WIDTH in a frame
	 */
	public static final int BOX_WIDTH=20;
	
	private static final int VIEW_WIDTH=40;
	private static final int VIEW_HEIGHT=25;
	private static final int RENDER_WIDTH=60;
	private static final int RENDER_HEIGHT=25;

	/**
	 * interval between two frames (unit = ms)
	 */
	private final int TICK=30;
//	private Timer timer=null;
	
	/**
	 * width and height of scene
	 * (unit = BOX_WIDTH)
	 */
	private int sceneWidth=0, sceneHeight=0;
	
	/**
	 * the range of scene that is displayed on the screen
	 * (unit = px)
	 */
	private Rect2D viewPort=new Rect2D();
	public Rect2D getViewPort()
	{
		return viewPort;
	}
	
	/**
	 * the range of scene that should be updated
	 * (unit = BOX_WIDTH)
	 */
	private Rect2D renderPort=new Rect2D();
	public Rect2D getRenderPort()
	{
		return renderPort;
	}

	private static Canvas canvas=null;
	public static Canvas instance()
	{
		if(canvas==null)canvas=new Canvas();
		return canvas;
	}
	
	private Mario mario=null;
	private LinkedList<Spirit> spirits=null;
	private StaticObject[][] staticObjs;
	private Spirit[][] buffer=null;
	
	/**
	 * a bit vector shows if viewPort reaches the end of scene
	 */
//	private int reachEnd=0b0000;
//	private static final int LEFT_END=0b0001;
//	private static final int RIGHT_END=0b0010;
//	private static final int TOP_END=0b0100;
//	private static final int BOTTOM_END=0b1000;
	
	private Canvas()
	{
		
	}
	
	/**
	 * Create a new scene
	 * @param sceneWidth unit = BOX_WIDTH
	 * @param sceneHeight unit = BOX_WIDTH
	 */
	public void newScene(int sceneWidth,int sceneHeight)
	{
		this.sceneWidth=sceneWidth;
		this.sceneHeight=sceneHeight;
		mario=null;
		spirits=new LinkedList<Spirit>();
		staticObjs=new StaticObject[sceneWidth][sceneHeight];
		
		int width=Math.min(sceneWidth*BOX_WIDTH, VIEW_WIDTH*BOX_WIDTH);
		int height=Math.min(sceneHeight*BOX_WIDTH, VIEW_HEIGHT*BOX_WIDTH);
		viewPort=new Rect2D(0,0,width,height);
		GameObject.viewPort=viewPort;
		
		width=Math.min(sceneWidth, RENDER_WIDTH);
		height=Math.min(sceneHeight, RENDER_HEIGHT);
		renderPort=new Rect2D(0,0,width,height);
	}
	
	/**
	 * initialize viewPort, renderPort, buffer. 
	 * initialize JFrame and show it in the center screen
	 */
	public void begin()
	{
		moveCamera(mario.getBounds().x-(viewPort.x+viewPort.width/2), 0); //set the location of renderPort automatically
		buffer=new Spirit[sceneWidth][sceneHeight];
		canvas.setBounds(0, 0, viewPort.getWidth(), viewPort.getHeight());
		setVisible(true);
	}
	
	public void addHero(Mario mario)
	{
		this.mario=mario;
		GameManager.instance().addKeyListener(mario);
	}
	
	public void addSpirit(Spirit obj)
	{
		spirits.add(obj);
	}
	
	public void addStatic(StaticObject obj)
	{
		Rect2D p=obj.getBounds();
		int x=p.getCanvasCenterX();
		int y=p.getCanvasCenterY();
		if(0<=x && x<staticObjs.length && 0<=y && y<staticObjs[x].length)
			staticObjs[x][y]=obj;
		else
			System.out.println("addStatic failed: out of range");
	}

//	public void clear()
//	{
//		mario=null;
//		spirits.clear();
//		buffer=null;
//		staticObjs=null;
//	}
	
	/**
	 * move viewPort and fit renderPort with it
	 * @param offsetX
	 * @param offsetY not used yet
	 */
	private void moveCamera(float offsetX,float offsetY)
	{
		float x1=viewPort.x+offsetX;
		x1=Math.max(x1, 0);
		x1=Math.min(x1, sceneWidth*BOX_WIDTH-viewPort.getWidth());
//		float y1=viewPort.y+offsetY;
//		y1=Math.max(y1, 0);
//		y1=Math.min(y1, sceneWidth*BOX_WIDTH-viewPort.getWidth());
//		if(viewPort.getX()==(int)x1 && viewPort.getY()==(int)y1)
		if(viewPort.getX()==(int)x1)
			return;
//		viewPort.setPosition(x1, y1);
		viewPort.setPosition(x1, 0);
		
		int x2=(int)(x1/BOX_WIDTH);
		x2-=(renderPort.getWidth()-viewPort.getWidth()/BOX_WIDTH)>>1;
		x2=Math.max(x2, 0);
		x2=Math.min(x2, sceneWidth-renderPort.getWidth());
//		int y2=(int)y1;
//		y2-=(viewPort.getHeight()/BOX_WIDTH-renderPort.getHeight())>>1;
//		y2=Math.max(y2, 0);
//		y2=Math.min(y2, sceneWidth*BOX_WIDTH-renderPort.getWidth());
//		renderPort.setPosition(x2, y2);
		renderPort.setPosition(x2, 0);
	}
	
	/**
	 * update mario, 
	 * update spirits in renderPort, 
	 * do collision test, 
	 * and repaint
	 */
	public void run()
	{
		while(true)
		{
//			System.out.println(GameManager.instance().getGameStatus());
			try {
				Thread.sleep(TICK);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GameManager game=GameManager.instance();
			if((game.getGameStatus() & GameManager.ABOUT_TO_END) != 0)
			{
				GameManager.instance().removeKeyListener(mario);
				GameManager.instance().acceptEnd();
				while((game.getGameStatus() & GameManager.ABOUT_TO_END) != 0)
				{
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(GameManager.instance().getGameStatus()!=GameManager.RUNNING)
			{
				continue;
			}
			mario.update();
			for(Spirit it : spirits)
			{
				Rect2D bounds=it.getBounds();
				if(renderPort.contains(bounds.getCanvasCenterX(), bounds.getCanvasCenterY()))
				{
					it.update();
				}
			}
			if(!mario.isDie())
				CollisionTest();
			repaint();
		}
	}
	
	//[dx, dy, side]
	private static final int[][] dir= new int[][]{
		{0, 0, 2},{0,-1, 0},{1,-1, 0},{1, 0, 1},{1, 1, 2},{0, 1, 2},{-1, 1, 2},{-1, 0, 3},{-1,-1, 0}
	};
//	private static final int[][] dir= new int[][]{
//		{0, 0, 2},{0,-1, 0},{1, 0, 1},{0, 1, 2},{-1, 0, 3}
//	};
	/**
	 * calculate if mario collids with other spirits and static objects
	 * if mario is close to the viewPort border, drag viewPort and renderPort
	 */
	private void CollisionTest()
	{
		//move the camera
		if(mario.getBounds().x>viewPort.x+2.0/3*viewPort.getWidth() && mario.getVelocity().x>0)
			moveCamera(mario.getVelocity().x, 0);
		if(mario.getBounds().x<viewPort.x+1.0/3*viewPort.getWidth() && mario.getVelocity().x<0)
			moveCamera(mario.getVelocity().x, 0);
		//WARNING: not considering move camera up and down
		
		//test all 8 directions of mario
		int x=mario.getBounds().getCanvasCenterX();
		int y=mario.getBounds().getCanvasCenterY();
		Rect2D collider=mario.getCollider();
		//test static object
		for(int k=0;k<dir.length;k++)
		{
			int xx=x+dir[k][0];
			int yy=y+dir[k][1];
			if(!renderPort.contains(xx,yy))continue;
			StaticObject rhs1=staticObjs[xx][yy];
			if(rhs1!=null)
			{
				if(collider.intersects(rhs1.getCollider()))
				{
					if(rhs1 instanceof Brick)
						keepOut(mario,rhs1,dir[k][2]);
					mario.onCollide(rhs1, dir[k][2]);
				}
			}
		}
		//map spirits to its location in buffer
		for(Spirit it : spirits)
		{
			if(!it.getVisibility())continue;
			Rect2D bounds=it.getBounds();
			int xx=bounds.getCanvasCenterX();
			int yy=bounds.getCanvasCenterY();
			if(!renderPort.contains(xx, yy))continue;
			buffer[xx][yy]=it;
		}
		//test spirits
		for(int k=0;k<dir.length;k++)
		{
			int xx=x+dir[k][0];
			int yy=y+dir[k][1];
			if(!renderPort.contains(xx,yy))continue;
			Spirit rhs2=buffer[xx][yy];
			if(rhs2!=null)
			{
				if(collider.intersects(rhs2.getCollider()))
				{
					mario.onCollide(rhs2, dir[k][2]);
				}
			}
		}
		//clear the buffer
		for(Spirit it : spirits)
		{
			Rect2D bounds=it.getBounds();
			int xx=bounds.getCanvasCenterX();
			int yy=bounds.getCanvasCenterY();
			if(0<=xx && xx<buffer.length && 0<=y && yy<buffer[xx].length)
				buffer[xx][yy]=it;
		}
	}
	
	/**
	 * adjust location of lhs to keep it out of arg
	 * @param lhs
	 * @param arg
	 * @param side
	 */
	private void keepOut(Spirit lhs,GameObject arg,int side)
	{
		if(side==1)
		{
			lhs.getBounds().x=arg.getBounds().getX()-lhs.getBounds().getWidth();
		}
		else if(side==3)
		{
			lhs.getBounds().x=arg.getBounds().getX()+arg.getBounds().getWidth();
		}
		else if(side==0)
		{
			lhs.getBounds().y=arg.bounds.y+arg.bounds.getHeight();
		}
		else
		{
			lhs.getBounds().y=arg.bounds.y-lhs.getBounds().getHeight();
		}
	}
	
	/**
	 * repaint in the image buffer
	 */
	private Image imageBuffer;
	private Graphics gBuffer;
	private void paintImageBuffer()
	{
		if(GameManager.instance().getGameStatus()==GameManager.END) 
		{
			return;
		}
		if(imageBuffer==null)
		{
			imageBuffer=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_INT_RGB);
			gBuffer=imageBuffer.getGraphics();
		}
		gBuffer.setColor(getBackground());  
		gBuffer.fillRect(0,0,this.getSize().width,this.getSize().height);
//		showRenderPort();
        for(int x=renderPort.getX();x<renderPort.getMaxX();x++)
        {
			for(int y=renderPort.getY();y<renderPort.getMaxY();y++)
			{
				GameObject obj2=staticObjs[x][y];
				if(obj2!=null && obj2.getVisibility())
				{
					obj2.paint(gBuffer);
				}
			}
		}
		for(Spirit it : spirits)
		{
			if(!it.getVisibility())continue;
			it.paint(gBuffer);
		}
		mario.paint(gBuffer);
	}
	
	/**
	 * swap image buffer and this.graphics
	 */
	@Override
	public void paint(Graphics g) 
	{
		if(GameManager.instance().getGameStatus()==GameManager.END) 
		{
			return;
		}
		super.paint(g);
    	paintImageBuffer();
//    	showCollider();
    	g.drawImage(imageBuffer,0,0,null);
	}

//	@Override
//	public void actionPerformed(ActionEvent e) {
//		update();
//	}
	
	private void showCollider()
	{
		if(GameManager.instance().getGameStatus()==GameManager.END) 
		{
			return;
		}
		gBuffer.setColor(Color.RED);
		Rect2D bounds=mario.getCollider();
		gBuffer.drawRect(bounds.getX()-viewPort.getX(), bounds.getY()-viewPort.getY(), bounds.getWidth(), bounds.getHeight());
        for(int x=renderPort.getX();x<renderPort.getMaxX();x++)
        {
			for(int y=renderPort.getY();y<renderPort.getMaxY();y++)
			{
				GameObject obj2=staticObjs[x][y];
				if(obj2!=null && obj2.getVisibility())
				{
					bounds=((Collider2D)obj2).getCollider();
					gBuffer.drawRect(bounds.getX()-viewPort.getX(), bounds.getY()-viewPort.getY(), bounds.getWidth(), bounds.getHeight());
				}
			}
		}
		for(Spirit it : spirits)
		{
			if(!it.getVisibility())continue;
			bounds=it.getCollider();
			gBuffer.drawRect(bounds.getX()-viewPort.getX(), bounds.getY()-viewPort.getY(), bounds.getWidth(), bounds.getHeight());
		}
		mario.paint(gBuffer);
	}
	
	private void showRenderPort()
	{
		System.out.println("renderPort: "+renderPort);
		for(int y=renderPort.getY();y<renderPort.getMaxY();y++)
        {
			for(int x=renderPort.getX();x<renderPort.getMaxX();x++)
			{
				GameObject obj2=staticObjs[x][y];
				if(obj2==null)
					System.out.print(' ');
				else if(obj2.getTag()==GameObject.TAG_BRICK)
					System.out.print('#');
//				else if(obj2.getTag()==GameObject.TAG_MARIO)
//					System.out.print('M');
				else if(obj2.getTag()==GameObject.TAG_TRAP)
					System.out.print('X');
				else if(obj2.getTag()==GameObject.TAG_TRAP)
					System.out.print('S');
			}
			System.out.println();
		}
		System.out.println();
	}
	
}