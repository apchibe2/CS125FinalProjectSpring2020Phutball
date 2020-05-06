package com.example.cs125finalprojectspring2020phutball.gamelogic;

public class Tile {
    private int x;
    private int y;

    public Tile(int setX, int setY) {
        x = setX;
        y = setY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int setX) {
        x = setX;
    }

    public void setY(int setY) {
        x = setY;
    }

    public void setId(int setId) {
        x = setId;
    }

    public String getString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Tile t) {
        if(t.getX() != x || t.getY() != y) {
            return false;
        }
        return true;
    }

}
