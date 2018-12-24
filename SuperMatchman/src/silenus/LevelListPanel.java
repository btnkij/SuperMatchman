package silenus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LevelListPanel extends JPanel
{
	private static final long serialVersionUID = -8598612961890533166L;

	private FlowLayout layout=null;
	
	public LevelListPanel()
	{
		setSize(815,538);
		layout=new FlowLayout();
		layout.setAlignment(FlowLayout.LEFT);
		setLayout(layout);
		LinkedList<String> list=SceneLoader.getLevelList();
		for(String name : list)
		{
			JButton btn=new JButton();
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					GameManager game=GameManager.instance();
					game.setLevelName(btn.getText());
					game.begin();
				}
			});
			btn.setPreferredSize(new Dimension(100,100));
			btn.setText(name);
			btn.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
			btn.setForeground(Color.WHITE);
			btn.setBackground(new Color(0x6F,0x8F,0xEF));
			btn.setHorizontalTextPosition(SwingConstants.CENTER);
			
			add(btn);
		}
	}
//	
//	@Override
//	public void mouseClicked(MouseEvent e) 
//	{
//		Component sender=this.getComponentAt(e.getPoint());
//		System.out.println(sender);
//		if(sender instanceof JLabel)
//		{
//			JLabel label=(JLabel)sender;
//			GameManager game=GameManager.instance();
//			game.setLevelName(label.getText());
//			game.begin();
//		}
//	}
//
//	@Override
//	public void mousePressed(MouseEvent e)
//	{
//		
//	}
//
//	@Override
//	public void mouseReleased(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mouseExited(MouseEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//	
}
