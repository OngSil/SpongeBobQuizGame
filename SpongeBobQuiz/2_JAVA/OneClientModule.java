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
				showSystemMsg("[ " + clientName + " ���� �����ϼ̽��ϴ�. ]\n(���� ������ �� : " + ms.clientList.size() + "�� / 6��)");  
				//showSystemMsg("clientJobSent: "+clientJobSent);
				ms.textArea.append(" [ ���� ������ ��� (�� " + ms.clientList.size() + "�� ������) ]\n");
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
				showSystemMsg("[ " + clientName + "���� �����ϼ̽��ϴ�. ]\n(���� ������ �� : " + ms.clientList.size() + "�� / 6��)");  
				ms.textArea.append("[ ���� ������ ��� (�� " + ms.clientList.size() + "�� ������) ]\n");
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
				showSystemMsg("��ε� �غ�Ϸ�");
				showSystemMsg("//Start");
				//showSystemMsg("//ReadyAll");
				for(int i=5; i>0; i--){
					try{
						showSystemMsg(i+"�� �Ŀ� ���� ����!");
						Thread.sleep(1000);
					}catch(InterruptedException ie){}
					}
					ms.gameStart=true;
					giveJob();
					ms.tm.start();
				
			}
		}else if(tempmsg.equals("//Postpone")){	
			//showSystemMsg("//Timer" + (toTime(time)));
			//System.out.println("����Ʈ����ư�� ������ //����Ʈ�� �޼���������...");
		}else if(tempmsg.startsWith("//ReduceTime")){
			//ms.Ÿ�Ӱ����޼ҵ�.����--??
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
				//list����/����/����/
			
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
			
				else if(tempmsg.startsWith("//isMyjob")){//ismyjob���� : ����
			clientMyjobistemp = tempmsg.substring(14);
			for(OneClientModule ocm : ms.clientList){
				if((ocm.clientMyjobis).equals(clientMyjobistemp)){
					ocm.clientName = "�����ùΤ�";

				}	
			}
			
				//clientMyjobis = tempmsg.substring(9);

				*/
		
		}else if(tempmsg.startsWith("//Vote")){		//Vote����

			String nickVoted = tempmsg.substring(6); //����
				for(OneClientModule ocm : ms.clientList){
					if(nickVoted.equals(ocm.clientName)){
						try{
							ocm.dos.writeUTF("//ǥȹ��");	//���������� //ǥȹ���̶�� �޽����� ��.
							ocm.dos.flush();
							break; 
						}catch(IOException ie){}
					}
				}
			//Vote����
			//Vote����

		}else if(tempmsg.startsWith("//��ǥ���")){//��ǥ��� �ù� (//ǥȹ���̶�� �޼����� ���� �����̴� //��ǥ��� ���� �̶�� ����.)
			String resultgot = tempmsg.substring(6);// �ù�
			System.out.println(resultgot);
			if(resultgot.equals("���Ǿ�")){
				ms.mafiaV++;
				//ms.mafiaV = mafiaVoted;
				System.out.println(ms.mafiaV);
			}else if(resultgot.equals("����")){
				ms.policeV++;
				//ms.policeV = policeVoted;
				System.out.println(ms.policeV);
			}else if(resultgot.equals("�ù�1")){
				ms.citizen1V++;
				//ms.citizen1V = citizenVoted1;
				System.out.println(ms.citizen1V);
			}else if(resultgot.equals("�ù�2")){
				ms.citizen2V++;
				//ms.citizen2V = citizenVoted2;
				System.out.println(ms.citizen2V);
			}else if(resultgot.equals("�ù�3")){
				ms.citizen3V++;
				//ms.citizen3V = citizenVoted3;
				System.out.println(ms.citizen3V);
			}else if (resultgot.equals("�ù�4")){
				ms.citizen4V++;
				//ms.citizen4V = citizenVoted4;
				System.out.println(ms.citizen4V);
				
			}


				
			
			
			
			
		}else if(tempmsg.startsWith("*����*")){
			for(OneClientModule ocm : ms.clientList){
				if((ocm.clientMyjobis).equals("����")){
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
				
				showSystemMsg("~~���Ǿư� �̰���ϴ�~~");
				ms.gameStart=false;
				
			}

			for(OneClientModule ocm : ms.clientList){
				//Mkilled���� : ����
				if((ocm.clientMyjobis).equals(tempmsg.substring(14))){
					ms.listAlive.remove(tempmsg.substring(14));
					ocm.clientName = "�����ùΤ�";
			
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
					if((ocm.clientMyjobis).equals("���Ǿ�")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";
						ms.gameStart=false;

					}	
				}
				showSystemMsg("!!! ���Ǿư� ���ش��߽��ϴ�. !!!");
				ms.listAlive.remove("���Ǿ�");
				


			}else if(ms.policeV>ms.mafiaV && ms.policeV>ms.citizen1V && ms.policeV>ms.citizen2V && ms.policeV>ms.citizen3V && ms.policeV>ms.citizen4V){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("����")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";
					}
				}
				showSystemMsg("!!! ������ ���ش��߽��ϴ�. !!!");
				ms.listAlive.remove("����");
			}else if(ms.citizen1V>ms.policeV && ms.citizen1V>ms.citizen4V && ms.citizen1V>ms.citizen2V && ms.citizen1V>ms.citizen3V && ms.citizen1V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("�ù�1")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
					
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";
					}
				}
				showSystemMsg("!!! �ù��� ���ش��߽��ϴ�. !!!");
				ms.listAlive.remove("�ù�1");
			}else if(ms.citizen2V>ms.policeV && ms.citizen2V>ms.citizen4V && ms.citizen2V>ms.citizen2V && ms.citizen2V>ms.citizen3V && ms.citizen2V>ms.citizen1V){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("�ù�2")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";
					}
				}
				ms.listAlive.remove("�ù�2");
				showSystemMsg("!!! �ù��� ���ش��߽��ϴ�. !!!");
			}else if(ms.citizen3V>ms.policeV && ms.citizen3V>ms.citizen4V && ms.citizen3V>ms.citizen2V && ms.citizen3V>ms.citizen1V && ms.citizen3V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("�ù�3")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
					
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";
					}
				}
				ms.listAlive.remove("�ù�3");
				showSystemMsg("!!! �ù��� ���ش��߽��ϴ�. !!!");
			}else if(ms.citizen4V>ms.policeV && ms.citizen4V>ms.citizen1V && ms.citizen4V>ms.citizen2V && ms.citizen4V>ms.citizen3V && ms.citizen4V>ms.mafiaV){
				for(OneClientModule ocm : ms.clientList){
					if((ocm.clientMyjobis).equals("�ù�4")){
						ocm.dos.writeUTF("//Kill");
						ocm.dos.flush();
						
						//listNicknames.remove(ocm.clientName);
						ocm.clientName = "�����ùΤ�";

					}
				}
				ms.listAlive.remove("�ù�4");
				showSystemMsg("!!! �ù��� ���ش��߽��ϴ�. !!!");
			}else{
				showSystemMsg("��ǥ�� ��ȿó�� �Ǿ����ϴ�.");
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
				
				showSystemMsg("~~���Ǿư� �̰���ϴ�~~");
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
		String Jobs[] = {"���Ǿ�", "����", "�ù�1", "�ù�2", "�ù�3", "�ù�4"};
		

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

	

