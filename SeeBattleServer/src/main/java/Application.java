 //package  src.

import java.util.Scanner;
import java.net.InetAddress;
import java.net.*;

public class Application {
    public static void main(String[] args) {
        int port = 8080;//changed
        Server server = new Server(port);
        server.start();
        System.out.println("server started");

        try{
        System.out.println(InetAddress.getLocalHost().getHostAddress().toString());}
        catch (Exception x){x.printStackTrace();}

        // console interface
        while(true)
        {
            Scanner scanner = new Scanner(System.in);
            String tokens[] = scanner.nextLine().split(" ");

            if (tokens[0].equals("stop"))
            {
                server.endWork();
                break;
            }

        }

        try {
            server.join();
        } catch (InterruptedException interruptedException)
        {
            System.out.println(interruptedException.toString());
        }

        System.out.println("server stopped");
    }
}
