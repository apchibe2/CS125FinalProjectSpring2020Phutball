package com.example.cs125finalprojectspring2020phutball.gamelogic;

public class Tile {
    private int x;
    private int y;
    private int id;

    public Tile(int setX, int setY, int setId) {
        x = setX;
        y = setY;
        id = setId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

}
