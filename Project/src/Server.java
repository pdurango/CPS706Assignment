import java.io.*;
import java.net.*;

public class Server {
    public static final int PORT = 40290;

    public static void main(String[] args) throws IOException {
        new Server().runServer();

    }
    public void runServer() throws IOException{
        ServerSocket serverSocket = new ServerSocket(PORT);
        //serverSocket.setSoTimeout(10000);
        System.out.println("Server up and ready...");
        while(true){
            Socket socket= serverSocket.accept();
            new ServerThread(socket).start();
        }

    }
    public class ServerThread extends Thread{
        Socket socket;
        ServerThread(Socket socket){
            this.socket = socket;
        }
        public void run(){
            try{
                String message = null;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter outToClient = new BufferedWriter(osw);
                while((message = bufferedReader.readLine()) != null){
                    System.out.println("Incoming message " + message);
                    break;

                }

                String [] fields = message.split("\\s");
                BufferedReader in = new BufferedReader(new FileReader("src/HisCinemaFiles/index.html"));
                String str = null;
                while((str = in.readLine()) != null){
                    outToClient.write(str + "\n");
                }
                outToClient.flush();
                bufferedReader.close();
                in.close();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}