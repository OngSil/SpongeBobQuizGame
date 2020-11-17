package cliffGame.server;

import java.io.*;
import java.net.*;
import java.util.*;

//�ϳ��� Ŭ���̾�Ʈ�� �޼����� �а� ����(��ε��ɽ���)�ϴ� '������ Ŭ����'
class OneClientModule implements Runnable{
	CliffGameServer ms;
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;	
	String chatId = "GUEST";
	String readyStatus = "(��� ��..)";
	boolean checkSelectNumber = false;
	int myNumber = 0;

	OneClientModule(CliffGameServer ms){
		this.ms = ms;
		this.s = ms.s;
		try{
			is = s.getInputStream();
			os = s.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
		}catch(IOException ie){
		}
	}

    @Override
	public void run(){
		listen();
	}

	void listen(){
		String msg = "";

		try{
			chatId = dis.readUTF();
			boardcast(chatId+"�� ����!!(���ο�: "+ms.v.size()+"��)"); //��� Ŭ���̾�Ʈ�鿡�� �� 
			ms.pln(chatId+"�� ����!!(���ο�: "+ms.v.size()+"��)"); //������ ����Ϳ� �� 
			Random r = new Random();
			ms.targetNumber = ms.v.size()*6 + r.nextInt(10) - 5;
			if(ms.targetNumber<7){ ms.targetNumber=7; } 
			while(true){
				msg = dis.readUTF();
				//ms.pln(msg);
				boardcast(msg);
				protocolProcess(msg);

				if( checkAllReady() ){
					for(int i=0; i<ms.v.size(); i++){
						if(ms.playerOrder == i){
							startTurn(i);
						}else{
							stopTurn(i);
						}
					}	
				}

				sendOrderList(ms.playerOrder);
				sendNowHeight(ms.nowNumber);

				System.out.println("��ǥ: "+ms.targetNumber+"/ ���� ����:"+ms.nowNumber);
				if(ms.nowNumber>=ms.targetNumber){ finishGame(); }
				
			}
		}catch(IOException ie){
			ms.v.remove(this);
			boardcast(chatId+"�� ����!!(���ο�: "+ms.v.size()+"��)");
			ms.pln(chatId+"�� ����!!(���ο�: "+ms.v.size()+"��)");
			responsList();
		}finally{
			closeAll();
		}
	}

	final String NOWHEIGHT = "NOWHEIGHT";
	void sendNowHeight(int nowHeight){
		boardcast(NOWHEIGHT+nowHeight+"�޴�");
	}

	final String ORDER_LIST = "ORDER_LIST";
	void sendOrderList(int nowOrder){
		StringBuffer sBuffer = new StringBuffer();
		for(OneClientModule ocm : ms.v){
			if(ms.v.indexOf(ocm)==nowOrder ){
				sBuffer.append(">> "+ocm.chatId+"\n\n");
			}else{
				sBuffer.append("   "+ocm.chatId+"\n\n");
			}
		}
		boardcast(ORDER_LIST+"<<����>>\n\n"+sBuffer);
		ms.pln(ORDER_LIST+sBuffer);

	}

	final String TIMER_START = "TIMER_START";
	final String TIMER_STOP = "TIMER_STOP";
	void startTurn(int i){
		try{
			ms.v.get(i).dos.writeUTF(TIMER_START);
			ms.v.get(i).dos.flush();
		}catch(IOException ie){ }
	}
	void stopTurn(int i){
		try{
			ms.v.get(i).dos.writeUTF(TIMER_STOP);
			ms.v.get(i).dos.flush();
		}catch(IOException ie){ }
	}


	final String FINISH_GAME = "FINISH_GAME";
	void finishGame(){
		System.out.println("��ǥ ���ڸ� �Ѱ���ϴ�.");
		StringBuffer sBuffer = new StringBuffer();
		for(OneClientModule ocm : ms.v){
			if(ocm==this) continue;
			sBuffer.append(ocm.chatId+": "+ocm.myNumber+"\n\n");
		}

		for(int i=0; i<ms.v.size(); i++){
			stopTurn(i);
		}

		sBuffer.append("����� "+ this.chatId+"�̰�, "+this.myNumber+"�� �Դϴ�.");
		boardcast(FINISH_GAME+sBuffer);
		ms.pln(FINISH_GAME+sBuffer);

		initialGame();
	}

	void initialGame(){
		ms.playerOrder=0;
		ms.nowNumber=0;
		//ms.targetNumber=31;
		for(OneClientModule ocm : ms.v){
			ocm.checkSelectNumber = false;
			ocm.myNumber = 0;
		}
	}
	void startGame(){
		boardcast(GAME_START);
		//ms.targetNumber=31;
	}


	final String READY_OK = "READY_OK";
	final String READY_STATUS = "(�غ�Ϸ�)";
	final String GAME_START = "GAME_START";

	boolean checkAllReady(){
		boolean bAllready = true;

		for(OneClientModule ocm : ms.v){
			bAllready = bAllready && ocm.readyStatus.equals(READY_STATUS);
		}
		if(ms.v.size()>=2 && bAllready){ //������ 3�� �̻� ��� �غ�Ǹ�.
			startGame();
		}

		return bAllready;
	}

	final String SEND_METER = "SEND_METER";
	void protocolProcess(String msg){
		ms.pln(msg);
		
		switch(msg){
			case REQUEST_LIST:
				responsList();
				break;
			case READY_OK:
				if(!readyStatus.equals(READY_STATUS)){
					readyStatus = READY_STATUS;
					responsList();
				}
				break;
		}

		String body="";
		if(msg.startsWith(SEND_METER)){
			body = msg.substring(SEND_METER.length() ); //��� �߶󳻰�
			try{
				int selectedNumber = Integer.parseInt(body);
				ms.nowNumber = ms.nowNumber+selectedNumber;
				myNumber = myNumber+selectedNumber;

				int nTemp = ms.playerOrder;
				nTemp++;
				nTemp = nTemp%ms.v.size();
				ms.playerOrder=nTemp;
				System.out.println("���� ����� "+ms.playerOrder);
			}catch(NumberFormatException nfe){ }
		}
	}

    final String REQUEST_LIST = "REQUEST_LIST";
	final String RESPONSE_LIST = "RESPONSE_LIST";
	void responsList(){
		StringBuffer sBuffer = new StringBuffer();
		for(OneClientModule ocm : ms.v){
			sBuffer.append(ocm.chatId+"\n"+ocm.readyStatus+"\n\n");
		}
		boardcast(RESPONSE_LIST+sBuffer);
		ms.pln(RESPONSE_LIST+sBuffer);
	}

	void closeAll(){
		try{
			if(dis != null){ dis.close(); }
			if(dos != null){ dos.close(); }
			if(is != null){ is.close(); }
			if(os != null){ os.close(); }
			if(s != null){ s.close(); }
 		}catch(IOException ie){}
	}

	void boardcast(String msg){ //��� Ŭ����
		try{
			for(OneClientModule ocm : ms.v){
				ocm.dos.writeUTF(msg);
				ocm.dos.flush();
			}
		}catch(IOException ie){}
	}
}



