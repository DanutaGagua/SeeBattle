package danuta.gagua.seabattlegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity
{
    String serverIP = "192.168.0.174";
    int serverPort = 8080;

    ArrayList<String> clients = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();

    Player player;
    Player playerOpponent = new Player("");

    ArrayAdapter<String> adapter;

    int pairsCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            player = (Player) arguments.getSerializable(Player.class.getSimpleName());
        }

        ListView clientsList = findViewById(R.id.list);;

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, clients);
        clientsList.setAdapter(adapter);

        clientsList.setOnItemClickListener(this::setOpponentData);
    }

    public void setOpponentData(AdapterView<?> parent, View v, int position, long id){
        playerOpponent.setIP(address.get(position));
        playerOpponent.setName(clients.get(position));
    }

    public void connect(View view)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(serverIP, serverPort);

                    DataInputStream inputStream =  new DataInputStream(socket.getInputStream());
                    DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

                    outStream.writeChar('c');
                    outStream.flush();

                    outStream.writeInt(player.getName().length());
                    outStream.writeBytes(player.getName());
                    outStream.flush();

                    outStream.writeChar('u');
                    outStream.flush();

                    pairsCounter = inputStream.readInt();

                    clients.clear();
                    address.clear();
                    for (int i = 0; i < pairsCounter; i++)
                    {       String nameBytes = inputStream.readUTF();

                         clients.add(i, nameBytes);

                        String ipBytes = inputStream.readUTF();

                          address.add(i, ipBytes);
                    }

                    outStream.close();
                    inputStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        while (thread.isAlive()){}

         adapter.notifyDataSetChanged();
    }

    public void go_to_game(View view )
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(serverIP,serverPort);

                    DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());

                     outStream.writeChar('d');
                    outStream.flush();

                    outStream.writeInt(player.getName().length());
                    outStream.writeBytes(player.getName());
                    outStream.flush();

                    outStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        while (thread.isAlive()){}

        player.setIP(address.get(clients.indexOf(player.getName())));

        Intent intent = new Intent(this, GameService.class);
        intent.putExtra("player", player);
        intent.putExtra("playerOpponent", playerOpponent);
        startActivity(intent);
    }
}
