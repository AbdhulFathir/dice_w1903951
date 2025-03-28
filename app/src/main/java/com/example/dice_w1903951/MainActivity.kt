// main app

package com.example.dice_w1903951
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dice_w1903951.ui.theme.Dice_w1903951Theme

var targetScore = 101 // Global variable for target score

var humanWins = 0
var computerWins = 0

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dice_w1903951Theme {
                Scaffold(
                    // body part
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            var showAboutDialog by rememberSaveable { mutableStateOf(false) }
                            var showTargetScoreDialog by rememberSaveable { mutableStateOf(false) }

                            Text("Dice Game", fontSize = 32.sp, modifier = Modifier.padding(bottom = 20.dp))

                            CustomButton("New Game") {
                                val intent = Intent(this@MainActivity, GameScreen::class.java)
                                startActivity(intent)
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            CustomButton("Set Target Score") { showTargetScoreDialog = true }

                            Spacer(modifier = Modifier.height(20.dp))

                            CustomButton("About") { showAboutDialog = true }


                            // About Dialog
                            if (showAboutDialog) {
                                AboutDialog(onDismissRequest = {showAboutDialog = false})
//                                AlertDialog(
//                                    onDismissRequest = { showAboutDialog = false },
//                                    title = { Text("About") },
//                                    text = {
//                                        Column {
//                                            Text("Student Name: John Doe")
//                                            Text("Student ID: w1903051")
//                                            Spacer(modifier = Modifier.height(10.dp))
//                                            Text(
//                                                "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
//                                                textAlign = TextAlign.Justify
//                                            )
//                                        }
//                                    },
//                                    confirmButton = {
//                                        Button(onClick = { showAboutDialog = false }) {
//                                            Text("OK")
//                                        }
//                                    }
//                                )
                            }

                            // Target Score Dialog
                            if (showTargetScoreDialog) {
                                TargetScoreDialog(onDismissRequest = { showTargetScoreDialog = false })
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(50.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Composable
fun AboutDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("About", fontSize = 20.sp) },
        text = {
            Column {
                Text("Student Name: Abdhul Fathir")
                Text("Student ID: w1903051")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                    textAlign = TextAlign.Justify
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest) { Text("OK") }
        }
    )
}




@Composable
fun TargetScoreDialog(onDismissRequest: () -> Unit) {
    var newTargetScore by remember { mutableIntStateOf(targetScore) }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Set Winning Target") },
        text = {
            TextField(
                value = newTargetScore.toString(),
                onValueChange = { newTargetScore = it.toIntOrNull() ?: targetScore },
                label = { Text("Target Score") }
            )
        },
        confirmButton = {
            Button(onClick = {
                targetScore = newTargetScore
                onDismissRequest()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
