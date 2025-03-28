package com.example.dice_w1903951
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dice_w1903951.ui.theme.Dice_w1903951Theme
import kotlin.random.Random


class GameScreen : ComponentActivity() {

    private var showResultDialog by mutableStateOf(false)
    private var resultTitle by mutableStateOf("")
    private var resultMessage by mutableStateOf("")
    private var resultColor by mutableStateOf(Color.Black)
    private var dialogImage by mutableIntStateOf(0)

    // Reference: https://developer.android.com/guide/fragments/saving-state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("showResultDialog", showResultDialog)
        outState.putString("resultTitle", resultTitle)
        outState.putString("resultMessage", resultMessage)
        outState.putInt("resultColor", resultColor.hashCode())
        outState.putInt("dialogImage", dialogImage)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state if available
        if (savedInstanceState != null) {
            showResultDialog = savedInstanceState.getBoolean("showResultDialog", false)
            resultTitle = savedInstanceState.getString("resultTitle", "")
            resultMessage = savedInstanceState.getString("resultMessage", "")
            resultColor = Color(savedInstanceState.getInt("resultColor"))
            dialogImage = savedInstanceState.getInt("dialogImage", 0)
        }

        enableEdgeToEdge()
        setContent {
            Dice_w1903951Theme {
                Scaffold(
                    //app bar
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "H: $humanWins / C: $computerWins",
                                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        )
                    },

                    // body part
                    content = { innerPadding ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var playerDice by remember { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }
                            var computerDice by remember { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }
                            var playerScore by rememberSaveable { mutableIntStateOf(0) } // total player score
                            var computerScore by rememberSaveable { mutableIntStateOf(0) }  // total computer score
                            var playerTurnScore by rememberSaveable  { mutableIntStateOf(0) } // Temporary score before adding to total
                            var computerTurnScore by rememberSaveable { mutableIntStateOf(0) } // Temporary score before adding to total
                            var playerRollCount by rememberSaveable { mutableIntStateOf(0) } //
                            var computerRollCount by rememberSaveable { mutableIntStateOf(0) }
                            var selectedPlayerDiceIndex by rememberSaveable { mutableIntStateOf(-1) }
                            var selectedComputerDiceIndex by rememberSaveable { mutableIntStateOf(-1) }
                            var totalRollCount by rememberSaveable { mutableIntStateOf(0) }
                            var testPlayerRollCount by rememberSaveable { mutableIntStateOf(0) } // total rolls of player (for debugging)
                            var testComputerRollCount by rememberSaveable { mutableIntStateOf(0) } // total rolls of computer (for debugging)
                            var isGameOver by rememberSaveable { mutableStateOf(false) }  // for game status


                            fun calculateScore(dice: List<Int>): Int {
                                var total = 0
                                for (die in dice) {
                                    total += die
                                }
                                return total
                            }

                            fun computerPlay() {
                                // random selection of computer re-roll , with ensure the roll or re-roll
                                if (computerRollCount == 0 || Random.nextBoolean()) {

                                    // random number to keep computer dice
                                    // random decision to dice a keep
                                    selectedComputerDiceIndex = if(Random.nextBoolean()) -1 else{Random.nextInt(0, 5)}

                                    computerDice = computerDice.mapIndexed { index, value ->
                                        if (selectedComputerDiceIndex == index) value else Random.nextInt(1, 7)
                                    }
                                    computerTurnScore += calculateScore(computerDice)
                                    computerRollCount++
                                    testComputerRollCount++
                                }
                            }

                            fun scoreTurn() {
                                playerScore += playerTurnScore

                                while (computerRollCount <3){
                                    if(computerRollCount <playerRollCount){
                                        computerPlay()
                                    }else{
                                        break
                                    }

                                }

                                computerScore += computerTurnScore


                                // to check tie break scenario


//                            computerScore += 55
//                            playerScore += 55
//                            if(playerScore > 150){
//                                playerScore += 5
//                            }

                                playerRollCount = 0
                                computerRollCount = 0
                                playerTurnScore = 0
                                computerTurnScore = 0


                                if (playerScore != computerScore && (playerScore >= targetScore || computerScore >= targetScore)) {
                                    resultTitle =
                                        if (playerScore >= computerScore) "You win!" else "You lose"
                                    resultMessage =
                                        if (playerScore >= computerScore) "Congratulations!" else "Better Luck Next Time!"
                                    resultColor =
                                        if (playerScore >= computerScore) Color.Green else Color.Red
                                    if (playerScore >= computerScore) humanWins++ else computerWins++
                                    dialogImage = if (playerScore >= computerScore) R.drawable.ic_win_image else  R.drawable.ic_lose_image
                                    isGameOver = true
                                    showResultDialog = true

                                }
                            }



                            fun rollDice() {
                               if (playerRollCount >= 3) return
                               playerRollCount++
                               testPlayerRollCount ++
                               totalRollCount++

                                playerDice = playerDice.mapIndexed { index, value ->
                                    if (selectedPlayerDiceIndex == index) value else Random.nextInt(1, 7)
                                }


                                if (computerRollCount < 3) {
                                    computerPlay()
                                }

                                playerTurnScore += calculateScore(playerDice)


                                // Tie Scenario - update score every roll
                                if (playerScore >= targetScore && computerScore >= targetScore) {
                                    scoreTurn()
                                }
                                selectedPlayerDiceIndex = -1
                                selectedComputerDiceIndex = -1
                            }



                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Player: $playerScore", style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "Computer: $computerScore", style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Text("Player's Dice", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                playerDice.forEachIndexed { index, dice ->
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(4.dp)
                                            .border(
                                                width = if (selectedPlayerDiceIndex == index) 4.dp else 0.dp,
                                                color = if (selectedPlayerDiceIndex == index) Color.Green else Color.Transparent
                                            )
                                            .clickable {
                                                if (totalRollCount != 0 && (playerRollCount == 1 || playerRollCount == 2)) {
                                                    selectedPlayerDiceIndex =
                                                        if (selectedPlayerDiceIndex == index) {
                                                            -1
                                                        } else {
                                                            index
                                                        }
                                                }


                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = if (totalRollCount == 0) R.drawable.dice_6 else getDiceImage(dice)),
                                            contentDescription = "Player Dice",
                                            modifier = Modifier.size(80.dp)
                                        )
                                    }
                                }
                            }


                            Spacer(modifier = Modifier.height(30.dp))
                            Text("Computer's Dice", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                computerDice.forEachIndexed { index, dice ->
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(4.dp)
                                            .border(
                                                width = if (selectedComputerDiceIndex == index) 4.dp else 0.dp,
                                                color = if (selectedComputerDiceIndex == index) Color.Red else Color.Transparent
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                    Image(
                                        painter = painterResource(id = if (totalRollCount == 0) R.drawable.dice_6 else getDiceImage(dice)),
                                        contentDescription = "Computer Dice",
                                        modifier = Modifier.size(80.dp)
                                    )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                            Row {
                                Button(
                                    onClick = {
//                playerDice = List(5) { Random.nextInt(1, 7) }
//                computerDice = List(5) { Random.nextInt(1, 7) }
                                        if (playerRollCount < 3) rollDice()
                                        if (playerRollCount == 3) scoreTurn()
                                    },
//                                    enabled = playerRollCount < 3
                                ) {
                                    Text("Throw")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Button(onClick = { scoreTurn() }, enabled = playerRollCount in 1..2) {
                                    Text("Score")
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Button(onClick = {
                                    setResult(Activity.RESULT_OK)
                                    finish()
                                }) {
                                    Text("Back")
                                }
                            }

//                            // for debugging purpose
//                            Spacer(modifier = Modifier.height(40.dp))
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(16.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Text(
//                                    "Player: $playerScore (+$playerTurnScore)",
//                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                                )
//                                Text(
//                                    "Computer: $computerScore (+$computerTurnScore)",
//                                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                                )
//                            }
//                            Spacer(modifier = Modifier.height(40.dp))
//                            Text(
//                                "Player: $playerScore (Rolls: $playerRollCount/3) total : $testPlayerRollCount",
//                                style = TextStyle(
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                            )
//                            Spacer(modifier = Modifier.height(40.dp))
//                            Text(
//                                "Computer: $computerScore (Rolls: $computerRollCount/3) total : $testComputerRollCount",
//                                style = TextStyle(
//                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                            )

                            if (showResultDialog) {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = {
                                        Text(
                                            resultTitle,
                                            color = resultColor,
                                            style = TextStyle(fontSize = 35.sp),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    text = {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            Image(
                                                painter = painterResource(id = dialogImage),
                                                contentDescription = resultTitle,
                                                modifier = Modifier.size(100.dp)
                                            )
                                            Text(
                                                text = resultMessage,
                                                color = resultColor,
                                                style = TextStyle(fontSize = 20.sp),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(onClick = {
                                            showResultDialog = false
                                            setResult(Activity.RESULT_OK)
                                            finish()
                                        }) {
                                            Text("OK")
                                        }
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }


}


fun getDiceImage(value: Int): Int {
    return when (value) {
        0 -> R.drawable.dice_6
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}








