package com.example.myapplication;

public class User {
    private String username; // имя игрока
    private String message; // последнее сообщение
    private int userID; // идентификатор игрока (в данном случае это порт сокета)

    public int[][] positions = new int[10][10];
    public int number_1_palub_ships;
    public int number_2_palub_ships;
    public int number_3_palub_ships;
    public int number_4_palub_ships;
    public int number_ships;

    public User() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
