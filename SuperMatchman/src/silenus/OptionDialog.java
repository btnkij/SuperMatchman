package silenus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class OptionDialog extends JDialog
{
	private static final long serialVersionUID = 359396490813235675L;
	
	public static final int RESTART=1;
	public static final int MAINMENU=2;
	public static final int CLOSE=4;
	
	private RoundButton btnRestart=null;
	private RoundButton btnMainMenu=null;
	private RoundButton btnClose=null;
	private JLabel lbMsg=null;
	BorderLayout layout=null;
//	FlowLayout flowLayout=null;
	private int result=0;

	public OptionDialog()
	{
		this.setSize(400,200);
		this.setUndecorated(true);
		this.setModal(true);
		
		JPanel background=new JPanel() {
			private static final long serialVersionUID = -1944811921093414285L;
			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.setColor(new Color(0xB6,0xED,0xED));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		background.setLayout(null);
		
		lbMsg=new JLabel();
		lbMsg.setFont(new Font("Comic Sans MS",Font.PLAIN,40));
		lbMsg.setForeground(Color.BLACK);
		lbMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lbMsg.setVerticalAlignment(SwingConstants.CENTER);
		lbMsg.setBounds(0,0,400,80);
		background.add(lbMsg);
		
		btnRestart=new RoundButton(Prefab.loadImage("restart", 100, 100));
		btnRestart.setBounds(50, 80, 100, 100);
		btnRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result=RESTART;
				setVisible(false);
			}
		});
		background.add(btnRestart);
		
		btnMainMenu=new RoundButton(Prefab.loadImage("menu", 100, 100));
		btnMainMenu.setBounds(150, 80, 100, 100);
		btnMainMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result=MAINMENU;
				setVisible(false);
			}
		});
		background.add(btnMainMenu,BorderLayout.CENTER);
		
		btnClose=new RoundButton(Prefab.loadImage("close", 100, 100));
		btnClose.setBounds(250, 80, 100, 100);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				result=CLOSE;
				setVisible(false);
			}
		});
		background.add(btnClose);
		
		this.setContentPane(background);
	}
	
	/**
	 * show dialog in the center of parent
	 * @param parent
	 * @param msg message to show
	 * @param option a bit vector decide which button to show
	 * @return return 1 if btnRestart clicked, 2 if btnClose clicked, otherwise 0
	 */
	public int showDialog(JFrame parent, String msg, int option)
	{
		this.setLocationRelativeTo(parent);
		lbMsg.setText(msg);
		btnRestart.setVisible((option & RESTART) != 0);
		btnMainMenu.setVisible((option & MAINMENU) != 0);
		btnClose.setVisible((option & CLOSE) != 0);
		result=0;
		setVisible(true);
		return result;
	}
}