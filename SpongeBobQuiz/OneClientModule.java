import java.io.*;
import java.net.*;
import java.util.*;

/* 중간관리자 모듈 : 모든 기능이 동시에 돌아가야 하기 때문에 Thread로 만들어쥬
   1. 서버의 소켓을 가져옵니다(클라이언트 연결 ㄱㄱ)
   2. 클라이언트로부터 메세지 받고
   3. 브로드캐스트로 뿌려줍니다
   4. 점수..도 뿌려줘야되는디?
*/
public class OneClientModule extends Thread {
	Socket s;
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;

	String msg2 = "";
	String clientId = "";
	String answer = "";
	int score = 0;
	
	QServer qs;
	
	//이름, 메세지or답 받아야하니까
	LinkedHashMap<String, String> clientMsg = new LinkedHashMap<String, String>();
	//이름, 점수 계산용? 내보내는용?
	LinkedHashMap<String, Integer> clientScore = new LinkedHashMap<String, Integer>();
	ArrayList<String> listClientId = new ArrayList<String>();

	OneClientModule(QServer qs){
		this.qs = qs;
		this.s = qs.s;

		try{
			is = s.getInputStream();
			dis = new DataInputStream(is);
			os = s.getOutputStream();
			dos = new DataOutputStream(os);
		}catch(IOException ie){}
	}

	public void run(){
		listen();
	}
	void listen(){
		try{			
				clientId = dis.readUTF();
				showSystemMsg(clientId +"님 입장! "+"(현재 접속자 수: "+qs.clientList.size()+"/4)");
				listClientId.add(clientId);
				qs.clientInfo.put(clientId, answer);
				setClientStatus();
			while(dis != null){
				String msg = dis.readUTF();
				filterMsg(msg);
			}
		}catch(IOException ie){
			showSystemMsg("//Exit"+clientId);
			qs.clientList.remove(this);
			//qs.clientList.remove(clientId);
			listClientId.remove(this);
			//closeAll();
			qs.textArea.append(clientId+"님 퇴장!! (총인원: "+qs.clientList.size()+"명)");

			if(qs.clientList.isEmpty() == true){
				try{
					s.close(); System.exit(0);
				}catch(IOException iee){} 
			} 
			showSystemMsg(clientId + "님 퇴장!");  
		}finally{
			closeAll();
		}
	}
	void filterMsg(String msg){
		if(msg.startsWith("//Chat")){
			answerCheck(msg.substring(6).trim());
			String msg2 = msg.substring(6);
			showSystemMsg(msg2);
		}else if(msg.startsWith("//Answer")){
			//answerCheck(msg.substring(8).trim());
		}else if(msg.startsWith("//Ready")){
			qs.readyPlayer++;
			
			if(qs.readyPlayer == 4 || qs.readyPlayer == qs.clientList.size()){
				showSystemMsg("//Start");
			//	scoreOutput();
				for(int a=3; a>0; a--){
					try{
						showSystemMsg(a+"초 후에 게임이 시작됩니다.");
						Thread.sleep(1000);
					}catch(InterruptedException iie){}
				}
				//타이머 시작
				qs.gameStart = true;
				Exam ex = new Exam(); ex.start();
			}
		}
	}

	//점수 계산해서 내보내기
	StringBuffer sb = new StringBuffer();
	
	void scoreOutput(int score2){
		for(OneClientModule ocm : qs.clientList){
			sb.append(ocm.score+"/");			
		}
	//	showSystemMsg("//Score"+"0/1/2/3/");
	//	showSystemMsg("//Score"+qs.Score1+"/"+qs.Score2+"/"+qs.Score3+"/"+qs.Score4+"/");
		showSystemMsg("//Score"+qs.clientList.size()+"/"+sb);
	}
	

	
	ArrayList<String> exList2 = new ArrayList<String>();
	ArrayList<String> answerList2 = new ArrayList<String>();
	void answerCheck(String msg){
		String ansNick = msg.substring(0, msg.indexOf(" ")+1);
		String ansStr = msg.substring(msg.lastIndexOf(" ")+1);
		BufferedReader br2;
		String line3 = "";
			try{
				FileReader fr = new FileReader("imgs/스폰지밥.txt");
				br2 = new BufferedReader(fr);
				while((line3 = br2.readLine()) != null){
					exList2.add(line3);
				}
			}catch(IOException ie){}
			//문제 리스트에서 7의배수 인덱스(정답)만 quizList에 담기
			for(int j=1; j<exList2.size(); j++){
				if( j%7 == 0 ){
					answerList2.add(exList2.get(j-1));
				} else if( j%7 != 0 ){
					continue;
				}
			}
//answerList2.get(qs.round)
			if( ansStr.trim().equals(answerList2.get(qs.round).trim()) ){
					if( ansNick.trim().equals(this.clientId) ){ //QMain.nickName	
					score++;
					
					qs.round++;
				}	
			}
			scoreOutput(score);
			
	}

	
	//문제 전체 리스트
	ArrayList<String> exList = new ArrayList<String>();
	//문제/답만 뽑아낸 리스트
	ArrayList<String> answerList = new ArrayList<String>();
	ArrayList<String> quizList = new ArrayList<String>();

	//문제랑 답 넣은 hashMap
//	HashMap<String[], String> examList = new HashMap<String[], String>();
	class Exam extends Thread {
		BufferedReader br;
		String line2 = "";
		StringBuffer sb2 = new StringBuffer();

		public void run(){
			try{
				FileReader fr = new FileReader("imgs/스폰지밥.txt");
				
				br = new BufferedReader(fr);
				while((line2 = br.readLine()) != null){
					exList.add(line2);
				}
			}catch(IOException ie){}
			
			//문제 리스트에서 7의배수 인덱스(정답)만 quizList에 담기

			for(int j=1; j<exList.size(); j++){
				if( j%7 == 0 ){
					answerList.add(exList.get(j-1));
				} else if( j%7 != 0 ){
					quizList.add(exList.get(j-1));
					// continue;
				}
			}
			for(int k=0; k<exList.size(); k++){
				sb2.append(exList.get(k)+"/");
			}
			showSystemMsg("//Exam"+sb2);
		}	
	}

	public void closeAll(){
			try{
				if(dos != null) dos.close();
				if(dis != null) dis.close();
				if(s != null) s.close();
			}catch(IOException ie){}
		}
	void showSystemMsg(String str){ //모든 접속자에게 메시지 뿌려줍니다
			try{
				for(OneClientModule ocm : qs.clientList){
					ocm.dos.writeUTF(str);
					ocm.dos.flush();
				}
			}catch(IOException ie){}
	}
	void setClientStatus(){
		String[] keys = new String[qs.clientInfo.size()];
		int index = 0;
		for(Map.Entry<String, String> mapEntry : qs.clientInfo.entrySet()){
			keys[index] = mapEntry.getKey();
			index++;;
		}
		for(int i=0; i<qs.clientInfo.size(); i++){
			showSystemMsg("//CList" + keys[i] + "#" + i);
		}
	}

}
