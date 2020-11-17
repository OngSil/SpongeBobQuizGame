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
	int readyPlayer; // ���� �غ�� Ŭ���̾�Ʈ ī��Ʈ
	int score;
	boolean gameStart; // ���� ���� ����
	String line = "";
	LinkedHashMap<String, DataOutputStream> clientList = new LinkedHashMap<String, DataOutputStream>(); // Ŭ���̾�Ʈ �̸�, ��Ʈ�� ����
	LinkedHashMap<String, Integer> clientInfo = new LinkedHashMap<String, Integer>(); // Ŭ���̾�Ʈ �̸�, ���� ����
	
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
		label_ServerStatus.setFont(new Font("�����ٸ����", Font.PLAIN, 20));
		
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
		
		btn_ServerStart = new JButton(" ���� ���� ");
		btn_ServerStart.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_ServerStart.setPreferredSize(new Dimension(120, 40));
		btn_ServerStart.setFocusPainted(false);
		btn_ServerStart.setFont(new Font("�����ٸ����", Font.BOLD, 16));
		btn_ServerStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_ServerStart.setForeground(Color.WHITE);
		btn_ServerStart.setBackground(Color.DARK_GRAY);
		btn_ServerStart.setBorder(null);
		panel_Btn.add(btn_ServerStart);
		btn_ServerStart.addActionListener(this);
		
		btn_ServerClose = new JButton(" ���� ���� ");
		btn_ServerClose.setHorizontalTextPosition(SwingConstants.CENTER);
		btn_ServerClose.setPreferredSize(new Dimension(120, 40));
		btn_ServerClose.setFocusPainted(false);
		btn_ServerClose.setFont(new Font("�����ٸ����", Font.BOLD, 16));
		btn_ServerClose.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn_ServerClose.setForeground(Color.WHITE);
		btn_ServerClose.setBackground(Color.DARK_GRAY);
		btn_ServerClose.setBorder(null);
		panel_Btn.add(btn_ServerClose);
		btn_ServerClose.addActionListener(this);
		btn_ServerClose.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e){ // '���� ���� & ����' ��ư �̺�Ʈ
		if(e.getSource() == btn_ServerStart){
			new Thread(){
				public void run() {
					try{
						Collections.synchronizedMap(clientList);
						ss = new ServerSocket(port);
						label_ServerStatus.setText("[ Server Started ]");
						textArea.append("[ ������ ���۵Ǿ����ϴ� ]" + "\n");
						btn_ServerStart.setEnabled(false);
						btn_ServerClose.setEnabled(true);
						while(true){
							s = ss.accept();
							if((clientList.size() + 1) > MAX_CLIENT || gameStart == true){ // ������ �ʰ��Ǿ��ų�, �������̶�� ���� ���� �ź�
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
			int select = JOptionPane.showConfirmDialog(null, "������ ���� �����Ͻðڽ��ϱ�?", "JAVA CatchMind Server", JOptionPane.OK_CANCEL_OPTION);
			try{
				if(select == JOptionPane.YES_OPTION){
					ss.close();
					label_ServerStatus.setText("[ Server Closed ]");
					textArea.append("[ ������ ����Ǿ����ϴ� ]" + "\n");
					btn_ServerStart.setEnabled(true);
					btn_ServerClose.setEnabled(false);
				}
			}catch(IOException io){}
		}
	}

	public void showSystemMsg(String msg){ //�ý��� �޽��� �� �������� �۽�
		Iterator<String> it = clientList.keySet().iterator();
		while(it.hasNext()){
			try{
				DataOutputStream dos = clientList.get(it.next());
				dos.writeUTF(msg);
				dos.flush();
			}catch(IOException ie){}
		}
	}

	//���� Ŭ����(���Ӱ���)
	public class GameManager extends Thread	{
		Socket s;
		DataInputStream dis;
		DataOutputStream dos;

		public GameManager(Socket s){ //���ӸŴ��� ���� �޾Ƽ� ����?
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
				if(!clientList.containsKey(clientName)){ //�̸��� �ߺ����� ������
					clientList.put(clientName, dos); //�̸�, �޽����� ����Ʈ�� �ִ´�
					clientInfo.put(clientName, score); //�̸�, ������ �ִ´�
				}else if(clientList.containsKey(clientName)){ //�̸��� �ߺ��Ǹ�
					s.close();
				}
				showSystemMsg(clientName+"�� ����!");
				textArea.append("���� ������ ��"+clientList.size()+"��");
				Iterator<String> it1 = clientList.keySet().iterator();
				while(it1.hasNext()) textArea.append(it1.next() + "\n");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				setClientInfo();//Ŭ���̾�Ʈ ��� ����
				while(dis != null){
					String msg = dis.readUTF();
					filter(msg); //��ɾ� ���͸�
				}
			}catch(IOException ie){
				clientList.remove(clientName); clientInfo.remove(clientName);
				closeAll();
				if(clientList.isEmpty() == true){
					try{
						ss.close(); System.exit(0);
					}catch(IOException e){}
				}
				showSystemMsg(clientName+"�� ����!");
				textArea.append("���� ������ ��"+clientList.size()+"��");
				Iterator<String> it1 = clientList.keySet().iterator();
				while(it1.hasNext()) textArea.append(" " + it1.next() + "\n");
				scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
				setClientInfo();
				readyPlayer = 0; //���ο� Ŭ���̾�Ʈ �����ص� ���� ���� ����
				gameStart = false;
				showSystemMsg("//GmEnd"); //Ŭ���̾�Ʈ ������ ���� ����
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
				showSystemMsg("//CList" + keys[i] + " " + values[i] + "#" + i); // ��ɾ� : Ŭ���̾�Ʈ ��� ����
			}
		}

		public void filter(String msg){
			String temp = msg.substring(0,7);
			String tempAns = msg.substring(msg.lastIndexOf(" ") + 1); // ���� ���� üũ
			if(temp.equals("//Chat ")){ //��ɾ� �Ϲ� ä��?
				answerCheck(msg.substring(7).trim());
				showSystemMsg(msg.substring(7));
			}else if(temp.equals("//Ready")){ //������ �غ� ���� üũ
				readyPlayer++;
				if(readyPlayer >= 2 && readyPlayer == clientList.size()){
					for(int i=3; i>0; i--){
						try{
							showSystemMsg("[ �� ������ ���۵˴ϴ� ]");
							Thread.sleep(1000);
						}catch(InterruptedException iie){}
					}
					StopWatch tm = new StopWatch(); tm.start();
					gameStart = true;
					Exam ex = new Exam(); ex.start(); //��������!!!!!!!!!!
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
					FileReader fr = new FileReader("imgs/��������Answer.txt");				
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
					showSystemMsg("[" + tempNick + "�� ����!! ]");
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
				FileReader fr = new FileReader("imgs/��������.txt");
				
				br = new BufferedReader(fr);
				
				while((line2 = br.readLine()) != null) { //������ ���پ� �о quizList�� ����
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

	// ���� Ŭ���� - Ÿ�̸�
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
						showSystemMsg("//GmEnd"); // �ð� �ʰ���, ���� ����
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
