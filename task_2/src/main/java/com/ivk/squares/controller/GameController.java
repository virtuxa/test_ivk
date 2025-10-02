package com.ivk.squares.controller;

import com.ivk.squares.dto.BoardDto;
import com.ivk.squares.dto.GameStatusDto;
import com.ivk.squares.dto.SimpleMoveDto;
import com.ivk.squares.service.GameEngine;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

// Класс для управления игрой
public class GameController {
    private static GameEngine gameEngine = new GameEngine();
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/squares/nextMove", new NextMoveHandler());
        server.createContext("/api/squares/gameStatus", new GameStatusHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("Web service running on http://localhost:8080");
        System.out.println("API endpoints:");
        System.out.println("POST /api/squares/nextMove");
        System.out.println("POST /api/squares/gameStatus");
    }
    
    // Класс для обработки запроса на следующий ход
    static class NextMoveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            
            // CORS headers
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                // Читаем тело запроса
                InputStream requestBody = exchange.getRequestBody();
                String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                // Парсим JSON (простой парсинг для демонстрации)
                BoardDto boardDto = parseBoardDto(body);
                SimpleMoveDto move = gameEngine.calculateNextMove(boardDto);
                
                String response;
                if (move != null) {
                    String color = move.getColor();
                    if (color == null || color.isEmpty() || color.trim().isEmpty() || "null".equals(color)) {
                        color = boardDto.getNextPlayerColor();
                    }
                    response = "{\"x\":" + move.getX() + ",\"y\":" + move.getY() + ",\"color\":\"" + color + "\"}";
                } else {
                    response = "{\"error\":\"No valid move\"}";
                }
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                
            } catch (Exception e) {
                String error = "{\"error\":\"" + e.getMessage() + "\"}";
                exchange.sendResponseHeaders(500, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        }
    }
    
    // Класс для обработки запроса на статус игры
    static class GameStatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }
            
            // CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                // Читаем тело запроса
                InputStream requestBody = exchange.getRequestBody();
                String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                
                // Парсим JSON (простой парсинг для демонстрации)
                BoardDto boardDto = parseBoardDto(body);
                GameStatusDto status = gameEngine.getGameStatus(boardDto);
                
                String response = "{\"status\":" + status.getStatus() + 
                    ",\"color\":\"" + status.getColor() + 
                    "\",\"message\":\"" + status.getMessage() + "\"}";
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                
            } catch (Exception e) {
                String error = "{\"error\":\"" + e.getMessage() + "\"}";
                exchange.sendResponseHeaders(500, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();
            }
        }
    }
    
    // Парсит доску
    private static BoardDto parseBoardDto(String json) {
        BoardDto dto = new BoardDto();
        
        try {
            // Извлекаем размер используя regex
            java.util.regex.Pattern sizePattern = java.util.regex.Pattern.compile("\"size\"\\s*:\\s*(\\d+)");
            java.util.regex.Matcher sizeMatcher = sizePattern.matcher(json);
            if (sizeMatcher.find()) {
                dto.setSize(Integer.parseInt(sizeMatcher.group(1)));
            } else {
                throw new RuntimeException("size not found");
            }
            
            // Извлекаем данные используя regex
            java.util.regex.Pattern dataPattern = java.util.regex.Pattern.compile("\"data\"\\s*:\\s*\"([^\"]*)\"");
            java.util.regex.Matcher dataMatcher = dataPattern.matcher(json);
            if (dataMatcher.find()) {
                dto.setData(dataMatcher.group(1));
            } else {
                throw new RuntimeException("data not found");
            }
            
            // Извлекаем цвет следующего игрока используя regex
            java.util.regex.Pattern colorPattern = java.util.regex.Pattern.compile("\"nextPlayerColor\"\\s*:\\s*\"([^\"]*)\"");
            java.util.regex.Matcher colorMatcher = colorPattern.matcher(json);
            if (colorMatcher.find()) {
                dto.setNextPlayerColor(colorMatcher.group(1));
            } else {
                throw new RuntimeException("nextPlayerColor not found");
            }
            
        } catch (Exception e) {
            System.out.println("JSON parsing error: " + e.getMessage());
            System.out.println("Original JSON: " + json);
            throw new RuntimeException("Invalid JSON format: " + e.getMessage());
        }
        
        return dto;
    }
}