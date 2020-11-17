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
	ImageIcon titleImg = new ImageIcon("Images//�ڳ�.png");

	ImageIcon backGround = new ImageIcon("Images//Conan Black Tissue.jpg");
	Image im = backGround.getImage();

	public static String ip, nickName, hiMsg;

	void init(){
		
		cp = getContentPane();
		bigJP = new JPanel();
		
		
		//ū �г� �ֱ�, ����� �Բ� (��ġ����)
		cp.add(bigJP, BorderLayout.CENTER);
		bigJP.setBorder(BorderFactory.createEmptyBorder(385,50,0,0));
		bigJP.setOpaque(false);
		
		//�α� ��ư �ֱ�
		loginB = new JButton(new ImageIcon("Images//loginButton.png"));
		loginB.setOpaque(true);
		loginB.setSize(10,5);
		loginB.setBorder(new LineBorder(Color.BLACK, 0 ,false));
		loginB.setBackground(new Color(0,156,255));
		loginB.setOpaque(false);
		loginB.addActionListener(this);

		//idâ �����
		labelID = new JLabel("ID");
		labelID.setFont(font1);
		labelID.setForeground(Color.WHITE);
		bigJP.add(labelID);

		idText = new JTextField();
		idText.setText("");
		idText.setColumns(15);

		bigJP.add(idText);
		
		//ipâ �����
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

	//�г� ���ֱ�
	class PanelPaint extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(im,0,0,getWidth(),getHeight(),this);
		}
	}

	//â ����
	void setUi(){
		setBounds(200,100,804,870); //â �ߴ���ġ, â ũ��
		setTitle("�ڳ����Ǿư��� �α���");
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
			playSound("�α��� ��ư�Ҹ�.wav");
			if(idText.getText().equals("") || ipText.getText().equals("")){
				JOptionPane.showMessageDialog(null,"�α��� ������ ����� �Է����ּ���.", "Login Error", JOptionPane.QUESTION_MESSAGE, iconImg2); 
			}else if(idText.getText().trim().length()>6){
				JOptionPane.showMessageDialog(null,"ID�� �ִ� 6�ڱ��� �Է����ּ���.", "IP Error", JOptionPane.QUESTION_MESSAGE, iconImg2);
				idText.setText("");
				idText.requestFocus();
			}else{
				nickName = idText.getText().trim();
				String ipTemp = ipText.getText();
				if(ipTemp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")){
				ip =ipTemp;
				playSound("�α��� ��ư�Ҹ�.wav");
				
				JOptionPane.showMessageDialog(null, "�α��� ����!", "LOGIN", JOptionPane.INFORMATION_MESSAGE, iconImg2);
				//hiMsg = JOptionPane.showInputDialog(null,"������ �ڱ�Ұ� ���� ��������.","�ڱ�Ұ�", JOptionPane.PLAIN_MESSAGE);
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
				

				//����GUI��ȯ �Ǵ� ���λ����� ��ȯ! 
				MainGameUI mgUI = new MainGameUI();
			}else{
				 JOptionPane.showMessageDialog(null, "IP �ּҸ� ��Ȯ�ϰ� �Է��϶��! ", "ERROR!", JOptionPane.WARNING_MESSAGE, iconImg2);
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
				System.out.println("���, ȿ������ ��� ������ ������� ���� ���ּ���");
			}
		}else{ 
			System.out.println("File Not Found!");
		}
	}	




	public static void main(String[] args) 
	{
		new Login().init();
		playSound("�α��� ���Ҹ�.wav");
	}
}
