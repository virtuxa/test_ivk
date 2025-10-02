package com.ivk.squares.model;

// Класс для представления хода в игре
public class Move {
    private final int x;
    private final int y;
    
    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return x == move.x && y == move.y;
    }
    
    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
