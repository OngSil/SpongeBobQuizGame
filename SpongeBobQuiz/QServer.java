import java.io.*;
import java.net.*;
import java.util.*; //관리용(소켓)
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/* 서버
   1. 클라이언트와 연결할 소켓 생성(서버 시작하면)
   2. 중간관리자 모듈에게 this로 넘겨쥬(클라이언트 소켓, 벡터)
   3. 모듈 thread start시켜쥬
   4. 종료 버튼 누르면 모든 시스템 종료시켜쥬
   5. 시간 제어해서 모듈로 -> 클라이언트로   :   모듈에서?
   6. 문제 출제 -> 모듈로 -> 클라이언트로 쏴   :   모듈에서?
   7. 점수 계산 -> 모듈로 -> 클라이언트로 쏴?   :   모듈에서?
*/
public class QServer extends JFrame implements ActionListener, Runnable {
	ServerSocket ss; //네트워크 연결
	Socket s;
	int port = 5555;
	int round = 0;

	JPanel contentPane, mainPanel, textAreaPanel, buttonPanel;
	JScrollPane scrollPane;
	JTextArea textArea;
	JLabel serverStatusLabel, serverLabel1, serverLabel2;
	JButton serverStartButton, serverCloseButton;
	ImageIcon titleImg = new ImageIcon("Images//코난.png");
	ImageIcon gun1,gun2;
	Font font = new Font("나눔바른고딕", Font.PLAIN, 20);
	
	OneClientModule ocm;
	Vector<OneClientModule> clientList = new Vector<OneClientModule>();
	LinkedHashMap<String, String> clientInfo = new LinkedHashMap<String, String>();

	public static final int MAX_CLIENT = 5;
	int Score;
	int readyPlayer = 0;
	boolean gameStart;
	
	QServer(){
		init();
	}
	void init(){
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0,1,10,0));
		
		mainPanel = new JPanel();
		contentPane.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		serverStatusLabel = new JLabel("    SpongeBob Quiz Server     ");
		serverStatusLabel.setFont(font);
		serverStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		//serverStatusLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		//serverStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(serverStatusLabel);

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

		setUI();
		serve();
	}
	
	void setUI(){
		setTitle("스폰지밥 퀴즈 서버");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,350,400);
		setLocationRelativeTo(null);		//윈도우 가운데 배치
		
	}
	

	//클라이언트 다중접속 가능하게 하는 서버소켓-내부클래스 생성
	void serve() {
			try{ //서버 시작 버튼을 누르면 소켓 연결
				ss = new ServerSocket(port);
				serverStatusLabel.setText("서버 가동중");
				textArea.append("서버 시작!!!!!"+"\n");
				System.out.println("서버 시작!!!!!"+"\n");
			}catch(IOException ie){} 
	}

	public void actionPerformed(ActionEvent e){ // '서버 시작 & 종료' 버튼 이벤트
		Object obj = e.getSource();
		if(obj == serverStartButton){ //서버시작 버튼을 누르면
			new Thread(this).start(); //서버소켓 연결!
			serverStartButton.setEnabled(false);
			serverCloseButton.setEnabled(true);
		} else if(obj == serverCloseButton){
			try{ //서버 종료 버튼 누르면 시스템 종료
				ss.close();
				serverStatusLabel.setText("            서버 종료          ");
				textArea.append("서버 종료ㅂㅂ"+"\n");
			//	ocm.showSystemMsg("//EndServer");
			}catch(IOException ie){} 			
		}
	}
	
	public void run(){
		while(true){
			try{
				s = ss.accept(); 
				ocm = new OneClientModule(this);
				clientList.add(ocm);
				int n = 0;
				textArea.append("총"+clientList.size()+"명 접속 ("+clientList.size()+"/4)");
				n++;
				if((clientList.size() + 1) > MAX_CLIENT ){ //4명 초과하거나, 게임 중이라면 소켓 닫아버리기
					s.close();
				} else { //위에 경우가 아니라면 게임 진행하기 위해 ocm 돌리기
					ocm.start();
				}
			}catch(IOException ie){}
		}
	}

	public static void main(String[] args) {
		new QServer();
	}
}
