import java.io.*;
import java.net.*;
import java.util.*; //������(����)
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/* ����
   1. Ŭ���̾�Ʈ�� ������ ���� ����(���� �����ϸ�)
   2. �߰������� ��⿡�� this�� �Ѱ���(Ŭ���̾�Ʈ ����, ����)
   3. ��� thread start������
   4. ���� ��ư ������ ��� �ý��� ���������
   5. �ð� �����ؼ� ���� -> Ŭ���̾�Ʈ��   :   ��⿡��?
   6. ���� ���� -> ���� -> Ŭ���̾�Ʈ�� ��   :   ��⿡��?
   7. ���� ��� -> ���� -> Ŭ���̾�Ʈ�� ��?   :   ��⿡��?
*/
public class QServer extends JFrame implements ActionListener, Runnable {
	ServerSocket ss; //��Ʈ��ũ ����
	Socket s;
	int port = 5555;
	int round = 0;

	JPanel contentPane, mainPanel, textAreaPanel, buttonPanel;
	JScrollPane scrollPane;
	JTextArea textArea;
	JLabel serverStatusLabel, serverLabel1, serverLabel2;
	JButton serverStartButton, serverCloseButton;
	ImageIcon titleImg = new ImageIcon("Images//�ڳ�.png");
	ImageIcon gun1,gun2;
	Font font = new Font("�����ٸ����", Font.PLAIN, 20);
	
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

		serverStartButton = new JButton("���� ����");
		serverStartButton.setFocusPainted(false);
		serverStartButton.setFont(font);
		buttonPanel.add(serverStartButton);
		serverStartButton.addActionListener(this);

		serverCloseButton = new JButton("���� ����");
		serverCloseButton.setFocusPainted(false);
		serverCloseButton.setFont(font);
		buttonPanel.add(serverCloseButton);
		serverCloseButton.setEnabled(false);
		serverCloseButton.addActionListener(this);

		setUI();
		serve();
	}
	
	void setUI(){
		setTitle("�������� ���� ����");
		setIconImage(titleImg.getImage());
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,350,400);
		setLocationRelativeTo(null);		//������ ��� ��ġ
		
	}
	

	//Ŭ���̾�Ʈ �������� �����ϰ� �ϴ� ��������-����Ŭ���� ����
	void serve() {
			try{ //���� ���� ��ư�� ������ ���� ����
				ss = new ServerSocket(port);
				serverStatusLabel.setText("���� ������");
				textArea.append("���� ����!!!!!"+"\n");
				System.out.println("���� ����!!!!!"+"\n");
			}catch(IOException ie){} 
	}

	public void actionPerformed(ActionEvent e){ // '���� ���� & ����' ��ư �̺�Ʈ
		Object obj = e.getSource();
		if(obj == serverStartButton){ //�������� ��ư�� ������
			new Thread(this).start(); //�������� ����!
			serverStartButton.setEnabled(false);
			serverCloseButton.setEnabled(true);
		} else if(obj == serverCloseButton){
			try{ //���� ���� ��ư ������ �ý��� ����
				ss.close();
				serverStatusLabel.setText("            ���� ����          ");
				textArea.append("���� ���ᤲ��"+"\n");
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
				textArea.append("��"+clientList.size()+"�� ���� ("+clientList.size()+"/4)");
				n++;
				if((clientList.size() + 1) > MAX_CLIENT ){ //4�� �ʰ��ϰų�, ���� ���̶�� ���� �ݾƹ�����
					s.close();
				} else { //���� ��찡 �ƴ϶�� ���� �����ϱ� ���� ocm ������
					ocm.start();
				}
			}catch(IOException ie){}
		}
	}

	public static void main(String[] args) {
		new QServer();
	}
}
