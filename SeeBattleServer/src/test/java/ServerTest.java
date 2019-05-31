import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerTest {
    public static void main(String args[])
    {
        int port = 3300;
        Server server = new Server(port);
        server.start();
        System.out.println("server started on port : " + Integer.toString(port));

        // test client work
        try {
            Thread.sleep(500);
            Socket socket = new Socket("127.0.0.1", port);

            DataInputStream inputStream =  new DataInputStream(socket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

            outStream.writeChar('c');
            outStream.flush();

            outStream.writeInt("sister".length());
            outStream.writeBytes("sister");
            outStream.writeInt("1234".length());
            outStream.writeBytes("1234");
            outStream.flush();

            System.out.println("send : (sister, 1234)");

            boolean result = inputStream.readBoolean();
            System.out.println("got answer: " + (result? "ok" : "not ok"));

            int pairsCounter = inputStream.readInt();
            while(pairsCounter --> 0)
            {
                int nameSize = inputStream.readInt();
                byte[] nameBytes = inputStream.readNBytes(nameSize);

                String name = new String(nameBytes);

                int ipSize = inputStream.readInt();
                byte[] ipBytes = inputStream.readNBytes(ipSize);

                String address = new String(ipBytes);

                System.out.println("got pair (" + name + ", " + address + ")");
            }

            inputStream.close();
            outStream.close();
            socket.close();

            System.out.println("disconnect");
            // disconnect
            socket = new Socket("127.0.0.1", port);

            inputStream =  new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());

            outStream.writeChar('d');
            outStream.flush();

            outStream.writeInt("sister".length());
            outStream.writeBytes("sister");
            outStream.flush();

            System.out.println("send : (sister)");

            result = inputStream.readBoolean();
            System.out.println("got answer: " + (result? "ok" : "not ok"));

            pairsCounter = inputStream.readInt();
            while(pairsCounter --> 0)
            {
                int nameSize = inputStream.readInt();
                byte[] nameBytes = inputStream.readNBytes(nameSize);

                String name = new String(nameBytes);

                int ipSize = inputStream.readInt();
                byte[] ipBytes = inputStream.readNBytes(ipSize);

                String address = new String(ipBytes);

                System.out.println("got pair (" + name + ", " + address + ")");
            }

            inputStream.close();
            outStream.close();
            socket.close();

            server.endWork();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
