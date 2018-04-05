import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Client
{
    public static final int PORT = 40290;
    public static final String IPADDRESS = "localhost"; //99.246.236.65

    public static void main(String[] args) throws Exception {
        new Client().runClient();
    }


    public void runClient() throws Exception{

        LinkedList<String> links = getIndexTCP();

        BufferedReader brClientFileChoice = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Pick a file from 1-4");
        String clientChoice = brClientFileChoice.readLine();

        if (clientChoice.equalsIgnoreCase("exit"))
        {
            System.out.println("Exiting program...");
            System.exit(0);
        }
        
        /* else if (Integer.parseInt(clientChoice) > links.size())
        {
            System.out.println("Pick again");
            clientChoice = brClientFileChoice.readLine();
        } */

        else
        {
            for(int i = 0; i < links.size(); i++)
            {
                if (links.get(i).contains(clientChoice))
                {
                    System.out.println(links.get(i));
                }
            }
        }
    }


    public LinkedList<String> getIndexTCP() throws IOException
    {
        InputStream inputStream;
        OutputStream outputStream;
        byte[] bytes;
        Socket clientSocket;
        PrintWriter outToServer;
        int count;
        File file = new File("src/ClientFiles/HisCinemaIndex.html");
        LinkedList<String> linksFromIndex = new LinkedList<>();

        //clientSocket = new Socket("localhost", PORT);
        clientSocket = new Socket(InetAddress.getByName(IPADDRESS), PORT);

        outToServer = new PrintWriter(clientSocket.getOutputStream(), true); //outputs to server

        outToServer.println("GET src/index.html HTTP/1.1\r\n\n");
        outToServer.flush();

        bytes = new byte[1024 * 2];
        inputStream = clientSocket.getInputStream();
        outputStream = new FileOutputStream(file);
        while ((count = inputStream.read(bytes)) >= 0) {
            outputStream.write(bytes, 0, count);
            System.out.println("count " + count);
        }

        linksFromIndex = htmlParser(file);
        return linksFromIndex;
    }


    public LinkedList<String> htmlParser (File htmlFile) throws IOException
    {
        LinkedList<String> links = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(htmlFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.contains("<"))
                    links.add(line);
            }
        }
        System.out.println("list " + links);
        return links;
    }
}