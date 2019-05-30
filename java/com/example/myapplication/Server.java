package com.example.myapplication;

import java.net.*;
import java.io.*;

public class Server extends Thread
{
    int port = 6666;
    User users[] = new User[2];
    Socket socket[];
    DataInputStream in[];
    DataOutputStream out[];
    String line = null;

    public Server(String name)
    {
        super( name);
    }

    private int gamer = 0, another_gamer = 1;

    private boolean running = false;

    private void runServer() {
        running = true;

        try {
            ServerSocket ss = new ServerSocket(port); // создаем сокет сервера и привязываем его к вышеуказанному порту

            for (int i = 0; i < 2; i += 1)
            {
                // заставляем сервер ждать подключений
                socket[i] = ss.accept();

                in[i] = new DataInputStream(socket[i].getInputStream());
                out[i] = new DataOutputStream(socket[i].getOutputStream());

                for (int k = 0; k < 10; k += 1)
                {
                    for (int j = 0; j < 10; j += 1)
                    {
                        line = in[i].readUTF();

                        users[i].positions[k][j] = Integer.parseInt(line);
                    }
                }

                line = in[i].readUTF();
                users[i].number_ships  = Integer.parseInt(line);

                line = in[i].readUTF();
                users[i].number_1_palub_ships = Integer.parseInt(line);

                line = in[i].readUTF();
                users[i].number_2_palub_ships = Integer.parseInt(line);

                line = in[i].readUTF();
                users[i].number_3_palub_ships = Integer.parseInt(line);

                line = in[i].readUTF();
                users[i].number_4_palub_ships = Integer.parseInt(line);
            }

            while (true)
            {
                for (int i = 0; i < 10; i += 1)
                {
                    for (int j = 0; j < 10; j += 1)
                    {
                        line = in[gamer].readUTF();

                         users[another_gamer].positions[i][j] = Integer.parseInt(line);
                    }
                }

                line = in[gamer].readUTF();
                users[another_gamer].number_ships  = Integer.parseInt(line);

                if (line == "0")
                {
                    running = false;
                }

                line = in[gamer].readUTF();
                users[another_gamer].number_1_palub_ships = Integer.parseInt(line);

                line = in[gamer].readUTF();
                users[another_gamer].number_2_palub_ships = Integer.parseInt(line);

                line = in[gamer].readUTF();
                users[another_gamer].number_3_palub_ships = Integer.parseInt(line);

                line = in[gamer].readUTF();
                users[another_gamer].number_4_palub_ships = Integer.parseInt(line);

                line = in[gamer].readUTF();

                if (!running)
                {
                    out[gamer].writeUTF("Вы выиграли");
                    out[gamer].flush();

                    out[another_gamer].writeUTF("Вы проиграли");
                    out[another_gamer].flush();

                    break;
                }

                out[another_gamer].writeUTF(line);
                out[another_gamer].flush();

                if (line == "Мимо")
                {
                    another_gamer = gamer;
                    gamer = 1 - gamer;
                }

                for (int k = 0; k < 2; k += 1)
                {
                    for (int i = 0; i < 10; i += 1)
                    {
                        for (int j = 0; j < 10; j += 1)
                        {
                            out[k].writeUTF(Integer.toString(users[another_gamer].positions[i][j]));
                            out[k].flush();
                        }
                    }

                    out[k].writeUTF(Integer.toString(users[another_gamer].number_ships));
                    out[k].flush();

                    out[k].writeUTF(Integer.toString(users[another_gamer].number_1_palub_ships));
                    out[k].flush();

                    out[k].writeUTF(Integer.toString(users[another_gamer].number_2_palub_ships));
                    out[k].flush();

                    out[k].writeUTF(Integer.toString(users[another_gamer].number_3_palub_ships));
                    out[k].flush();

                    out[k].writeUTF(Integer.toString(users[another_gamer].number_4_palub_ships));
                    out[k].flush();
                }
            }

            for (int i = 0; i < 2; i += 1)
            {
                socket[i].close();

                in[i].close();
                out[i].close();
            }

            ss.close();

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    @Override public void run() {
        super.run();

        runServer();
    }
}
