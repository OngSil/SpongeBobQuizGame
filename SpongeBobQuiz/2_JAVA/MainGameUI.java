package kopia.kp;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.border.*;


public class  MainGameUI extends JFrame
{
	Container cp;
	JPanel contentPane, mainPanel, chatPanel, panel_ListClient, panel_Top;
	JButton sendButton, button_Ready, ruleBookButton, endGameButton, voteButton, abilityButton;
	JLabel label_Client1, label_Client2, label_Client3, label_Client4, label_Client5, label_Client6, job, dayLabel, eventLabel;
	Label label_Client1_Info, label_Client2_Info, label_Client3_Info, label_Client4_Info, label_Client5_Info, label_Client6_Info;
	Label label_Client1_Msg, label_Client2_Msg, label_Client3_Msg, label_Client4_Msg, label_Client5_Msg, label_Client6_Msg;
	Label label_Time;
	JScrollPane scrollPane;
	ImageIcon i1, i2;
	Image im, im2;
	JTextArea textArea;
	JTextField textField;
	ImageIcon titleImg = new ImageIcon("Images//코난.png");

	Font font1 = new Font("궁서", Font.PLAIN, 15);
	
	String nickName = Login.nickName;
	String ip = Login.ip;
	String hiMsg = Login.hiMsg;
	Socket s;
	int port = 3333; 

	String clientName, clientIdx;
	boolean gameStart;
	String clientJob="";

	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;


	//TreeSet <String> listNicknamesgot = new TreeSet <String>();
	ImageIcon ii = new ImageIcon("Images//코난.png");
	//Object [] userList = new Object [6];
	Object [] userList = {"", "", "", "", "", ""};

