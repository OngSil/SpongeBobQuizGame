package cliffGame.client;

import java.io.*;
import java.net.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;



public class Game implements ActionListener, FocusListener, Runnable{
    JFrame mainFrame;
    final int MAIN_WIDTH = 600;
    final int MAIN_HEIGHT = 600;

    Color BLANKCOLOR = new Color(0,0,0,0);

    Clip clipBGM;
    Clip clipEndingClip;
    AudioInputStream ais;

    Container cp;
        StartPage sp;
        ReadyPage rp;
        PlayPage pp;
        ResultPage rtp;

    Game(){
        mainFrame = new JFrame();
        mainFrame.setTitle("Cliff Game");
		mainFrame.setLocation(10, 10);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        int width = MAIN_WIDTH+mainFrame.getInsets().left+mainFrame.getInsets().right;
        int height = MAIN_HEIGHT+mainFrame.getInsets().bottom+mainFrame.getInsets().top;
        mainFrame.setSize(new Dimension(MAIN_WIDTH+mainFrame.getInsets().left, MAIN_HEIGHT+mainFrame.getInsets().top) );
        //System.out.println(mainFrame);

        setUserFont();
    }

    void pln(String str){
		System.out.println(str);
	}
	void p(String str){
		System.out.print(str);
	}
    
