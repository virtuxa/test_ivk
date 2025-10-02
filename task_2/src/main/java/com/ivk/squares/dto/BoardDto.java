package com.ivk.squares.dto;

// Класс для представления доски
public class BoardDto {
    private int size;
    private String data;
    private String nextPlayerColor;
    
    // Конструктор по умолчанию
    public BoardDto() {}
    
    // Конструктор
    public BoardDto(int size, String data, String nextPlayerColor) {
        this.size = size;
        this.data = data;
        this.nextPlayerColor = nextPlayerColor;
    }
    
    // Возвращает размер
    public int getSize() {
        return size;
    }
    
    // Устанавливает размер
    public void setSize(int size) {
        this.size = size;
    }
    
    // Возвращает данные
    public String getData() {
        return data;
    }
    
    // Устанавливает данные
    public void setData(String data) {
        this.data = data;
    }
    
    // Возвращает цвет следующего игрока
    public String getNextPlayerColor() {
        return nextPlayerColor;
    }
    
    // Устанавливает цвет следующего игрока
    public void setNextPlayerColor(String nextPlayerColor) {
        this.nextPlayerColor = nextPlayerColor;
    }
}