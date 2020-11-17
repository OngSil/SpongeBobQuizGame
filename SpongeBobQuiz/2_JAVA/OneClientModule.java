package kopia.kp;
import java.io.*;
import java.net.*;
import java.util.*;

class OneClientModule extends Thread
{
	MafiaServer ms;
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	String clientName = "";
	String clientJobSent = "";
	String clientMyjobis ="";
	String Jobgiven="";

	//ArrayList<String> listAlive = new ArrayList<String>();
	ArrayList<String> listNicknames = new ArrayList<String>();

	
	String realmsg, dummy;
	int mafiaVoted =0;
	int policeVoted =0;
	int citizenVoted1 =0;
	int citizenVoted2 =0;
	int citizenVoted3 =0;
	int citizenVoted4 =0;

	OneClientModule(MafiaServer ms){
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

	public void run(){
		listen();
	}
	
	void listen(){
			try{
				clientName = dis.readUTF();
				//clientJobSent = dis.readUTF();
				showSystemMsg("[ " + clientName + " 님이 입장하셨습니다. ]\n(현재 접속자 수 : " + ms.clientList.size() + "명 / 6명)");  
				//showSystemMsg("clientJobSent: "+clientJobSent);
				ms.textArea.append(" [ 현재 접속자 명단 (총 " + ms.clientList.size() + "명 접속중) ]\n");
				ms.clientInfo.put(clientName,dummy);
				setClientStatus();
				
				listNicknames.add(clientName);
				while(true){
					String msg = dis.readUTF();
					filterMsg(msg);
					/*

					if(ms.gameStart){
						if(listAlive.size()<3){
							ms.gameStart=false;
						}
						
					}
					*/
				}
			}catch(IOException io){
				ms.clientList.remove(this);
				ms.clientInfo.remove(clientName);
				setClientStatus();
				showSystemMsg("[ " + clientName + "님이 퇴장하셨습니다. ]\n(현재 접속자 수 : " + ms.clientList.size() + "명 / 6명)");  
				ms.textArea.append("[ 현재 접속자 명단 (총 " + ms.clientList.size() + "명 접속중) ]\n");
				ms.gameStart = false;
				showSystemMsg("//GmEnd"); 
				/*if(ms.clientList.isEmpty() == true){
					try{
						ms.ss.close(); System.exit(0);
					}catch(IOException e){}
				}*/
			}finally{
				closeAll();
			}
		}
	public void showSystemMsg(String msgToshow){
		try{
			for(OneClientModule ocm : ms.clientList){
				
				ocm.dos.writeUTF(msgToshow);
				ocm.dos.flush();
			}
		}catch(IOException ie){}
	}

	public void filterMsg(String tempmsg){ 
		if(tempmsg.startsWith("//Chat")){
			realmsg = tempmsg.substring(6);
			showSystemMsg(realmsg);
		}else if(tempmsg.startsWith("//Ready")){
			ms.readyPlayer++;
			showSystemMsg("readyPlayer: "+ms.readyPlayer);
			if(ms.readyPlayer == 6){
				showSystemMsg("모두들 준비완료");
				showSystemMsg("//Start");
				//showSystemMsg("//ReadyAll");
				for(int i=5; i>0; i--){
					try{
						showSystemMsg(i+"초 후에 게임 시작!");
						Thread.sleep(1000);
					}catch(InterruptedException ie){}
					}
					ms.gameStart=true;
					giveJob();
					ms.tm.start();
				
			}
		}else if(tempmsg.equals("//Postpone")){	
			//showSystemMsg("//Timer" + (toTime(time)));
			//System.out.println("포스트폰버튼이 눌려서 //포스트폰 메세지가쓰임...");
		}else if(tempmsg.startsWith("//ReduceTime")){
			//ms.타임관리메소드.변수--??
		}else if(tempmsg.equals("//ShowList")){
			
			/*StringBuffer clientNicknamesList = new StringBuffer();
				int numberUsers = 0;
			for(String ln : listNicknames){
					clientNicknamesList.append(ln+"/");
					numberUsers++;

			}*/
			
			StringBuffer clientNicknamesList = new StringBuffer();
			int numberUsers = 0;
			for(OneClientModule ocm : ms.clientList){
				clientNicknamesList.append(ocm.clientName+"/");
				numberUsers++;
				//list주현/정훈/혜빈/
			
			}

			for(OneClientModule ocm : ms.clientList){
				try{
					ocm.dos.writeUTF("//List"+ clientNicknamesList + numberUsers);
					ocm.dos.flush();
				}catch(IOException ie){}
			}
		}else if(tempmsg.startsWith("//isMyjob")){
				clientMyjobis = tempmsg.substring(9);
		/*
		}
			
				else if(tempmsg.startsWith("//isMyjob")){//ismyjob직업 : ㅇㅇ
			clientMyjobistemp = tempmsg.substring(14);
			for(OneClientModule ocm : ms.clientList){
				if((ocm.clientMyjobis).equals(clientMyjobistemp)){
					ocm.clientName = "죽은시민ㅠ";

				}	
			}
			
				//clientMyjobis = tempmsg.substring(9);

				*/
		
		}else if(tempmsg.startsWith("//Vote")){		//Vote주현

			String nickVoted = tempmsg.substring(6); //주현
				for(OneClientModule ocm : ms.clientList){
					if(nickVoted.equals(ocm.clientName)){
						try{
							ocm.dos.writeUTF("//표획득");	//주현이한테 //표획득이라고 메시지가 감.
							ocm.dos.flush();
							break; 
						}catch(IOException ie){}
					}
				}
			//Vote주현
			//Vote혜빈

		}else if(tempmsg.startsWith("//투표결과")){//투표결과 시민 (//표획득이라는 메세지를 받은 주현이는 //투표결과 직업 이라고 보냄.)
			String resultgot = tempmsg.substring(6);// 시민
			System.out.println(resultgot);
			if(resultgot.equals("마피아")){
				ms.mafiaV++;
				//ms.mafiaV = mafiaVoted;
				System.out.println(ms.mafiaV);
			}else if(resultgot.equals("경찰")){
				ms.policeV++;
				//ms.policeV = policeVoted;
				System.out.println(ms.policeV);
			}else if(resultgot.equals("시민1")){
				ms.citizen1V++;
				//ms.citizen1V = citizenVoted1;
				System.out.println(ms.citizen1V);
			}else if(resultgot.equals("시민2")){
				ms.citizen2V++;
				//ms.citizen2V = citizenVoted2;
				System.out.println(ms.citizen2V);
			}else if(resultgot.equals("시민3")){
				ms.citizen3V++;
				//ms.citizen3V = citizenVoted3;
				System.out.println(ms.citizen3V);
			}else if (resultgot.equals("시민4")){
				ms.citizen4V++;
				//ms.citizen4V = citizenVoted4;
				System.out.println(ms.citizen4V);
				
			}


				
			
			
			
			
		}else if(tempmsg.startsWith("*경찰*")){
			for(OneClientModule ocm : ms.clientList){
				if((ocm.clientMyjobis).equals("경찰")){
					try{
						ocm.dos.writeUTF(tempmsg);
						ocm.dos.flush();
					}catch(IOException ie){}

				}	
			}
			
		}else if(tempmsg.equals("//gameEnd")){
			ms.gameStart=false;
		}else if(tempmsg.startsWith("//Mkilled")){
			if(ms.listAlive.size()<3){
				
				showSystemMsg("~~마피아가 이겼습니다~~");
				ms.gameStart=false;
				
			}

			for(OneClientModule ocm : ms.clientList){
				//Mkilled직업 : ㅇㅇ
				if((ocm.clientMyjobis).equals(tempmsg.substring(14))){
					ms.listAlive.remove(tempmsg.substring(14));
					ocm.clientName = "죽은시민ㅠ";
			
				}	
			}
		}
		
		else{
			showSystemMsg(tempmsg);
		}
	}


	public void getOut(){

		try{
			if(ms.mafiaV>ms.policeV && ms.mafiaV>ms.citizen1V && ms.mafiaV>ms.citizen2V && ms.mafiaV>ms.citizen3V && ms.mafiaV>ms.citizen4V){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("마피아")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";
						ms.gameStart=false;

					}	
				}
				showSystemMsg("!!! 마피아가 살해당했습니다. !!!");
				ms.listAlive.remove("마피아");
				


			}else if(ms.policeV>ms.mafiaV && ms.policeV>ms.citizen1V && ms.policeV>ms.citizen2V && ms.policeV>ms.citizen3V && ms.policeV>ms.citizen4V){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("경찰")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";
					}
				}
				showSystemMsg("!!! 경찰이 살해당했습니다. !!!");
				ms.listAlive.remove("경찰");
			}else if(ms.citizen1V>ms.policeV && ms.citizen1V>ms.citizen4V && ms.citizen1V>ms.citizen2V && ms.citizen1V>ms.citizen3V && ms.citizen1V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("시민1")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
					
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";
					}
				}
				showSystemMsg("!!! 시민이 살해당했습니다. !!!");
				ms.listAlive.remove("시민1");
			}else if(ms.citizen2V>ms.policeV && ms.citizen2V>ms.citizen4V && ms.citizen2V>ms.citizen2V && ms.citizen2V>ms.citizen3V && ms.citizen2V>ms.citizen1V){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("시민2")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";
					}
				}
				ms.listAlive.remove("시민2");
				showSystemMsg("!!! 시민이 살해당했습니다. !!!");
			}else if(ms.citizen3V>ms.policeV && ms.citizen3V>ms.citizen4V && ms.citizen3V>ms.citizen2V && ms.citizen3V>ms.citizen1V && ms.citizen3V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("시민3")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
					
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";
					}
				}
				ms.listAlive.remove("시민3");
				showSystemMsg("!!! 시민이 살해당했습니다. !!!");
			}else if(ms.citizen4V>ms.policeV && ms.citizen4V>ms.citizen1V && ms.citizen4V>ms.citizen2V && ms.citizen4V>ms.citizen3V && ms.citizen4V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("시민4")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "죽은시민ㅠ";

					}
				}
				ms.listAlive.remove("시민4");
				showSystemMsg("!!! 시민이 살해당했습니다. !!!");
			}else{
				showSystemMsg("투표가 무효처리 되었습니다.");
			}
		}catch(IOException ie){}
		

	}
	void setClientStatus(){
		String [] keys = new String[ms.clientInfo.size()];
		int index = 0;
		for(Map.Entry<String, String> mapEntry : ms.clientInfo.entrySet()){
			keys[index] = mapEntry.getKey();
			index++;
		}
		for(int i=0; i<ms.clientList.size(); i++){
			showSystemMsg("//CList" + keys[i] + "#"+ i);
		}

	}



	public void resetVote(){
			System.out.println("ms.listALive.size"+ms.listAlive.size());
		
			if(ms.listAlive.size()<3){
				
				showSystemMsg("~~마피아가 이겼습니다~~");
				ms.gameStart=false;
				
			}
						
		ms.mafiaV =0;
		ms.policeV =0;
		ms.citizen1V =0;
		ms.citizen2V =0;
		ms.citizen3V =0;
		ms.citizen4V =0;
	}

	public void giveJob(){
		String Jobs[] = {"마피아", "경찰", "시민1", "시민2", "시민3", "시민4"};
		

		Random r = new Random();
		String job;
		int a[]= new int[6];
		for(int i=0;i<6;i++ ){
			a[i]= r.nextInt(6);
			for(int j=0; j<i; j++){
				if(a[i]==a[j]){
					i--;
				}
			}
		}
		try{
			int k =0;
			for(OneClientModule ocm : ms.clientList){
					Jobgiven = ("//Job"+ Jobs[a[k]]);
					ms.listAlive.add(Jobgiven.substring(5));
					ocm.dos.writeUTF(Jobgiven);
					ocm.dos.flush();
					k++;
				}	
		}catch(IOException ie){}
		///
		for(String ad : ms.listAlive){
			System.out.println(ad);
		}
		System.out.println("listalivesize"+ms.listAlive.size());
	}

	
	public void closeAll(){
			try{
				if(dos != null) dos.close();
				if(dis != null) dis.close();
				if(s != null) s.close();
			}catch(IOException ie){}
		}
}

	

