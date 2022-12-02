package com.example.lykkehjulet.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lykkehjulet.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.reflect.typeOf

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord: Set<Char>
    private var usedLetters: MutableSet<Char> = mutableSetOf()
    private var wrongLetters: MutableSet<Char> = mutableSetOf()
    private var scoreIncrease: String = ""
    private var currentCategory: String = ""
    var userGuess: String = ""

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(category: String): Set<Char> {
        currentWord = when (category){
            "person" -> wordInLetters(person.random())
            "title" -> wordInLetters(title.random())
            "nature" -> wordInLetters(nature.random())
            "food" -> wordInLetters(food.random())
            else -> {
                wordInLetters(place.random())
            }
        }
        return currentWord
    }

    private fun wordInLetters(currentWord: String): MutableSet<Char>{

        var letters: MutableSet<Char> = mutableSetOf()
        for (letter in currentWord.toCharArray()){
            letters.add(letter)
        }
        return letters
    }

    fun updateUserGuess(guessedLetter: String) {
        userGuess = guessedLetter
    }

    fun spinWheel(){
        currentCategory = categories.random()
        currentWord = pickRandomWordAndShuffle(currentCategory)
        scoreIncrease = value.random()
        val updatedScore = _uiState.value.score.plus(0)
        val lives = _uiState.value.lives.minus(0)
        updateGameState(updatedScore, lives)
    }

    fun checkUserGuess() {
        usedLetters.add(userGuess.toCharArray()[0])
        if (currentWord.contains(userGuess[0])) {
            val updatedScore = _uiState.value.score.plus(scoreIncrease.toInt())
            val lives = _uiState.value.lives.minus(0)
            updateGameState(updatedScore, lives)
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(isGuessWrong = true)
            }
            val updatedScore = _uiState.value.score.plus(0)
            val lives = _uiState.value.lives.minus(1)
            wrongLetters.add(userGuess.toCharArray()[0])
            updateGameState(updatedScore, lives)
            updateUserGuess("")
        }
    }

    private fun updateGameState(updatedScore: Int, updatedLives: Int) {
        if (updatedLives == 0) {
            _uiState.update { currentState ->
                currentState.copy(
                    score = updatedScore,
                    isGameOver = true,

                )
            }
        }
        else {
            _uiState.update { currentState ->
                currentState.copy(
                    currentWord = currentWord,

                    scoreIncrease = scoreIncrease,
                    usedLetters = usedLetters,
                    currentCategory = currentCategory,
                    isGuessWrong = false,
                    score = updatedScore,
                    lives = updatedLives

                )
            }
        }
    }

    fun resetGame() {
        usedLetters.clear()
        _uiState.value = GameUiState()
    }

}