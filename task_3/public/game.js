class SquaresGame {
    constructor() {
        this.board = [];
        this.boardSize = 5;
        this.playerColor = 'W';
        this.currentPlayer = 'W';
        this.gameActive = false;
        this.gameLog = [];
        
        this.initializeElements();
        this.bindEvents();
    }
    
    // Инициализация элементов
    initializeElements() {
        this.gameBoard = document.getElementById('gameBoard');
        this.boardSizeSelect = document.getElementById('boardSize');
        this.playerColorSelect = document.getElementById('playerColor');
        this.newGameBtn = document.getElementById('newGameBtn');
        this.currentPlayerText = document.getElementById('currentPlayerText');
        this.gameStatusText = document.getElementById('gameStatusText');
        this.gameLogElement = document.getElementById('gameLog');
    }
    
    // Связывание событий
    bindEvents() {
        this.newGameBtn.addEventListener('click', () => this.startNewGame());
        this.boardSizeSelect.addEventListener('change', () => this.updateBoardSize());
    }
    
    // Начало новой игры
    startNewGame() {
        this.boardSize = parseInt(this.boardSizeSelect.value);
        this.playerColor = this.playerColorSelect.value;
        this.currentPlayer = 'W'; // Белые всегда ходят первыми
        this.gameActive = true;
        
        this.initializeBoard();
        this.renderBoard();
        this.updateUI();
        this.addLogEntry('Игра начата! Размер доски: ' + this.boardSize + 'x' + this.boardSize, 'game-end');
        
        // Если компьютер ходит первым
        if (this.currentPlayer !== this.playerColor) {
            this.makeAIMove();
        }
    }
    
    // Инициализация доски
    initializeBoard() {
        this.board = [];
        for (let y = 0; y < this.boardSize; y++) {
            this.board[y] = [];
            for (let x = 0; x < this.boardSize; x++) {
                this.board[y][x] = ' ';
            }
        }
    }
    
    // Рендеринг доски
    renderBoard() {
        this.gameBoard.innerHTML = '';
        this.gameBoard.style.gridTemplateColumns = `repeat(${this.boardSize}, 1fr)`;
        
        for (let y = 0; y < this.boardSize; y++) {
            for (let x = 0; x < this.boardSize; x++) {
                const cell = document.createElement('div');
                cell.className = 'cell';
                cell.dataset.x = x;
                cell.dataset.y = y;
                
                if (this.board[y][x] !== ' ') {
                    cell.classList.add('occupied');
                    if (this.board[y][x] === 'W') {
                        cell.classList.add('white');
                        cell.textContent = '⚪';
                    } else if (this.board[y][x] === 'B') {
                        cell.classList.add('black');
                        cell.textContent = '⚫';
                    }
                }
                
                if (this.gameActive && this.currentPlayer === this.playerColor) {
                    cell.addEventListener('click', (e) => this.handleCellClick(e));
                }
                
                this.gameBoard.appendChild(cell);
            }
        }
    }
    
    // Обработка клика на клетке
    handleCellClick(event) {
        console.log('Cell clicked - gameActive:', this.gameActive, 'currentPlayer:', this.currentPlayer, 'playerColor:', this.playerColor);
        if (!this.gameActive || this.currentPlayer !== this.playerColor) {
            console.log('Click ignored - game not active or not player turn');
            return;
        }
        
        const x = parseInt(event.target.dataset.x);
        const y = parseInt(event.target.dataset.y);
        
        if (this.board[y][x] !== ' ') {
            console.log('Cell occupied:', x, y, this.board[y][x]);
            return; // Клетка уже занята
        }
        
        console.log('Making player move:', x, y, this.playerColor);
        this.makeMove(x, y, this.playerColor);
    }
    
    // Делаем ход
    makeMove(x, y, color) {
        // Проверяем, что клетка свободна
        if (this.board[y][x] !== ' ') {
            // Если это ход AI, делаем случайный ход
            if (color !== this.playerColor) {
                this.makeRandomMove();
                return;
            }
            return;
        }
        
        this.board[y][x] = color;
        console.log('Made move:', x, y, color, 'Setting board[' + y + '][' + x + '] =', color);
        this.addLogEntry(`${color === 'W' ? 'Белые' : 'Черные'} ходят в (${x}, ${y})`, 
                         color === this.playerColor ? 'player-move' : 'ai-move');
        
        this.renderBoard();
        
        // Проверяем условия окончания игры
        if (this.checkWin(color)) {
            this.endGame(color);
            return;
        }
        
        if (this.isBoardFull()) {
            this.endGame(null); // Ничья
            return;
        }
        
        // Переключаем игрока
        this.currentPlayer = this.currentPlayer === 'W' ? 'B' : 'W';
        console.log('After move - currentPlayer:', this.currentPlayer, 'playerColor:', this.playerColor, 'gameActive:', this.gameActive);
        this.updateUI();
        this.renderBoard(); // Перерисовываем доску, чтобы перевесить обработчики кликов для нового игрока
        
        // Если следующий ход за компьютером
        if (this.currentPlayer !== this.playerColor) {
            console.log('AI turn, calling makeAIMove');
            setTimeout(() => this.makeAIMove(), 500);
        } else {
            console.log('Player turn, waiting for click');
        }
    }
    
    // Делаем ход ИИ
    async makeAIMove() {
        if (!this.gameActive || this.currentPlayer === this.playerColor) {
            return;
        }
        
        try {
            const boardData = this.boardToString();
            const response = await fetch('/api/squares/nextMove', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "size": this.boardSize,
                    "data": boardData,
                    "nextPlayerColor": this.currentPlayer
                }, null, 2) // Форматируем JSON с отступами
            });
            
            if (!response.ok) {
                throw new Error('Ошибка API');
            }
            
            const move = await response.json();
            
            // Проверяем, не закончена ли игра
            if (move.x === -1 && move.y === -1) {
                // Игра уже закончена, есть победитель
                this.endGame(move.color);
                return;
            }
            
            // Проверяем, что ход валидный
            if (move.x >= 0 && move.y >= 0 && move.x < this.boardSize && move.y < this.boardSize) {
                this.makeMove(move.x, move.y, this.currentPlayer);
            } else {
                throw new Error('Некорректный ход от ИИ');
            }
            
        } catch (error) {
            console.error('Ошибка при получении хода ИИ:', error);
            this.addLogEntry('Ошибка при получении хода ИИ: ' + error.message, 'error');
            // Делаем случайный ход как fallback
            this.makeRandomMove();
        }
    }
    
    // Проверяем статус игры
    async checkGameStatus() {
        try {
            const boardData = this.boardToString();
            const response = await fetch('/api/squares/gameStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    "size": this.boardSize,
                    "data": boardData,
                    "nextPlayerColor": this.currentPlayer
                }, null, 2)
            });
            
            if (!response.ok) {
                throw new Error('Ошибка API');
            }
            
            const status = await response.json();
            console.log('Game status:', status);
            return status;
            
        } catch (error) {
            console.error('Ошибка при проверке статуса игры:', error);
            return null;
        }
    }
    
    // Делаем случайный ход
    makeRandomMove() {
        const emptyCells = [];
        for (let y = 0; y < this.boardSize; y++) {
            for (let x = 0; x < this.boardSize; x++) {
                if (this.board[y][x] === ' ') {
                    emptyCells.push({x, y});
                }
            }
        }
        
        if (emptyCells.length > 0) {
            const randomCell = emptyCells[Math.floor(Math.random() * emptyCells.length)];
            this.makeMove(randomCell.x, randomCell.y, this.currentPlayer);
        }
    }
    
    // Проверяем, есть ли победитель
    checkWin(color) {
        // Проверяем все возможные квадраты 2x2
        for (let y = 0; y < this.boardSize - 1; y++) {
            for (let x = 0; x < this.boardSize - 1; x++) {
                if (this.board[y][x] === color &&
                    this.board[y][x + 1] === color &&
                    this.board[y + 1][x] === color &&
                    this.board[y + 1][x + 1] === color) {
                    
                    // Подсвечиваем выигрышный квадрат
                    this.highlightWinningSquare(x, y);
                    return true;
                }
            }
        }
        return false;
    }
    
    // Подсвечиваем выигрышный квадрат
    highlightWinningSquare(startX, startY) {
        const cells = this.gameBoard.querySelectorAll('.cell');
        for (let y = startY; y < startY + 2; y++) {
            for (let x = startX; x < startX + 2; x++) {
                const cell = Array.from(cells).find(cell => 
                    parseInt(cell.dataset.x) === x && parseInt(cell.dataset.y) === y
                );
                if (cell) {
                    cell.classList.add('winning');
                }
            }
        }
    }
    
    // Проверяем, заполнена ли доска
    isBoardFull() {
        for (let y = 0; y < this.boardSize; y++) {
            for (let x = 0; x < this.boardSize; x++) {
                if (this.board[y][x] === ' ') {
                    return false;
                }
            }
        }
        return true;
    }
    
    // Завершаем игру
    endGame(winner) {
        this.gameActive = false;
        
        if (winner) {
            const winnerText = winner === 'W' ? 'Белые' : 'Черные';
            this.gameStatusText.textContent = `🎉 ${winnerText} выиграли!`;
            this.addLogEntry(`Игра окончена! Победили ${winnerText}!`, 'game-end');
        } else {
            this.gameStatusText.textContent = '🤝 Ничья!';
            this.addLogEntry('Игра окончена! Ничья!', 'game-end');
        }
        
        this.currentPlayerText.textContent = 'Игра окончена';
        this.renderBoard(); // Убираем обработчики событий
    }
    
    // Преобразуем доску в строку
    boardToString() {
        let result = '';
        for (let y = 0; y < this.boardSize; y++) {
            for (let x = 0; x < this.boardSize; x++) {
                result += this.board[y][x];
            }
        }
        console.log('Board data being sent:', result);
        console.log('Board visualization:');
        for (let y = 0; y < this.boardSize; y++) {
            let row = '';
            for (let x = 0; x < this.boardSize; x++) {
                row += this.board[y][x] + ' ';
            }
            console.log('Row', y + ':', row);
        }
        return result;
    }
    
    // Обновляем UI
    updateUI() {
        if (!this.gameActive) {
            this.currentPlayerText.textContent = 'Игра не активна';
            this.gameStatusText.textContent = '';
            return;
        }
        
        const playerText = this.currentPlayer === 'W' ? 'Белые' : 'Черные';
        const isPlayerTurn = this.currentPlayer === this.playerColor;
        
        this.currentPlayerText.textContent = `Ход: ${playerText} ${isPlayerTurn ? '(Ваш ход)' : '(Ход ИИ)'}`;
        this.gameStatusText.textContent = isPlayerTurn ? 'Кликните на пустую клетку' : 'ИИ думает...';
    }
    
    // Обновляем размер доски
    updateBoardSize() {
        if (!this.gameActive) {
            this.boardSize = parseInt(this.boardSizeSelect.value);
        }
    }
    
    // Добавляем запись в лог
    addLogEntry(message, type = '') {
        const timestamp = new Date().toLocaleTimeString();
        const entry = document.createElement('div');
        entry.className = `log-entry ${type}`;
        entry.textContent = `[${timestamp}] ${message}`;
        
        this.gameLogElement.appendChild(entry);
        this.gameLogElement.scrollTop = this.gameLogElement.scrollHeight;
        
        this.gameLog.push({timestamp, message, type});
    }
}

// Инициализация игры
document.addEventListener('DOMContentLoaded', () => {
    new SquaresGame();
});
