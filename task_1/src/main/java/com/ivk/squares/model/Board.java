package com.ivk.squares.model;

import java.util.ArrayList;
import java.util.List;

// Класс для представления игровой доски
public class Board {
    private final int size;
    private final char[][] board;
    private int moveCount;
    
    public Board(int size) {
        this.size = size;
        this.board = new char[size][size];
        this.moveCount = 0;
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' '; // Пустая клетка
            }
        }
    }
    
    public boolean makeMove(int x, int y, char color) {
        if (isValidMove(x, y)) {
            board[y][x] = color;
            moveCount++;
            return true;
        }
        return false;
    }
    
    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size && board[y][x] == ' ';
    }
    
    public boolean isFull() {
        return moveCount >= size * size;
    }
    
    public char getCell(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return board[y][x];
        }
        return ' ';
    }
    
    public int getSize() {
        return size;
    }
    
    public int getMoveCount() {
        return moveCount;
    }
    
    // Проверяет, есть ли квадрат из фишек указанного цвета
    public boolean hasSquare(char color) {
        // Проверяем все возможные квадраты размера 2x2
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (isSquare(i, j, color)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Проверяет, является ли квадрат 2x2 начиная с позиции (startY, startX) квадратом из фишек цвета color
    private boolean isSquare(int startY, int startX, char color) {
        return board[startY][startX] == color &&
               board[startY][startX + 1] == color &&
               board[startY + 1][startX] == color &&
               board[startY + 1][startX + 1] == color;
    }
    
    // Возвращает список всех возможных ходов
    public List<Move> getPossibleMoves() {
        List<Move> moves = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == ' ') {
                    moves.add(new Move(x, y));
                }
            }
        }
        return moves;
    }
    
    // Создает копию доски
    public Board copy() {
        Board newBoard = new Board(size);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                newBoard.board[y][x] = this.board[y][x];
            }
        }
        newBoard.moveCount = this.moveCount;
        return newBoard;
    }
    
    // Возвращает строковое представление доски для отладки
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                sb.append(board[y][x] == ' ' ? '_' : board[y][x]).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
