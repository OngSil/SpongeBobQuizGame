import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


class QuizServer_new extends JFrame implements ActionListener {
	JPanel contentPane, panel_Main, panel_TextArea, panel_Btn;
	JScrollPane scrollPane;
	JTextArea textArea;
	JLabel label_ServerStatus;
	JButton btn_ServerStart, btn_ServerClose;
	
	ServerSocket ss;
	Socket s;
	int port = 3000;
	public static final int MAX_CLIENT = 4;
	int readyPlayer; // 게임 준비된 클라이언트 카운트
	int score;
	boolean gameStart; // 게임 시작 상태
	String line = "";
	LinkedHashMap<String, DataOutputStream> clientList = new LinkedHashMap<String, DataOutputStream>(); // 클라이언트 이름, 스트림 관리
	LinkedHashMap<String, Integer> clientInfo = new LinkedHashMap<String, Integer>(); // 클라이언트 이름, 점수 관리
	
	public void init(){
		setTitle("SpongeBob Quiz Server");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 10, 0));
		
		panel_Main = new JPanel();
		contentPane.add(panel_Main);
		panel_Main.setLayout(new BoxLayout(panel_Main, BoxLayout.Y_AXIS));
		
		label_ServerStatus = new JLabel("[ Server Waiting .. ]");
		label_ServerStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		label_ServerStatus.setPreferredSize(new Dimension(96, 50));
		panel_Main.add(label_ServerStatus);
		label_ServerStatus.setHorizontalTextPosition(SwingConstants.CENTER);
		label_ServerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		label_ServerStatus.setFont(new Font("나눔바른고딕", Font.PLAIN, 20));
		
		panel_TextArea = new JPanel();
		panel_Main.add(panel_TextArea);
		panel_TextArea.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_TextArea.add(scrollPane);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		panel_Btn = new JPanel();
		panel_Btn.setPreferredSize(new Dimension(10, 43));
		panel_Btn.setAutoscrolls(true);
		panel_Main.add(panel_Btn);
		panel_Btn.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btn_ServerStart = new JButton(" 서버 시작 ");
		btn_ServerStart.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_ServerStart.setPreferredSize(new Dimension(120, 40));
		btn_ServerStart.setFocusPainted(false);
		btn_ServerStart.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
		btn_ServerStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_ServerStart.setForeground(Color.WHITE);
		btn_ServerStart.setBackground(Color.DARK_GRAY);
		btn_ServerStart.setBorder(null);
		panel_Btn.add(btn_ServerStart);
		btn_ServerStart.addActionListener(this);
		
		btn_ServerClose = new JButton(" 서버 종료 ");
		btn_ServerClose.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_ServerClose.setPreferredSize(new Dimension(120, 40));
		btn_ServerClose.setFocusPainted(false);
		btn_ServerClose.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
		btn_ServerClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_ServerClose.setForeground(Color.WHITE);
		btn_ServerClose.setBackground(Color.DARK_GRAY);
		btn_ServerClose.setBorder(null);
		panel_Btn.add(btn_ServerClose);
		btn_ServerClose.addActionListener(this);
		btn_ServerClose.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e){ // '서버 시작 & 종료' 버튼 이벤트
		if(e.getSource() == btn_ServerStart){
			new Thread(){
				public void run() {
					try{
						Collections.synchronizedMap(clientList);
						ss = new ServerSocket(port);
						label_ServerStatus.setText("[ Server Started ]");
						textArea.append("[ 서버가 시작되었습니다 ]" + "\n");
						btn_ServerStart.setEnabled(false);
						btn_ServerClose.setEnabled(true);
						while(true){
							s = ss.accept();
							if((clientList.size() + 1) > MAX_CLIENT || gameStart == true){ // 정원이 초과되었거나, 게임중이라면 소켓 연결 거부
								s.close();
							}else{
								Thread gm = new GameManager(s);
								gm.start();
							}
						}
					}catch(IOException io){}
				}
			}.start();
		}else if(e.getSource() == btn_ServerClose){
			int select = JOptionPane.showConfirmDialog(null, "서버를 정말 종료하시겠습니까?", "JAVA CatchMind Server", JOptionPane.OK_CANCEL_OPTION);
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					label_ServerStatus.setText("[ Server Closed ]");
					textArea.append("[ 서버가 종료되었습니다 ]" + "\n");
					btn_ServerStart.setEnabled(true);
					btn_ServerClose.setEnabled(false);
				}
			}catch(IOException io){}
		}
	}

	public void showSystemMsg(String msg){ //시스템 메시지 및 프로토콜 송신
		Iterator<String> it = clientList.keySet().iterator();
		while(it.hasNext()){
			try{
				DataOutputStream dos = clientList.get(it.next());
				dos.writeUTF(msg);
				dos.flush();
			}catch(IOException ie){}
		}
	}

	//내부 클래스(게임관리)
	public class GameManager extends Thread	{
		Socket s;
		DataInputStream dis;
		DataOutputStream dos;

		public GameManager(Socket s){ //게임매니저 소켓 받아서 관리?
			this.s = s;
			try{
				dis = new DataInputStream(this.s.getInputStream());
				dos = new DataOutputStream(this.s.getOutputStream());
			}catch(IOException ie){}
		}

		public void run(){
			String clientName = "";
			try{
				clientName = dis.readUTF();
				if(!clientList.containsKey(clientName)){ //이름이 중복되지 않으면
					clientList.put(clientName, dos); //이름, 메시지를 리스트에 넣는다
					clientInfo.put(clientName, score); //이름, 점수를 넣는다
				}else if(clientList.containsKey(clientName)){ //이름이 중복되면
					s.close();
				}
				showSystemMsg(clientName+"님 입장!");
				textArea.append("현재 접속자 총"+clientList.size()+"명");
				Iterator<String> it1 = clientList.keySet().iterator();
				while(it1.hasNext()) textArea.append(it1.next() + "\n");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				setClientInfo();//클라이언트 목록 갱신
				while(dis != null){
					String msg = dis.readUTF();
					filter(msg); //명령어 필터링
				}
			}catch(IOException ie){
				clientList.remove(clientName); clientInfo.remove(clientName);
				closeAll();
				if(clientList.isEmpty() == true){
					try{
						ss.close(); System.exit(0);
					}catch(IOException e){}
				}
				showSystemMsg(clientName+"님 퇴장!");
				textArea.append("현재 접속자 총"+clientList.size()+"명");
				Iterator<String> it1 = clientList.keySet().iterator();
				while(it1.hasNext()) textArea.append(" " + it1.next() + "\n");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				setClientInfo();
				readyPlayer = 0; //새로운 클라이언트 접속해도 게임 시작 가능
				gameStart = false;
				showSystemMsg("//GmEnd"); //클라이언트 나가면 라운드 종료
			}
		}
		
		public void closeAll(){
			try{
				if(dos != null) dos.close();
				if(dis != null) dis.close();
				if(s != null) s.close();
			}catch(IOException ie){}
		}

		public void setClientInfo(){
			String[] keys = new String[clientInfo.size()];
			int[] values = new int[clientInfo.size()];
			int index = 0;
			for(Map.Entry<String, Integer> mapEntry : clientInfo.entrySet()){
			    keys[index] = mapEntry.getKey();
			    values[index] = mapEntry.getValue();
			    index++;
			}
			for(int i=0; i<clientList.size(); i++){
				showSystemMsg("//CList" + keys[i] + " " + values[i] + "#" + i); // 명령어 : 클라이언트 목록 갱신
			}
		}

		public void filter(String msg){
			String temp = msg.substring(0,7);
			String tempAns = msg.substring(msg.lastIndexOf(" ") + 1); // 정답 내용 체크
			if(temp.equals("//Chat ")){ //명령어 일반 채팅?
				answerCheck(msg.substring(7).trim());
				showSystemMsg(msg.substring(7));
			}else if(temp.equals("//Ready")){ //접속자 준비 상태 체크
				readyPlayer++;
				if(readyPlayer >= 2 && readyPlayer == clientList.size()){
					for(int i=3; i>0; i--){
						try{
							showSystemMsg("[ 곧 게임이 시작됩니다 ]");
							Thread.sleep(1000);
						}catch(InterruptedException iie){}
					}
					StopWatch tm = new StopWatch(); tm.start();
					gameStart = true;
					Exam ex = new Exam(); ex.start(); //문제출제!!!!!!!!!!
					showSystemMsg("//Start");
					
				}
			}
		}
	
		public void answerCheck(String msg){
			String tempNick = msg.substring(0, msg.indexOf(" "));
			String tempAns = msg.substring(msg.lastIndexOf(" ") + 1);
			String line = "";
			BufferedReader br;
			ArrayList<String> quizList = new ArrayList<String>();
				try{
					FileReader fr = new FileReader("imgs/스폰지밥Answer.txt");				
					br = new BufferedReader(fr);
					
					while((line = br.readLine()) != null) {
							quizList.add(line);			
					}
					}catch(IOException ie){
				}				
				int a=0;
				if(tempAns.equals(quizList.get(a)) && gameStart == true){
					
					showSystemMsg("//GmEnd");
					gameStart = false;
					readyPlayer = 0;
					showSystemMsg("[" + tempNick + "님 정답!! ]");
					clientInfo.put(tempNick, clientInfo.get(tempNick) + 1);
					setClientInfo();
					a++;
				}	

		}
	}
	int round = 0;
	int k;
	class Exam extends Thread {
		BufferedReader br;
		ArrayList<String> quizList = new ArrayList<String>();
		
		String line2 = "";
		StringBuffer sb = new StringBuffer();
		
		public void run(){
			try{
				FileReader fr = new FileReader("imgs/스폰지밥.txt");
				
				br = new BufferedReader(fr);
				
				while((line2 = br.readLine()) != null) { //문제를 한줄씩 읽어서 quizList에 담음
					quizList.add(line2);
				}

				for(int k=0; k<quizList.size(); k++){
					sb.append(quizList.get(k)+"/");
				}
				showSystemMsg("//Exam" + sb);

			}catch(IOException ie){
			}
		}
	}

	// 내부 클래스 - 타이머
	class StopWatch extends Thread
	{
		long preTime = System.currentTimeMillis();
		
		public void run() {
			try{
				while(gameStart == true){
					sleep(10);
					long time = System.currentTimeMillis() - preTime;
					showSystemMsg("//Timer" + (toTime(time))); //
					if(toTime(time).equals("00 : 00")){
						showSystemMsg("//GmEnd"); // 시간 초과시, 게임 종료
						readyPlayer = 0;
						gameStart = false;
						break;
					}else if(readyPlayer == 0){
						break;
					}
				}
			}catch (Exception e){}
		}  


		
		String toTime(long time){
			int m = (int)(1-(time / 1000.0 / 60.0));
			int s = (int)(60-(time % (1000.0 * 60) / 1000.0));
			return String.format("%02d : %02d", m, s);
		}
	}
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					QuizServer_new qs = new QuizServer_new();
					qs.init();
					qs.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
