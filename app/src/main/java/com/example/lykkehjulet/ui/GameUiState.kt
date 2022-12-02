package com.example.lykkehjulet.ui

data class GameUiState(
    val currentWord: Set<Char> = hashSetOf(),
    val wrongLetters: Set<Char> = hashSetOf(),
    val usedLetters: Set<Char> = setOf(),
    val isGuessWrong: Boolean = false,
    val score: Int = 0,
    val scoreIncrease: String = "",
    val isGameOver: Boolean = false,
    val currentCategory: String = "",
    val lives: Int = 5
)