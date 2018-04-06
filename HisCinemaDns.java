import java.io.*;
import java.net.*;

public class HisCinemaDns {
	static int PORT = 40290;
	private Socket connectionSocket;

	public static void main(String[] args) throws Exception{
		new HisCinemaDns().runServer();

	}
	public void runServer() throws Exception{
		
		String path = "C:/Users/LiranF/workspace/CPS706- W2018 Assignment/";
		String messageReceive;
		String fileLocation;
		
		ServerSocket welcomeSocket = new ServerSocket(PORT);
		welcomeSocket.setSoTimeout(60000);
		
		System.out.println("Server up and ready...");
		
		while(true){
			connectionSocket = welcomeSocket.accept();
			
			BufferedReader inFromClient = 
					new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			messageReceive = inFromClient.readLine();
			fileLocation = this.parseRequest(messageReceive);
			File file = new File(path + fileLocation);
			sendFile(file);
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
