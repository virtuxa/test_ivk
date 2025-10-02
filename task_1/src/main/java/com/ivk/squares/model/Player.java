package com.ivk.squares.model;

// Класс для представления игрока
public class Player {
    public enum Type {
        USER, COMP
    }
    
    public enum Color {
        WHITE('W'), BLACK('B');
        
        private final char symbol;
        
        Color(char symbol) {
            this.symbol = symbol;
        }
        
        public char getSymbol() {
            return symbol;
        }
        
        public static Color fromSymbol(char symbol) {
            for (Color color : values()) {
                if (color.symbol == symbol) {
                    return color;
                }
            }
            throw new IllegalArgumentException("Unknown color symbol: " + symbol);
        }
    }
    
    private final Type type;
    private final Color color;
    
    public Player(Type type, Color color) {
        this.type = type;
        this.color = color;
    }
    
    public Type getType() {
        return type;
    }
    
    public Color getColor() {
        return color;
    }
    
    public char getColorSymbol() {
        return color.getSymbol();
    }
    
    @Override
    public String toString() {
        return type.name().toLowerCase() + " " + color.getSymbol();
    }
}
