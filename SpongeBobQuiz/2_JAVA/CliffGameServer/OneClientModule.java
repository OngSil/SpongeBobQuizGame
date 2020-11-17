package cliffGame.server;

import java.io.*;
import java.net.*;
import java.util.*;

//하나의 클라이언트의 메세지를 읽고 전달(브로드케스팅)하는 '서버측 클래스'
class OneClientModule implements Runnable{
	CliffGameServer ms;
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;	
	String chatId = "GUEST";
	String readyStatus = "(대기 중..)";
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
			boardcast(chatId+"님 입장!!(총인원: "+ms.v.size()+"명)"); //모든 클라이언트들에게 씀 
			ms.pln(chatId+"님 입장!!(총인원: "+ms.v.size()+"명)"); //서버의 모니터에 씀 
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

				System.out.println("목표: "+ms.targetNumber+"/ 현재 누적:"+ms.nowNumber);
				if(ms.nowNumber>=ms.targetNumber){ finishGame(); }
				
			}
		}catch(IOException ie){
			ms.v.remove(this);
			boardcast(chatId+"님 퇴장!!(총인원: "+ms.v.size()+"명)");
			ms.pln(chatId+"님 퇴장!!(총인원: "+ms.v.size()+"명)");
			responsList();
		}finally{
			closeAll();
		}
	}

	final String NOWHEIGHT = "NOWHEIGHT";
	void sendNowHeight(int nowHeight){
		boardcast(NOWHEIGHT+nowHeight+"메다");
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
		boardcast(ORDER_LIST+"<<순서>>\n\n"+sBuffer);
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
		System.out.println("목표 숫자를 넘겼습니다.");
		StringBuffer sBuffer = new StringBuffer();
		for(OneClientModule ocm : ms.v){
			if(ocm==this) continue;
			sBuffer.append(ocm.chatId+": "+ocm.myNumber+"\n\n");
		}

		for(int i=0; i<ms.v.size(); i++){
			stopTurn(i);
		}

		sBuffer.append("꼴찌는 "+ this.chatId+"이고, "+this.myNumber+"점 입니다.");
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
	final String READY_STATUS = "(준비완료)";
	final String GAME_START = "GAME_START";

	boolean checkAllReady(){
		boolean bAllready = true;

		for(OneClientModule ocm : ms.v){
			bAllready = bAllready && ocm.readyStatus.equals(READY_STATUS);
		}
		if(ms.v.size()>=2 && bAllready){ //게임은 3인 이상 모두 준비되면.
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
			body = msg.substring(SEND_METER.length() ); //헤더 잘라내고
			try{
				int selectedNumber = Integer.parseInt(body);
				ms.nowNumber = ms.nowNumber+selectedNumber;
				myNumber = myNumber+selectedNumber;

				int nTemp = ms.playerOrder;
				nTemp++;
				nTemp = nTemp%ms.v.size();
				ms.playerOrder=nTemp;
				System.out.println("다음 사람은 "+ms.playerOrder);
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

	void boardcast(String msg){ //모든 클라이
		try{
			for(OneClientModule ocm : ms.v){
				ocm.dos.writeUTF(msg);
				ocm.dos.flush();
			}
		}catch(IOException ie){}
	}
}



