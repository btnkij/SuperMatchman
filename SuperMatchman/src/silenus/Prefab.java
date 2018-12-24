package silenus;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Prefab
{
	public static final Prefab MARIO=new Prefab(16,20,"mario");
	public static final Prefab BRICK=new Prefab(20,20,"brick");
	public static final Prefab SPIKE=new Prefab(20,20,"spike");
	public static final Prefab STAR=new Prefab(20,20,"star");
	public static final Prefab GEAR=new Prefab(20,20,"gear");

	public Image[] img=null;
	public int width=0,height=0;
	
	public static Image loadImage(String imageName,int width,int height)
	{
		try {
			return ImageIO.read(new File("assets/"+imageName+".png"))
					.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param imageName file name without extend name, can only be in PNG format. A sequence of image must name in order from 0
	 */
	public Prefab(int width,int height,String imageName)
	{
		this.width=width;
		this.height=height;
		int nFrame=0;
		while(true)
		{
			File file=new File("assets/"+imageName+nFrame+".png");
			if(!file.exists())break;
			nFrame++;
		}
		img=new Image[nFrame];
		for(int i=0;i<nFrame;i++)
		{
			img[i]=loadImage(imageName+i,width,height);
		}	
	}
}