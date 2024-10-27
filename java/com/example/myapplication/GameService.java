package danuta.gagua.seabattlegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameService extends AppCompatActivity {
    Player player;
    Player playerOpponent;

    boolean flag = true;
    String state = "none";
    int x = -1, y = -1;

    TextView message;
    TableLayout playerField, opponentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_game);

        message = findViewById(R.id.message);

        Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            player = (Player) arguments.getSerializable("player");
            player.getField().setFieldButtons(getApplicationContext());
            player.getField().setChangedFlag(false);
            player.getField().checkShips(player);

            playerOpponent = (Player) arguments.getSerializable("playerOpponent");
            playerOpponent.setField(getApplicationContext());
        }

         if ( player.getIP().compareTo(playerOpponent.getIP()) > 0) flag = false;

         playerField = buildGameField(player.getField(), R.id.field_view);
         opponentField = buildGameField(playerOpponent.getField(), R.id.field_view_opponent);

         setElements();
    }

    public void setElements(){
        if (flag){
            opponentField.setVisibility(View.VISIBLE);
            playerField.setVisibility(View.GONE);

            setButton("Send Coords", this::sendCoords);
        } else {
            opponentField.setVisibility(View.GONE);
            playerField.setVisibility(View.VISIBLE);

            setButton("Get Coords", this::getCoords);
        }
    }

    private void setButton(String name, View.OnClickListener listener){
        Button button = findViewById(R.id.button);
        button.setText(name);
        button.setOnClickListener(listener);
    }

    public TableLayout buildGameField(Field field, int id){
        TableLayout tableLayout = findViewById(id);
        tableLayout.removeAllViews();

        for (int i = 0; i < 10; i++ ) {
            TableRow row = new TableRow(getApplicationContext()); // создание строки таблицы
            for (int j = 0; j < 10; j++)
            {
                row.addView(field.getCells()[i][j].getButton(),
                        new TableRow.LayoutParams(100,100));// добавление кнопки в строку таблицы
            }
            tableLayout.addView(row,
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT)); // добавление строки в таблицу
        }

        return tableLayout;
    }


