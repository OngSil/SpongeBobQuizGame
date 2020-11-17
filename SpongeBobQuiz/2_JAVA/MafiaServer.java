package kopia.kp;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.border.*;
import java.net.*;
import java.awt.*;
import java.util.*;

public class  MafiaServer extends JFrame implements ActionListener
{

	ArrayList<String> listAlive = new ArrayList<String>();
	ServerSocket ss;
	int port = 3333;
	JPanel contentPane, mainPanel, textAreaPanel, buttonPanel;
	JScrollPane scrollPane;
	JTextArea textArea;
	JLabel serverStatusLabel, serverLabel1, serverLabel2;
	JButton serverStartButton, serverCloseButton;
	ImageIcon titleImg = new ImageIcon("Images//코난.png");
	ImageIcon gun1,gun2;
	Socket s;
	Font font = new Font("궁서", Font.PLAIN, 20);
	Vector<OneClientModule> clientList = new Vector<OneClientModule>();
	LinkedHashMap<String, String> clientInfo = new LinkedHashMap<String, String>();
	OneClientModule ocm;

	int mafiaV=0;
	int policeV=0;
	int citizen1V=0;
	int citizen2V=0;
	int citizen3V=0;
	int citizen4V=0;
	
	public static final int MAX_CLIENT = 6;
	boolean gameStart=true;

	int readyPlayer =0;

	StopWatch tm = new StopWatch();
	
	MafiaServer(){
		init();
		setUI();
	}

	void init(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0,1,10,0));
		
		mainPanel = new JPanel();
		contentPane.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		serverStatusLabel = new JLabel("        「 냥피아 서버 상태 」        ");
		serverStatusLabel.setFont(font);
		serverStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//serverStatusLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		//serverStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(serverStatusLabel);
		
		gun1 = new ImageIcon("images//TEST.JPG");
		serverLabel1 = new JLabel(gun1);
		serverLabel1.setBounds(30,1,40,30);
		serverStatusLabel.add(serverLabel1);

		gun2 = new ImageIcon("images//TEST2.jpg");
		serverLabel2 = new JLabel(gun2);
		serverLabel2.setBounds(250,1,40,30);
		serverStatusLabel.add(serverLabel2);

		textAreaPanel = new JPanel();
		mainPanel.add(textAreaPanel);
		textAreaPanel.setLayout(new BorderLayout());

		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.black));
		textAreaPanel.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		buttonPanel = new JPanel();
		buttonPanel.setAutoscrolls(true);
		mainPanel.add(buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		serverStartButton = new JButton("서버 시작");
		serverStartButton.setFocusPainted(false);
		serverStartButton.setFont(font);
		buttonPanel.add(serverStartButton);
		serverStartButton.addActionListener(this);

		serverCloseButton = new JButton("서버 종료");
		serverCloseButton.setFocusPainted(false);
		serverCloseButton.setFont(font);
		buttonPanel.add(serverCloseButton);
		serverCloseButton.setEnabled(false);
		serverCloseButton.addActionListener(this);
	}
	
	void setUI(){
		setTitle("냥피아 게임 서버");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,350,400);
		setLocationRelativeTo(null);		//윈도우 가운데 배치
		
	}
	
	void createServer(){
			try{
				ss = new ServerSocket(port);
				serverStatusLabel.setText("          서버 가동중         ");
				textArea.append(" 서버가 가동중입니다. " +"\n");
				serverStartButton.setEnabled(false);
				serverCloseButton.setEnabled(true);
				while(true){
					s = ss.accept();
					ocm = new OneClientModule(this);
					clientList.add(ocm);
					pln("여기냐?");
					if((clientList.size()  ) > MAX_CLIENT) s.close();
					else{
						ocm.start();
					}
				}
			}catch(IOException ie){}
			}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == serverStartButton){
			new Thread(){
				public void run(){
					pln("여기냐?");
					createServer();
					pln("여기냐?");
				}
			}.start();
		}else if(e.getSource() == serverCloseButton){
			ImageIcon tempimg = new ImageIcon("Images//icon2.png");
			int select = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "마피아 서버", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, tempimg);
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					ocm.showSystemMsg("//EndServer");
					serverStatusLabel.setText("          서버 종료        ");
					textArea.append(" 서버가 종료되었습니다. "+ "\n");
					serverStartButton.setEnabled(true);
					serverCloseButton.setEnabled(false);
				}
			}catch(IOException ie){}
		}
	}
	//내부클래스 시간 제어
	class StopWatch extends Thread{
		int n=1;

			
		public void run(){
			while(gameStart){
				startDay();
				if(gameStart){
					startNight();
				}
			}
		}

		public void startDay(){
			long preTime = System.currentTimeMillis();
			//int n=1;
			ocm.showSystemMsg("//Day");
			ocm.showSystemMsg("자 !" + n+"번째 날이 밝았습니다.");
			try{
				while(gameStart){
					sleep(1000);
					if(n==1){
						long time = System.currentTimeMillis() - preTime;
						ocm.showSystemMsg("//Timer" + (toFirstTime(time)));
						ocm.showSystemMsg("//FirstDay");
						if(toFirstTime(time).equals("00 : 00")){
							break;
						}

					}else{
						long time = System.currentTimeMillis() - preTime;
						ocm.showSystemMsg("//Timer" +(toTime(time)));
						if(toTime(time).equals("00 : 50")){
							ocm.showSystemMsg("//Vote");
						}else if(toTime(time).equals("00 : 10")){
							ocm.showSystemMsg("//VoteFinished");
							ocm.getOut();
							ocm.resetVote();
						}else if(toTime(time).equals("00 : 00")){
							break;
						}
					}
				}
			}catch(Exception e){}
		}
		String toFirstTime(long time){
			int m = (int)(0-(time / 1000.0 / 60.0));
			int s = (int)(30-(time % (1000.0 * 60) / 1000.0));
			return String.format("%02d : %02d", m, s);
		}
		//낮, 2분
		String toTime(long time){
			int m = (int)(2-(time / 1000.0 / 60.0));
			int s = (int)(60-(time % (1000.0 * 60) / 1000.0));
			return String.format("%02d : %02d", m, s);
		}

		public void startNight(){
			long preTime = System.currentTimeMillis();
			ocm.showSystemMsg("//Night");
			ocm.showSystemMsg("자 !" +n+"번째 밤이 왔습니다.");
			ocm.showSystemMsg("//DeleteVote");
			ocm.resetVote();
			try{
				while(gameStart){
					sleep(1000);
					long time = System.currentTimeMillis()-preTime;
					ocm.showSystemMsg("//Timer"+(toTimeNight(time)));
					if(toTimeNight(time).equals("00 : 00")){
						n++;
						break;
					}
				}
			}catch(Exception e){}
		}
		//밤, 30s
		String toTimeNight(long time){
			int m = 0;
			int s = (int)(30-(time % (1000.0 * 30) / 1000.0));
			return String.format("%02d : %02d", m, s);
		}
	}


	void pln(String str){
		System.out.println(str);
	}
	public static void main(String args[]){
		new MafiaServer();
	}
}

//port 3333