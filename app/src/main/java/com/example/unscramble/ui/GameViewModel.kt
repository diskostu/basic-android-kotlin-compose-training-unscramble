package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


private val _uiState = MutableStateFlow(GameUiState())
val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

private lateinit var currentWord: String
private var usedWords: MutableSet<String> = mutableSetOf()

private fun pickRandomWordAndShuffle(): String {
    currentWord = allWords.random()
    return if (usedWords.contains(currentWord)) {
        pickRandomWordAndShuffle()
    } else {
        usedWords.add(currentWord)
        shuffleCurrentWord(currentWord)
    }
}

fun shuffleCurrentWord(word: String): String {
    val charArray = word.toCharArray()
    charArray.shuffle()
    while (String(charArray).equals(word)) {
        charArray.shuffle()
    }

    return String(charArray)
}


fun resetGame() {
    usedWords.clear()
    _uiState.value = GameUiState(pickRandomWordAndShuffle())
}

class GameViewModel : ViewModel() {

    init {
        resetGame()
    }
}