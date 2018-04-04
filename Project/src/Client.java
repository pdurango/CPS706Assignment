import java.io.*;
import java.net.*;

public class Client
{

    public static void main(String[] args)
    {
        try
        {
            Socket clientSocket;
            Socket connection = null;
            String message = "brown bricks";
            String serverReply;

            clientSocket = new Socket("localhost", 40290);
            //clientSocket = new Socket(InetAddress.getByName("192.168.1.16"), 40290);
            //clientSocket = new Socket(InetAddress.getByName("99.246.236.65"), 40290);

            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("Server says: " + inputFromServer.readLine());

            BufferedReader userInputBR = new BufferedReader(new InputStreamReader(System.in));
            String userInput = userInputBR.readLine();

            out.println(userInput);
            System.out.println("Server says2: " + inputFromServer.readLine());

            if ("exit".equalsIgnoreCase(userInput)) {
                clientSocket.close();
                //break;
            }


        } catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }
}