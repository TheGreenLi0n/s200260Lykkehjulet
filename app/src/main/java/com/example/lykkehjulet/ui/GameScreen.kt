package com.example.lykkehjulet.ui

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lykkehjulet.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lykkehjulet.ui.theme.LykkehjuletTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)){


        GameStatus(
            category = gameUiState.currentCategory,
            score = gameUiState.score)
        GameLayout(
            currentWord = gameUiState.currentWord,
            usedLetters = gameUiState.usedLetters,
            scoreIncrease = gameUiState.scoreIncrease,
            userGuess = gameViewModel.userGuess,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            isGuessWrong = gameUiState.isGuessWrong)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
        Button(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 8.dp),
            onClick = { gameViewModel.checkUserGuess() }
        ) {
            Text(stringResource(R.string.submit))
        }
        OutlinedButton(
                onClick = { gameViewModel.spinWheel() },
        modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)
        ) {
        Text(stringResource(R.string.spin))
        }
        }
    }
    if (gameUiState.isGameOver) {
        FinalScoreDialog(
            score = gameUiState.score,
            onPlayAgain = { gameViewModel.resetGame() }
        )
    }
}

@Composable
fun GameStatus( category: String, score: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .size(48.dp),
    ) {
        Text(
            text = stringResource(R.string.category, category),
            fontSize = 18.sp,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            text = stringResource(R.string.score, score),
            fontSize = 18.sp,
        )
    }
}

@Composable
fun GameLayout(currentWord: Set<Char>,
               usedLetters: Set<Char>,
               scoreIncrease: String,
               isGuessWrong: Boolean,
               userGuess: Char,
               onUserGuessChanged: (String) -> Unit,
               onKeyboardDone: () -> Unit,
               modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),

        ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "possible points: $scoreIncrease",
                fontSize = 32.sp,
                modifier = modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .size(48.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for ( letter in currentWord){
                    Box(modifier = modifier
                        .border(width = 2.dp, color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(5.dp))
                        .width(width = 35.dp)

                    ){
                       if (usedLetters.contains(letter)){
                           Text(
                               text = letter.toString(),
                               fontSize = 45.sp,
                               modifier = modifier
                                   .align(alignment = Alignment.Center)
                           )
                       }
                       else {
                           Text(
                               text = letter.toString(),
                               fontSize = 45.sp,
                               modifier = modifier.alpha(1f)
                           )
                       }
                    }
                }
            }
            Text(
                text = stringResource(R.string.instructions),
                fontSize = 17.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            OutlinedTextField(
                value = userGuess.toString(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onUserGuessChanged,
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.wrong_guess)) }
                    else {
                        Text(stringResource(R.string.enter_your_word))
                    }
                },
                isError = isGuessWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                ),
            )
        }
    }
}

@Composable
private fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(stringResource(R.string.congratulations)) },
        text = { Text(stringResource(R.string.you_scored, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(
                onClick = onPlayAgain
            ) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LykkehjuletTheme {
        GameScreen()
    }
}
