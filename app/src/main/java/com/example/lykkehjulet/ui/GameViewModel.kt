package com.example.lykkehjulet.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lykkehjulet.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()
    private var usedLetters: MutableSet<Char> = mutableSetOf()
    private val wrongLetters: MutableSet<Char> = mutableSetOf()
    private val ScoreIncrease: String = ""
    private val currentCategory: String = ""
    var userGuess: Char = ' '

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        }
        else {
            usedWords.add(currentWord)
            return currentWord
        }
    }

    private fun wordInLetters(currentWord: String): Set<Char>{

        var letters: Set<Char> = hashSetOf()
        for (letter in currentWord.split("")){
            letters.plus(letter)
        }
        return letters
    }

    fun updateUserGuess(guessedLetter: String) {
        userGuess = guessedLetter[0]
    }

    fun spinWheel(){

    }

    fun checkUserGuess() {
        if (currentWord.contains(currentWord, ignoreCase = true)) {
            if (ScoreIncrease == "Bankrupt"){
                val updatedScore = 0
                updateGameState(updatedScore)
            }
            else {
                val updatedScore = _uiState.value.score.plus(ScoreIncrease.toInt())
                updateGameState(updatedScore)
            }
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(isGuessWrong = true)
            }
            updateUserGuess("")
        }
    }

    private fun updateGameState(updatedScore: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                isGuessWrong = false,
                score = updatedScore,
                isGameOver = true
            )
        }
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentWord = wordInLetters(pickRandomWordAndShuffle()))
    }

}