	MainGameUI(){
		init();
		setUI();
		startChat();
		System.out.println("s:" +s);
		System.out.println("nickName: "+ nickName);
		System.out.println("hiMsg: "+ hiMsg);
	}
	
	
	public void init(){
		//접속자명단+레디 들어갈 왼쪽 긴 패널생성!, 컨테이너에 추가 !
		contentPane = new JPanel();		
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		mainPanel = new JPanel();		//메인패널 색상
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setOpaque(true);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		job = new JLabel("직업");			//바꿔야할곳
		job.setBounds(60,5,100,30);
		job.setForeground(Color.RED);
		job.setFont(font1);
		mainPanel.add(job);

		//클라이언트 패널
		panel_ListClient = new JPanel();
		panel_ListClient.setOpaque(true);
		panel_ListClient.setBounds(0,35,268,870);
		panel_ListClient.setBackground(Color.BLACK);
		mainPanel.add(panel_ListClient);
		
		panel_ListClient.setLayout(null);
		//icon2.png
		label_Client1 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client1.setBounds(10,0,248,120); //접속자1위치
		panel_ListClient.add(label_Client1);
		//label_Client1.setFont(font1);
		
		label_Client2 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client2.setBounds(10,130,248,120); //접속자2위치
		panel_ListClient.add(label_Client2);
		//label_Client2.setFont(font1);

		label_Client3 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client3.setBounds(10,260,248,120); //접속자1위치
		panel_ListClient.add(label_Client3);
		//label_Client3.setFont(font1);

		label_Client4 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client4.setBounds(10,390,248,120); //접속자1위치
		panel_ListClient.add(label_Client4);
		//label_Client4.setFont(font1);
		
		label_Client5 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client5.setBounds(10,520,248,120); //접속자1위치
		panel_ListClient.add(label_Client5);
		
		label_Client6 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client6.setBounds(10,650,248,120); //접속자1위치
		panel_ListClient.add(label_Client6);

		button_Ready = new JButton(new ImageIcon("Images//ready7.png"));
		button_Ready.setOpaque(true);
		button_Ready.setBorder(new LineBorder(Color.BLACK, 0 ,false));
		button_Ready.setBackground(new Color(0,156,255));
		button_Ready.setOpaque(false);
		button_Ready.setBounds(10,775,243,61); //접속자1위치
		panel_ListClient.add(button_Ready);
		//닉네임
		label_Client1_Info = new Label("닉네임유저님");//변수 클라이언트서버.nickname넣을 예정
		label_Client1_Info.setFont(font1);
		label_Client1_Info.setForeground(Color.WHITE);
		label_Client1_Info.setAlignment(Label.CENTER);
		label_Client1_Info.setBackground(new Color(38,38,38));
		label_Client1_Info.setBounds(140,20,90,25);
		label_Client1.add(label_Client1_Info);
		//자기소개
		label_Client1_Msg = new Label();//변수 클라이언트서버.hiMsg넣을 예정
		label_Client1_Msg.setFont(font1);
		label_Client1_Msg.setForeground(Color.WHITE);
		label_Client1_Msg.setAlignment(Label.CENTER);
		label_Client1_Msg.setBackground(new Color(38,38,38));
		label_Client1_Msg.setBounds(140,35,90,45);
		label_Client1.add(label_Client1_Msg);
		
		label_Client2_Info = new Label("닉네임유저2");						//바꿔야할곳
		label_Client2_Info.setFont(font1);
		label_Client2_Info.setForeground(Color.WHITE);
		label_Client2_Info.setAlignment(Label.CENTER);
		label_Client2_Info.setBackground(new Color(38,38,38));
		label_Client2_Info.setBounds(140,20,90,25);
		label_Client2.add(label_Client2_Info);

		label_Client2_Msg = new Label();							//바꿔야할곳
		label_Client2_Msg.setFont(font1);
		label_Client2_Msg.setForeground(Color.WHITE);
		label_Client2_Msg.setAlignment(Label.CENTER);
		label_Client2_Msg.setBackground(new Color(38,38,38));
		label_Client2_Msg.setBounds(140,35,90,45);
		label_Client2.add(label_Client2_Msg);

		label_Client3_Info = new Label("닉네임유저3");						//바꿔야할곳
		label_Client3_Info.setFont(font1);
		label_Client3_Info.setForeground(Color.WHITE);
		label_Client3_Info.setAlignment(Label.CENTER);
		label_Client3_Info.setBackground(new Color(38,38,38));
		label_Client3_Info.setBounds(140,20,90,25);
		label_Client3.add(label_Client3_Info);

		label_Client3_Msg = new Label();							//바꿔야할곳
		label_Client3_Msg.setFont(font1);
		label_Client3_Msg.setForeground(Color.WHITE);
		label_Client3_Msg.setAlignment(Label.CENTER);
		label_Client3_Msg.setBackground(new Color(38,38,38));
		label_Client3_Msg.setBounds(140,35,90,45);
		label_Client3.add(label_Client3_Msg);

		label_Client4_Info = new Label("닉네임유저4");						//바꿔야할곳
		label_Client4_Info.setFont(font1);
		label_Client4_Info.setForeground(Color.WHITE);
		label_Client4_Info.setAlignment(Label.CENTER);
		label_Client4_Info.setBackground(new Color(38,38,38));
		label_Client4_Info.setBounds(140,20,90,25);
		label_Client4.add(label_Client4_Info);
	
		label_Client4_Msg = new Label();							//바꿔야할곳
		label_Client4_Msg.setFont(font1);
		label_Client4_Msg.setForeground(Color.WHITE);
		label_Client4_Msg.setAlignment(Label.CENTER);
		label_Client4_Msg.setBackground(new Color(38,38,38));
		label_Client4_Msg.setBounds(140,35,90,45);
		label_Client4.add(label_Client4_Msg);

		label_Client5_Info = new Label("닉네임유저5");						//바꿔야할곳
		label_Client5_Info.setFont(font1);
		label_Client5_Info.setForeground(Color.WHITE);
		label_Client5_Info.setAlignment(Label.CENTER);
		label_Client5_Info.setBackground(new Color(38,38,38));
		label_Client5_Info.setBounds(140,20,90,25);
		label_Client5.add(label_Client5_Info);

		label_Client5_Msg = new Label();							//바꿔야할곳
		label_Client5_Msg.setFont(font1);
		label_Client5_Msg.setForeground(Color.WHITE);
		label_Client5_Msg.setAlignment(Label.CENTER);
		label_Client5_Msg.setBackground(new Color(38,38,38));
		label_Client5_Msg.setBounds(140,35,90,45);
		label_Client5.add(label_Client5_Msg);

		label_Client6_Info = new Label("닉네임유저6");						//바꿔야할곳
		label_Client6_Info.setFont(font1);
		label_Client6_Info.setForeground(Color.WHITE);
		label_Client6_Info.setAlignment(Label.CENTER);
		label_Client6_Info.setBackground(new Color(38,38,38));
		label_Client6_Info.setBounds(140,20,90,25);
		label_Client6.add(label_Client6_Info);

		label_Client6_Msg = new Label();							//바꿔야할곳
		label_Client6_Msg.setFont(font1);
		label_Client6_Msg.setForeground(Color.WHITE);
		label_Client6_Msg.setAlignment(Label.CENTER);
		label_Client6_Msg.setBackground(new Color(38,38,38));
		label_Client6_Msg.setBounds(140,35,90,45);
		label_Client6.add(label_Client6_Msg);
		
		//상단 타이머 패널
		panel_Top = new JPanel();
		panel_Top.setOpaque(true);
		panel_Top.setBounds(268,0,520,250);
		panel_Top.setBackground(Color.BLACK);
		mainPanel.add(panel_Top);
		
		panel_Top.setLayout(null);
		label_Time = new Label("00:00");//시간 변수 넣기
		label_Time.setFont(font1);
		label_Time.setForeground(Color.RED);
		label_Time.setBackground(Color.BLACK);
		label_Time.setBounds(460,0,50,40);
		panel_Top.add(label_Time);
		
		dayLabel = new JLabel("1번째 낮");//바꿔야할곳
		dayLabel.setFont(font1);
		dayLabel.setBounds(110,0,80,30);
		dayLabel.setForeground(Color.RED);
		
		panel_Top.add(dayLabel);
		
		eventLabel = new JLabel(new ImageIcon("Images//낮.png"));								//바꿔야할곳
		eventLabel.setBounds(0,40,520,210);
		//eventLabel.setBorder(new LineBorder(new Color(127, 219, 254), 5, false));
		panel_Top.add(eventLabel);

		//채팅 패널
		chatPanel = new JPanel();		
		MatteBorder b1 = new MatteBorder(5,5,5,5,Color.BLACK);	//채팅패널 두께지정
		chatPanel.setBorder(b1);
		chatPanel.setBackground(Color.BLACK);
		chatPanel.setBounds(268,260,520,602);
		chatPanel.setOpaque(true);
		mainPanel.add(chatPanel);
		chatPanel.setLayout(null);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0,0,520,480);
		chatPanel.add(scrollPane);

