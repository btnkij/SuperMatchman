package silenus;

import java.io.File; 
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException; 

public class SceneLoader 
{
	public static LinkedList<String> getLevelList()
	{
		LinkedList<String> list=new LinkedList<String>();
		Element xroot=getXRoot();
		NodeList xLevelList=xroot.getChildNodes();
		Element xlevel=null;
		for(int i=0;i<xLevelList.getLength();i++)
		{
			Node xnode=xLevelList.item(i);
			if(xnode.getNodeType()!=Node.ELEMENT_NODE)continue;
			xlevel=(Element)xnode;
			String name=xlevel.getAttribute("name");
			if(name!=null)list.add(name);
		}
		return list;
	}
	
	public static void loadLevel(String levelName)
	{   
		Element xroot=getXRoot();
		NodeList xLevelList=xroot.getChildNodes();
		Element xlevel=null;
		for(int i=0;i<xLevelList.getLength();i++)
		{
			Node xnode=xLevelList.item(i);
			if(xnode.getNodeType()==Node.ELEMENT_NODE)
			{
				xlevel=(Element)xnode;
				String name=xlevel.getAttribute("name");
				if(name!=null && name.equals(levelName))break;
			}
		}
		
		Canvas canvas=Canvas.instance();
		Element xscene=(Element)xlevel.getElementsByTagName("scene").item(0);
		int width=Integer.parseInt(xscene.getAttribute("width"));
		int height=Integer.parseInt(xscene.getAttribute("height"));
		canvas.newScene(width, height);
		
		NodeList xRowList=xscene.getChildNodes();
		int curY=0;
		for(int i=0;i<xRowList.getLength();i++)
		{
			Node xnode=xRowList.item(i);
			if(xnode.getNodeType()!=Node.ELEMENT_NODE)continue;
			String row=xnode.getTextContent();
			parseRow(row,curY);
			curY++;
		}
		
		Element xobjects=(Element)xlevel.getElementsByTagName("objects").item(0);
		NodeList xObjectList=xobjects.getChildNodes();
		for(int i=0;i<xObjectList.getLength();i++)
		{
			Node xnode=xObjectList.item(i);
			if(xnode.getNodeType()!=Node.ELEMENT_NODE)continue;
			GameObject obj=parseObject((Element)xnode);
			addObject(obj);
		}
	}
	
	private static Element getXRoot()
	{
		Document xdoc = null; 
		try 
	    { 
	    	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder builder = builderFactory.newDocumentBuilder(); 
	        xdoc = builder.parse(new File("level/level.xml")); 
	    } 
	    catch (ParserConfigurationException e) 
	    { 
	    	e.printStackTrace();  
	    } 
	    catch (SAXException e)
	    { 
	        e.printStackTrace(); 
	    } 
	    catch (IOException e)
	    { 
	        e.printStackTrace(); 
	    }
		return xdoc.getDocumentElement();
	}
	
	private static void parseRow(String row,int y)
	{
		for(int x=0;x<row.length();x++)
		{
			GameObject obj = null;
			char ch=row.charAt(x);
			if(ch==' ')continue;
			switch(ch)
			{
			case '#':
				obj=new Brick();
				break;
			case 'M':
				obj=new Mario();
				break;
			case '^':
				obj=new Spike(0);
				break;
			case '>':
				obj=new Spike(1);
				break;
			case 'v':
				obj=new Spike(2);
				break;
			case '<':
				obj=new Spike(3);
				break;
			case 'S':
				obj=new Star();
				break;
			default:
				// complex objects are described in node "object"
				continue;
			}
//			System.out.printf("%d %d\n", x,y);
			obj.getBounds().setPosition(x*Canvas.BOX_WIDTH, y*Canvas.BOX_WIDTH);
			addObject(obj);
		}
	}
	
	private static GameObject parseObject(Element xe)
	{
		GameObject obj=null;
		String type=xe.getAttribute("type");
		switch(type)
		{
		case "gear":
			Gear gear=new Gear();
			int x1=Integer.parseInt(xe.getAttribute("x1"));
			int y1=Integer.parseInt(xe.getAttribute("y1"));
			int x2=Integer.parseInt(xe.getAttribute("x2"));
			int y2=Integer.parseInt(xe.getAttribute("y2"));
			gear.setRoute(x1, y1, x2, y2);
			float v=Float.parseFloat(xe.getAttribute("v"));
			gear.getVelocity().x=x2-x1;
			gear.getVelocity().y=y2-y1;
			gear.getVelocity().normalize();
			gear.getVelocity().multiply(v);
			obj=gear;
			break;
		default:
			System.out.println("SceneLoader: unknown type \""+type+"\"");
			return null;
		}
		int x=Integer.parseInt(xe.getAttribute("x"));
		int y=Integer.parseInt(xe.getAttribute("y"));
		Canvas.instance();
		obj.getBounds().setPosition(x*Canvas.BOX_WIDTH, y*Canvas.BOX_WIDTH);
		return obj;
	}
	
	private static void addObject(GameObject obj)
	{
		if(obj instanceof StaticObject)
			Canvas.instance().addStatic((StaticObject)obj);
		else if(obj instanceof Mario)
			Canvas.instance().addHero((Mario)obj);
		else if(obj instanceof Spirit)
			Canvas.instance().addSpirit((Spirit)obj);
	}
}
