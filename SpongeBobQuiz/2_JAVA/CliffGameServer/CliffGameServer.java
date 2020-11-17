package cliffGame.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class CliffGameServer implements Runnable{
	ServerSocket ss;
	Socket s;
    int port = 7777;
    OneClientModule ocm;
	Vector<OneClientModule> v = new Vector<OneClientModule>();
	
	int nowNumber=0;
	int targetNumber = 31;
	int playerOrder = 0;

    CliffGameServer(){
		new Thread(this).start();

		try{

			ss = new ServerSocket(port);
			pln(port+"번에서 서버 대기중..");
			while(true){
				s = ss.accept();
				ocm = new OneClientModule(this);
				v.add(ocm);
                new Thread(ocm).start();
			}
		}catch(IOException ie){
			pln(port+"번 포트는 이미 사용중..");
		}finally{
			try{
				if(ss != null) ss.close();
			}catch(IOException ie){}
		}
	}
	
	@Override
	public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try{
            while(true){
				String str = br.readLine();
				switch(str){
					default:
						pln("Admin>> "+str);
						ocm.boardcast("Admin>> "+str);
						break;

					case "out":
						pln("강퇴할 상대를 입력하세요.");
						String outId = br.readLine();
						for(OneClientModule ocm : v){
							if(ocm.chatId.equals(outId)){
								v.remove(ocm);
								ocm.closeAll();
								break;
							}							
						}
						break;
					case "target":
						pln("귓속말 상대를 입력하세요.");
						String client = br.readLine();
						pln("귓속말을 입력하세요.");
						String whisper = br.readLine();
						for(OneClientModule ocm : v){
							if(ocm.chatId.equals(client)){
								ocm.dos.writeUTF("Admin whisper>> "+whisper);
								ocm.dos.flush();
								break;
							}							
						}
						break;

					case "option":
						pln("<list>회원list  <target>귓속말  <out>강퇴  <exit>서버종료");
						break;

					case "list":
						int i=0;
						for(OneClientModule ocm : v){
							pln(i+":"+ocm.chatId);
							i++;
						}
						break;

					case "exit":
						pln("서버를 종료합니다.");
						if(ocm!=null) { ocm.boardcast("서버를 종료합니다."); }
						System.exit(0);
						break;
				}
			}
		}catch(IOException ioe){ }
	}

    void pln(String str){
		System.out.println(str);
	}
	void p(String str){
		System.out.print(str);
    }
    
    public static void main(String[] args) {
        new CliffGameServer();
    }
}