    void setUserFont(){
        try{
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("imgs/DOSSaemmul.ttf")));
        } catch (IOException | FontFormatException e) {}
    }

    public void setBGM(String fileName){
        try{
            ais = AudioSystem.getAudioInputStream(new File(fileName) );
            clipBGM = AudioSystem.getClip();
            clipBGM.stop();
            clipBGM.open( ais );
            clipBGM.setFramePosition(0);
        }catch(IllegalArgumentException iae){
            System.out.println("스피커나 이어폰을 연결하세요.");
        
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    public void playBGM(boolean bPlay)
    {
        try{
            if(bPlay){
                FloatControl gainControl = (FloatControl)clipBGM.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-15.0f);
                clipBGM.start();
                clipBGM.loop(Clip.LOOP_CONTINUOUSLY);
            }else{
                clipBGM.loop(0);
                clipBGM.stop();
            }
        }catch(Exception e){
            System.out.println("스피커나 이어폰을 연결하세요.");
        }
    }

    public void setEndingClip(String fileName){
        try{
            ais = AudioSystem.getAudioInputStream(new File(fileName) );
            clipEndingClip = AudioSystem.getClip();
            clipEndingClip.stop();
            clipEndingClip.open( ais );
            clipEndingClip.setFramePosition(0);
        }catch(IllegalArgumentException iae){
            System.out.println("스피커나 이어폰을 연결하세요.");
        
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    public void playEnding(boolean bPlay)
    {
        try{
            if(bPlay){
                FloatControl gainControl = (FloatControl)clipEndingClip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-15.0f);
                clipEndingClip.start();
                clipEndingClip.loop(Clip.LOOP_CONTINUOUSLY);
            }else{
                clipEndingClip.loop(0);
                clipEndingClip.stop();
            }
        }catch(Exception e){
            System.out.println("스피커나 이어폰을 연결하세요.");
        }
    }


    void initUI(){
        sp = new StartPage(this);
        rp = new ReadyPage(this);
        pp = new PlayPage(this);
        rtp = new ResultPage(this);

        cp = mainFrame.getContentPane();
        cp.add(sp.startPage);
        cp.revalidate();
        cp.repaint();

        sp.helpButton.addActionListener(this);
        sp.connectButton.addActionListener(this);
        sp.userName.addActionListener(this);
        sp.ipAddress.addActionListener(this);
        sp.userName.addFocusListener(this);
        sp.ipAddress.addFocusListener(this);

        rp.messageInput.addActionListener(this);

        rp.sendButton.addActionListener(this);
        rp.readyButton.addActionListener(this);
        rp.backButton.addActionListener(this);

        pp.oneMeterButton.addActionListener(this);
        pp.twoMeterButton.addActionListener(this);
        pp.threeMeterButton.addActionListener(this);

        rtp.goToStartButton.addActionListener(this);

        sp.closeButton.addActionListener(this);

        setBGM("imgs/bgm1.wav");
        playBGM(true);

        setEndingClip("imgs/ending.wav");
        //playEnding(true);
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.initUI();
    }

    String chatId = "GUEST";
    String userName = "GUEST";
    String defaultIP = "127.0.0.1";
    int port = 7777;
	Socket clientSocket;
	DataInputStream dis;
    DataOutputStream dos;

    public void reloadPage(Component c){
        cp.remove(0);
        cp.add(c);
        cp.revalidate();
        cp.repaint();
    }

    void makeSocket(){
        try{
            clientSocket = new Socket(this.defaultIP, port);
            pln("서버와 연결 성공");
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
            new Thread(this).start();
            //new Thread(new DThread(this) ).start();
            new DThread(this).start();

        }catch(UnknownHostException ne){
			pln("해당 서버를 찾을 수 없음.");
			makeSocket();
		}catch(IOException ie){
			makeSocket();
		}catch(NumberFormatException ne){
			pln("유효하지 않은 포트임.");
			makeSocket();
		}
    }

    void inputChatId(){
		try{
			dos.writeUTF(this.userName);
			dos.flush();
		}catch(IOException ie){
		}
    }
    void inputMsg(String str){ //keyboard -> socket 
		try{
            dos.writeUTF(str);
            dos.flush();
		}catch(IOException ie){ }
	}
	void closeAll(){
		try{
			if(dis != null) dis.close();
			if(dos != null) dos.close();
			if(clientSocket != null) clientSocket.close();
		}catch(IOException ie){}
    }
    
    @Override
	public void run(){
        try{
            while(true){
                String msg = dis.readUTF();
                pln(msg);
                protocolProcess(msg);
            }
		}catch(IOException ie){
            /*pln("서버가 다운됨.. 2초후에 종료하겠습니다");
			try{
				Thread.sleep(2000);
				System.exit(0);
			}catch(InterruptedException iie){}*/
		}finally{
			closeAll();
		}
    }

    final String SEND_METER = "SEND_METER";
    void sendMeter(int number){
        try{
			dos.writeUTF(SEND_METER+Integer.toString(number) );
			dos.flush();
		}catch(IOException ie){
		}
    }

    final String REQUEST_LIST = "REQUEST_LIST";
    final String RESPONSE_LIST = "RESPONSE_LIST";
    void requestList(){
        try{
			dos.writeUTF(REQUEST_LIST);
			dos.flush();
		}catch(IOException ie){
		}
    }

    final String READY_OK = "READY_OK";
    void setClientReady(boolean b){
        try{
			dos.writeUTF(READY_OK);
			dos.flush();
		}catch(IOException ie){
		}
    }

    final String CHATLOG = "CHAT_LOG";
    final String GAME_START = "GAME_START";
    final String GAMEORDER_LIST = "GAMEORDER_LIST";
    final String TIMER_START = "TIMER_START";
    final String FINISH_GAME = "FINISH_GAME";
	final String TIMER_STOP = "TIMER_STOP";
	final String ORDER_LIST = "ORDER_LIST";
    final String NOWHEIGHT = "NOWHEIGHT";
    boolean startTimer = false;

    void protocolProcess(String msg){
        String body="";
        if(msg.startsWith(RESPONSE_LIST)){ //목록 요청에 대한 응답이면
            rp.connectList.setText(""); //리스트 초기화 하고
            body = msg.substring(RESPONSE_LIST.length() ); //헤더 잘라내고
            rp.connectList.append(body); //리스트 표시한다.

        }else if(msg.startsWith(CHATLOG)){
            body = msg.substring(CHATLOG.length() ); //헤더 잘라내고
            System.out.println(body);
            rp.chatLog.append(body+"\n");
            rp.messageScroll.getVerticalScrollBar().setValue(rp.messageScroll.getVerticalScrollBar().getMaximum()); //자동 스크롤

        }else if(msg.startsWith(GAME_START)){
            reloadPage(pp.playPage);

        }else if(msg.startsWith(TIMER_START)){
            this.startTimer=true;
            pp.oneMeterButton.setVisible(true);
            pp.twoMeterButton.setVisible(true);
            pp.threeMeterButton.setVisible(true);
            System.out.println(chatId+"의 타이머동작");

        }else if(msg.startsWith(TIMER_STOP)){
            this.startTimer=false;
            pp.oneMeterButton.setVisible(false);
            pp.twoMeterButton.setVisible(false);
            pp.threeMeterButton.setVisible(false);
            System.out.println(chatId+"의 타이머멈춤");
            
        }else if(msg.startsWith(ORDER_LIST)){
            pp.infoList.setText("");
            body = msg.substring(ORDER_LIST.length() ); //헤더 잘라내고
            pp.infoList.append(body);

        }else if(msg.startsWith(NOWHEIGHT)){
            pp.totalHeight.setText("");
            body = msg.substring(NOWHEIGHT.length() ); //헤더 잘라내고
            //pp.totalHeight.append(body);
            pp.totalHeight.setText(body);
                
        }else if(msg.startsWith(FINISH_GAME)){
            body = msg.substring(FINISH_GAME.length() ); //헤더 잘라내고
            System.out.println(body);
            reloadPage(rtp.resultPage);
            rtp.result.setText("");
            rtp.result.append(body+"\n");
            playBGM(false);
            playEnding(true);

        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();
        if( obj instanceof JButton ){ //타입이 버튼이면
            JButton b = (JButton)obj;
            if(b==sp.helpButton){ //도움말 버튼
                sp.jd.setVisible(true);
                sp.jd.setLocationRelativeTo(this.mainFrame);

            }else if(b==sp.connectButton){ //접속 버튼
                makeSocket();
                inputChatId();
                requestList();
                reloadPage(rp.readyPage);

            }else if(b==sp.closeButton){ //닫기 버튼
                sp.jd.setVisible(false);
            }

            if(b==rp.sendButton){ //전송 버튼
                String str = rp.messageInput.getText();
                if(str.length()!=0){
                    inputMsg(CHATLOG+this.userName+">> "+str );
                    rp.messageInput.setText("");
                }

            }else if(b==rp.readyButton){ //준비하기 버튼
                rp.readyButton.setVisible(false);
                pp.oneMeterButton.setVisible(false);
                pp.twoMeterButton.setVisible(false);
                pp.threeMeterButton.setVisible(false);
                setClientReady(true);

            }else if(b==rp.backButton){ //뒤로가기 버튼
                rp.readyButton.setVisible(true);
                rp.chatLog.setText("");
                requestList();
                closeAll();
                reloadPage(sp.startPage);
            }
            
            if(b==pp.oneMeterButton){ //1메다
                sendMeter(1);
            }else if(b==pp.twoMeterButton){ //2메다
                sendMeter(2);
            }else if(b==pp.threeMeterButton){ //3메다
                sendMeter(3);
            }

            if(b==rtp.goToStartButton){ //처음으로 버튼
                rp.readyButton.setVisible(true);
                rp.chatLog.setText("");
                closeAll();
                requestList();
                reloadPage(sp.startPage);
                playBGM(true);
                playEnding(false);
            }

        }else if( obj instanceof JTextField ){
            JTextField jf = (JTextField)obj;
            if(jf==sp.userName){
                this.userName = sp.userName.getText();
                sp.userName.setFocusToggle();

            }else if(jf==sp.ipAddress){
                this.defaultIP = sp.ipAddress.getText();
                sp.ipAddress.setFocusToggle();
            }

            if(jf==rp.messageInput){
                String str = rp.messageInput.getText();
                if(str.length()!=0){
                    inputMsg(CHATLOG+this.userName+">> "+str );
                    rp.messageInput.setText("");
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e){
        Object obj = e.getSource();
        if( obj instanceof JTextField ){ //타입이 JTextField라면
            JTextField jt = (JTextField)obj;
            if(jt==sp.userName){
                sp.userName.setText("");
            }else if(jt==sp.ipAddress){
                sp.ipAddress.setText("");
            }
        }
    }
    @Override
    public void focusLost(FocusEvent e){
        Object obj = e.getSource();
        if( obj instanceof JTextField ){ //타입이 JTextField라면
            JTextField jt = (JTextField)obj;
            if(jt==sp.userName){
                if(sp.userName.getText().length() == 0){
                    sp.userName.setText(this.userName);
                }else{
                    this.userName = sp.userName.getText();
                }
            }else if(jt==sp.ipAddress){
                if(sp.ipAddress.getText().length() == 0){
                    sp.ipAddress.setText(this.defaultIP);
                }else{
                    this.defaultIP = sp.ipAddress.getText();
                }
            }
        }
    }
}


class DThread extends Thread{
    Game g;
    DThread(Game g){
        this.g = g;
    }

    @Override
    public void run(){
        int count=1;
        long nowTime=0L;
        while(true){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException ie){ }

            if(g.startTimer){
                if(count>10){
                    g.pp.timer.setText("");
                    g.sendMeter(1);
                    g.startTimer=false;
                    count=1;
                }else{
                    g.pp.timer.setText(Integer.toString(count));
                    count++;
                }
            }else{
                g.pp.timer.setText("");
                count=1;
            }
        }
    }
}

class DDialog extends JDialog{
    DDialog(){
        super();
    }
}

class DLabel extends JLabel{
    DLabel(int fontSize){
        super.setOpaque(false);
        super.setForeground(Color.WHITE);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, fontSize) );
    }
}

class DPanel extends JPanel{
    Image img;

    DPanel(String str){
        try{
            this.img = ImageIO.read(new File(str));
        }catch(IOException ioe){
            System.out.println("DPanel image fail");
        }
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(this.img, 0, 0, null);
        setOpaque(false);
        super.paintComponent(g);
    }
}

class DButton extends JButton{
    Image img;
    DPanel mutePanel = new DPanel("imgs/page01/page01_mute.png");

    DButton(String str){
        try{
            this.img = ImageIO.read(new File(str));
        }catch(IOException ioe){
            System.out.println("DButton image fail");
        }
    }

    void setMutePaint(){
        mutePanel.setBounds(0, 0, 40, 40);
        this.add(mutePanel);
    }

    void resetMutePaint(){
        this.remove(mutePanel);
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(this.img, 0, 0, null);
    }
}

class DTextField extends JTextField{
    Image img;

    DTextField(String str){
        super.setHorizontalAlignment(JTextField.CENTER);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, 26) );
        try{
            this.img = ImageIO.read(new File(str));
        }catch(IOException ioe){
            System.out.println("DTextField image fail");
        }
    }

    void setFocusToggle(){
        super.setFocusable(false);
        super.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(this.img, 0, 0, null);
        setOpaque(true);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        super.paintComponent(g);
    }
}

class DTextArea extends JTextArea{
    Image img;
    DTextArea(){
        super.setEditable(false);
        super.setLineWrap(true);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, 20) );
    }
    DTextArea(String str){
        super.setEditable(false);
        super.setLineWrap(true);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, 20) );
        try{
            this.img = ImageIO.read(new File(str));
        }catch(IOException ioe){
            System.out.println("DTextArea image fail");
        }
    }
    DTextArea(int fontSize, String str){
        super.setEditable(false);
        super.setLineWrap(true);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, fontSize) );
        try{
            this.img = ImageIO.read(new File(str));
        }catch(IOException ioe){
            System.out.println("DTextArea image fail");
        }
    }
    @Override
    public void paintComponent(Graphics g){
        g.drawImage(this.img, 0, 0, null);
        setOpaque(false);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        super.paintComponent(g);
    }
}

class DScrollPane extends JScrollPane{
    Image img;

    DScrollPane(Component view){
        super(view);
        super.setOpaque(false);
        super.getViewport().setOpaque(false);
        super.setBackground(new Color(0,0,0,128));
        super.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        super.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        super.setFont(new Font("DOSSaemmul", Font.TRUETYPE_FONT, 20) );
    }
}






