const express = require('express');
const cors = require('cors');
const axios = require('axios');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const API_BASE_URL = process.env.API_BASE_URL || 'http://localhost:8080';

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.static('public'));

// Прокси для API запросов к веб-сервису
app.post('/api/squares/nextMove', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/api/squares/nextMove`, req.body);
        res.json(response.data);
    } catch (error) {
        console.error('Error calling nextMove API:', error.message);
        res.status(500).json({ error: 'Failed to get next move' });
    }
});

app.post('/api/squares/gameStatus', async (req, res) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/api/squares/gameStatus`, req.body);
        res.json(response.data);
    } catch (error) {
        console.error('Error calling gameStatus API:', error.message);
        res.status(500).json({ error: 'Failed to get game status' });
    }
});

// Главная страница
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => {
    console.log(`Web application running on http://localhost:${PORT}`);
    console.log(`API service expected at: ${API_BASE_URL}`);
});
