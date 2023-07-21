package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameViewModelTest {

    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        // arrange
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        // act
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        // assert
        currentGameUiState = viewModel.uiState.value

        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FORST_CORRECT_ANSWER, currentGameUiState.score)
    }


    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        // arrange
        val incorrectPlayerWord = "and"

        // act
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        // assert
        val currentGameUiState = viewModel.uiState.value

        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
    }


    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        // arrange
        val currentGameUiState = viewModel.uiState.value
        val unScrambledWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        // assert
        assertNotEquals(unScrambledWord, currentGameUiState.currentScrambledWord)
        assertTrue(currentGameUiState.currentWordCount == 1)
        assertTrue(currentGameUiState.score == 0)
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertFalse(currentGameUiState.isGameOver)
    }


    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        // arrange
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        // act
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

            // assert
            assertEquals(expectedScore, currentGameUiState.score)
        }

        // assert
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }


    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        // we do 1 successful guess, so that score and word count increases
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        // we skip a word
        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount
        viewModel.skipWord()

        // we check score and word count
        currentGameUiState = viewModel.uiState.value
        // score has to be unchanged
        assertEquals(SCORE_AFTER_FORST_CORRECT_ANSWER, currentGameUiState.score)
        // word count has to be increased by 1
        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)
    }


    companion object {
        private const val SCORE_AFTER_FORST_CORRECT_ANSWER = SCORE_INCREASE
    }
}