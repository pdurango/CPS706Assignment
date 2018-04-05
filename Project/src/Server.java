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
            InputStream inputStream;
            OutputStream outputStream;
            int size = 0;
            byte[] bytes;
            OutputStream os;
            BufferedReader inputFromClient;

            welcomeSocket = new ServerSocket(40290);
            System.out.println("Waiting for connection...");
            while (true)
            {
                connection = welcomeSocket.accept();
                System.out.println("Connection received from " + connection.getInetAddress().getHostName());
                os = connection.getOutputStream();

                inputFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                clientMessage = inputFromClient.readLine();
                System.out.println(clientMessage);
                //pw.println("Hello " + clientMessage);

                //OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
                //outWriter.write("200 OK HTTP/1.1\r\n\n");
                //outWriter.flush();
                bytes = new byte[1024 * 2];
                inputStream = new FileInputStream("src/HisCinemaFiles/index.html");
                outputStream = connection.getOutputStream();
                while ((size = inputStream.read(bytes)) >= 0) {
                    outputStream.write(bytes, 0, size);
                    System.out.println("Size: " + size);
                }
                 inputStream.close(); outputStream.close(); inputFromClient.close(); os.close();

                System.out.println("Sent index.html to " + connection.getInetAddress().getHostName());
            }
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
}

