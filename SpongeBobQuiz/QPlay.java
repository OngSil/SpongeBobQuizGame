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

class QPlay extends JFrame implements ActionListener {
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
	Color green = new Color(165, 214, 167); //문제내는 보드 표시

	Socket s;
	String ip = QMain.ip;
	String nickName = QMain.nickName;
	int port = 5555;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	String playerName, playerIdx, beReady; //접속자 이름,점수, 인덱스 관리
	String exitClient = ""; //나간 사람
	int playerScore = 0;
	public static boolean gameStart; //게임 시작 상태 체크
	public static int round = 1;
	MediaPlayer p; 
	QMain qm;


	QPlay(QMain qm){
		this.qm = qm;

		init();
		startChat();
	}

	void init(){
		//cp2 = getContentPane();
		loadImg(); 

		background = new JLabel(bg); 
		background.setBounds(0,0,970, 596); //770,500
		background.setOpaque(false);
		
		quiz_board = new JPanel(); //문제내는 판(텍스트 교체 해야함)   new JPanel();
		quiz_board.setLayout(new GridLayout(2,3));
	//	quiz_board.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
	//	quiz_board.setHorizontalAlignment(SwingConstants.CENTER);
	//	quiz_board.setForeground(Color.WHITE);
		quiz_board.setBounds(240,75, 360,115);
		quiz_board.setOpaque(false);			
		quiz_board.setBackground(green);
		
		answer_board = new JLabel();
		answer_board.setVisible(true);
		answer_board.setBounds(240,75, 360,115);
		answer_board.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		answer_board.setHorizontalAlignment(SwingConstants.CENTER);
		answer_board.setForeground(Color.WHITE);
		background.add(answer_board);

		q1 = new JLabel();
		q1.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q1.setHorizontalAlignment(SwingConstants.CENTER);
		q1.setForeground(Color.WHITE);

		q2 = new JLabel();
		q2.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q2.setHorizontalAlignment(SwingConstants.CENTER);
		q2.setForeground(Color.WHITE);

		q3 = new JLabel();
		q3.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q3.setHorizontalAlignment(SwingConstants.CENTER);
		q3.setForeground(Color.WHITE);

		q4 = new JLabel();
		q4.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q4.setHorizontalAlignment(SwingConstants.CENTER);
		q4.setForeground(Color.WHITE);

		q5 = new JLabel();
		q5.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q5.setHorizontalAlignment(SwingConstants.CENTER);
		q5.setForeground(Color.WHITE);

		q6 = new JLabel();
		q6.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		q6.setHorizontalAlignment(SwingConstants.CENTER);
		q6.setForeground(Color.WHITE);

		quiz_board.add(q1); quiz_board.add(q2); quiz_board.add(q3);
		quiz_board.add(q4); quiz_board.add(q5); quiz_board.add(q6);
		background.add(quiz_board);

		answer = new JTextField(); //정답입력창
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
		user1 = new JTextField(); //user1 점수판
        user1.setEditable(false);
        user1.setBounds(46,464,145,45);
        user1.setOpaque(true);
        user1.setBackground(navy);
        user1.setHorizontalAlignment(SwingConstants.CENTER);
        user1.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
	    user1.setForeground(Color.WHITE);
		background.add(user1);
		id1 = new JLabel(); //id 창
		id1.setOpaque(false);
		id1.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
		id1.setForeground(Color.WHITE);
		//id1.setBackground(Color.green);
		id1.setBounds(80,535,80,30);
		background.add(id1);

		dd = new JLabel();
//      dd.setVisible(false);
        dd.setBounds(211,326,157,136);
        dd.setOpaque(false);
        background.add(dd);
        user2 = new JTextField(); //user2 점수판
        user2.setEditable(false);
        user2.setBounds(213,464,145,45);
        user2.setOpaque(true);
        user2.setBackground(navy);
        user2.setHorizontalAlignment(SwingConstants.CENTER);
        user2.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
	    user2.setForeground(Color.WHITE);
        background.add(user2);
        id2 = new JLabel(); //id 창
        id2.setOpaque(false);
        id2.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
        id2.setForeground(Color.WHITE);
        //id2.setBackground(Color.green);
        id2.setBounds(250,535,80,30);
        background.add(id2);

        da = new JLabel();
//      da.setVisible(false);
        da.setBounds(365,326,157,136);
        da.setOpaque(false);
        background.add(da);
        user3 = new JTextField(); //user3 점수판
        user3.setEditable(false);
        user3.setBounds(383,464,145,45);
        user3.setOpaque(true);
        user3.setBackground(navy);
        user3.setHorizontalAlignment(SwingConstants.CENTER);
        user3.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
	    user3.setForeground(Color.WHITE);
        background.add(user3);
        id3 = new JLabel(); //id 창
        id3.setOpaque(false);
        id3.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
        id3.setForeground(Color.WHITE);
        //id3.setBackground(Color.green);
        id3.setBounds(415,535,80,30);
        background.add(id3);
		
		jj = new JLabel();
//		jj.setVisible(false);
		jj.setBounds(540,326,157,136);
		jj.setOpaque(false);
		background.add(jj);
		user4 = new JTextField(); //user4 점수판
        user4.setEditable(false);
        user4.setBounds(550,464,145,45);
        user4.setOpaque(true);
        user4.setBackground(navy);
        user4.setHorizontalAlignment(SwingConstants.CENTER);
        user4.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
    	user4.setForeground(Color.WHITE);
        background.add(user4);
		id4 = new JLabel(); //id 창
		id4.setOpaque(false);
		id4.setFont(new Font("나눔바른고딕", Font.PLAIN, 15));
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

		ready_done = new JButton(readyDoneImg); //ready 완료 버튼
		ready_done.setVisible(false);
		ready_done.setBounds(745,20,180,60);
		ready_done.setOpaque(false);
		ready_done.setBorderPainted(false);
		ready_done.setContentAreaFilled(false);
		ready_done.addActionListener(this);
		background.add(ready_done);

		textArea = new JTextArea(); //입력받을 채팅창
		textArea.setEditable(false); 
		textArea.setLineWrap(true); //자동줄바꿈
		tsp = new JScrollPane(textArea);
		tsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		tsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tsp.setBounds(750,180,177,230);
		tsp.setOpaque(false);	
		background.add(tsp);

		textField = new JTextField(); //채팅입력창
		textField.setEditable(true); 
		textField.setBounds(750,410,177,30);
		textField.setOpaque(true);
		textField.setBackground(Color.white);
		
		background.add(textField);

		exit = new JButton(exitImg);
		exit.setBounds(745,460,180,60);
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setOpaque(false);
		exit.addActionListener(this);
		background.add(exit);

		// 타이머 영역
		label_Timer = new JLabel("00 : 00");
		label_Timer.setHorizontalTextPosition(SwingConstants.CENTER);
		label_Timer.setHorizontalAlignment(SwingConstants.CENTER);
		label_Timer.setFont(new Font("나눔바른고딕", Font.PLAIN, 35));
		label_Timer.setForeground(Color.WHITE);
		label_Timer.setBounds(735,88,200,70);
		background.add(label_Timer);

		add(background);
		
		setUI();
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
			File f = new File("imgs/(this)game_board2.png"); //배경  
			BufferedImage bi = ImageIO.read(f);
			bg = new ImageIcon(bi);

			//quiz2 = new ImageIcon(ImageIO.read(new File("imgs/(this)quiz.png")));
			readyImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_ready.png")));
			exitImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_exit.png")));
			readyDoneImg = new ImageIcon(ImageIO.read(new File("imgs/(this)game_ready_done.png")));
		}catch(IOException ie){}
	}
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == exit){
			int select = JOptionPane.showConfirmDialog(
				null, "게임 그만할거얌?", "게임 종료", 
				JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.PLAIN_MESSAGE, null);
			if(select == 0){
				System.exit(0);
			}
		} 
	}
	public void startChat(){ //채팅 소켓 생성
		try{ //소켓 생성
			s = new Socket(ip, port);
			System.out.println("서버 연결 성공했으므니다!");
			is = s.getInputStream(); //소켓의 내용 가져오기 위해
			os = s.getOutputStream(); //소켓으로 내용 내보내기 위해
			dis = new DataInputStream(is); //데이터 스트림 형식으로 가져올거임			
			dos = new DataOutputStream(os); //데이터 스트림 형식으로 내보낼거임ㅋ

			textArea.append("스폰지밥 퀴즈 게임에 오신것을 환영합니다"+"\n");
			Listen listen = new Listen();
			Speak speak = new Speak();
			new Thread(listen).start();
			new Thread(speak).start();

			textField.addKeyListener(speak);
			ready.addActionListener(speak);
		}catch(UnknownHostException uhe){
			JOptionPane.showMessageDialog(null, "호스트 어딨음?", "에러ㄷㄷ", JOptionPane.WARNING_MESSAGE);
		}catch(IOException ie){
			JOptionPane.showMessageDialog(null, "서버에 접속할 수 없음요", "서버 에러ㄷㄷ", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
	class Listen extends Thread {
		Listen(){
		//	start();
		}
		public void run(){
			String msg = "";
			while(true){
				try{
					msg = dis.readUTF();
					if (msg.startsWith("//CList")){
						playerName = msg.substring(7, msg.indexOf("#"));
					
						playerIdx = msg.substring(msg.indexOf("#") + 1);
						updateClientList(); //클라이언트 목록 갱신
					} else if(msg.startsWith("//Start")){
						gameStart = true;
					///	updateScore();
					} else if(msg.startsWith("//Ready")){
						try{
							dos.writeUTF("//Start");
							dos.flush();
						}catch(IOException ie){}
					} else if(msg.startsWith("//Exam")){
						String em = msg.substring(6);
						Thread Exam2 = new Exam2(em);
						Exam2.start();
					} else if(msg.startsWith("//Exit")){ //나간 클라이언트 체크
						exitClient = msg.substring(6);
						deleteClientList(exitClient);
						System.out.println("exitClient: "+msg);
					} else if(msg.startsWith("//Score")){
						String msg3 = msg.substring(7);
						String listSize = msg3.substring(0, msg3.indexOf("/")); //-> 0~1/
						String nam = msg3.substring(msg3.indexOf("/")+1); //-> 2/3/4/
						
						
						String score, score2, score3, score4;
	
						if(listSize.trim().equals("2")){
							score = nam.substring(0, nam.indexOf("/")); //-> 0~1/
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/
							System.out.println("nam: "+nam);

							score2 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/
							System.out.println("nam: "+nam);

							user1.setText("점수: "+score);
							user2.setText("점수: "+score2);	
						}else if(listSize.trim().equals("3")){
							score = nam.substring(0, nam.indexOf("/")); //-> 0~1/
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/

							score2 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/
							
							score3 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/

							user1.setText("점수: "+score);
							user2.setText("점수: "+score2);
							user3.setText("점수: "+score3);
						}else if(listSize.trim().equals("4")){
							score = nam.substring(0, nam.indexOf("/")); //-> 0~1/
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/

							score2 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/
							
							score3 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/

							score4 = nam.substring(0, nam.indexOf("/"));
							nam = nam.substring(nam.indexOf("/")+1); //-> 3/4/

							user1.setText("점수: "+score);
							user2.setText("점수: "+score2);
							user3.setText("점수: "+score3);
							user3.setText("점수: "+score4);
						}
						
					

					//	user3.setText("점수: "+score3);
					//	user4.setText("점수: "+score4);  
					}else{
						textArea.append(msg+"\n");
						//tsp.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
					}
					
				}catch(IOException ie){}
			}
			
		}
		
		
		HashMap<String, Integer> realClientlist = new HashMap<String, Integer>();

		//한명씩 들어왔을때
		public void updateClientList(){
			ImageIcon character;
			if(Integer.parseInt(playerIdx) == 0){
				id1.setText(playerName);
				id1.setHorizontalAlignment(SwingConstants.CENTER);
				character = new ImageIcon("imgs/(this)game_s.png");
				character.getImage().flush();
				sp.setIcon(character);
				realClientlist.put(playerName,0);
		//		deleteClientList();
			}else if(Integer.parseInt(playerIdx) == 1){
				id2.setText(playerName);
				id2.setHorizontalAlignment(SwingConstants.CENTER);
				character  = new ImageIcon("imgs/(this)game_b.png");
				character.getImage().flush();
				dd.setIcon(character);
				realClientlist.put(playerName,1);
			//	deleteClientList();
			}else if(Integer.parseInt(playerIdx) == 2){
				id3.setText(playerName);
				id3.setHorizontalAlignment(SwingConstants.CENTER);
				character  = new ImageIcon("imgs/(this)game_d.png");
				character.getImage().flush();
				da.setIcon(character);
				realClientlist.put(playerName,2);
			//	deleteClientList();
			}else if(Integer.parseInt(playerIdx) == 3){
				id4.setText(playerName);
				id4.setHorizontalAlignment(SwingConstants.CENTER);
				character  = new ImageIcon("imgs/(this)game_o.png");
				character.getImage().flush();
				jj.setIcon(character);
				realClientlist.put(playerName,3);
			//	deleteClientList();
			}
		}



		// 나갈때 캐릭터 없애기
		// -> 나가면 서버한테 "얘 나감" 메시지 보내고
		// -> 그럼 서버가 모듈한테 "얘 나갔대" 보내고
		// -> 모듈이 클라이언트한테 "얘 나감"
		// -> 클라이언트는 "캐릭터 업데이트"
		public void deleteClientList(String outClient){
			ImageIcon deleteCharacter1, deleteCharacter2, deleteCharacter3, deleteCharacter4;

			Iterator<String> keys = realClientlist.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				if(key.equals(outClient)){
					realClientlist.get(key);
					System.out.println("나간사람 index: "+realClientlist.get(key));
					if(realClientlist.get(key) == 0){
						deleteCharacter1 = new ImageIcon("imgs/(this)game_s_empty.png");
						deleteCharacter1.getImage().flush();
						sp.setIcon(deleteCharacter1);
					}else if(realClientlist.get(key) == 1){
						deleteCharacter2 = new ImageIcon("imgs/(this)game_b_empty.png");
						deleteCharacter2.getImage().flush();
						dd.setIcon(deleteCharacter2);
					}else if(realClientlist.get(key) == 2){
						deleteCharacter3 = new ImageIcon("imgs/(this)game_d_empty.png");
						deleteCharacter3.getImage().flush();
						da.setIcon(deleteCharacter3);
					}else if(realClientlist.get(key) == 3){
						deleteCharacter4 = new ImageIcon("imgs/(this)game_o_empty.png");
						deleteCharacter4.getImage().flush();
						jj.setIcon(deleteCharacter4);
					}
				} 
			}
			/*for (Entry<Integer, String> entry : realClientlist.entrySet()) {
				//System.out.println("[Key]:" + entry.getKey() + " [Value]:" + entry.getValue());
				if(entry.getValue().equals(outClient)){
					
				}
			}*/
			/*	
			if(Integer.parseInt(playerIdx) == 0){ //1번째 접속자
			//	deleteCharacter = new ImageIcon("imgs/(this)game_s_empty.png");
			//	deleteCharacter.getImage().flush();
			//	sp.setIcon(deleteCharacter);
				
				deleteCharacter2 = new ImageIcon("imgs/(this)game_b_empty.png");
				deleteCharacter2.getImage().flush();
				dd.setIcon(deleteCharacter2);

				deleteCharacter3 = new ImageIcon("imgs/(this)game_d_empty.png");
				deleteCharacter3.getImage().flush();
				da.setIcon(deleteCharacter3);

				deleteCharacter4 = new ImageIcon("imgs/(this)game_o_empty2.png");
				deleteCharacter4.getImage().flush();
				jj.setIcon(deleteCharacter4);
			}else if(Integer.parseInt(playerIdx) == 1){
			//	deleteCharacter = new ImageIcon("imgs/(this)game_b_empty.png");
			//	deleteCharacter.getImage().flush();
			//	dd.setIcon(deleteCharacter);

				deleteCharacter3 = new ImageIcon("imgs/(this)game_d_empty.png");
				deleteCharacter3.getImage().flush();
				da.setIcon(deleteCharacter3);

				deleteCharacter4 = new ImageIcon("imgs/(this)game_o_empty2.png");
				deleteCharacter4.getImage().flush();
				jj.setIcon(deleteCharacter4);
			}else if(Integer.parseInt(playerIdx) == 2){
			//	deleteCharacter = new ImageIcon("imgs/(this)game_d_empty.png");
			//	deleteCharacter.getImage().flush();
			//	da.setIcon(deleteCharacter);

				deleteCharacter4 = new ImageIcon("imgs/(this)game_o_empty2.png");
				deleteCharacter4.getImage().flush();
				jj.setIcon(deleteCharacter4);
			}//else if(Integer.parseInt(playerIdx) == 3){
			//	deleteCharacter = new ImageIcon("imgs/(this)game_o_empty2.png");
			//	deleteCharacter.getImage().flush();
			//	jj.setIcon(deleteCharacter);
		//	}
			*/
	}
}
	class Exam2 extends Thread {
		String str;

		Exam2(String str){
			this.str = str;
		}

		ArrayList<String> exList = new ArrayList<String>();
		ArrayList<String> quizList = new ArrayList<String>();
		ArrayList<String> answerList = new ArrayList<String>();
		public void run(){
			String temp2[];
			//답만 뽑아낸 리스트
			
			if(gameStart = true){
				temp2 = str.split("/");
				for(String strTemp : temp2){
					exList.add(strTemp);
				}
				for(int j=1; j<exList.size(); j++){
					if( j%7 == 0 ){
						answerList.add(exList.get(j-1));
					} else if( j%7 != 0 ){
						quizList.add(exList.get(j-1));
						// continue;
					}
				}

				int c = 0; int d = 0;
		//		if(round == 1){
					q1.setText(quizList.get(c)); c++; timeSleep();
					q2.setText(quizList.get(c)); c++; timeSleep();
					q3.setText(quizList.get(c)); c++; timeSleep();
					q4.setText(quizList.get(c)); c++; timeSleep();
					q5.setText(quizList.get(c)); c++; timeSleep();
					q6.setText(quizList.get(c)); c++; timeSleep();
					quiz_board.setVisible(false);try{ Thread.sleep(1000); }catch(Exception e){};
					answer_board.setVisible(true);
					answer_board.setText("정답: "+answerList.get(d)); timeSleep(); d++;
					answer_board.setVisible(false);
				//	round++;
			//	} else if(round == 2){
					quiz_board.setVisible(true);
					q1.setText(""); q2.setText(""); q3.setText("");
					q4.setText(""); q5.setText(""); q6.setText("");

					q1.setText(quizList.get(c)); c++; timeSleep();
					q2.setText(quizList.get(c)); c++; timeSleep();
					q3.setText(quizList.get(c)); c++; timeSleep();
					q4.setText(quizList.get(c)); c++; timeSleep();
					q5.setText(quizList.get(c)); c++; timeSleep();
					q6.setText(quizList.get(c)); c++; timeSleep();
					quiz_board.setVisible(false);try{ Thread.sleep(1000); }catch(Exception e){};
					answer_board.setVisible(true);
					answer_board.setText("정답: "+answerList.get(d)); d++;
					answer_board.setVisible(false);
			//		round++;

					quiz_board.setVisible(true);
					q1.setText(""); q2.setText(""); q3.setText("");
					q4.setText(""); q5.setText(""); q6.setText("");

					q1.setText(quizList.get(c)); c++; timeSleep();
					q2.setText(quizList.get(c)); c++; timeSleep();
					q3.setText(quizList.get(c)); c++; timeSleep();
					q4.setText(quizList.get(c)); c++; timeSleep();
					q5.setText(quizList.get(c)); c++; timeSleep();
					q6.setText(quizList.get(c)); c++; timeSleep();
					quiz_board.setVisible(false);try{ Thread.sleep(1000); }catch(Exception e){};
					answer_board.setVisible(true);
					answer_board.setText("정답: "+answerList.get(d)); d++;
				//	round++;
			//	}
			}
		}
		void timeSleep(){
			try{
				Thread.sleep(850);
			}catch(Exception e){}
		}

	}

	class Speak extends Thread implements ActionListener, KeyListener {
		Speak(){
		}
		public void run(){
			//String message = textField.getText();
				try{
					dos.writeUTF(qm.nickName);
					dos.flush();
				}catch(IOException ie){}
		}
		public void actionPerformed(ActionEvent e){
			Object obj = e.getSource();
				if(obj == ready){
					try{
						dos.writeUTF("//Chat"+nickName+"님 준비완료!");
						dos.flush();
					//	dos.writeUTF("//BeReady");
						dos.writeUTF("//Ready");
						dos.flush();
						ready.setVisible(false);
						ready_done.setVisible(true);
						ready_done.setEnabled(false);
					}catch(IOException ie){}
				} 
		}
		//채팅 입력 요거!!! //-> 엔터쳤을때 메시지 내보내려면. KeyEvent를 걸어줘야 함!!!
		public void keyReleased(KeyEvent e){ //무언가 실행을 했을때 메시지를 내보내야함
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String chat = textField.getText();
				textField.setText("");
				try{
					dos.writeUTF("//Chat"+nickName + " : "+chat);
					//System.out.println(nickName + " : "+chat);
					dos.flush();
					
				}catch(IOException ie){}
			}
		}
		public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e){}
	}
//	public static void main(String[] args) {
//		new QPlay(QMain qm);
//	}
}
