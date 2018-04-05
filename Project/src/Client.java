import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Client
{

    public static void main(String[] args) throws Exception
    {

        //BufferedReader brClientFileChoice = new BufferedReader(new InputStreamReader(System.in));
        //System.out.println("Pick a file from 1-4");
        //String clientChoice = brClientFileChoice.readLine();
        getLinkTCP();
    }

    public static LinkedList<String> getLinkTCP() throws Exception
    {
        InputStream inputStream;
        OutputStream outputStream;
        byte[] bytes;
        Socket clientSocket;
        PrintWriter outToServer;
        int count;

        clientSocket = new Socket("localhost", 40290);
        //clientSocket = new Socket(InetAddress.getByName("192.168.1.16"), 40290);
        //clientSocket = new Socket(InetAddress.getByName("99.246.236.65"), 40290);

        outToServer = new PrintWriter(clientSocket.getOutputStream(), true); //outputs to server

        outToServer.println("GET src/index.html HTTP/1.1\r\n\n");
        outToServer.flush();

        bytes = new byte[1024 * 2];
        inputStream = clientSocket.getInputStream();
        outputStream = new FileOutputStream("src/ClientFiles/HisCinemaIndex.html");
        while ((count = inputStream.read(bytes)) >= 0) {
            outputStream.write(bytes, 0, count);
            System.out.println("count " + count);
        }
        //parsehtml into linkedlist full of video links
        //return linkedlist
    return null;
    }

    public static LinkedList<String> htmlParser (File htmlCode)
    {
        return null;
    }
}