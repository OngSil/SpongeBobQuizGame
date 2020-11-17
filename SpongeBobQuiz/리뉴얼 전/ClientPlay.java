import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javafx.embed.swing.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class ClientPlay extends JFrame implements ActionListener {
	JPanel quiz_board;
	JLabel background, quiz, sp, dd, da, jj, chat, label_Timer, 
		   id1, id2, id3, id4, q1, q2, q3, q4, q5, q6, answer_board;	
	JButton ready, exit, ready_done;
	ImageIcon bg, quiz2, spongeBob, ddoong, daram, jing, readyImg, exitImg, readyDoneImg;
	JTextPane tp;
	JTextField textField, answer, user1, user2, user3, user4;
	JScrollPane tsp, scrollPane;
	JTextArea textArea;

	Color navy = new Color(32, 56, 100);
	Color green = new Color(165, 214, 167); //�������� ���� ǥ��

	Socket s;
	String msg2 ="";
	int port = 3000;
	String playerName, playerScore, playerIdx; //������ �̸�,����, �ε��� ����
	boolean gameStart; //���� ���� ���� üũ
	MediaPlayer p; 

//	Main m;

	ClientPlay(){
		//this.m = m;
	//	Exam2 em = new Exam2(this);
		inIt();
		
	}

	void inIt(){
		//cp2 = getContentPane();
		loadImg(); 

		background = new JLabel(bg); 
		background.setBounds(0,0,970, 596); //770,500
		background.setOpaque(false);
		
		quiz_board = new JPanel(); //�������� ��(�ؽ�Ʈ ��ü �ؾ���)   new JPanel();
		quiz_board.setLayout(new GridLayout(2,3));
	//	quiz_board.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
	//	quiz_board.setHorizontalAlignment(SwingConstants.CENTER);
	//	quiz_board.setForeground(Color.WHITE);
		quiz_board.setBounds(240,75, 360,115);
		quiz_board.setOpaque(false);			
		quiz_board.setBackground(green);
		
		answer_board = new JLabel();
		answer_board.setVisible(true);
		answer_board.setBounds(240,75, 360,115);
		answer_board.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		answer_board.setHorizontalAlignment(SwingConstants.CENTER);
		answer_board.setForeground(Color.WHITE);
		background.add(answer_board);

		q1 = new JLabel();
		q1.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q1.setHorizontalAlignment(SwingConstants.CENTER);
		q1.setForeground(Color.WHITE);

		q2 = new JLabel();
		q2.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q2.setHorizontalAlignment(SwingConstants.CENTER);
		q2.setForeground(Color.WHITE);

		q3 = new JLabel();
		q3.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q3.setHorizontalAlignment(SwingConstants.CENTER);
		q3.setForeground(Color.WHITE);

		q4 = new JLabel();
		q4.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q4.setHorizontalAlignment(SwingConstants.CENTER);
		q4.setForeground(Color.WHITE);

		q5 = new JLabel();
		q5.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q5.setHorizontalAlignment(SwingConstants.CENTER);
		q5.setForeground(Color.WHITE);

		q6 = new JLabel();
		q6.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		q6.setHorizontalAlignment(SwingConstants.CENTER);
		q6.setForeground(Color.WHITE);

		quiz_board.add(q1); quiz_board.add(q2); quiz_board.add(q3);
		quiz_board.add(q4); quiz_board.add(q5); quiz_board.add(q6);
		background.add(quiz_board);

		answer = new JTextField(); //�����Է�â
		answer.setEditable(false); 
		answer.setVisible(false);
		answer.setBounds(320,205,140,30);
		answer.setOpaque(true);
		answer.setBackground(Color.white);
		answer.addActionListener(this);
		background.add(answer);

		sp = new JLabel();
//		sp.setVisible(false);
		sp.setBounds(50,326,157,136);
		sp.setOpaque(false);
		background.add(sp);
		user1 = new JTextField(); //user1 ������
      user1.setEditable(false);
      user1.setBounds(46,464,145,45);
      user1.setOpaque(true);
      user1.setBackground(navy);
      user1.setHorizontalAlignment(SwingConstants.CENTER);
      user1.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
	  user1.setForeground(Color.WHITE);
		background.add(user1);
		id1 = new JLabel(); //id â
		id1.setOpaque(false);
		id1.setFont(new Font("�����ٸ����", Font.PLAIN, 15));
		id1.setForeground(Color.WHITE);
		//id1.setBackground(Color.green);
		id1.setBounds(80,535,80,30);
		background.add(id1);

		dd = new JLabel();
//      dd.setVisible(false);
      dd.setBounds(211,326,157,136);
      dd.setOpaque(false);
      background.add(dd);
      user2 = new JTextField(); //user2 ������
      user2.setEditable(false);
      user2.setBounds(213,464,145,45);
      user2.setOpaque(true);
      user2.setBackground(navy);
      user2.setHorizontalAlignment(SwingConstants.CENTER);
      user2.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
	  user2.setForeground(Color.WHITE);
      background.add(user2);
      id2 = new JLabel(); //id â
      id2.setOpaque(false);
      id2.setFont(new Font("�����ٸ����", Font.PLAIN, 15));
      id2.setForeground(Color.WHITE);
      //id2.setBackground(Color.green);
      id2.setBounds(250,535,80,30);
      background.add(id2);

      da = new JLabel();
//      da.setVisible(false);
      da.setBounds(365,326,157,136);
      da.setOpaque(false);
      background.add(da);
      user3 = new JTextField(); //user3 ������
      user3.setEditable(false);
      user3.setBounds(383,464,145,45);
      user3.setOpaque(true);
      user3.setBackground(navy);
      user3.setHorizontalAlignment(SwingConstants.CENTER);
      user3.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
	  user3.setForeground(Color.WHITE);
      background.add(user3);
      id3 = new JLabel(); //id â
      id3.setOpaque(false);
      id3.setFont(new Font("�����ٸ����", Font.PLAIN, 15));
      id3.setForeground(Color.WHITE);
      //id3.setBackground(Color.green);
      id3.setBounds(415,535,80,30);
      background.add(id3);
		
		jj = new JLabel();
//		jj.setVisible(false);
		jj.setBounds(540,326,157,136);
		jj.setOpaque(false);
		background.add(jj);
		user4 = new JTextField(); //user4 ������
      user4.setEditable(false);
      user4.setBounds(550,464,145,45);
      user4.setOpaque(true);
      user4.setBackground(navy);
      user4.setHorizontalAlignment(SwingConstants.CENTER);
      user4.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
	  user4.setForeground(Color.WHITE);
      background.add(user4);
		id4 = new JLabel(); //id â
		id4.setOpaque(false);
		id4.setFont(new Font("�����ٸ����", Font.PLAIN, 15));
		id4.setForeground(Color.WHITE);
		//id4.setBackground(Color.green);
		id4.setBounds(584,535,80,30);
		background.add(id4);
		
		ready = new JButton(readyImg);
		ready.setBounds(745,20,180,60);
		ready.setOpaque(false);
		ready.setBorderPainted(false);
		ready.setContentAreaFilled(false);
		ready.addActionListener(this);
		background.add(ready);

		ready_done = new JButton(readyDoneImg); //ready �Ϸ� ��ư
		ready_done.setVisible(false);
		ready_done.setBounds(745,20,180,60);
		ready_done.setOpaque(false);
		ready_done.setBorderPainted(false);
		ready_done.setContentAreaFilled(false);
		ready_done.addActionListener(this);
		background.add(ready_done);

		textArea = new JTextArea(); //�Է¹��� ä��â
		textArea.setEditable(false); 
		tsp = new JScrollPane(textArea);
		tsp.setBounds(750,180,177,230);
		tsp.setOpaque(false);	
		background.add(tsp);

		textField = new JTextField(); //ä���Է�â
		textField.setEditable(true); 
		textField.setBounds(750,410,177,30);
		textField.setOpaque(true);
		textField.setBackground(Color.white);
	//	textField.addKeyListener(this);
		background.add(textField);

		exit = new JButton(exitImg);
		exit.setBounds(745,460,180,60);
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setOpaque(false);
		exit.addActionListener(this);
		background.add(exit);

		// Ÿ�̸� ����
		label_Timer = new JLabel("00 : 00");
		label_Timer.setHorizontalTextPosition(SwingConstants.CENTER);
		label_Timer.setHorizontalAlignment(SwingConstants.CENTER);
		label_Timer.setFont(new Font("�����ٸ����", Font.PLAIN, 35));
		label_Timer.setForeground(Color.WHITE);
		label_Timer.setBounds(735,88,200,70);
		background.add(label_Timer);

		add(background);
		
		setUI();

		startChat();
	}
	void setUI(){
		setTitle("SpongeBob Quiz");
		setSize(970, 596); //770,500
		//pack();
		setVisible(true);
		setLocation(200,100);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void loadImg(){
		try{
			File f = new File("imgs/(this)game_board2.png"); //���  
			BufferedImage bi = ImageIO.read(f);
			bg = new ImageIcon(bi);

			//quiz2 = new ImageIcon(ImageIO.read(new File("imgs/(this)quiz.png")));
			readyImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_ready.png")));
			exitImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_exit.png")));
			readyDoneImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_ready_done.png")));
		}catch(IOException ie){}
	}
/*	
	@Override
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == ready){
			ready.setVisible(false);
			ready_done.setVisible(true);
			
		} else if(obj == exit){
			int select = JOptionPane.showConfirmDialog(null, "���� �����ұ��?", "EXIT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(select == JOptionPane.YES_OPTION) System.exit(0);
		}
	}
*/	
	//ä�� ���� ����
	public void startChat(){
		String nickName = Main_new.nickName;
		String ip = Main_new.ip;

		try{
			Socket s = new Socket(ip, port);
			System.out.println("���� ���� ����!");
			Send Send = new Send(s, nickName); //�������� ����, id������ ��Ŭ
			Listen listen = new Listen(s); //�������� �޴� ��Ŭ
	//		Exam2 exam2 = new Exam2(s, nickName);
			new Thread(Send).start();
			new Thread(listen).start();
			

			//�̺�Ʈ ������ �߰�
			textField.addKeyListener(new Send(s, nickName));
			ready.addActionListener(new Send(s, nickName));
		}catch(UnknownHostException uhe){
			//JOptionPane.showMessageDialog(null, "ȣ��Ʈ�� ã�� �� ����", "ERROR", JOption_.WARNING_MESSAGE);
		}catch(IOException ie){
			JOptionPane.showMessageDialog(null, "���� ���� ����!");
			System.exit(0);
		}
	}
	
	//���� ��ư �׼� �̺�Ʈ ó��
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == exit){
			int select = JOptionPane.showConfirmDialog(null, "���� �����ұ��?", "EXIT", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if(select == JOptionPane.YES_OPTION) System.exit(0);	
		}
	}
	
	//���� Ŭ���� - �۽�
	class Send extends Thread implements KeyListener, ActionListener {
		DataOutputStream dos; 
		Socket s; //������ ���
		String nickName;

		Send(Socket s, String nickName){ //���ϰ� id�� ��������
			this.s = s;
			try{
				dos = new DataOutputStream(this.s.getOutputStream());
				this.nickName = nickName;
			}catch(IOException ie){}
		}

		public void run(){
			try{
				dos.writeUTF(nickName);
			}catch(IOException ie){}
		}

		public void actionPerformed(ActionEvent e){
				
			if(e.getSource() == ready){
				try{
					dos.writeUTF("//Chat " + "[ " + nickName + " �� �غ� �Ϸ�! ]");
					dos.flush();
					dos.writeUTF("//Ready");
					dos.flush();
					ready.setVisible(false);
				    ready_done.setVisible(true);
				    ready_done.setEnabled(false);
					
				//	dos.writeUTF("//Answer" + nickName + " : "  + ans);
				//	dos.flush(); 
				}catch(IOException ie){}
			}
		}
		
		public void keyReleased(KeyEvent e){ // ä�� �Է�
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String chat = textField.getText();
				textField.setText("");
				try{
					dos.writeUTF("//Chat " + nickName + " : " + chat);
					dos.flush();
				}catch(IOException io){}
			}
		}
		public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e){}
	}

	//���� Ŭ���� - ����
	class Listen extends Thread {
		Socket s;
		DataInputStream dis;

		Listen(Socket s){ //������ �޾ƿ´�
			this.s = s;
			try{
				dis = new DataInputStream(this.s.getInputStream());
			}catch(IOException ie){}
		}
		public void run(){
			while(dis != null){
				try{
					String msg = dis.readUTF();
					if( msg.startsWith("//CList")) { //�������� : Ŭ���̾�Ʈ��� ����
						playerName = msg.substring(7, msg.indexOf(" "));
						playerScore = msg.substring(msg.indexOf(" ") + 1, msg.indexOf("#"));
						playerIdx = msg.substring(msg.indexOf("#") + 1);
						updateClientList(); //Ŭ���̾�Ʈ ��� ����
						bgm("//Play"); 
					}else if( msg.startsWith("//Start")){
						gameStart = true;
						ready.setEnabled(false);
						ready.setVisible(false);
						ready_done.setVisible(true);
						ready_done.setEnabled(false);
					}else if( msg.equals("//GmEnd")) {
							gameStart = false;
							quiz_board.setEnabled(true);
							textField.setEnabled(true);
							ready_done.setVisible(true);
							ready_done.setEnabled(false);
							bgm("//Stop");
					}else if( msg.startsWith("//Exam")){ 
						String msg2 = msg.substring(6);
						Thread Exam2 = new Exam2(msg2);
						Exam2.start();
					}else if( msg.startsWith("//Timer")){ //Ÿ�̸� �ð� ǥ��
						//label_Timer.setText(msg.substring(7));
						Thread Time2 = new Time2();
						Time2.start();
					}else{
						textArea.append(msg+ "\n");
						//scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
					}
				}catch(IOException ie){
					textArea.append("������ �� ������ ���������ϴ�. 3�� �� ������ ����˴ϴ�");
					try{
						Thread.sleep(3000);
						System.exit(0);
					}catch(InterruptedException ite){}
				}
			}
		}
		  public void updateClientList(){
			 ImageIcon character;
			 if(Integer.parseInt(playerIdx) == 0){ //1��° ������
				id1.setText(playerName);
				id1.setHorizontalAlignment(SwingConstants.CENTER);
				character = new ImageIcon("imgs/(this)game_s.png");
				character.getImage().flush();
				sp.setIcon(character);

				//label_Client1.setIcon(ii);
			//	user1.setText("[" + playerName + " / " + "����: " + playerScore + "]");
				user1.setText(playerScore+" ��");
			 }else if(Integer.parseInt(playerIdx) == 1){
				id2.setText(playerName);
				id2.setHorizontalAlignment(SwingConstants.CENTER);
				character = new ImageIcon("imgs/(this)game_b.png");
				character.getImage().flush();
				dd.setIcon(character);
				//deleteClientList();

			//	user2.setText("[" + playerName + " / " + "����: " + playerScore + "]");
				user2.setText(playerScore+" ��");
			 }else if(Integer.parseInt(playerIdx) == 2){
				id3.setText(playerName);
				id3.setHorizontalAlignment(SwingConstants.CENTER);
				character = new ImageIcon("imgs/(this)game_d.png");
				character.getImage().flush();
				da.setIcon(character);
				//deleteClientList();
			
			//	user3.setText("[" + playerName + " / " + "����: " + playerScore + "]");
				user3.setText(playerScore+" ��");
			  }else if(Integer.parseInt(playerIdx) == 3){
				id4.setText(playerName);
				id4.setHorizontalAlignment(SwingConstants.CENTER);
				character = new ImageIcon("imgs/(this)game_o.png");
				character.getImage().flush();
				jj.setIcon(character);
				//deleteClientList();

			//	user4.setText("[" + playerName + " / " + "����: " + playerScore + "]");
				user4.setText(playerScore+" ��");
			 }
		  }

		public void deleteClientList(){
			ImageIcon deleteCharacter;
			if(Integer.parseInt(playerIdx) == 0){ //1��° ������
				deleteCharacter = new ImageIcon("imgs/(this)game_s_empty.png");
				deleteCharacter.getImage().flush();
				sp.setIcon(deleteCharacter);
			}else if(Integer.parseInt(playerIdx) == 1){
				deleteCharacter = new ImageIcon("imgs/(this)game_b_empty.png");
				deleteCharacter.getImage().flush();
				dd.setIcon(deleteCharacter);
			}else if(Integer.parseInt(playerIdx) == 2){
				deleteCharacter = new ImageIcon("imgs/(this)game_d_empty.png");
				deleteCharacter.getImage().flush();
				dd.setIcon(deleteCharacter);
			}else if(Integer.parseInt(playerIdx) == 3){
				deleteCharacter = new ImageIcon("imgs/(this)game_o_empty2.png");
				deleteCharacter.getImage().flush();
				jj.setIcon(deleteCharacter);
			}
	}

	    public void bgm(String play){ // BGM ��� & ����
         try{
            if(play.equals("//Play")){//���� ����Ҹ�
               JFXPanel panel = new JFXPanel();
               File a = new File("�������� ȿ����\\Ǯ��������ۺ��Ҹ�.mp3");
               Media bgm = new Media(a.toURI().toURL().toString());
               p = new MediaPlayer(bgm);
               p.play();
            }else if(play.equals("//Stop")){
               p.stop();
               p.setMute(true);
               p.dispose();
            }
         }catch(Exception e){}
      }
	}	


 class Exam2 extends Thread {
	//ClientPlay cp;
	String str;

	Exam2(String str){
		this.str = str;
	}

//	ClientPlay.Listen cpl = new ClientPlay().new Listen(s);
			public void run(){	
				if(gameStart = true){
						String temp2[];
						//	String msg2 = msg.substring(6);
							temp2 = str.split("/");

							String[] quizQ = new String[56];
							String[] quizQL = new String[48];
							String[] quizA = new String[8];

							int k=0;
							for(String strTemp : temp2){
								quizQ[k] = strTemp;
					//			System.out.println(quizQ[k]);
								k++;
							}
							

					//		q1.setText(quizQ[0]); timeSleep(); 
					//		q2.setText(quizQ[1]); timeSleep();
					//		q3.setText(quizQ[2]); timeSleep(); 
					//		q4.setText(quizQ[3]); timeSleep();	
					//		q5.setText(quizQ[4]); timeSleep(); 
					//		q6.setText(quizQ[5]); timeSleep();
					//		try{ Thread.sleep(1000); }catch(Exception e){}
					//		timeSleep();

							quizA[0] = quizQ[6];
							quizA[1] = quizQ[13];
							quizA[2] = quizQ[20];
							quizA[3] = quizQ[27];
							quizA[4] = quizQ[34];
							quizA[5] = quizQ[41];
							quizA[6] = quizQ[48];
							quizA[7] = quizQ[55];

							int v = 0;
							for(int e=0; e<6; e++){
								quizQL[v] = quizQ[e];
								v++;
							}

							int m = 6;
							for(int t=7; t<13; t++){
								quizQL[m] = quizQ[t];
								m++;
							}

							int n = 12;
							for(int d=14; d<20; d++){
								quizQL[n] = quizQ[d];
								n++;
							}

							int s = 18;
							for(int f=21; f<27; f++){
								quizQL[s] = quizQ[f];
								s++;
							}

							int b = 24;
							for(int q=28; q<34; q++){
								quizQL[b] = quizQ[q];
								b++;
							}

							int u = 30;
							for(int o=35; o<41; o++){
								quizQL[u] = quizQ[o];
								u++;
							}

							int z = 36;
							for(int q=42; q<48; q++){
								quizQL[z] = quizQ[q];
								z++;
							}

							int x = 42;
							for(int o=49; o<55; o++){
								quizQL[x] = quizQ[o];
								x++;
							}
					
							q1.setText(quizQ[0]); timeSleep(); 
							q2.setText(quizQ[1]); timeSleep();
							q3.setText(quizQ[2]); timeSleep(); 
							q4.setText(quizQ[3]); timeSleep();	
							q5.setText(quizQ[4]); timeSleep(); 
							q6.setText(quizQ[5]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							quiz_board.setVisible(false);
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[6]); 
							timeSleep();
							
							answer_board.setText("");
							answer_board.setVisible(false);
							quiz_board.setVisible(true);
							q1.setText(""); q2.setText(""); q3.setText(""); 
							q4.setText("");	q5.setText(""); q6.setText("");
							timeSleep();
							q1.setText(quizQ[7]); timeSleep(); 
							q2.setText(quizQ[8]);timeSleep();
							q3.setText(quizQ[9]); timeSleep(); 
							q4.setText(quizQ[10]); timeSleep();	
							q5.setText(quizQ[11]); timeSleep(); 
							q6.setText(quizQ[12]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[13]); 
							quiz_board.setVisible(false);



							q1.setText(quizQ[13]); timeSleep(); 
							q2.setText(quizQ[14]); timeSleep();
							q3.setText(quizQ[15]); timeSleep(); 
							q4.setText(quizQ[16]); timeSleep();	
							q5.setText(quizQ[17]); timeSleep(); 
							q6.setText(quizQ[18]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							quiz_board.setVisible(false);
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[6]); 
							timeSleep();
							
							answer_board.setText("");
							answer_board.setVisible(false);
							quiz_board.setVisible(true);
							q1.setText(""); q2.setText(""); q3.setText(""); 
							q4.setText("");	q5.setText(""); q6.setText("");
							timeSleep();
							q1.setText(quizQ[19]); timeSleep(); 
							q2.setText(quizQ[20]);timeSleep();
							q3.setText(quizQ[21]); timeSleep(); 
							q4.setText(quizQ[22]); timeSleep();	
							q5.setText(quizQ[23]); timeSleep(); 
							q6.setText(quizQ[24]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[13]); 
							quiz_board.setVisible(false);

							q1.setText(quizQ[25]); timeSleep(); 
							q2.setText(quizQ[26]); timeSleep();
							q3.setText(quizQ[27]); timeSleep(); 
							q4.setText(quizQ[30]); timeSleep();	
							q5.setText(quizQ[31]); timeSleep(); 
							q6.setText(quizQ[32]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							quiz_board.setVisible(false);
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[6]); 
							timeSleep();
							
							answer_board.setText("");
							answer_board.setVisible(false);
							quiz_board.setVisible(true);
							q1.setText(""); q2.setText(""); q3.setText(""); 
							q4.setText("");	q5.setText(""); q6.setText("");
							timeSleep();
							q1.setText(quizQ[33]); timeSleep(); 
							q2.setText(quizQ[34]);timeSleep();
							q3.setText(quizQ[35]); timeSleep(); 
							q4.setText(quizQ[36]); timeSleep();	
							q5.setText(quizQ[37]); timeSleep(); 
							q6.setText(quizQ[38]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[13]); 
							quiz_board.setVisible(false);

							q1.setText(quizQ[39]); timeSleep(); 
							q2.setText(quizQ[40]); timeSleep();
							q3.setText(quizQ[41]); timeSleep(); 
							q4.setText(quizQ[42]); timeSleep();	
							q5.setText(quizQ[43]); timeSleep(); 
							q6.setText(quizQ[44]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							quiz_board.setVisible(false);
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[6]); 
							timeSleep();
							
							answer_board.setText("");
							answer_board.setVisible(false);
							quiz_board.setVisible(true);
							q1.setText(""); q2.setText(""); q3.setText(""); 
							q4.setText("");	q5.setText(""); q6.setText("");
							timeSleep();
							q1.setText(quizQ[45]); timeSleep(); 
							q2.setText(quizQ[46]);timeSleep();
							q3.setText(quizQ[47]); timeSleep(); 
							q4.setText(quizQ[48]); timeSleep();	
							q5.setText(quizQ[49]); timeSleep(); 
							q6.setText(quizQ[50]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							answer_board.setVisible(true);
							answer_board.setText("����: "+quizQ[13]); 
							quiz_board.setVisible(false);
				}
			}
				void timeSleep(){
						try{
								Thread.sleep(850);
						}catch(Exception e){}
					}
			
			}	

	public class Time2 extends Thread {
		int time = 60;
		
		@Override
			public void run(){
			while(true){
				try{
					showSec();
					sleep(1000);
				
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}

		void showSec(){
			time--;
			label_Timer.setText(time+"��");
			if(time == 0){
				System.exit(0);
			}
		}
	}

}

/*
 class Exam2 extends Thread {
	ClientPlay cp;
	Socket s;

	Exam2(ClientPlay cp){
		this.cp = cp;
		this.s = cp.s;
	}
	ClientPlay.Listen cpl = new ClientPlay().new Listen(s);

		String msg2 = cpl.msg2;
			public void run(){	
						String temp2[];
						temp2 = msg2.split("/");
							String[] quizQL = new String[48];
							String[] quizA = new String[8];
							String[] quizQ = new String[56];

							int k=0;
							for(String strTemp : temp2){
								quizQ[k] = strTemp;
								System.out.println(quizQ[k]);
								k++;
							}

							quizA[0] = quizQ[6];
							quizA[1] = quizQ[13];
							quizA[2] = quizQ[20];
							quizA[3] = quizQ[27];
							quizA[4] = quizQ[34];
							quizA[5] = quizQ[41];
							quizA[6] = quizQ[48];
							quizA[7] = quizQ[55];

							int v = 0;
							for(int e=0; e<6; e++){
								quizQL[v] = quizQ[e];
								v++;
							}

							int m = 6;
							for(int t=7; t<13; t++){
								quizQL[m] = quizQ[t];
								m++;
							}

							int n = 12;
							for(int d=14; d<20; d++){
								quizQL[n] = quizQ[d];
								n++;
							}

							int s = 18;
							for(int f=21; f<27; f++){
								quizQL[s] = quizQ[f];
								s++;
							}

							int b = 24;
							for(int q=28; q<34; q++){
								quizQL[b] = quizQ[q];
								b++;
							}

							int u = 30;
							for(int o=35; o<41; o++){
								quizQL[u] = quizQ[o];
								u++;
							}

							int z = 36;
							for(int q=42; q<48; q++){
								quizQL[z] = quizQ[q];
								z++;
							}

							int x = 42;
							for(int o=49; o<55; o++){
								quizQL[x] = quizQ[o];
								x++;
							}
					
							cp.q1.setText(quizQ[0]); timeSleep(); 
							cp.q2.setText(quizQ[1]); timeSleep();
							cp.q3.setText(quizQ[2]); timeSleep(); 
							cp.q4.setText(quizQ[3]); timeSleep();	
							cp.q5.setText(quizQ[4]); timeSleep(); 
							cp.q6.setText(quizQ[5]); timeSleep();
							try{ Thread.sleep(1000); }catch(Exception e){}
							cp.quiz_board.setVisible(false);
							cp.answer_board.setVisible(true);
							cp.answer_board.setText("����: "+quizQ[6]); 
							timeSleep();
							
							cp.answer_board.setText("");
							cp.answer_board.setVisible(false);
							cp.quiz_board.setVisible(true);
							cp.q1.setText(""); cp.q2.setText(""); cp.q3.setText(""); 
							cp.q4.setText(""); cp.q5.setText(""); cp.q6.setText("");
						//	timeSleep();
							cp.q1.setText(quizQ[7]);// timeSleep(); 
							cp.q2.setText(quizQ[8]); //timeSleep();
							cp.q3.setText(quizQ[9]); //timeSleep(); 
							cp.q4.setText(quizQ[10]);// timeSleep();	
							cp.q5.setText(quizQ[11]); //timeSleep(); 
							cp.q6.setText(quizQ[12]); //timeSleep();
						//	try{ Thread.sleep(1000); }catch(Exception e){}
							cp.answer_board.setVisible(true);
							cp.answer_board.setText("����: "+quizA[7]); 
							cp.quiz_board.setVisible(false);
				}
				void timeSleep(){
						try{
								Thread.sleep(850);
						}catch(Exception e){}
					}

			}	





/*						System.out.println(quizQ[6]);
						System.out.println(quizQ[13]);
						System.out.println(quizQ[20]);
						System.out.println(quizQ[27]);
						System.out.println(quizQ[34]);
						System.out.println(quizQ[41]);
						System.out.println(quizQ[48]);
						System.out.println(quizQ[55]);*/
						//�丸 �̾Ƴ���
/*						for(int i=0; i<quizQ.size(); i++){
							if(!quizQ.isEmpty()){
								if((i%7)==0){
								//	System.out.println(quizQ.get(i+6));
									quizA.add(quizQ.get(i+6)); //�丸 �����迭�� ����
								} 
							}
						}  */
						//������ �̾Ƴ���
					//	for(int i=0; i<quizQ.size(); i++){
					//		if(!quizQ.isEmpty()){
					//			 if((i%7) != 0){
									//System.out.println(quizQ.get(i+6));
					//				quizQL.add(quizQ.get(i+6)); //������ �����迭�� ����
									//System.out.println(quizQL);
									
					//			}
					//		}
					//}