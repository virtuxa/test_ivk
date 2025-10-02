package com.ivk.squares.service;

import com.ivk.squares.dto.BoardDto;
import com.ivk.squares.dto.GameStatusDto;
import com.ivk.squares.dto.SimpleMoveDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private final Random random = new Random();
    
    public SimpleMoveDto calculateNextMove(BoardDto boardDto) {
        char[][] board = parseBoard(boardDto);
        char currentColor = boardDto.getNextPlayerColor().charAt(0);
        
        // Сначала проверяем, есть ли уже победитель
        if (hasSquare(board, boardDto.getSize(), 'W')) {
            return new SimpleMoveDto(-1, -1, "W"); // Белые уже выиграли
        }
        if (hasSquare(board, boardDto.getSize(), 'B')) {
            return new SimpleMoveDto(-1, -1, "B"); // Черные уже выиграли
        }
        
        // Сначала проверяем, можем ли мы выиграть за один ход
        SimpleMoveDto winningMove = findWinningMove(board, currentColor, boardDto.getSize());
        if (winningMove != null) {
            return winningMove;
        }
        
        // Затем проверяем, нужно ли блокировать выигрышный ход противника
        char opponentColor = (currentColor == 'W') ? 'B' : 'W';
        SimpleMoveDto blockingMove = findBlockingMove(board, opponentColor, boardDto.getSize());
        if (blockingMove != null) {
            return blockingMove;
        }
        
        // Иначе выбираем случайный ход
        List<SimpleMoveDto> possibleMoves = getPossibleMoves(board, boardDto.getSize());
        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }
        
        return null;
    }
    
    public GameStatusDto getGameStatus(BoardDto boardDto) {
        try {
            char[][] board = parseBoard(boardDto);
            int size = boardDto.getSize();
            
            // Проверяем корректность доски
            if (!isValidBoard(board, size)) {
                return new GameStatusDto(-1, "", "Invalid board state");
            }
            
            // Проверяем, есть ли квадрат у белых
            if (hasSquare(board, size, 'W')) {
                return new GameStatusDto(1, "W", "White wins!");
            }
            
            // Проверяем, есть ли квадрат у черных
            if (hasSquare(board, size, 'B')) {
                return new GameStatusDto(1, "B", "Black wins!");
            }
            
            // Проверяем, заполнена ли доска
            if (isBoardFull(board, size)) {
                return new GameStatusDto(2, "", "Draw");
            }
            
            // Игра продолжается
            return new GameStatusDto(0, boardDto.getNextPlayerColor(), "Ok");
            
        } catch (Exception e) {
            return new GameStatusDto(-1, "", "Error: " + e.getMessage());
        }
    }
    
    // Парсит доску
    private char[][] parseBoard(BoardDto boardDto) {
        String data = boardDto.getData().replaceAll("[\\r\\n]", "");
        int size = boardDto.getSize();
        char[][] board = new char[size][size];
        
        int index = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (index < data.length()) {
                    char cell = data.charAt(index++);
                    board[y][x] = cell;
                } else {
                    board[y][x] = ' ';
                }
            }
        }
        
        return board;
    }
    
    // Проверяет, является ли доска корректной
    private boolean isValidBoard(char[][] board, int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                char cell = board[y][x];
                if (cell != ' ' && cell != 'W' && cell != 'B') {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Проверяет, есть ли квадрат указанного цвета
    private boolean hasSquare(char[][] board, int size, char color) {
        for (int y = 0; y < size - 1; y++) {
            for (int x = 0; x < size - 1; x++) {
                if (board[y][x] == color &&
                    board[y][x + 1] == color &&
                    board[y + 1][x] == color &&
                    board[y + 1][x + 1] == color) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Проверяет, заполнена ли доска
    private boolean isBoardFull(char[][] board, int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Ищет выигрышный ход
    private SimpleMoveDto findWinningMove(char[][] board, char color, int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == ' ') {
                    board[y][x] = color;
                    if (hasSquare(board, size, color)) {
                        board[y][x] = ' '; // Восстанавливаем
                        return new SimpleMoveDto(x, y, String.valueOf(color));
                    }
                    board[y][x] = ' '; // Восстанавливаем
                }
            }
        }
        return null;
    }
    
    // Ищет блокирующий ход
    private SimpleMoveDto findBlockingMove(char[][] board, char opponentColor, int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == ' ') {
                    board[y][x] = opponentColor;
                    if (hasSquare(board, size, opponentColor)) {
                        board[y][x] = ' '; // Восстанавливаем
                        return new SimpleMoveDto(x, y, ""); // Цвет будет установлен в контроллере
                    }
                    board[y][x] = ' '; // Восстанавливаем
                }
            }
        }
        return null;
    }
    
    // Возвращает список возможных ходов
    private List<SimpleMoveDto> getPossibleMoves(char[][] board, int size) {
        List<SimpleMoveDto> moves = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (board[y][x] == ' ') {
                    moves.add(new SimpleMoveDto(x, y, "")); // пустая строка означает, что цвет будет установлен в контроллере
                }
            }
        }
        return moves;
    }
}