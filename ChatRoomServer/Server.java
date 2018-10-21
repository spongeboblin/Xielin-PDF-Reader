import java.net.ServerSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;

class MySocket{
	public int id;
	public Socket socket;
	public MySocket(int id, Socket socket){
		this.id=id;
		this.socket=socket;
	}
}

public class Server {
	private static final int PORT = 12345;
	private List<MySocket> mList = new ArrayList<MySocket>();
	private ServerSocket server = null;
	private ExecutorService myExecutorService = null;
	
	private static final int ALL = -1;
	private static int count = 0;
	
	public static void main(String[] args) {
		new Server();
	}

	public Server()
	{
		try
		{
			server = new ServerSocket(PORT,5);
			InetAddress address = InetAddress.getLocalHost();
			String ip = address.getHostAddress();
			myExecutorService = Executors.newCachedThreadPool();
			System.out.println("Server is running on "+ip+":"+PORT+"...\n");
			MySocket myclient = null;
			Socket client = null;
			while(true)
			{
				count+=1;
				client = server.accept();
				myclient = new MySocket(count, client);
				mList.add(myclient);
				myExecutorService.execute(new Service(myclient));
			}
			
		}catch(Exception e){e.printStackTrace();}
	}
	
	class Service implements Runnable
	{
		private MySocket mysocket;
		private BufferedReader in = null;
		private String msg = "";
		
		public Service(MySocket mysocket) {
			this.mysocket = mysocket;
			try
			{
				in = new BufferedReader(new InputStreamReader(mysocket.socket.getInputStream()));
				msg = "Your id is "+this.mysocket.id;
				this.sendmsg(this.mysocket.id);
				msg = "User:" +this.mysocket.id+" on "+this.mysocket.socket.getInetAddress() + " joins pdf reading"
	                        +" current online number:" +mList.size();  
				//this.sendmsg(ALL);
			}catch(IOException e){e.printStackTrace();}
		}
		
		
		
		@Override
		public void run() {
			try{
				while(true)
				{
					if((msg = in.readLine()) != null)
					{
						if(msg.equals(""))
						{
                            break;
						}else{
                            this.sendmsg(ALL); 
						}
					}
				}
			}catch(Exception e){e.printStackTrace();}
		}
		
		public void sendmsg(int client_id)
		{
			System.out.println("user"+mysocket.id+" on "+mysocket.socket.getInetAddress() + "  make change: " + msg);
			int num = mList.size();
			if(client_id != ALL){
				for(int index = 0;index<num;index++){
					if(client_id==mList.get(index).id){
						Socket mSocket = mList.get(index).socket;  
						PrintWriter pout = null;  
						try {  
							pout = new PrintWriter(new BufferedWriter(  
									new OutputStreamWriter(mSocket.getOutputStream(),"UTF-8")),true);  
							pout.println(msg);  
						}catch (IOException e) {e.printStackTrace();}
						break;
					}
				}
				return;
			}
			
			for(int index = 0;index < num;index++)
			{
				Socket mSocket = mList.get(index).socket;  
                PrintWriter pout = null;  
                try {  
                    pout = new PrintWriter(new BufferedWriter(  
                            new OutputStreamWriter(mSocket.getOutputStream(),"UTF-8")),true);  
                    pout.println(msg);  
                }catch (IOException e) {e.printStackTrace();}
			}
		}
		
	}
}
