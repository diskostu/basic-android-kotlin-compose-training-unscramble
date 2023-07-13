package com.example.unscramble.ui

data class GameUiState(
    val currentScrambledWord: String = "",
    var isGuessedWordWrong: Boolean = false
)
