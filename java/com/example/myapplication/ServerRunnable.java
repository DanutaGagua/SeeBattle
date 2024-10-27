package danuta.gagua.seabattlegame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerRunnable implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private final String serverIP = "192.168.0.174";
    private final int serverPort = 8080;

    private String command;

    int x = -1, y = -1;
    String state = "none";

    ServerRunnable(String command){
        this.command = command;
    }

    ServerRunnable(String command, int x, int y){
        this.command = command;
        this.x = x;
        this.y = y;
    }

    ServerRunnable(String command, String state){
        this.command = command;
        this.state = state;
    }

    private void connect(){
        try{
            socket = new Socket(serverIP, serverPort);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void disconnect(){
        try{
            in.close();
            out.close();
            socket.close();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void command(){
        try{
            switch (command){
                case "send_coords":
                    out.writeChar('w');
                    out.flush();
                    out.writeInt(x);
                    out.writeInt(y);
                    out.flush();
                    break;
                case "send_answer":
                    out.writeChar('s');
                    out.flush();
                    out.writeUTF(state);
                    out.flush();
                    break;
                case "check_answer":
                    out.writeChar('a');
                    out.flush();
                    state = in.readUTF();
                    break;
                case "get_coords":
                    out.writeChar('g');
                    out.flush();
                    x = in.readInt();
                    y = in.readInt();
                    break;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void run(){
        connect();
        command();
        disconnect();
    }
}
