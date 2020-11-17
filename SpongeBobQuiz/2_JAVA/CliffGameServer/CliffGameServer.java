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
			pln(port+"������ ���� �����..");
			while(true){
				s = ss.accept();
				ocm = new OneClientModule(this);
				v.add(ocm);
                new Thread(ocm).start();
			}
		}catch(IOException ie){
			pln(port+"�� ��Ʈ�� �̹� �����..");
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
						pln("������ ��븦 �Է��ϼ���.");
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
						pln("�ӼӸ� ��븦 �Է��ϼ���.");
						String client = br.readLine();
						pln("�ӼӸ��� �Է��ϼ���.");
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
						pln("<list>ȸ��list  <target>�ӼӸ�  <out>����  <exit>��������");
						break;

					case "list":
						int i=0;
						for(OneClientModule ocm : v){
							pln(i+":"+ocm.chatId);
							i++;
						}
						break;

					case "exit":
						pln("������ �����մϴ�.");
						if(ocm!=null) { ocm.boardcast("������ �����մϴ�."); }
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