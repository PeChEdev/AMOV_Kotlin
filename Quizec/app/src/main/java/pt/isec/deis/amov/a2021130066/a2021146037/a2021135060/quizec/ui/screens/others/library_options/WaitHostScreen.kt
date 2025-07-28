package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun WaitHostScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    idQuestionario : String
) {
    val questionario = viewModel.getQuestionarioByID(idQuestionario)
    val timeQuest by viewModel.newTimeQuest.collectAsState()
    var startAndStop by remember { mutableStateOf(true) }
    var sendOnce by remember { mutableStateOf(false) }
    var stopThread by remember { mutableStateOf(false) }

    var clockRunning by remember { mutableStateOf(false) }

    if (sendOnce)
        if (!stopThread)
            LaunchedEffect(Unit) {
                clockRunning = !clockRunning
                val result = viewModel.timeIsClocking(viewModel.newTimeQuest.value.tempolimite!!)
                if (result)
                    clockRunning = !clockRunning
                startAndStop = !startAndStop
            }
        else
            clockRunning = false

    if(viewModel.timeRemaining.value.toInt() == 0 || !clockRunning)
        questionario.active = false

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.greenYellowQuizec))
            .padding(8.dp)
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screens.DetailsSurvey.toString()) {
                    popUpTo(Screens.DetailsSurvey.toString()) { inclusive = true }
                }
            },
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.End)
                .padding(top = 16.dp, end = 16.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = idQuestionario,
            color = colorResource(R.color.white),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    //Nomes pessoas que se vÃ£o conectando TODO
                    Text(
                        text = stringResource(R.string.people_here),
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.white),
                    )
                    for (i in 0 until (questionario.respostas?.size ?: 0)) {
                        TextNames(questionario.respostas?.get(i) ?: "" )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(.5f))

        AnimatedVisibility(visible = clockRunning, enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            //Or if (clockRunning) {
            Text(
                text = viewModel.timeRemaining.value,
                color = colorResource(R.color.white),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(.5f))
            //}
        }

        Button(
            onClick = {
                if (!startAndStop) {
                    timeQuest.tempolimite = Timestamp.now()
                    viewModel.updateTimeQuestToFirestore(timeQuest)
                    startAndStop = !startAndStop
                    stopThread = !stopThread
                }
                if (!sendOnce) {
                    timeQuest.id = idQuestionario
                    timeQuest.tempolimite =
                        viewModel.prepareTime() // Vai adicionar ao tempo atual os minutos já definidos
                    viewModel.addTimeQuestToFirestore(timeQuest)
                    startAndStop = !startAndStop
                    sendOnce = !sendOnce
                }
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.black),
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(0.7f)
                .size(width = 300.dp, height = 50.dp)
                .padding(8.dp)
                .border(
                    BorderStroke(2.dp, color = colorResource(R.color.black)),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Text(
                text = if (startAndStop) stringResource(R.string.start)
                else stringResource(R.string.stop),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TextNames(name:String) {
    Text(
        text = "-> $name",
        color = colorResource(R.color.white),
    )
}