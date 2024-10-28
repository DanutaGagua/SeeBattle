//package com.example.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorkerRunnable implements  Runnable {
    // concurrent map <name of client, client ip>
    private static ConcurrentHashMap<String, String> clients = new ConcurrentHashMap<String, String>();

    private static int x = -1, y = -1;
    private static String answer = "none";

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
        outStream.writeUTF(Integer.toString( clients.size() ));
        outStream.flush();

        for (Map.Entry<String, String> client : clients.entrySet()) {

            if (client.getValue() == socket.getInetAddress().getHostAddress()) continue;

            outStream.writeUTF(client.getKey());
            outStream.flush();

            outStream.writeUTF(client.getValue());
            outStream.flush();
        }
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
                    String address = new String(socket.getInetAddress().getHostAddress());

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

                case 'w':
                    x = inStream.readInt();
                    y = inStream.readInt();

                    System.out.println("coords: (" + x + ", " + y + ")");
                    break;

                case 'g':
                    outStream.writeInt(x);
                    outStream.writeInt(y);
                    outStream.flush();

                    System.out.println("get coords: (" + x + ", " + y + ")");

                    x = -1;
                    y = -1;
                    break;

                case 's':
                    answer = inStream.readUTF();
                    System.out.println("get answer: " + answer);
                    break;

                case 'a':
                    outStream.writeUTF(answer);
                    outStream.flush();

                    if (!answer.equals("none")){
                        System.out.println("send answer: " + answer);
                    }

                    answer = "none";
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
