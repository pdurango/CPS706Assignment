import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Client
{
    public static final int PORTHIS = 40290;
    public static final String IPADDRESSHIS = "localhost"; //99.246.236.65
    public static final int PORTHER = 40291;
    public static final String IPADDRESSHER = "localhost"; //99.246.236.65

    public static void main(String[] args) throws Exception
    {
        new Client().runClient();
    }


    public void runClient() throws Exception
    {

        LinkedList<String> links = getIndexTCP();

        if (!links.isEmpty())
        {
            BufferedReader brClientFileChoice = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Pick a file from 1-4");
            String clientChoice = brClientFileChoice.readLine();

            if (clientChoice.equalsIgnoreCase("exit"))
            {
                System.out.println("Exiting program...");
                System.exit(0);
            } else
            {
                for (int i = 0; i < links.size(); i++)
                {
                    if (links.get(i).contains(clientChoice))
                    {
                        System.out.println(links.get(i));
                        getCDNFileTCP(links.get(i));
                    }
                }
            }
        } else
            System.exit(0);
    }


    public LinkedList<String> getIndexTCP() throws IOException //HisCinemaServer
    {
        InputStream inputStream;
        OutputStream outputStream;
        byte[] bytes;
        Socket clientSocket;
        PrintWriter outToServer;
        int count;
        File file = new File("src/ClientFiles/HisCinemaIndex.html");
        LinkedList<String> linksFromIndex;
        String serverHTTPMessage;

        //clientSocket = new Socket("localhost", PORT);
        clientSocket = new Socket(InetAddress.getByName(IPADDRESSHIS), PORTHIS);

        outToServer = new PrintWriter(clientSocket.getOutputStream(), true); //outputs to server

        outToServer.println("GET index.html HTTP/1.1");
        outToServer.flush();

        bytes = new byte[1024 * 8];
        inputStream = clientSocket.getInputStream();
        outputStream = new FileOutputStream(file);
        while ((count = inputStream.read(bytes)) >= 0)
        {
            outputStream.write(bytes, 0, count);
        }

        serverHTTPMessage = htmlParserHTTPMessage(file);
        System.out.println("message from HisCinemaServer: " + serverHTTPMessage);
        linksFromIndex = htmlParser(file);
        return linksFromIndex;
    }


    public void getCDNFileTCP(String videoLink) throws IOException //HerCDNServer
    {
        Socket clientSocket;
        int count;
        int fileRequestedNum = Integer.parseInt(videoLink.replaceAll("[\\D]", ""));
        String CDNServerName = "www.herCDN.com/F";
        clientSocket = new Socket(InetAddress.getByName(IPADDRESSHER), PORTHER);
        String outHTTPRequest = "GET " + CDNServerName + fileRequestedNum + " HTTP/1.1";
        System.out.println("outHTTPRequest: " + outHTTPRequest);


        //OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        System.out.println("1");
        output.write(outHTTPRequest);
        output.newLine(); //IMPORTANT
        output.flush();
        System.out.println("2");
        System.out.println("3");

        String receivedFile = "src/ClientFiles/received" + fileRequestedNum + ".png";

        InputStreamReader inputFromServer = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader bfInputFromServer = new BufferedReader(inputFromServer);
        System.out.println("4");
        String serverResponse = bfInputFromServer.readLine();
        System.out.println(serverResponse);

        File file = new File(receivedFile);

        byte[] bytes = new byte[16 * 1024];
        InputStream in = clientSocket.getInputStream();
        OutputStream out = new FileOutputStream(receivedFile);
        while ((count = in.read(bytes)) != -1)
        {
            out.write(bytes, 0, count);
        }
    }


    public LinkedList<String> htmlParser(File htmlFile) throws IOException
    {
        LinkedList<String> links = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(htmlFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!(line.contains("<") || line.contains("HTTP/1.1")))
                    links.add(line);
            }
        }
        System.out.println("list " + links);
        return links;
    }

    @SuppressWarnings("Duplicates")
    public String htmlParserHTTPMessage(File htmlFile) throws IOException
    {
        String message = "";
        File file = new File("src/ClientFiles/HerCDNIndex.html");
        try (BufferedReader br = new BufferedReader(new FileReader(htmlFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.contains("200 OK HTTP/1.1"))
                {
                    message = "200 OK HTTP/1.1";
                    break;
                } else if (line.contains("505 Version Not Supported HTTP/1.1"))
                {
                    message = "505 Version Not Supported HTTP/1.1";
                    break;
                } else if (line.contains("400 BAD REQUEST HTTP/1.1"))
                {
                    message = "400 BAD REQUEST HTTP/1.1";
                    break;
                } else if (line.contains("404 File Not Found HTTP/1.1"))
                {
                    message = "404 File Not Found HTTP/1.1";
                    break;
                }
            }
        }
        return message;
    }

    @SuppressWarnings("Duplicates")
    public String htmlParserHTTPMessage(File htmlFile, int fileNum) throws IOException
    {
        String message = "";
        String filePath = "src/ClientFiles/" + fileNum;

        try (BufferedReader br = new BufferedReader(new FileReader(htmlFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.contains("200 OK HTTP/1.1"))
                {
                    message = "200 OK HTTP/1.1";
                    break;
                } else if (line.contains("505 Version Not Supported HTTP/1.1"))
                {
                    message = "505 Version Not Supported HTTP/1.1";
                    break;
                } else if (line.contains("400 BAD REQUEST HTTP/1.1"))
                {
                    message = "400 BAD REQUEST HTTP/1.1";
                    break;
                } else if (line.contains("404 File Not Found HTTP/1.1"))
                {
                    message = "404 File Not Found HTTP/1.1";
                    break;
                } else
                {
                    BufferedReader inputStream = new BufferedReader(new FileReader(htmlFile));
                    File UIFile = new File(filePath);
                    // if File doesnt exists, then create it
                    if (!UIFile.exists())
                    {
                        UIFile.createNewFile();
                    }
                    FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
                    BufferedWriter outputStream = new BufferedWriter(filewriter);
                    String count;
                    while ((count = inputStream.readLine()) != null)
                    {
                        outputStream.write(count);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            }
        }
        return message;
    }
}

