package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Questionario

@Composable
fun LibraryScreen(
    viewModel: FirebaseViewModel,
    navController: NavController,
) {
    val questList by viewModel.allquestinarios.collectAsState()
    LaunchedEffect(Unit) { viewModel.startObserverOfListQuest() }

    Column( modifier = Modifier.background(Color.White) ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(questList) { quest ->
                if(quest.criadorPor.equals(viewModel.user.value?.email)){
                    QuestionarioCard(quest, navController = navController, viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun QuestionarioCard(
    questionario: Questionario,
    navController: NavController,
    viewModel: FirebaseViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screens.DetailsSurvey.toString()) {
                    popUpTo(Screens.DetailsSurvey.toString()) { inclusive = true }
                }
                viewModel.changeQuestOnWatch(questionario)
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(2f)
                    ) {
                        Text(
                            text = questionario.titulo ?: "N/A",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.black)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = questionario.data ?: "N/A",
                            color = colorResource(R.color.black)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = if (questionario.active == true) "Active" else "",
                            color = colorResource(R.color.black),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }
    }
}