package com.example.lykkehjulet.ui

data class GameUiState(
    val currentWord: Set<Char> = hashSetOf(),
    val wrongLetters: Set<Char> = hashSetOf(),
    val isGuessWrong: Boolean = false,
    val score: Int = 0,
    val ScoreIncrease: Int = 0,
    val isGameOver: Boolean = false,
    val currentCategory: String = ""
)