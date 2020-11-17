import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

class QMain extends JFrame implements ActionListener {
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
			// 빈칸으로 접속 못하게 방지
			if(ia1.getText().equals("") || ib1.getText().equals("")){
				JOptionPane.showMessageDialog(null, "똑바로 적으셈!!!");
			}else if(ib1.getText().trim().length()>7){ //아이디 글자 수 제한
				JOptionPane.showMessageDialog(null, "ID는 7까지만 가능");
				ib1.setText("");
				ib1.requestFocus();
			}else{
				nickName = ib1.getText().trim();
				String temp = ia1.getText();
				if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){
					ip = temp;

					System.out.println("ip: "+ip);
					System.out.println("ID: "+nickName);
					System.out.println("접속 성공!");
					start.setEnabled(false);
					ia1.setEnabled(false);
					ib1.setEnabled(false);

					//다음 페이지로 이동
					setVisible(false);
					QPlay qp = new QPlay(this);
				} else {
					JOptionPane.showMessageDialog(null, "공백없이 IP입력ㄱㄱ");
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new QMain().init();
	}
}
