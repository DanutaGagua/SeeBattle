package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GameService extends AppCompatActivity implements OnTouchListener
{
    private int[][] positions = new int[10][10];

    float x;
    float y;

     Socket socket;
     DataInputStream in;
     DataOutputStream out;

    boolean flag = true;
    int serverPort = 6666;
    String address = "127.0.0.1";

    int number_1_palub_ships;
    int number_2_palub_ships;
    int number_3_palub_ships;
    int number_4_palub_ships;
    int number_ships;

    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_game);

        draw_field();

        message = (TextView) findViewById(R.id.message);

         Field field  = new Field(positions);

         Bundle arguments = getIntent().getExtras();

        if(arguments!=null){
            field = (Field) arguments.getSerializable(Field.class.getSimpleName());
        }

        address = field.IP;

        if (address == "127.0.0.1")
        {
            new Server("Server").start();

            flag = true;
        }

         try {
             InetAddress ipAddress = InetAddress.getByName(address);
             socket = new Socket(ipAddress, serverPort);

              in  = new DataInputStream(socket.getInputStream());
              out  = new DataOutputStream(socket.getOutputStream());

             send_info(out);

         } catch (Exception x) {
            x.printStackTrace();
             Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        if (flag)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                x = event.getX();
                y = event.getY();

                if (x < 80 || x > 680 || y < 80 || y > 680) {
                    return false;
                }

                int j = getIndex(x);
                int i = getIndex(y);

                process_cell(i, j);

                send_info(out);

                try {
                    out.writeUTF(message.toString());
                    out.flush();

                    String line = in.readUTF();

                    if (line == "Вы выиграли" || line == "Вы проиграли")
                    {
                        message.setText(line);
                    }

                    message.setText(line);

                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

            if (message.toString() == "Мимо")
            {
                flag = !flag;
            }
        }

        draw_field();

        return true;
    }

    private int getIndex(float x)
    {
        int i = (int) x;

        i = (i - 80) / 60;

        return i;
    }

    private void draw_field()
    {
        FieldView field = new FieldView(this, positions, flag);
        field.setOnTouchListener(this);

        LinearLayout layout = (LinearLayout)findViewById(R.id.service_game) ;

        layout.removeAllViews();

        layout.addView(field);
    }

    private void process_cell(int i, int j)
    {
        switch (positions[i][j]) {
            case 0:

                message.setText("Мимо");

                positions[i][j] = 7;

                break;

            case 1:

                message.setText("Убит");

                number_1_palub_ships--;
                number_ships--;

                positions[i][j] = 5;

                fill_enviroment(i, j);

                break;

            case 2:
                change_state_2_palub_ship(i, j);
                break;

            case 3:
                change_state_3_palub_ship(i, j);
                break;

            case 4:
                change_state_4_palub_ship(i, j);
                break;
        }
    }

    private void change_state_2_palub_ship(int i, int j)
    {
        boolean flag = install_flag_2_palub_ship(i-1, j);

        if (!flag)
        {
            flag = install_flag_2_palub_ship(i, j-1);
        }

        if (!flag)
        {
            flag = install_flag_2_palub_ship(i+1, j);
        }

        if (!flag)
        {
            flag = install_flag_2_palub_ship(i, j+1);
        }

        if (flag)
        {
            message.setText("Убит");

            number_2_palub_ships--;
            number_ships--;

            positions[i][j] = 5;

            fill_enviroment(i, j);
        }
        else
        {
            message.setText("Попал");

            positions[i][j] = 6;
        }
    }

    public boolean install_flag_2_palub_ship(int i, int j)
    {
        if ( i > -1 && i < 10 && j > -1 && j < 10)
        {
            if (positions[i][j] == 6)
            {
                positions[i][j] = 5;

                fill_enviroment(i, j);

                return true;
            }
        }

        return false;
    }

    private void change_state_3_palub_ship(int i, int j)
    {
        boolean flag = install_flag_3_palub_ship(i-2, j, i-1, j);

        if (!flag)
        {
            flag = install_flag_3_palub_ship(i, j-2, i, j-1);
        }

        if (!flag)
        {
            flag = install_flag_3_palub_ship(i+2, j, i+1, j);
        }

        if (!flag)
        {
            flag = install_flag_3_palub_ship(i, j+2, i, j+1);
        }

        if (!flag)
        {
            flag = install_flag_3_palub_ship(i-1, j, i+1, j);
        }

        if (!flag)
        {
            flag = install_flag_3_palub_ship(i, j-1, i, j+1);
        }

        if (flag)
        {
            message.setText("Убит");

            number_3_palub_ships--;
            number_ships--;

            positions[i][j] = 5;

            fill_enviroment(i, j);
        }
        else
        {
            message.setText("Попал");

            positions[i][j] = 6;
        }
    }

    public boolean install_flag_3_palub_ship(int i1, int j1, int i2, int j2)
    {
        if ( i1 > -1 && i1 < 10 && j1 > -1 && j1 < 10 &&
                i2 > -1 && i2 < 10 && j2 > -1 && j2 < 10)
        {
            if (positions[i1][j1] == 6 && positions[i2][j2] == 6)
            {
                positions[i1][j1] = 5;
                positions[i2][j2] = 5;

                fill_enviroment(i1,j1);
                fill_enviroment(i2,j2);

                return true;
            }
        }

        return false;
    }

    private void change_state_4_palub_ship(int i, int j)
    {
        boolean flag = install_flag_4_palub_ship(i-3, j, i-2, j, i-1, j);

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i, j-3, i, j-2, i, j-1);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i+3, j,i+2, j, i+1, j);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i, j+3, i, j+2, i, j+1);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i-1, j,i-1, j, i+1, j);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i, j-1, i, j-1, i, j+1);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i-1, j, i+1, j, i+2, j);
        }

        if (!flag)
        {
            flag = install_flag_4_palub_ship(i, j-1, i, j+1, i, j+2);
        }

        if (flag)
        {
            message.setText("Убит");

            number_4_palub_ships--;
            number_ships--;

            positions[i][j] = 5;
            fill_enviroment(i,j);
        }
        else
        {
            message.setText("Попал");

            positions[i][j] = 6;
        }
    }

    public boolean install_flag_4_palub_ship(int i1, int j1, int i2, int j2, int i3, int j3)
    {
        if ( i1 > -1 && i1 < 10 && j1 > -1 && j1 < 10 &&
                i2 > -1 && i2 < 10 && j2 > -1 && j2 < 10 &&
                i3 > -1 && i3 < 10 && j3 > -1 && j3 < 10)
        {
            if (positions[i1][j1] == 6 && positions[i2][j2] == 6 && positions[i3][j3] == 6)
            {
                positions[i1][j1] = 5;
                positions[i2][j2] = 5;
                positions[i3][j3] = 5;

                fill_enviroment(i1,j1);
                fill_enviroment(i2,j2);
                fill_enviroment(i3,j3);

                return true;
            }
        }

        return false;
    }

    private void fill_enviroment(int i, int j)
    {
        if (positions[i-1][j-1] == 0 && i-1 > -1 && i-1 < 10 && j-1 > -1 && j-1 < 10 ) positions[i-1][j-1] = 7;
        if (positions[i-1][j] == 0 && i-1 > -1 && i-1 < 10 ) positions[i-1][j] = 7;
        if (positions[i][j-1] == 0 && j-1 > -1 && j-1 < 10 ) positions[i][j-1] = 7;
        if (positions[i-1][j+1] == 0 && i-1 > -1 && i-1 < 10 && j+1 > -1 && j+1 < 10 ) positions[i-1][j+1] = 7;
        if (positions[i+1][j-1] == 0 && i+1 > -1 && i+1 < 10 && j-1 > -1 && j-1 < 10 ) positions[i+1][j-1] = 7;
        if (positions[i][j+1] == 0  && j+1 > -1 && j+1 < 10 ) positions[i][j+1] = 7;
        if (positions[i+1][j] == 0 && i+1 > -1 && i+1 < 10 ) positions[i+1][j] = 7;
        if (positions[i+1][j+1] == 0 && i+1 > -1 && i+1 < 10 && j+1 > -1 && j+1 < 10 ) positions[i+1][j+1] = 7;
    }

    private void send_info(DataOutputStream out)
    {
        try{
            for (int i0 = 0; i0 < 10; i0 += 1)
            {
                for (int j0 = 0; j0 < 10; j0 += 1)
                {
                    out.writeUTF(Integer.toString( positions[i0][j0]));
                    out.flush();
                }
            }

            out.writeUTF(Integer.toString( number_ships));
            out.flush();

            out.writeUTF(Integer.toString( number_1_palub_ships));
            out.flush();

            out.writeUTF(Integer.toString( number_2_palub_ships));
            out.flush();

            out.writeUTF(Integer.toString( number_3_palub_ships));
            out.flush();

            out.writeUTF(Integer.toString( number_4_palub_ships));
            out.flush();

        }catch (Exception x) {
            x.printStackTrace();
        }
    }
}


