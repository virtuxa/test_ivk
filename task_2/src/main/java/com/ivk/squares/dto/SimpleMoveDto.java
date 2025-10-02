package com.ivk.squares.dto;

// Класс для представления хода
public class SimpleMoveDto {
    private int x;
    private int y;
    private String color;
    
    // Конструктор по умолчанию
    public SimpleMoveDto() {}
    
    // Конструктор
    public SimpleMoveDto(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    // Возвращает x
    public int getX() {
        return x;
    }
    
    // Устанавливает x
    public void setX(int x) {
        this.x = x;
    }
    
    // Возвращает y
    public int getY() {
        return y;
    }
    
    // Устанавливает y
    public void setY(int y) {
        this.y = y;
    }
    
    // Возвращает цвет
    public String getColor() {
        return color;
    }
    
    // Устанавливает цвет
    public void setColor(String color) {
        this.color = color;
    }
}