//    private void process_cell(int i, int j)
//    {
//        switch (positions[i][j]) {
//            case 1:
//
//                message.setText("Убит");
//
//                number_1_palub_ships--;
//                number_ships--;
//
//                positions[i][j] = 5;
//
//                fill_enviroment(i, j);
//
//                break;
//
//            case 2:
//                change_state_2_palub_ship(i, j);
//                break;
//
//            case 3:
//                change_state_3_palub_ship(i, j);
//                break;
//
//            case 4:
//                change_state_4_palub_ship(i, j);
//                break;
//        }
//    }
//
//    private void change_state_2_palub_ship(int i, int j)
//    {
//        boolean flag = install_flag_2_palub_ship(i-1, j);
//
//        if (!flag)
//        {
//            flag = install_flag_2_palub_ship(i, j-1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_2_palub_ship(i+1, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_2_palub_ship(i, j+1);
//        }
//
//        if (flag)
//        {
//            message.setText("Убит");
//
//            number_2_palub_ships--;
//            number_ships--;
//
//            positions[i][j] = 5;
//
//            fill_enviroment(i, j);
//        }
//        else
//        {
//            message.setText("Попал");
//
//            positions[i][j] = 6;
//        }
//    }
//
//    public boolean install_flag_2_palub_ship(int i, int j)
//    {
//        if ( i > -1 && i < 10 && j > -1 && j < 10)
//        {
//            if (positions[i][j] == 6)
//            {
//                positions[i][j] = 5;
//
//                fill_enviroment(i, j);
//
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void change_state_3_palub_ship(int i, int j)
//    {
//        boolean flag = install_flag_3_palub_ship(i-2, j, i-1, j);
//
//        if (!flag)
//        {
//            flag = install_flag_3_palub_ship(i, j-2, i, j-1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_3_palub_ship(i+2, j, i+1, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_3_palub_ship(i, j+2, i, j+1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_3_palub_ship(i-1, j, i+1, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_3_palub_ship(i, j-1, i, j+1);
//        }
//
//        if (flag)
//        {
//            message.setText("Убит");
//
//            number_3_palub_ships--;
//            number_ships--;
//
//            positions[i][j] = 5;
//
//            fill_enviroment(i, j);
//        }
//        else
//        {
//            message.setText("Попал");
//
//            positions[i][j] = 6;
//        }
//    }
//
//    public boolean install_flag_3_palub_ship(int i1, int j1, int i2, int j2)
//    {
//        if ( i1 > -1 && i1 < 10 && j1 > -1 && j1 < 10 &&
//                i2 > -1 && i2 < 10 && j2 > -1 && j2 < 10)
//        {
//            if (positions[i1][j1] == 6 && positions[i2][j2] == 6)
//            {
//                positions[i1][j1] = 5;
//                positions[i2][j2] = 5;
//
//                fill_enviroment(i1,j1);
//                fill_enviroment(i2,j2);
//
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void change_state_4_palub_ship(int i, int j)
//    {
//        boolean flag = install_flag_4_palub_ship(i-3, j, i-2, j, i-1, j);
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i, j-3, i, j-2, i, j-1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i+3, j,i+2, j, i+1, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i, j+3, i, j+2, i, j+1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i-1, j,i-1, j, i+1, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i, j-1, i, j-1, i, j+1);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i-1, j, i+1, j, i+2, j);
//        }
//
//        if (!flag)
//        {
//            flag = install_flag_4_palub_ship(i, j-1, i, j+1, i, j+2);
//        }
//
//        if (flag)
//        {
//            message.setText("Убит");
//
//            number_4_palub_ships--;
//            number_ships--;
//
//            positions[i][j] = 5;
//            fill_enviroment(i,j);
//        }
//        else
//        {
//            message.setText("Попал");
//
//            positions[i][j] = 6;
//        }
//    }
//
//    public boolean install_flag_4_palub_ship(int i1, int j1, int i2, int j2, int i3, int j3)
//    {
//        if ( i1 > -1 && i1 < 10 && j1 > -1 && j1 < 10 &&
//                i2 > -1 && i2 < 10 && j2 > -1 && j2 < 10 &&
//                i3 > -1 && i3 < 10 && j3 > -1 && j3 < 10)
//        {
//            if (positions[i1][j1] == 6 && positions[i2][j2] == 6 && positions[i3][j3] == 6)
//            {
//                positions[i1][j1] = 5;
//                positions[i2][j2] = 5;
//                positions[i3][j3] = 5;
//
//                fill_enviroment(i1,j1);
//                fill_enviroment(i2,j2);
//                fill_enviroment(i3,j3);
//
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private void fill_enviroment(int i, int j)
//    {
//        if (positions[i-1][j-1] == 0 && i-1 > -1 && i-1 < 10 && j-1 > -1 && j-1 < 10 ) positions[i-1][j-1] = 7;
//        if (positions[i-1][j] == 0 && i-1 > -1 && i-1 < 10 ) positions[i-1][j] = 7;
//        if (positions[i][j-1] == 0 && j-1 > -1 && j-1 < 10 ) positions[i][j-1] = 7;
//        if (positions[i-1][j+1] == 0 && i-1 > -1 && i-1 < 10 && j+1 > -1 && j+1 < 10 ) positions[i-1][j+1] = 7;
//        if (positions[i+1][j-1] == 0 && i+1 > -1 && i+1 < 10 && j-1 > -1 && j-1 < 10 ) positions[i+1][j-1] = 7;
//        if (positions[i][j+1] == 0  && j+1 > -1 && j+1 < 10 ) positions[i][j+1] = 7;
//        if (positions[i+1][j] == 0 && i+1 > -1 && i+1 < 10 ) positions[i+1][j] = 7;
//        if (positions[i+1][j+1] == 0 && i+1 > -1 && i+1 < 10 && j+1 > -1 && j+1 < 10 ) positions[i+1][j+1] = 7;
//    }
//
//    private void send_info( )
//    {
////        Thread thread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                try
////                {   serverSocket = new ServerSocket(serverPort);
////
////                    InetAddress ipAddress = InetAddress.getByName(address);
////                    socket = new Socket(ipAddress, serverPort);
////                    player_socket = serverSocket.accept();
////
////                    out  = new DataOutputStream(socket.getOutputStream() );
////
////                    player_in  = new DataInputStream(player_socket.getInputStream() );
////
////                    out.writeUTF(message.toString());
////                    out.flush();
////
////                    if (message.toString() == "Мимо")
////                    {
////                        flag = !flag;
////                    }
////
////                    if (number_ships == 0)
////                    {
////                        message.setText("Вы победили");}
////
////                    player_in.close();
////                     out.close();
////                    serverSocket.close();
////                    socket.close();
////                }
////                catch (Exception x){
////                    x.printStackTrace();
////                }
////            }
////        });
////
////        thread.start();
////
////        while (thread.isAlive()){}
//
//    }
//
//    private void play_game()
//    {
////        while (true) {
////
////            if (flag) continue;
////
////            Thread thread = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        serverSocket = new ServerSocket(serverPort);
////
////                        socket = new Socket(address, serverPort);
////                        player_socket = serverSocket.accept();
////
////                        out = new DataOutputStream(socket.getOutputStream());
////
////                        player_in = new DataInputStream(player_socket.getInputStream());
////
////                          if (!flag) {
////                                    if (player_in.readUTF() == "Мимо") {
////                                        flag = !flag;
////                                    }
////
////                                    for (int i = 0; i < 10; i += 1) {
////                                        for (int j = 0; j < 10; j += 1) {
////                                            another_gamer_positions[i][j] = Integer.parseInt(player_in.readUTF());
////                                        }
////                                    }
////
////                                    another_gamer_number_4_palub_ships = Integer.parseInt(player_in.readUTF());
////                                    another_gamer_number_3_palub_ships = Integer.parseInt(player_in.readUTF());
////                                    another_gamer_number_2_palub_ships = Integer.parseInt(player_in.readUTF());
////                                    another_gamer_number_1_palub_ships = Integer.parseInt(player_in.readUTF());
////                                    another_gamer_number_ships = Integer.parseInt(player_in.readUTF());
////
////                                    if (another_gamer_number_ships == 0) {
////                                        message.setText("Вы проиграли");
////                                    }
////                                }
////
////                        player_in.close();
////                         out.close();
////                        serverSocket.close();
////                        socket.close();
////                    } catch (IOException x) {
////                        x.printStackTrace();
////                    }
////                }
////
////            });
////
////            thread.start();
////
////            while (thread.isAlive()) {
////            }
////
//////            if (flag)
//////            {
//////                draw_field(another_gamer_positions);
//////            }
//////            else draw_field (positions);
////
////            if (number_ships == 0)
////            {
////                Intent intent = new Intent(this, MainActivity.class);
////                 startActivity(intent);
////            }
////        }
//    }

    public void next_turn(){
        flag = !flag;

        setElements();
    }

    public void sendCoords(View view){
        Cell shipCell = playerOpponent.getField().getShipCell();

        if (shipCell != null) {
            x = shipCell.getX();
            y = shipCell.getY();

            Thread thread = new Thread(new ServerRunnable("send_coords", x, y));
            thread.start();
            while (thread.isAlive()){}

            setButton("Check answer", this::checkAnswer);
        }
    }

    public void getCoords(View view){
        ServerRunnable runnable = new ServerRunnable("get_coords");

        Thread thread = new Thread(runnable);
        thread.start();
        while (thread.isAlive()){};

        x = runnable.x;
        y = runnable.y;

        if (x != -1 && y != -1){
            setButton("Send answer", this::sendAnswer);
        }
    }

    public void checkAnswer(View view){
        ServerRunnable runnable = new ServerRunnable("check_answer");

        Thread thread = new Thread(runnable);
        thread.start();
        while (thread.isAlive()){};

        state = runnable.state;

        if (state.equals("none")){
            message.setText("Нет ответа");
        } else {
            message.setText(state);
            playerOpponent.getField().getCells()[y][x].setState(state);

            if (state.equals("miss")){
                next_turn();
            } else {
                setButton("Send Coords", this::sendCoords);
            }
        }
    }

    public void sendAnswer(View view){
        state = player.getField().checkSelectedCell(x, y);

        Thread thread = new Thread(new ServerRunnable("send_answer", state));
        thread.start();
        while (thread.isAlive()){}

        message.setText(state);

        if (state.equals("miss")){
            next_turn();
        } else {
            setButton("Get Coords", this::getCoords);
        }
    }
}