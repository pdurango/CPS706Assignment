import java.io.*;
import java.net.*;

public class Server
{
    public static void main (String[] args)
    {
        try
        {
            ServerSocket welcomeSocket;
            Socket connection;
            String clientMessage;

            welcomeSocket = new ServerSocket(40290);
            System.out.println("Waiting for connection...");
            while (true)
            {
                connection = welcomeSocket.accept();
                System.out.println("Connection received from " + connection.getInetAddress().getHostName());
                OutputStream os = connection.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.println("What's your name?");

                BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                clientMessage = inputFromClient.readLine();
                pw.println("Hello " + clientMessage);
                pw.close();

                System.out.println("Just said hello to " + clientMessage);
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}