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

            playerOpponent.getField().setChangedFlag(true);
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

            playerOpponent.getField().setChangedFlag(false);

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
                playerOpponent.getField().setChangedFlag(true);
                setButton("Send Coords", this::sendCoords);
            }
        }

        if (state.equals("done")){
            Ship ship = playerOpponent.getField().addKilledShip();
            if (ship != null){
                playerOpponent.getField().setEnvironmentOfKilledShip(ship);
            }
        }

        checkOfGameEnd();
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

        if (state.equals("done")){
            player.getField().setEnvironmentOfKilledShip();
        }

        checkOfGameEnd();
    }

    private void checkOfGameEnd(){
        if (player.getField().getShips().getShipsCount() == 0){
            message.setText("Вы проиграли");
            setButton("End game", this::endGame);
        } else {
            if (playerOpponent.getField().getShips().getShipsCount() == 10){
                message.setText("Вы выиграли");
                setButton("End game", this::endGame);
            }
        }
    }

    public void endGame(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}