		textArea = new JTextArea();
		//textArea.setBorder(new LineBorder(new Color(127, 219, 254), 5, false));	//채팅로그 테두리 컬러 무시
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		textArea.setBackground(Color.WHITE);			//채팅로그컬러
		
		textField = new JTextField();
		//textField.setBorder(new LineBorder(new Color(181,227,0), 4, false));	//텍스트필드 컬러 무시
		textField.setBackground(Color.WHITE);
		textField.setOpaque(true);
		textField.setBounds(0,560,450,40);
		chatPanel.add(textField);
		textField.requestFocus();
		textField.setColumns(10);
		
		endGameButton = new JButton("게임 종료");
		endGameButton.setBounds(0,500,170,40);
		chatPanel.add(endGameButton);

		ruleBookButton = new JButton("규칙 설명");
		ruleBookButton.setBounds(180,500,170,40);
		chatPanel.add(ruleBookButton);

		voteButton = new JButton("투표 하기");
		voteButton.setBounds(360,500,170,40);
		chatPanel.add(voteButton);

		sendButton = new JButton("전송");
		sendButton.setBounds(450,560,70,40);
		sendButton.requestFocus();
		chatPanel.add(sendButton);

		abilityButton = new JButton("능력 사용");
		abilityButton.setBounds(360,500,170,40);
		chatPanel.add(abilityButton);
		abilityButton.setVisible(false);
		abilityButton.setEnabled(false);
	}

	void setUI(){
		setTitle("코난마피아게임 로그인");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setSize(804,910);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//채팅 소켓 만들기1
	public void startChat(){
		try{
			s = new Socket(ip, port);
			textArea.append("냥피아 게임에 오신 것을 환영합니다."+"\n");
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		
			Speaker speaker = new Speaker();
			Listener listener = new Listener();
			
			//버튼 리스너에 추가
			textField.addKeyListener(speaker);
			sendButton.addActionListener(speaker);
			button_Ready.addActionListener(speaker);
			ruleBookButton.addActionListener(speaker);
			endGameButton.addActionListener(speaker);
			voteButton.addActionListener(speaker);
			abilityButton.addActionListener(speaker);
			
		}catch(UnknownHostException uh){
			JOptionPane.showMessageDialog(null,"호스트를 찾을 수 없음!", "에러남에러남", JOptionPane.WARNING_MESSAGE);
		}catch(IOException ie){	
			JOptionPane.showMessageDialog(null,"서버에 접속 실패! 서버가 열려있는 것이 맞는지유??", "에러남에러남",JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}

	public void showListNicknames(){
		try{
			dos.writeUTF("//ShowList");
			dos.flush();
		}catch(IOException ie){}
	}

	class Listener extends Thread{
		

		Listener(){
			start();
		}
		public void run(){//listen
			while(true){
				try{
					//서버에 쓰여진 메시지 읽어온 것
					String msgChatRead = dis.readUTF();
					System.out.println(msgChatRead);
					//명령어들 받아온 것 실행
					if(msgChatRead.startsWith("//CList")){//클라이언트 리스트 업데이트dkf
						clientName = msgChatRead.substring(7,msgChatRead.indexOf("#"));
						clientIdx = msgChatRead.substring(msgChatRead.indexOf("#")+1);
						updateClientList();
					}else if(msgChatRead.startsWith("//Start")){
						gameStart = true;
						//랜덤 직업 받기, 팝업 창 업데이트(직업)
						//그림 창 설정 업데이트(낮)
						//bgm 바꾸기
						endGameButton.setEnabled(true);
					}else if(msgChatRead.equals("//EndServer")){
						textArea.append("서버와의 연결이 끊어졌습니다. 3초 후 프로그램을 종료합니다.");
						try{
							Thread.sleep(3000);
							System.exit(0);
						}catch(InterruptedException ire){
						}
					}else if(msgChatRead.equals("//Vote")){
						if( sendButton.isEnabled() ){
							JOptionPane.showMessageDialog(null,"투표하기 버튼을 눌러 죽일 멤버를 투표해주세요!","킬킬!",JOptionPane.PLAIN_MESSAGE);
							voteButton.setEnabled(true);
						}
					}else if(msgChatRead.equals("//Day")|| msgChatRead.equals("//FirtDay")){
						ImageIcon imageDay = new ImageIcon ("Images//낮.png");
						imageDay.getImage().flush();
						eventLabel.setIcon(imageDay);
						if(job.getText().startsWith("직업")){

							sendButton.setEnabled(true);
							textField.setEnabled(true);
							voteButton.setVisible(true);
							voteButton.setEnabled(false);
							contentPane.repaint();
							contentPane.revalidate();
						}
						//voteButton.setEnabled(true);
					}else if(msgChatRead.equals("//VoteFinished")){
						voteButton.setEnabled(false);
					}else if(msgChatRead.equals("//Night")){
						ImageIcon imageNight = new ImageIcon("Images//밤.png");
						imageNight.getImage().flush();
						eventLabel.setIcon(imageNight);
						sendButton.setEnabled(false);
						textField.setEnabled(false);
						if( job.getText().equals("직업 : 마피아") || job.getText().equals("직업 : 경찰")){
							abilityButton.setEnabled(true);
						}
					}else if(msgChatRead.equals("//DeleteVote")){
						voteButton.setVisible(false);
						voteButton.setEnabled(false);
						abilityButton.setVisible(true);
						//abilityButton.setEnabled(true);
						contentPane.repaint();
						contentPane.revalidate();
					}else if(msgChatRead.startsWith("//Job")){
						clientJob = msgChatRead.substring(5);
						System.out.println(clientJob);
						if(clientJob.equals("마피아")){
							job.setText("직업 : 마피아");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 마피아입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("경찰")){
							job.setText("직업 : 경찰");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 경찰입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("시민1")){
							job.setText("직업 : 시민1");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 시민입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("시민2")){
							job.setText("직업 : 시민2");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 시민입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("시민3")){
							job.setText("직업 : 시민3");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 시민입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("시민4")){
							job.setText("직업 : 시민4");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 시민입니다.","직업 안내",JOptionPane.PLAIN_MESSAGE);
						}
					}else if(msgChatRead.startsWith("//Timer")){
						String timer = msgChatRead.substring(7);
						label_Time.setText(timer);
						//System.out.println(msgChatRead.substring(7));
					}else if(msgChatRead.equals("//FirstDay")){

						voteButton.setEnabled(false);
					}else if(msgChatRead.startsWith("자 !")){
						
						String msgDayNight = msgChatRead.substring(3,msgChatRead.indexOf("이"));
						dayLabel.setText(msgDayNight);
						JOptionPane.showMessageDialog(null, msgDayNight, "시간이 흘렀어용!", JOptionPane.PLAIN_MESSAGE);
					}else if(msgChatRead.startsWith("//List")){
						

						//List주현/정훈/동혁
						String realList = msgChatRead.substring(6); // 주현/정훈/동혁/ss/ff/ddd
						String userName0 = realList.substring(0,realList.indexOf("/")); // 주현
						userList[0] = userName0;

						String preUserName1 = realList.substring(realList.indexOf("/")+1); // 정훈/동혁/ss/ff/ddd
						String userName1 = preUserName1.substring(0,preUserName1.indexOf("/")); //정훈
						userList[1] = userName1;
						
						String numberUser = realList.substring(realList.lastIndexOf("/")+1);
						System.out.println(numberUser);

						int numberUsers = Integer.parseInt(numberUser);

						if(numberUsers>=3){
					
							String preUserName2 = preUserName1.substring(preUserName1.indexOf("/")+1); // 동혁/ss/ff/ddd
							String userName2 = preUserName2.substring(0,preUserName2.indexOf("/"));
							userList[2] = userName2;
							if(numberUsers>=4){
								String preUserName3 = preUserName2.substring(preUserName2.indexOf("/")+1);
								String userName3 = preUserName3.substring(0,preUserName3.indexOf("/"));
								userList[3] = userName3;
								if(numberUsers>=5){
									String preUserName4 = preUserName3.substring(preUserName3.indexOf("/")+1); // 동혁/ss/ff/ddd
									String userName4 = preUserName4.substring(0,preUserName4.indexOf("/"));
									userList[4] = userName4;
									if(numberUsers>=6){

										String preUserName5 = preUserName4.substring(preUserName4.indexOf("/")+1); // 동혁/ss/ff/ddd
										String userName5 = preUserName5.substring(0,preUserName5.indexOf("/"));
										userList[5] = userName5;
									}
								}
							}
						}

					}else if(msgChatRead.equals("//표획득")){
						dos.writeUTF("//투표결과"+clientJob);
						dos.flush();
						
						//System.out.println("//투표결과"+clientJob);
						
					}else if(msgChatRead.equals("//Kill")){
						//ImageIcon imageNight = new ImageIcon("Images//night.jpg");
						//imageNight.getImage().flush();
						//eventLabel.setIcon(imageNight);
					
						job.setText("죽었어잉");
						

						//dos.writeUTF("저("+nickName+")죽었어요...");
						//dos.flush();
						JOptionPane.showMessageDialog(null, "당신은 살해당했습니다...","직업 안내",JOptionPane.PLAIN_MESSAGE);
						sendButton.setEnabled(false);
						textField.setEnabled(false);
						voteButton.setEnabled(false);
						abilityButton.setEnabled(false);
					
							dos.writeUTF("님들이 저("+nickName+")죽였어요...");
							//dos.writeUTF("//isMyjob"+job.getText());
							dos.flush();

					}else if(msgChatRead.startsWith("//MafiaKills")){
						String getMafiaKills = msgChatRead.substring(12);
						if(getMafiaKills.equals(nickName)){
							dos.writeUTF("//Mkilled"+job.getText());
							job.setText("죽었어잉");
							//"//Mkilled직업 : ㅇㅇ"14
							//직업 : 시민4

							dos.writeUTF("마피아가 간밤에 "+nickName+"님을 살해했습니다...");
							dos.flush();
							JOptionPane.showMessageDialog(null, "당신은 살해당했습니다...","직업 안내",JOptionPane.PLAIN_MESSAGE);
							sendButton.setEnabled(false);
							textField.setEnabled(false);
							voteButton.setEnabled(false);
							abilityButton.setEnabled(false);
						}
					}else if(msgChatRead.startsWith("//policeA")){
						String getPolice = msgChatRead.substring(9);
						if(getPolice.equals(nickName)){
							dos.writeUTF("*경찰* 당신이 알고싶은 사람 "+job.getText());
							dos.flush();
						}
					}
					
					
					else if(msgChatRead.startsWith("!!! 마피아가")){
						ImageIcon winCitizen = new ImageIcon ("Images//시민승.PNG");
						winCitizen.getImage().flush();
						eventLabel.setIcon(winCitizen);
						dos.writeUTF("//gameEnd");
						dos.flush();
						JOptionPane.showMessageDialog(null,"시민팀 승리!!!","겜끝",JOptionPane.PLAIN_MESSAGE);
						gameStart = false;
					}else if(msgChatRead.equals("~~마피아가 이겼습니다~~")){
						ImageIcon winMafia = new ImageIcon ("Images//마피아승.png");
						winMafia.getImage().flush();
						eventLabel.setIcon(winMafia);
						dos.writeUTF("//gameEnd");
						dos.flush();
						JOptionPane.showMessageDialog(null,"마피아 승리!!!","겜끝",JOptionPane.PLAIN_MESSAGE);
						gameStart = false;
					
					}
					
					else if(msgChatRead.startsWith("!!! 시민이")){
						ImageIcon killedCitizen = new ImageIcon ("Images//killed.jpg");
						killedCitizen.getImage().flush();
						eventLabel.setIcon(killedCitizen);
						JOptionPane.showMessageDialog(null,"여러분의 마녀사냥으로 시민이 한 명 희생되어씁니다..","살인사건!!!",JOptionPane.PLAIN_MESSAGE);
					}else if(msgChatRead.startsWith("!!! 경찰이")){
					ImageIcon killedCitizen = new ImageIcon ("Images//killed.jpg");
						killedCitizen.getImage().flush();
						eventLabel.setIcon(killedCitizen);
						JOptionPane.showMessageDialog(null,"여러분의 마녀사냥으로 경찰이 희생되어씁니다..","살인사건!!!",JOptionPane.PLAIN_MESSAGE);
					}else{
						textArea.append(msgChatRead+"\n");
						scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
					}
				}catch(StringIndexOutOfBoundsException sibe){
					System.out.println(sibe);
				}catch(IOException ie){
				}
			}
		}
	
		public void updateClientList(){
			ImageIcon imageiconClient;
			if(Integer.parseInt(clientIdx)==0){
				imageiconClient= new ImageIcon("Images//gin.png");
				imageiconClient.getImage().flush();
				label_Client1.setIcon(imageiconClient);
				label_Client1_Info.setFont(font1);
				label_Client1_Info.setText(clientName +"님");
				
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==1){
				imageiconClient = new ImageIcon("Images//bye.png");
				imageiconClient.getImage().flush();
				label_Client2.setIcon(imageiconClient);
				label_Client2_Info.setFont(font1);
				label_Client2_Info.setText(clientName +"님");
				
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==2){
				imageiconClient = new ImageIcon("Images//chianti.png");
				imageiconClient.getImage().flush();
				label_Client3.setIcon(imageiconClient);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText(clientName +"님");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==3){
				imageiconClient = new ImageIcon("Images//kir.png");
				imageiconClient.getImage().flush();
				label_Client4.setIcon(imageiconClient);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText(clientName +"님");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==4){
				imageiconClient = new ImageIcon("Images//vodka.png");
				imageiconClient.getImage().flush();
				label_Client5.setIcon(imageiconClient);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText(clientName +"님");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==5){
				imageiconClient = new ImageIcon("Images//vermouth.png");
				imageiconClient.getImage().flush();
				label_Client6.setIcon(imageiconClient);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText(clientName +"님");
				deleteClientList();
			}
		}

		void deleteClientList(){
			ImageIcon watingIcon = new ImageIcon("images\\shadow.png");

			if(Integer.parseInt(clientIdx) == 0){
				label_Client2.setIcon(watingIcon);
				label_Client2_Info.setText("기다리는중...");
				label_Client2_Info.setFont(font1);
				label_Client3.setIcon(watingIcon);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText("기다리는중...");
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("기다리는중...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("기다리는중...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("기다리는중...");
			}else if(Integer.parseInt(clientIdx) == 1){
				label_Client3.setIcon(watingIcon);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText("기다리는중...");
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("기다리는중...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("기다리는중...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("기다리는중...");
			}else if(Integer.parseInt(clientIdx) == 2){
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("기다리는중...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("기다리는중...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("기다리는중...");
			}else if(Integer.parseInt(clientIdx) == 3){	
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("기다리는중...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("기다리는중...");
			}else if(Integer.parseInt(clientIdx) == 4){
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("기다리는중...");
			}
		}
	}
	class Speaker extends Thread implements KeyListener, ActionListener
	{
		//startChat()에서 소킷, 닉넴 받음
		Speaker(){
			new Thread(this).start();
		}
		public void run(){//계속쓰기
			try{
				dos.writeUTF(nickName);//닉네임보내주기
				//dos.writeUTF(clientJob);
				//textArea.append("잘뜨나?");
				System.out.println(nickName);
				//System.out.println(clientJob);
			}catch(IOException ie){}
		}
	//JButton sendButton, button_Ready, postponeTime, reduceTime, voteButton, abilityButton;
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == button_Ready){
				try{
					dos.writeUTF("//Chat"+nickName+"님 레디함!");
					dos.flush();
					dos.writeUTF("//Ready");
					dos.flush();
					button_Ready.setEnabled(false);
				}catch(IOException ie){
				}
			}else if(e.getSource() == ruleBookButton){
				JOptionPane.showMessageDialog(null,
				"당신은 마피아의 꾀임에 넘어가\n비트캠프마을에 초청되었습니다.\n당신이 시민일 경우 마피아를 색출하여\n그를 처단하면 살아남을 것 입니다.\n당신이 마피아인 경우 시민이 \n한명이 남는다면 당신의 승리가 됩니다. ", "기본 규칙", JOptionPane.PLAIN_MESSAGE);
			}else if(e.getSource() == endGameButton){
				ImageIcon tempi = new ImageIcon("Images//TEST.JPG");
				int result = JOptionPane.showConfirmDialog(null, "정말 게임을 종료 하시겠습니까?","게임 종료",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,tempi);
				if(result == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}else if(e.getSource() == voteButton){
				showListNicknames();
				Object objVote = JOptionPane.showInputDialog(
					null,
					"죽일 사람을 지목하세용!",
					"투표시간",
					JOptionPane.QUESTION_MESSAGE,
					ii,
					userList,
					userList[5]);
				if(objVote !=null){
					voteButton.setEnabled(false);
					String resultVote = objVote.toString();
					try{
						dos.writeUTF("//Vote"+ resultVote);
						dos.flush();
					}catch(IOException ie){}
				}

				
			}else if(e.getSource() == sendButton){
				String msgChatButton = textField.getText();
				textField.setText("");
				try{
					//msgChat 서버에 쓰기
					dos.writeUTF("//Chat "+nickName+">> " + msgChatButton);
					dos.flush();
				}catch(IOException ie){}
			}else if(e.getSource() == abilityButton){
				System.out.println("job.getTExt():"+job.getText());
				if(job.getText().equals("직업 : 마피아")){
					showListNicknames();
					Object objVoteMafia = JOptionPane.showInputDialog(
						null,
						"죽일 사람을 지목하세용!",
						"마피아 능력발휘",
						JOptionPane.QUESTION_MESSAGE,
						ii,
						userList,
						userList[5]);
					if(objVoteMafia !=null){
						abilityButton.setEnabled(false);
						String resultVoteMafia = objVoteMafia.toString();
						try{
							dos.writeUTF("//MafiaKills"+ resultVoteMafia);
							dos.flush();
						}catch(IOException ie){}
						
					}

				
				}else if(job.getText().equals("직업 : 경찰")){
					System.out.println("job.getTExt():"+job.getText());
					showListNicknames();
					Object abilityPolice = JOptionPane.showInputDialog(
						null,
						"누구의 비밀을 알고시펑?!",
						"경찰 능력발휘",
						JOptionPane.QUESTION_MESSAGE,
						ii,
						userList,
						userList[5]);
					if(abilityPolice !=null){
						abilityButton.setEnabled(false);
						
						String policeA = abilityPolice.toString();
						try{
							dos.writeUTF("//policeA"+ policeA);
							dos.flush();
						}catch(IOException ie){}
						
					
					}
				}
		}
		}
		public void keyReleased(KeyEvent e){ // 채팅 입력
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				//채팅창에 쓴 메시지 : msgChat
				String msgChatEnter = textField.getText();
				textField.setText("");
				try{
					//msgChat 서버에 쓰기
					dos.writeUTF("//Chat "+nickName+">> " +msgChatEnter);
					dos.flush();
				}catch(IOException ie){}
			}
		}

		public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e){}
	}
	void pln(String str){
		System.out.println(str);
	}
	public static void main(String args[]){
		new MainGameUI();
	}
}

