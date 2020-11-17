import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;


class Main_new extends JFrame implements ActionListener
{
	Container cp;
	JPanel ip_pan, id_pan;
	JLabel ba, ia, ib;
	JButton start;
	JTextField ia1, ib1;
	ImageIcon i = new ImageIcon("imgs/(this)main_back.png");
	ImageIcon ii = new ImageIcon("imgs/(this)game_start2.png");

	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	public static String ip, nickName;

	Color W = new Color(255, 255, 255);

	void init(){
	
		cp = getContentPane();
		
		JLabel ba = new JLabel(i);
		
		ba.setBounds(0, 0, 970, 596); //770,500
		ba.setOpaque(true);

		
		ia1 = new JTextField("127.0.0.1", 40);
		ib1 = new JTextField(40);
		start = new JButton(ii);
		start.setBorderPainted(false);
		start.setContentAreaFilled(false);


		ia1.setBounds(380, 215, 170, 30);
		ib1.setBounds(380, 250, 170, 30);
		start.setBounds(390, 290, 150, 60);

		ia1.setBackground(Color.white);
		ib1.setBackground(Color.white);
		//start.setFont(new Font("맑은고딕", Font.BOLD, 12));

		ia1.setOpaque(true);
		ib1.setOpaque(true);
		start.setOpaque(false);

		ba.add(ia1); ba.add(ib1); ba.add(start);
		start.addActionListener(this);
		ia1.addActionListener(this);
		ib1.addActionListener(this);

	
		cp.add(ba);

		setUI();

	}
	void setUI(){
		setTitle("SPONGEBOB GAME");
		setSize(970, 596);
		setVisible(true);
		setLocation(300, 300);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void actionPerformed(ActionEvent e){
		
		Object obj = e.getSource();

		if(obj == start){
			  if (ib1.getText().equals("") || ia1.getText().equals("")){         
				System.out.println("IP 뜩브르 즉으르...");
         }else if (ib1.getText().trim().length()>10){
            System.out.println("ID 뜩브르 즉으르...");
            ib1.setText("");
			ib1.requestFocus();
         }else{
            nickName = ib1.getText().trim();
            String temp = ia1.getText();
            if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){
               ip = temp;
			  
               System.out.println("IP: " +ip);
			   System.out.println("ID: " +nickName);
			   System.out.println("성공~");
               start.setEnabled(false);
               ib1.setEnabled(false);
               ia1.setEnabled(false);
              setVisible(false);  
			  ClientPlay p = new ClientPlay();
            }else{
               System.out.println("공백없이 IP입력해주세요");
			   return;  
            }
          
	   }
	}
}
			

	public static void main(String[] args) 
	{
		new Main_new().init();
	}
}
