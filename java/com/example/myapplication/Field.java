package com.example.myapplication;

import java.io.Serializable;

public class Field implements Serializable
{
    public int[][] positions = new int[10][10];
    public String IP = "127.0.0.1";

    public Field(int[][] positions)
    {
        for (int i = 0; i < 10; i += 1)
        {
            for (int j = 0; j < 10; j += 1)
            {
                this.positions[i][j] = positions[i][j];
            }
        }
    }
}
