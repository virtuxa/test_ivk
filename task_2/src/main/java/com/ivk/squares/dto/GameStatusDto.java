package com.ivk.squares.dto;

// Класс для представления статуса игры
public class GameStatusDto {
    private int status;
    private String color;
    private String message;
    
    // Конструктор по умолчанию
    public GameStatusDto() {}
    
    // Конструктор
    public GameStatusDto(int status, String color, String message) {
        this.status = status;
        this.color = color;
        this.message = message;
    }
    
    // Возвращает статус
    public int getStatus() {
        return status;
    }
    
    // Устанавливает статус
    public void setStatus(int status) {
        this.status = status;
    }
    
    // Возвращает цвет
    public String getColor() {
        return color;
    }
    
    // Устанавливает цвет
    public void setColor(String color) {
        this.color = color;
    }
    
    // Возвращает сообщение
    public String getMessage() {
        return message;
    }
    
    // Устанавливает сообщение
    public void setMessage(String message) {
        this.message = message;
    }
}