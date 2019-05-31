import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        int port = 1488;
        Server server = new Server(port);
        server.start();
        System.out.println("server started");

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
