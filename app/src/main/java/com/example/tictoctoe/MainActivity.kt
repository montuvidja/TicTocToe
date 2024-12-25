package com.example.tictoctoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictoctoe.ui.theme.TicTocToyTheme
import com.example.tictoctoy.R

enum class Win{
    PLAYER,
    AI,
    DRAW
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TicTocToyTheme {

                TTTScreen()

            }
        }
    }
}

@Composable
fun TTTScreen() {
    val playerTurn = remember { mutableStateOf(true) }
    val moves = remember {
        mutableStateListOf<Boolean?>(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    val win = remember { mutableStateOf<Win?>(null) }

    val onTap: (Offset) -> Unit = {
        /*if (playerTurn.value) {
            val x = (it.x / 333).toInt()
            val y = (it.y / 333).toInt()
            Log.i("TAG it.X,it.Y", "it.x = ${it.x}, it.y= ${it.y}")
            Log.i("TAG X,Y", "x = $x, y= $y")
            val posInMove = y * 3 + x
            Log.i("TAG Position", "$posInMove")
            if (moves[posInMove] == null) {
                moves[posInMove] = true
                playerTurn.value = false
                win.value = checkEndGame(moves)
            }
        }*/
        val x = (it.x / 333).toInt()
        val y = (it.y / 333).toInt()
        Log.i("TAG it.X,it.Y", "it.x = ${it.x}, it.y= ${it.y}")
        Log.i("TAG X,Y", "x = $x, y= $y")
        val posInMove = y * 3 + x
        Log.i("TAG Position", "$posInMove")



            if (moves[posInMove] == null) {
                if (playerTurn.value) {
                    moves[posInMove] = true
                    playerTurn.value = false
                } else {
                    moves[posInMove] = false
                    playerTurn.value = true
                }
                win.value = checkEndGame(moves)
            }

    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Tic Toc Toe", fontSize = 30.sp, modifier = Modifier.padding(16.dp))
        Header(playerTurn.value)
        Board(moves, onTap)

        /*if (!playerTurn.value && win.value == null) {
            CircularProgressIndicator(color = Color.Red, modifier = Modifier.padding(16.dp))

            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
                    delay(1500L)
                    while (true) {
                        val i = Random.nextInt(9)
                        if (moves[i] == null) {
                            moves[i] = false
                            playerTurn.value = true
                            win.value = checkEndGame(moves)
                            break
                        }
                    }
                }
            }
        }*/

        if (win.value != null){
            when(win.value){
                Win.PLAYER -> {
                    Text(text = "Player 1 has won \uD83C\uDF89", fontSize = 24.sp)
                }

                Win.AI -> {
                    Text(text = "Player 2 has won \uD83C\uDF89", fontSize = 24.sp)
                }

                Win.DRAW -> {
                    Text(text = "It's a Draw \uD83C\uDF89", fontSize = 24.sp)
                }

                else -> {
                    Text(text = "Result Pending \uD83C\uDF89", fontSize = 24.sp)
                }
            }

            Button(onClick = {
                playerTurn.value = true
                win.value = null
                for (i in 0..8){
                    moves[i] = null
                }
            }) {
                    Text(text = "Click to start over")
            }
        }
    }



}

fun checkEndGame(m: List<Boolean?>) : Win? {
    var win: Win? = null
    if(
        (m[0] == true && m[1] == true && m[2] == true) ||
        (m[3] == true && m[4] == true && m[5] == true) ||
        (m[6] == true && m[7] == true && m[8] == true) ||
        (m[0] == true && m[3] == true && m[6] == true) ||
        (m[1] == true && m[4] == true && m[7] == true) ||
        (m[2] == true && m[5] == true && m[8] == true) ||
        (m[0] == true && m[4] == true && m[8] == true) ||
        (m[2] == true && m[4] == true && m[6] == true)
    )
        win = Win.PLAYER

    if(
        (m[0] == false && m[1] == false && m[2] == false) ||
        (m[3] == false && m[4] == false && m[5] == false) ||
        (m[6] == false && m[7] == false && m[8] == true) ||
        (m[0] == false && m[3] == false && m[6] == false) ||
        (m[1] == false && m[4] == false && m[7] == false) ||
        (m[2] == false && m[5] == false && m[8] == false) ||
        (m[0] == false && m[4] == false && m[8] == false) ||
        (m[2] == false && m[4] == false && m[6] == false)
    )
        win = Win.AI

    if(win == null) {
        var available = false
        for (i in 0..8) {
            if (m[i] == null)
                available = true
        }
        if (!available)
            win = Win.DRAW
    }
    return win
}

@Composable
fun Header(playerTurn: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val playerBoxColor = if (playerTurn) Color.Blue else Color.LightGray
        val aiBoxColor = if (playerTurn) Color.LightGray else Color.Red

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(playerBoxColor)
        ) {
            Text(
                text = "Player 1",
                color = if (playerTurn) Color.White else Color.Black,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(50.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .background(aiBoxColor)
        ) {
            Text(
                text = "Player 2",
                color = if (playerTurn) Color.Black else Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center)


            )
        }
    }
}

@Composable
fun Board(moves: List<Boolean?>, onTap: (Offset) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(36.dp)
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = onTap
                )
            }
    ) {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize(1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(2.dp)
                    .background(Color.Black)
            ) {}
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(2.dp)
                    .background(Color.Black)
            ) {}
        }

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .width(2.dp)
                    .background(Color.Black)
            ) {}
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .width(2.dp)
                    .background(Color.Black)
            ) {}
        }

        Column(modifier = Modifier.fillMaxSize(1f)) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        Column(modifier = Modifier.weight(1f)) {
                            Log.i("Index.......", "${i * 3 + j}")
                            GetComposableFromMove(move = moves[i * 3 + j])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GetComposableFromMove(move: Boolean?) {
    when (move) {
        true -> Image(
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            colorFilter = ColorFilter.tint(Color.Blue)
        )

        false -> Image(
            painter = painterResource(id = R.drawable.ic_o),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            colorFilter = ColorFilter.tint(Color.Red)
        )

        null -> Image(
            painter = painterResource(id = R.drawable.ic_null),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(1f),
            colorFilter = ColorFilter.tint(Color.LightGray)
        )
    }

}

