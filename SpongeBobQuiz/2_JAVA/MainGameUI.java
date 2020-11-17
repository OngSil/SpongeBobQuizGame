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
	ImageIcon titleImg = new ImageIcon("Images//�ڳ�.png");

	Font font1 = new Font("�ü�", Font.PLAIN, 15);
	
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
	ImageIcon ii = new ImageIcon("Images//�ڳ�.png");
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
		//�����ڸ��+���� �� ���� �� �гλ���!, �����̳ʿ� �߰� !
		contentPane = new JPanel();		
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		mainPanel = new JPanel();		//�����г� ����
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setOpaque(true);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		job = new JLabel("����");			//�ٲ���Ұ�
		job.setBounds(60,5,100,30);
		job.setForeground(Color.RED);
		job.setFont(font1);
		mainPanel.add(job);

		//Ŭ���̾�Ʈ �г�
		panel_ListClient = new JPanel();
		panel_ListClient.setOpaque(true);
		panel_ListClient.setBounds(0,35,268,870);
		panel_ListClient.setBackground(Color.BLACK);
		mainPanel.add(panel_ListClient);
		
		panel_ListClient.setLayout(null);
		//icon2.png
		label_Client1 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client1.setBounds(10,0,248,120); //������1��ġ
		panel_ListClient.add(label_Client1);
		//label_Client1.setFont(font1);
		
		label_Client2 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client2.setBounds(10,130,248,120); //������2��ġ
		panel_ListClient.add(label_Client2);
		//label_Client2.setFont(font1);

		label_Client3 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client3.setBounds(10,260,248,120); //������1��ġ
		panel_ListClient.add(label_Client3);
		//label_Client3.setFont(font1);

		label_Client4 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client4.setBounds(10,390,248,120); //������1��ġ
		panel_ListClient.add(label_Client4);
		//label_Client4.setFont(font1);
		
		label_Client5 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client5.setBounds(10,520,248,120); //������1��ġ
		panel_ListClient.add(label_Client5);
		
		label_Client6 = new JLabel(new ImageIcon("Images//shadow.png"));
		label_Client6.setBounds(10,650,248,120); //������1��ġ
		panel_ListClient.add(label_Client6);

		button_Ready = new JButton(new ImageIcon("Images//ready7.png"));
		button_Ready.setOpaque(true);
		button_Ready.setBorder(new LineBorder(Color.BLACK, 0 ,false));
		button_Ready.setBackground(new Color(0,156,255));
		button_Ready.setOpaque(false);
		button_Ready.setBounds(10,775,243,61); //������1��ġ
		panel_ListClient.add(button_Ready);
		//�г���
		label_Client1_Info = new Label("�г���������");//���� Ŭ���̾�Ʈ����.nickname���� ����
		label_Client1_Info.setFont(font1);
		label_Client1_Info.setForeground(Color.WHITE);
		label_Client1_Info.setAlignment(Label.CENTER);
		label_Client1_Info.setBackground(new Color(38,38,38));
		label_Client1_Info.setBounds(140,20,90,25);
		label_Client1.add(label_Client1_Info);
		//�ڱ�Ұ�
		label_Client1_Msg = new Label();//���� Ŭ���̾�Ʈ����.hiMsg���� ����
		label_Client1_Msg.setFont(font1);
		label_Client1_Msg.setForeground(Color.WHITE);
		label_Client1_Msg.setAlignment(Label.CENTER);
		label_Client1_Msg.setBackground(new Color(38,38,38));
		label_Client1_Msg.setBounds(140,35,90,45);
		label_Client1.add(label_Client1_Msg);
		
		label_Client2_Info = new Label("�г�������2");						//�ٲ���Ұ�
		label_Client2_Info.setFont(font1);
		label_Client2_Info.setForeground(Color.WHITE);
		label_Client2_Info.setAlignment(Label.CENTER);
		label_Client2_Info.setBackground(new Color(38,38,38));
		label_Client2_Info.setBounds(140,20,90,25);
		label_Client2.add(label_Client2_Info);

		label_Client2_Msg = new Label();							//�ٲ���Ұ�
		label_Client2_Msg.setFont(font1);
		label_Client2_Msg.setForeground(Color.WHITE);
		label_Client2_Msg.setAlignment(Label.CENTER);
		label_Client2_Msg.setBackground(new Color(38,38,38));
		label_Client2_Msg.setBounds(140,35,90,45);
		label_Client2.add(label_Client2_Msg);

		label_Client3_Info = new Label("�г�������3");						//�ٲ���Ұ�
		label_Client3_Info.setFont(font1);
		label_Client3_Info.setForeground(Color.WHITE);
		label_Client3_Info.setAlignment(Label.CENTER);
		label_Client3_Info.setBackground(new Color(38,38,38));
		label_Client3_Info.setBounds(140,20,90,25);
		label_Client3.add(label_Client3_Info);

		label_Client3_Msg = new Label();							//�ٲ���Ұ�
		label_Client3_Msg.setFont(font1);
		label_Client3_Msg.setForeground(Color.WHITE);
		label_Client3_Msg.setAlignment(Label.CENTER);
		label_Client3_Msg.setBackground(new Color(38,38,38));
		label_Client3_Msg.setBounds(140,35,90,45);
		label_Client3.add(label_Client3_Msg);

		label_Client4_Info = new Label("�г�������4");						//�ٲ���Ұ�
		label_Client4_Info.setFont(font1);
		label_Client4_Info.setForeground(Color.WHITE);
		label_Client4_Info.setAlignment(Label.CENTER);
		label_Client4_Info.setBackground(new Color(38,38,38));
		label_Client4_Info.setBounds(140,20,90,25);
		label_Client4.add(label_Client4_Info);
	
		label_Client4_Msg = new Label();							//�ٲ���Ұ�
		label_Client4_Msg.setFont(font1);
		label_Client4_Msg.setForeground(Color.WHITE);
		label_Client4_Msg.setAlignment(Label.CENTER);
		label_Client4_Msg.setBackground(new Color(38,38,38));
		label_Client4_Msg.setBounds(140,35,90,45);
		label_Client4.add(label_Client4_Msg);

		label_Client5_Info = new Label("�г�������5");						//�ٲ���Ұ�
		label_Client5_Info.setFont(font1);
		label_Client5_Info.setForeground(Color.WHITE);
		label_Client5_Info.setAlignment(Label.CENTER);
		label_Client5_Info.setBackground(new Color(38,38,38));
		label_Client5_Info.setBounds(140,20,90,25);
		label_Client5.add(label_Client5_Info);

		label_Client5_Msg = new Label();							//�ٲ���Ұ�
		label_Client5_Msg.setFont(font1);
		label_Client5_Msg.setForeground(Color.WHITE);
		label_Client5_Msg.setAlignment(Label.CENTER);
		label_Client5_Msg.setBackground(new Color(38,38,38));
		label_Client5_Msg.setBounds(140,35,90,45);
		label_Client5.add(label_Client5_Msg);

		label_Client6_Info = new Label("�г�������6");						//�ٲ���Ұ�
		label_Client6_Info.setFont(font1);
		label_Client6_Info.setForeground(Color.WHITE);
		label_Client6_Info.setAlignment(Label.CENTER);
		label_Client6_Info.setBackground(new Color(38,38,38));
		label_Client6_Info.setBounds(140,20,90,25);
		label_Client6.add(label_Client6_Info);

		label_Client6_Msg = new Label();							//�ٲ���Ұ�
		label_Client6_Msg.setFont(font1);
		label_Client6_Msg.setForeground(Color.WHITE);
		label_Client6_Msg.setAlignment(Label.CENTER);
		label_Client6_Msg.setBackground(new Color(38,38,38));
		label_Client6_Msg.setBounds(140,35,90,45);
		label_Client6.add(label_Client6_Msg);
		
		//��� Ÿ�̸� �г�
		panel_Top = new JPanel();
		panel_Top.setOpaque(true);
		panel_Top.setBounds(268,0,520,250);
		panel_Top.setBackground(Color.BLACK);
		mainPanel.add(panel_Top);
		
		panel_Top.setLayout(null);
		label_Time = new Label("00:00");//�ð� ���� �ֱ�
		label_Time.setFont(font1);
		label_Time.setForeground(Color.RED);
		label_Time.setBackground(Color.BLACK);
		label_Time.setBounds(460,0,50,40);
		panel_Top.add(label_Time);
		
		dayLabel = new JLabel("1��° ��");//�ٲ���Ұ�
		dayLabel.setFont(font1);
		dayLabel.setBounds(110,0,80,30);
		dayLabel.setForeground(Color.RED);
		
		panel_Top.add(dayLabel);
		
		eventLabel = new JLabel(new ImageIcon("Images//��.png"));								//�ٲ���Ұ�
		eventLabel.setBounds(0,40,520,210);
		//eventLabel.setBorder(new LineBorder(new Color(127, 219, 254), 5, false));
		panel_Top.add(eventLabel);

		//ä�� �г�
		chatPanel = new JPanel();		
		MatteBorder b1 = new MatteBorder(5,5,5,5,Color.BLACK);	//ä���г� �β�����
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
		//textArea.setBorder(new LineBorder(new Color(127, 219, 254), 5, false));	//ä�÷α� �׵θ� �÷� ����
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
		textArea.setBackground(Color.WHITE);			//ä�÷α��÷�
		
		textField = new JTextField();
		//textField.setBorder(new LineBorder(new Color(181,227,0), 4, false));	//�ؽ�Ʈ�ʵ� �÷� ����
		textField.setBackground(Color.WHITE);
		textField.setOpaque(true);
		textField.setBounds(0,560,450,40);
		chatPanel.add(textField);
		textField.requestFocus();
		textField.setColumns(10);
		
		endGameButton = new JButton("���� ����");
		endGameButton.setBounds(0,500,170,40);
		chatPanel.add(endGameButton);

		ruleBookButton = new JButton("��Ģ ����");
		ruleBookButton.setBounds(180,500,170,40);
		chatPanel.add(ruleBookButton);

		voteButton = new JButton("��ǥ �ϱ�");
		voteButton.setBounds(360,500,170,40);
		chatPanel.add(voteButton);

		sendButton = new JButton("����");
		sendButton.setBounds(450,560,70,40);
		sendButton.requestFocus();
		chatPanel.add(sendButton);

		abilityButton = new JButton("�ɷ� ���");
		abilityButton.setBounds(360,500,170,40);
		chatPanel.add(abilityButton);
		abilityButton.setVisible(false);
		abilityButton.setEnabled(false);
	}

	void setUI(){
		setTitle("�ڳ����Ǿư��� �α���");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setSize(804,910);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	//ä�� ���� �����1
	public void startChat(){
		try{
			s = new Socket(ip, port);
			textArea.append("���Ǿ� ���ӿ� ���� ���� ȯ���մϴ�."+"\n");
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		
			Speaker speaker = new Speaker();
			Listener listener = new Listener();
			
			//��ư �����ʿ� �߰�
			textField.addKeyListener(speaker);
			sendButton.addActionListener(speaker);
			button_Ready.addActionListener(speaker);
			ruleBookButton.addActionListener(speaker);
			endGameButton.addActionListener(speaker);
			voteButton.addActionListener(speaker);
			abilityButton.addActionListener(speaker);
			
		}catch(UnknownHostException uh){
			JOptionPane.showMessageDialog(null,"ȣ��Ʈ�� ã�� �� ����!", "������������", JOptionPane.WARNING_MESSAGE);
		}catch(IOException ie){	
			JOptionPane.showMessageDialog(null,"������ ���� ����! ������ �����ִ� ���� �´�����??", "������������",JOptionPane.WARNING_MESSAGE);
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
					//������ ������ �޽��� �о�� ��
					String msgChatRead = dis.readUTF();
					System.out.println(msgChatRead);
					//��ɾ�� �޾ƿ� �� ����
					if(msgChatRead.startsWith("//CList")){//Ŭ���̾�Ʈ ����Ʈ ������Ʈdkf
						clientName = msgChatRead.substring(7,msgChatRead.indexOf("#"));
						clientIdx = msgChatRead.substring(msgChatRead.indexOf("#")+1);
						updateClientList();
					}else if(msgChatRead.startsWith("//Start")){
						gameStart = true;
						//���� ���� �ޱ�, �˾� â ������Ʈ(����)
						//�׸� â ���� ������Ʈ(��)
						//bgm �ٲٱ�
						endGameButton.setEnabled(true);
					}else if(msgChatRead.equals("//EndServer")){
						textArea.append("�������� ������ ���������ϴ�. 3�� �� ���α׷��� �����մϴ�.");
						try{
							Thread.sleep(3000);
							System.exit(0);
						}catch(InterruptedException ire){
						}
					}else if(msgChatRead.equals("//Vote")){
						if( sendButton.isEnabled() ){
							JOptionPane.showMessageDialog(null,"��ǥ�ϱ� ��ư�� ���� ���� ����� ��ǥ���ּ���!","ųų!",JOptionPane.PLAIN_MESSAGE);
							voteButton.setEnabled(true);
						}
					}else if(msgChatRead.equals("//Day")|| msgChatRead.equals("//FirtDay")){
						ImageIcon imageDay = new ImageIcon ("Images//��.png");
						imageDay.getImage().flush();
						eventLabel.setIcon(imageDay);
						if(job.getText().startsWith("����")){

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
						ImageIcon imageNight = new ImageIcon("Images//��.png");
						imageNight.getImage().flush();
						eventLabel.setIcon(imageNight);
						sendButton.setEnabled(false);
						textField.setEnabled(false);
						if( job.getText().equals("���� : ���Ǿ�") || job.getText().equals("���� : ����")){
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
						if(clientJob.equals("���Ǿ�")){
							job.setText("���� : ���Ǿ�");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� ���Ǿ��Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("����")){
							job.setText("���� : ����");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� �����Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("�ù�1")){
							job.setText("���� : �ù�1");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� �ù��Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("�ù�2")){
							job.setText("���� : �ù�2");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� �ù��Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("�ù�3")){
							job.setText("���� : �ù�3");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� �ù��Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}else if(clientJob.equals("�ù�4")){
							job.setText("���� : �ù�4");
							dos.writeUTF("//isMyjob"+clientJob);
							System.out.println("//isMyjob"+clientJob);
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� �ù��Դϴ�.","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						}
					}else if(msgChatRead.startsWith("//Timer")){
						String timer = msgChatRead.substring(7);
						label_Time.setText(timer);
						//System.out.println(msgChatRead.substring(7));
					}else if(msgChatRead.equals("//FirstDay")){

						voteButton.setEnabled(false);
					}else if(msgChatRead.startsWith("�� !")){
						
						String msgDayNight = msgChatRead.substring(3,msgChatRead.indexOf("��"));
						dayLabel.setText(msgDayNight);
						JOptionPane.showMessageDialog(null, msgDayNight, "�ð��� �귶���!", JOptionPane.PLAIN_MESSAGE);
					}else if(msgChatRead.startsWith("//List")){
						

						//List����/����/����
						String realList = msgChatRead.substring(6); // ����/����/����/ss/ff/ddd
						String userName0 = realList.substring(0,realList.indexOf("/")); // ����
						userList[0] = userName0;

						String preUserName1 = realList.substring(realList.indexOf("/")+1); // ����/����/ss/ff/ddd
						String userName1 = preUserName1.substring(0,preUserName1.indexOf("/")); //����
						userList[1] = userName1;
						
						String numberUser = realList.substring(realList.lastIndexOf("/")+1);
						System.out.println(numberUser);

						int numberUsers = Integer.parseInt(numberUser);

						if(numberUsers>=3){
					
							String preUserName2 = preUserName1.substring(preUserName1.indexOf("/")+1); // ����/ss/ff/ddd
							String userName2 = preUserName2.substring(0,preUserName2.indexOf("/"));
							userList[2] = userName2;
							if(numberUsers>=4){
								String preUserName3 = preUserName2.substring(preUserName2.indexOf("/")+1);
								String userName3 = preUserName3.substring(0,preUserName3.indexOf("/"));
								userList[3] = userName3;
								if(numberUsers>=5){
									String preUserName4 = preUserName3.substring(preUserName3.indexOf("/")+1); // ����/ss/ff/ddd
									String userName4 = preUserName4.substring(0,preUserName4.indexOf("/"));
									userList[4] = userName4;
									if(numberUsers>=6){

										String preUserName5 = preUserName4.substring(preUserName4.indexOf("/")+1); // ����/ss/ff/ddd
										String userName5 = preUserName5.substring(0,preUserName5.indexOf("/"));
										userList[5] = userName5;
									}
								}
							}
						}

					}else if(msgChatRead.equals("//ǥȹ��")){
						dos.writeUTF("//��ǥ���"+clientJob);
						dos.flush();
						
						//System.out.println("//��ǥ���"+clientJob);
						
					}else if(msgChatRead.equals("//Kill")){
						//ImageIcon imageNight = new ImageIcon("Images//night.jpg");
						//imageNight.getImage().flush();
						//eventLabel.setIcon(imageNight);
					
						job.setText("�׾�����");
						

						//dos.writeUTF("��("+nickName+")�׾����...");
						//dos.flush();
						JOptionPane.showMessageDialog(null, "����� ���ش��߽��ϴ�...","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
						sendButton.setEnabled(false);
						textField.setEnabled(false);
						voteButton.setEnabled(false);
						abilityButton.setEnabled(false);
					
							dos.writeUTF("�Ե��� ��("+nickName+")�׿����...");
							//dos.writeUTF("//isMyjob"+job.getText());
							dos.flush();

					}else if(msgChatRead.startsWith("//MafiaKills")){
						String getMafiaKills = msgChatRead.substring(12);
						if(getMafiaKills.equals(nickName)){
							dos.writeUTF("//Mkilled"+job.getText());
							job.setText("�׾�����");
							//"//Mkilled���� : ����"14
							//���� : �ù�4

							dos.writeUTF("���Ǿư� ���㿡 "+nickName+"���� �����߽��ϴ�...");
							dos.flush();
							JOptionPane.showMessageDialog(null, "����� ���ش��߽��ϴ�...","���� �ȳ�",JOptionPane.PLAIN_MESSAGE);
							sendButton.setEnabled(false);
							textField.setEnabled(false);
							voteButton.setEnabled(false);
							abilityButton.setEnabled(false);
						}
					}else if(msgChatRead.startsWith("//policeA")){
						String getPolice = msgChatRead.substring(9);
						if(getPolice.equals(nickName)){
							dos.writeUTF("*����* ����� �˰���� ��� "+job.getText());
							dos.flush();
						}
					}
					
					
					else if(msgChatRead.startsWith("!!! ���Ǿư�")){
						ImageIcon winCitizen = new ImageIcon ("Images//�ùν�.PNG");
						winCitizen.getImage().flush();
						eventLabel.setIcon(winCitizen);
						dos.writeUTF("//gameEnd");
						dos.flush();
						JOptionPane.showMessageDialog(null,"�ù��� �¸�!!!","�׳�",JOptionPane.PLAIN_MESSAGE);
						gameStart = false;
					}else if(msgChatRead.equals("~~���Ǿư� �̰���ϴ�~~")){
						ImageIcon winMafia = new ImageIcon ("Images//���Ǿƽ�.png");
						winMafia.getImage().flush();
						eventLabel.setIcon(winMafia);
						dos.writeUTF("//gameEnd");
						dos.flush();
						JOptionPane.showMessageDialog(null,"���Ǿ� �¸�!!!","�׳�",JOptionPane.PLAIN_MESSAGE);
						gameStart = false;
					
					}
					
					else if(msgChatRead.startsWith("!!! �ù���")){
						ImageIcon killedCitizen = new ImageIcon ("Images//killed.jpg");
						killedCitizen.getImage().flush();
						eventLabel.setIcon(killedCitizen);
						JOptionPane.showMessageDialog(null,"�������� ���������� �ù��� �� �� ����Ǿ�ϴ�..","���λ��!!!",JOptionPane.PLAIN_MESSAGE);
					}else if(msgChatRead.startsWith("!!! ������")){
					ImageIcon killedCitizen = new ImageIcon ("Images//killed.jpg");
						killedCitizen.getImage().flush();
						eventLabel.setIcon(killedCitizen);
						JOptionPane.showMessageDialog(null,"�������� ���������� ������ ����Ǿ�ϴ�..","���λ��!!!",JOptionPane.PLAIN_MESSAGE);
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
				label_Client1_Info.setText(clientName +"��");
				
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==1){
				imageiconClient = new ImageIcon("Images//bye.png");
				imageiconClient.getImage().flush();
				label_Client2.setIcon(imageiconClient);
				label_Client2_Info.setFont(font1);
				label_Client2_Info.setText(clientName +"��");
				
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==2){
				imageiconClient = new ImageIcon("Images//chianti.png");
				imageiconClient.getImage().flush();
				label_Client3.setIcon(imageiconClient);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText(clientName +"��");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==3){
				imageiconClient = new ImageIcon("Images//kir.png");
				imageiconClient.getImage().flush();
				label_Client4.setIcon(imageiconClient);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText(clientName +"��");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==4){
				imageiconClient = new ImageIcon("Images//vodka.png");
				imageiconClient.getImage().flush();
				label_Client5.setIcon(imageiconClient);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText(clientName +"��");
				deleteClientList();
			}else if(Integer.parseInt(clientIdx)==5){
				imageiconClient = new ImageIcon("Images//vermouth.png");
				imageiconClient.getImage().flush();
				label_Client6.setIcon(imageiconClient);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText(clientName +"��");
				deleteClientList();
			}
		}

		void deleteClientList(){
			ImageIcon watingIcon = new ImageIcon("images\\shadow.png");

			if(Integer.parseInt(clientIdx) == 0){
				label_Client2.setIcon(watingIcon);
				label_Client2_Info.setText("��ٸ�����...");
				label_Client2_Info.setFont(font1);
				label_Client3.setIcon(watingIcon);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText("��ٸ�����...");
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("��ٸ�����...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("��ٸ�����...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("��ٸ�����...");
			}else if(Integer.parseInt(clientIdx) == 1){
				label_Client3.setIcon(watingIcon);
				label_Client3_Info.setFont(font1);
				label_Client3_Info.setText("��ٸ�����...");
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("��ٸ�����...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("��ٸ�����...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("��ٸ�����...");
			}else if(Integer.parseInt(clientIdx) == 2){
				label_Client4.setIcon(watingIcon);
				label_Client4_Info.setFont(font1);
				label_Client4_Info.setText("��ٸ�����...");
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("��ٸ�����...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("��ٸ�����...");
			}else if(Integer.parseInt(clientIdx) == 3){	
				label_Client5.setIcon(watingIcon);
				label_Client5_Info.setFont(font1);
				label_Client5_Info.setText("��ٸ�����...");
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("��ٸ�����...");
			}else if(Integer.parseInt(clientIdx) == 4){
				label_Client6.setIcon(watingIcon);
				label_Client6_Info.setFont(font1);
				label_Client6_Info.setText("��ٸ�����...");
			}
		}
	}
	class Speaker extends Thread implements KeyListener, ActionListener
	{
		//startChat()���� ��Ŷ, �г� ����
		Speaker(){
			new Thread(this).start();
		}
		public void run(){//��Ӿ���
			try{
				dos.writeUTF(nickName);//�г��Ӻ����ֱ�
				//dos.writeUTF(clientJob);
				//textArea.append("�߶߳�?");
				System.out.println(nickName);
				//System.out.println(clientJob);
			}catch(IOException ie){}
		}
	//JButton sendButton, button_Ready, postponeTime, reduceTime, voteButton, abilityButton;
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == button_Ready){
				try{
					dos.writeUTF("//Chat"+nickName+"�� ������!");
					dos.flush();
					dos.writeUTF("//Ready");
					dos.flush();
					button_Ready.setEnabled(false);
				}catch(IOException ie){
				}
			}else if(e.getSource() == ruleBookButton){
				JOptionPane.showMessageDialog(null,
				"����� ���Ǿ��� ���ӿ� �Ѿ\n��Ʈķ�������� ��û�Ǿ����ϴ�.\n����� �ù��� ��� ���ǾƸ� �����Ͽ�\n�׸� ó���ϸ� ��Ƴ��� �� �Դϴ�.\n����� ���Ǿ��� ��� �ù��� \n�Ѹ��� ���´ٸ� ����� �¸��� �˴ϴ�. ", "�⺻ ��Ģ", JOptionPane.PLAIN_MESSAGE);
			}else if(e.getSource() == endGameButton){
				ImageIcon tempi = new ImageIcon("Images//TEST.JPG");
				int result = JOptionPane.showConfirmDialog(null, "���� ������ ���� �Ͻðڽ��ϱ�?","���� ����",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,tempi);
				if(result == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}else if(e.getSource() == voteButton){
				showListNicknames();
				Object objVote = JOptionPane.showInputDialog(
					null,
					"���� ����� �����ϼ���!",
					"��ǥ�ð�",
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
					//msgChat ������ ����
					dos.writeUTF("//Chat "+nickName+">> " + msgChatButton);
					dos.flush();
				}catch(IOException ie){}
			}else if(e.getSource() == abilityButton){
				System.out.println("job.getTExt():"+job.getText());
				if(job.getText().equals("���� : ���Ǿ�")){
					showListNicknames();
					Object objVoteMafia = JOptionPane.showInputDialog(
						null,
						"���� ����� �����ϼ���!",
						"���Ǿ� �ɷ¹���",
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

				
				}else if(job.getText().equals("���� : ����")){
					System.out.println("job.getTExt():"+job.getText());
					showListNicknames();
					Object abilityPolice = JOptionPane.showInputDialog(
						null,
						"������ ����� �˰����?!",
						"���� �ɷ¹���",
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
		public void keyReleased(KeyEvent e){ // ä�� �Է�
			if(e.getKeyCode()==KeyEvent.VK_ENTER){
				//ä��â�� �� �޽��� : msgChat
				String msgChatEnter = textField.getText();
				textField.setText("");
				try{
					//msgChat ������ ����
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

