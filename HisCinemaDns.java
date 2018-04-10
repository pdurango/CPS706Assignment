import java.io.*;
import java.net.*;

public class HisCinemaDns {
	private static final int PORT = 40290;
	private Socket connectionSocket;

	public static void main(String[] args) throws Exception{
		new HisCinemaDns().runTCPServer();
	}
	
	public void runTCPServer() throws Exception{
		ServerSocket welcomeSocket = new ServerSocket(PORT); // Create socket with port  
		welcomeSocket.setSoTimeout(60000); // Set timeout for server of 1 minute
		
		System.out.println("Server up and ready...");
		
		while(true){
			connectionSocket = welcomeSocket.accept(); // Listens to a connection and accepts it from client. Returns new socket
			new ServerThread(connectionSocket).start();
		}
		
	}
	
	public class ServerThread extends Thread{
		Socket socket;
		ServerThread(Socket socket){
			this.socket = socket;
		}
		public void run(){
			try{
				String path = "C:/Users/LiranF/workspace/CPS706- W2018 Assignment/"; //Path where index file resides
				String messageReceive; // Variable will store the 'GET' request received from client
				String fileLocation; // Variable will store the directory and name of file. i.e. directory/nameoffile.html
				
				BufferedReader inFromClient = 
						new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				messageReceive = inFromClient.readLine();
				fileLocation = this.parseRequest(messageReceive);
				File file = new File(path + fileLocation);
				sendFile(file);
			}catch (IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public String parseRequest(String message){
			String[] tokens = message.split(" ");
			message = tokens[1].toString();
			return message;
		}
		
		public void sendFile(File file) throws Exception{
			FileInputStream readFile = new FileInputStream(file); 
			DataOutputStream outToClient =
					new DataOutputStream(connectionSocket.getOutputStream());
			
			byte[] buffer = new byte[4096];
			while (readFile.read(buffer) > 0) {
				outToClient.write(buffer);
			}  
			outToClient.flush();
			readFile.close();
			outToClient.close();
			System.out.println("File sent succesfully!");
		}
	
	}
}
