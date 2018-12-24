package silenus;

import java.awt.CardLayout;
import javax.swing.*;

public class GameManager extends JFrame
{
	private static final long serialVersionUID = 5829724297927759470L;
	
	private static GameManager game=null;
	public static GameManager instance()
	{
		if(game==null)game=new GameManager();
		return game;
	}
	
	public int gameStatus=END;
	public static final int WAITING=1;
	public static final int RUNNING=2;
	public static final int ABOUT_TO_END=4;
	public static final int WIN=5;
	public static final int FAIL=6;
	public static final int END=8;
	public int getGameStatus()
	{
		return gameStatus;
	}
	
	private CardLayout layout=new CardLayout();
	private Canvas canvas=null;
	private OptionDialog optionDialog=null;
	private LevelListPanel levelListPanel=null;
	private Thread thread=null;
	
	private String levelName=null;
	public String getLevelName() { return levelName; }
	public void setLevelName(String value) { levelName=value; }
	
	public static void main(String[] args)
	{
		GameManager game=GameManager.instance();
		game.init();
	}
	
	private GameManager() 
	{
		this.setSize(815,538);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.setLayout(layout);
		canvas=Canvas.instance();
		this.add(canvas, "canvas");
		optionDialog=new OptionDialog();
		levelListPanel=new LevelListPanel();
		this.add(levelListPanel, "levelListPanel");
		
		gameStatus=END;
	}
	
	public void init()
	{
		layout.show(this.getContentPane(), "levelListPanel");
		this.setVisible(true);
	}
	
	public void begin()
	{
		layout.show(this.getContentPane(), "canvas");
		SceneLoader.loadLevel(levelName);
		canvas.begin();
		this.requestFocus();
		if(thread==null)
		{
			thread=new Thread(canvas);
			thread.start();
		}
		gameStatus=RUNNING;
	}
	
	public void win()
	{
		gameStatus=WIN;
	}
	
	public void fail()
	{
		gameStatus=FAIL;
	}
	
	public void pause()
	{
		gameStatus=WAITING;
		if(optionDialog.isVisible())return;
		int result=optionDialog.showDialog(this, "Paused", 
				OptionDialog.RESTART|OptionDialog.MAINMENU|OptionDialog.CLOSE);
		if(result==OptionDialog.RESTART) // restart
		{
			begin();
		}
		else if(result==OptionDialog.MAINMENU) // main menu
		{
			gameStatus=END;
			layout.show(this.getContentPane(), "levelListPanel");
		}
		else if(result==OptionDialog.CLOSE) // close
		{
			gameStatus=RUNNING;
		}
	}
	
	public void acceptEnd()
	{
		String msg=null;
		int showButton=OptionDialog.RESTART | OptionDialog.MAINMENU;
		switch(gameStatus)
		{
		case WIN: msg="You win."; break;
		case FAIL: msg="You lose."; break;
		}
		gameStatus=ABOUT_TO_END;
		int result=optionDialog.showDialog(this, msg, showButton);
		if(result==OptionDialog.RESTART) // restart
		{
			begin();
		}
		else if(result==OptionDialog.MAINMENU) // main menu
		{
			gameStatus=END;
			layout.show(this.getContentPane(), "levelListPanel");
		}
//		else if(result==OptionDialog.CLOSE) // close
//		{
//			gameStatus=RUNNING;
//			layout.show(this.getContentPane(), "levelListPanel");
//		}
	}
}
