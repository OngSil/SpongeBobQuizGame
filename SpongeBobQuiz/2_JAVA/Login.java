package kopia.kp;
import java.awt.*;
import java.awt.Font;//
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;//
import javax.swing.BorderFactory;//
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;//
import javax.swing.text.*;//
import java.awt.image.BufferedImage;
import java.io.*;
import javax.sound.sampled.*; //
//import javafx.scene.media.*; //

public class Login extends JFrame implements ActionListener{
	Font font1 = new Font("Tahoma Bold", Font.PLAIN, 20);
	Container cp;
	JPanel bigJP;
	JButton loginButton;
	JLabel labelID, labelIP;
	JTextField idText, ipText;
	JButton loginB;
	ImageIcon titleImg = new ImageIcon("Images//코난.png");

	ImageIcon backGround = new ImageIcon("Images//Conan Black Tissue.jpg");
	Image im = backGround.getImage();

	public static String ip, nickName, hiMsg;

	void init(){
		
		cp = getContentPane();
		bigJP = new JPanel();
		
		
		//큰 패널 넣기, 여백과 함께 (위치설정)
		cp.add(bigJP, BorderLayout.CENTER);
		bigJP.setBorder(BorderFactory.createEmptyBorder(385,50,0,0));
		bigJP.setOpaque(false);
		
		//로긴 버튼 넣기
		loginB = new JButton(new ImageIcon("Images//loginButton.png"));
		loginB.setOpaque(true);
		loginB.setSize(10,5);
		loginB.setBorder(new LineBorder(Color.BLACK, 0 ,false));
		loginB.setBackground(new Color(0,156,255));
		loginB.setOpaque(false);
		loginB.addActionListener(this);

		//id창 만들기
		labelID = new JLabel("ID");
		labelID.setFont(font1);
		labelID.setForeground(Color.WHITE);
		bigJP.add(labelID);

		idText = new JTextField();
		idText.setText("");
		idText.setColumns(15);

		bigJP.add(idText);
		
		//ip창 만들기
		labelIP = new JLabel("IP");
		labelIP.setFont(font1);
		labelIP.setForeground(Color.WHITE);
		bigJP.add(labelIP);

		ipText = new JTextField();
		ipText.setText("127.0.0.1");
		ipText.setColumns(15);
		ipText.setBounds(20, 10, 20, 20);
		bigJP.add(ipText);
		bigJP.add(loginB);
		setUi();
		
	}

	//패널 배경넣기
	class PanelPaint extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(im,0,0,getWidth(),getHeight(),this);
		}
	}

	//창 구현
	void setUi(){
		setBounds(200,100,804,870); //창 뜨는위치, 창 크기
		setTitle("코난마피아게임 로그인");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PanelPaint panelP = new PanelPaint();
		add(panelP);
		
	}

	public void actionPerformed(ActionEvent e){
		//ImageIcon iconImg1 = new ImageIcon("Images//icon1.png");
		ImageIcon iconImg2 = new ImageIcon("Images//icon2.png");
		Object obj = e.getSource();
		idText.requestFocus();
		if(obj == loginB){
			playSound("로그인 버튼소리.wav");
			if(idText.getText().equals("") || ipText.getText().equals("")){
				JOptionPane.showMessageDialog(null,"로그인 정보를 제대로 입력해주세요.", "Login Error", JOptionPane.QUESTION_MESSAGE, iconImg2); 
			}else if(idText.getText().trim().length()>6){
				JOptionPane.showMessageDialog(null,"ID는 최대 6자까지 입력해주세요.", "IP Error", JOptionPane.QUESTION_MESSAGE, iconImg2);
				idText.setText("");
				idText.requestFocus();
			}else{
				nickName = idText.getText().trim();
				String ipTemp = ipText.getText();
				if(ipTemp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){
				ip =ipTemp;
				playSound("로그인 버튼소리.wav");
				
				JOptionPane.showMessageDialog(null, "로그인 성공!", "LOGIN", JOptionPane.INFORMATION_MESSAGE, iconImg2);
				//hiMsg = JOptionPane.showInputDialog(null,"간단한 자기소개 말을 적으세욤.","자기소개", JOptionPane.PLAIN_MESSAGE);
				loginB.setEnabled(false);
				idText.setEnabled(false);
				ipText.setEnabled(false);
				setVisible(false);
				/*
				cp.remove(0);
				bigJP.add();

				cp.add(bigJP2
				cp.revalidate();
				cp.repaint();*/
				

				//메인GUI소환 또는 메인생성자 소환! 
				MainGameUI mgUI = new MainGameUI();
			}else{
				 JOptionPane.showMessageDialog(null, "IP 주소를 정확하게 입력하라옹! ", "ERROR!", JOptionPane.WARNING_MESSAGE, iconImg2);
			}
			}

		}
	}

	
	static void playSound(String filename){ 
		File file = new File("./waves/" + filename);
		if(file.exists()){ 
			try{
				AudioInputStream stream = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(stream);
				clip.start();
			}catch(Exception e){
				//e.printStackTrace();
				System.out.println("배경, 효과음을 듣고 싶으면 오디오를 연결 해주세요");
			}
		}else{ 
			System.out.println("File Not Found!");
		}
	}	




	public static void main(String[] args) 
	{
		new Login().init();
		playSound("로그인 배경소리.wav");
	}
}
