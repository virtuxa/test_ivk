package com.ivk.squares.ai;

import com.ivk.squares.model.Board;
import com.ivk.squares.model.Move;
import com.ivk.squares.model.Player;

import java.util.List;
import java.util.Random;

// Класс для реализации ИИ игрока
public class AI {
    private final Random random = new Random();
    
    /**
     * Выбирает лучший ход для ИИ
     */
    public Move chooseMove(Board board, Player currentPlayer, Player opponent) {
        List<Move> possibleMoves = board.getPossibleMoves();
        
        if (possibleMoves.isEmpty()) {
            return null; // Нет возможных ходов
        }
        
        // Проверяем, можем ли мы выиграть за один ход
        Move winningMove = findWinningMove(board, currentPlayer);
        if (winningMove != null) {
            return winningMove;
        }
        
        // Проверяем, нужно ли блокировать выигрышный ход противника
        Move blockingMove = findBlockingMove(board, opponent);
        if (blockingMove != null) {
            return blockingMove;
        }
        
        // Иначе выбираем случайный ход
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }
    
    /**
     * Ищет выигрышный ход для текущего игрока
     */
    private Move findWinningMove(Board board, Player player) {
        List<Move> possibleMoves = board.getPossibleMoves();
        
        for (Move move : possibleMoves) {
            Board testBoard = board.copy();
            testBoard.makeMove(move.getX(), move.getY(), player.getColorSymbol());
            
            if (testBoard.hasSquare(player.getColorSymbol())) {
                return move;
            }
        }
        
        return null;
    }
    
    /**
     * Ищет ход, который блокирует выигрыш противника
     */
    private Move findBlockingMove(Board board, Player opponent) {
        List<Move> possibleMoves = board.getPossibleMoves();
        
        for (Move move : possibleMoves) {
            Board testBoard = board.copy();
            testBoard.makeMove(move.getX(), move.getY(), opponent.getColorSymbol());
            
            if (testBoard.hasSquare(opponent.getColorSymbol())) {
                return move; // Блокируем этот ход
            }
        }
        
        return null;
    }
}
