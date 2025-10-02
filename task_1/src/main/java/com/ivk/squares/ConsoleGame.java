package com.ivk.squares;

import com.ivk.squares.game.Game;
import com.ivk.squares.model.Board;
import com.ivk.squares.model.Move;
import com.ivk.squares.model.Player;

import java.util.Scanner;

// Главный класс консольной игры
public class ConsoleGame {
    private final Scanner scanner;
    private Game game;
    
    public ConsoleGame() {
        this.scanner = new Scanner(System.in);
        this.game = new Game();
    }
    
    public static void main(String[] args) {
        ConsoleGame consoleGame = new ConsoleGame();
        consoleGame.run();
    }
    
    public void run() {
        printWelcome();
        
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            String[] parts = input.split("\\s+");
            String command = parts[0].toUpperCase();
            
            try {
                switch (command) {
                    case "GAME":
                        handleGameCommand(parts);
                        break;
                    case "MOVE":
                        handleMoveCommand(parts);
                        break;
                    case "EXIT":
                        System.out.println("Goodbye!");
                        return;
                    case "HELP":
                        printHelp();
                        break;
                    default:
                        System.out.println("Incorrect command");
                }
            } catch (Exception e) {
                System.out.println("Incorrect command");
            }
        }
    }
    
    // Обработка команды GAME
    private void handleGameCommand(String[] parts) {
        if (parts.length != 4) {
            System.out.println("Incorrect command");
            return;
        }
        
        try {
            int boardSize = Integer.parseInt(parts[1]);
            if (boardSize <= 2) {
                System.out.println("Incorrect command");
                return;
            }
            
            Player player1 = parsePlayer(parts[2]);
            Player player2 = parsePlayer(parts[3]);
            
            if (player1 == null || player2 == null) {
                System.out.println("Incorrect command");
                return;
            }
            
            game.startNewGame(boardSize, player1, player2);
            System.out.println("New game started");
            
            // Выводим пустую доску
            printBoard();
            
            // Если первый игрок - компьютер, делаем его ход
            if (game.getCurrentPlayer().getType() == Player.Type.COMP) {
                makeAIMove();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Incorrect command");
        }
    }
    
    // Обработка команды MOVE
    private void handleMoveCommand(String[] parts) {
        if (!game.isGameActive()) {
            System.out.println("Game is not active!");
            return;
        }
        
        if (parts.length != 3) {
            System.out.println("Incorrect command format. Use: MOVE X Y");
            return;
        }
        
        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            
            if (game.makeMove(x, y)) {
                // Выводим доску после хода
                printBoard();
                
                // Проверяем условия окончания игры
                if (!game.isGameActive()) {
                    // Игра закончилась, определяем результат
                    if (game.getWinner() != null) {
                        System.out.println("Game finished. " + game.getWinner().getColorSymbol() + " wins!");
                    } else if (game.isDraw()) {
                        System.out.println("Game finished. Draw");
                    }
                    game.reset();
                } else {
                    // Если следующий игрок - компьютер, делаем его ход
                    if (game.getCurrentPlayer().getType() == Player.Type.COMP) {
                        makeAIMove();
                    }
                }
            } else {
                System.out.println("Incorrect command");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Incorrect command");
        }
    }
    
    // Делает ход ИИ
    private void makeAIMove() {
        Move aiMove = game.getAIMove();
        if (aiMove != null) {
            System.out.println(game.getCurrentPlayer().getColorSymbol() + " " + aiMove);
            game.makeMove(aiMove.getX(), aiMove.getY());
            
            // Выводим доску после хода ИИ
            printBoard();
            
            // Проверяем условия окончания игры после хода ИИ
            if (!game.isGameActive()) {
                // Игра закончилась, определяем результат
                if (game.getWinner() != null) {
                    System.out.println("Game finished. " + game.getWinner().getColorSymbol() + " wins!");
                } else if (game.isDraw()) {
                    System.out.println("Game finished. Draw");
                }
                game.reset();
            }
        }
    }
    
    // Парсит игрока
    private Player parsePlayer(String playerStr) {
        String[] playerParts = playerStr.split(",");
        if (playerParts.length != 2) {
            return null;
        }
        
        String typeStr = playerParts[0].trim().toLowerCase();
        String colorStr = playerParts[1].trim().toUpperCase();
        
        Player.Type type;
        if ("user".equals(typeStr)) {
            type = Player.Type.USER;
        } else if ("comp".equals(typeStr)) {
            type = Player.Type.COMP;
        } else {
            return null;
        }
        
        Player.Color color;
        if ("W".equals(colorStr)) {
            color = Player.Color.WHITE;
        } else if ("B".equals(colorStr)) {
            color = Player.Color.BLACK;
        } else {
            return null;
        }
        
        return new Player(type, color);
    }
    
    // Выводит приветствие
    private void printWelcome() {
        System.out.println("Welcome to Squares Game!");
        System.out.println("Type 'HELP' for available commands.");
    }
    
    // Выводит доску
    private void printBoard() {
        if (!game.isGameActive()) {
            return;
        }
        
        Board board = game.getBoard();
        int size = board.getSize();
        
        // Создаем строку с координатами
        StringBuilder coords = new StringBuilder("\n   ");
        for (int i = 0; i < size; i++) {
            coords.append(" ").append(i);
        }
        System.out.println(coords.toString());
        
        for (int y = 0; y < size; y++) {
            System.out.print(y + "  ");
            for (int x = 0; x < size; x++) {
                char cell = board.getCell(x, y);
                if (cell == ' ') {
                    System.out.print("_ ");
                } else if (cell == 'W') {
                    System.out.print("W ");
                } else if (cell == 'B') {
                    System.out.print("B ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    // Выводит справку
    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("GAME N, U1,W U2,B - Start new game");
        System.out.println("  N - board size (integer > 2)");
        System.out.println("  U1, U2 - player parameters in format 'TYPE,C'");
        System.out.println("    TYPE: 'user' or 'comp'");
        System.out.println("    C: 'W' (white) or 'B' (black)");
        System.out.println("MOVE X, Y - Make a move");
        System.out.println("  X, Y - coordinates (0-based)");
        System.out.println("EXIT - Exit the game");
        System.out.println("HELP - Show this help");
    }
}
