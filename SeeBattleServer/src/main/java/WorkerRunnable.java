import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerRunnable implements  Runnable {
    // concurrent map <name of client, client ip>
    private static ConcurrentHashMap<String, String> clients = new ConcurrentHashMap<String, String>();

    Socket socket;
    DataInputStream inStream;
    DataOutputStream outStream;

    WorkerRunnable(Socket workerSocket)
    {
        socket = workerSocket;
    }

    /*
     * Adds a new client. Returns true if all is ok
     */
    private boolean connect(String name, String address)
    {
        if(clients.containsKey(name))
        {
            return false;
        }
        else
        {
            clients.put(name, address);
            return  true;
        }
    }

    // the same as connect
    private boolean disconnect(String name)
    {
        if(clients.containsKey(name))
        {
            clients.remove(name);

            return  true;
        }
        else
        {
            return false;
        }
    }

    // put in outStream the all clients
    // format of output is : count of pairs (let its N) pair<Name, Address> * n
    private void giveClients() throws IOException {
        outStream.writeInt(clients.size());

        for (Map.Entry<String, String> client : clients.entrySet()) {
            outStream.writeInt(client.getKey().length());
            outStream.writeBytes(client.getKey());

            outStream.writeInt(client.getValue().length());
            outStream.writeBytes(client.getValue());
        }

        outStream.flush();
    }

    public void run() {
        try {
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());

            // get command {connect ('c')  | disconnect ('d') | update ('u')}
            char command = inStream.readChar();

            // do command
            switch (command)
            {
                // connect
                case 'c':
                    // get Name, Address pair
                    int nameSize = inStream.readInt();
                    byte[] nameBytes = inStream.readNBytes(nameSize);

                    String name = new String(nameBytes);

                    int ipSize = inStream.readInt();
                    byte[] ipBytes;
                    ipBytes = inStream.readNBytes(ipSize);

                    String address = new String(ipBytes);

                    System.out.println("server: connect: got pair (" + name + ", " + address + ")");

                    // give response
                    boolean commandResult = connect(name, address);

                    outStream.writeBoolean(commandResult);

                    giveClients();
                    break;

                // disconnect
                case 'd':
                    nameSize = inStream.readInt();
                    nameBytes = inStream.readNBytes(nameSize);
                    name = new String(nameBytes);

                    System.out.println("server: disconnect: got name (" + name + ")");

                    commandResult = disconnect(name);

                    outStream.writeBoolean(commandResult);

                    giveClients();
                    break;

                // update == return list of connections
                case 'u':
                    giveClients();
                    break;
            }

            // give <name, address> map
            socket.close();
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
