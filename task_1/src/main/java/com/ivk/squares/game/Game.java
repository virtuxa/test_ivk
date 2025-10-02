package com.ivk.squares.game;

import com.ivk.squares.model.Board;
import com.ivk.squares.model.Move;
import com.ivk.squares.model.Player;
import com.ivk.squares.ai.AI;

// Класс для управления игрой
public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player winner;
    private AI ai;
    private boolean gameActive;
    
    public Game() {
        this.ai = new AI();
        this.gameActive = false;
    }
    
    // Начинает новую игру
    public void startNewGame(int boardSize, Player player1, Player player2) {
        this.board = new Board(boardSize);
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.gameActive = true;
    }
    
    // Делает ход
    public boolean makeMove(int x, int y) {
        if (!gameActive) {
            return false;
        }
        
        if (board.makeMove(x, y, currentPlayer.getColorSymbol())) {
            // Проверяем условия окончания игры
            if (board.hasSquare(currentPlayer.getColorSymbol())) {
                winner = currentPlayer; // Запоминаем победителя
                gameActive = false;
                return true; // Игра окончена, есть победитель
            }
            
            if (board.isFull()) {
                winner = null; // Ничья
                gameActive = false;
                return true; // Игра окончена, ничья
            }
            
            // Переключаем игрока
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            return true;
        }
        
        return false; // Некорректный ход
    }
    
    // Получает ход от ИИ
    public Move getAIMove() {
        if (!gameActive || currentPlayer.getType() != Player.Type.COMP) {
            return null;
        }
        
        Player opponent = (currentPlayer == player1) ? player2 : player1;
        return ai.chooseMove(board, currentPlayer, opponent);
    }
    
    // Проверяет, активна ли игра
    public boolean isGameActive() {
        return gameActive;
    }
    
    // Получает текущего игрока
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    // Получает доску
    public Board getBoard() {
        return board;
    }
    
    // Проверяет, есть ли победитель
    public Player getWinner() {
        return winner;
    }
    
    // Проверяет, ничья ли
    public boolean isDraw() {
        return !gameActive && !board.hasSquare(player1.getColorSymbol()) && 
               !board.hasSquare(player2.getColorSymbol()) && board.isFull();
    }
    
    // Сбрасывает игру
    public void reset() {
        this.gameActive = false;
        this.board = null;
        this.player1 = null;
        this.player2 = null;
        this.currentPlayer = null;
        this.winner = null;
    }
}
