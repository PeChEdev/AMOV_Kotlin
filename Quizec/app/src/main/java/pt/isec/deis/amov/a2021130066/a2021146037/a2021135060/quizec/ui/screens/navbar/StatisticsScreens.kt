package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.QuestionsType
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Question
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Questionario

@Composable
fun StatisticsScreen(
    viewModel: FirebaseViewModel,
    navController: NavController,
) {
    val questList by viewModel.allquestinarios.collectAsState()
    LaunchedEffect(Unit) {viewModel.startObserverOfListQuest()}

    Column( modifier = Modifier.background(Color.White) ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(questList) { quest ->
                if(quest.criadorPor.equals(viewModel.user.value?.email)){
                    Questionario(quest, navController = navController, viewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun Questionario(
    questionario: Questionario,
    navController: NavController,
    viewModel: FirebaseViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${Screens.SurveyStats}/${questionario.id}") {
                    popUpTo(Screens.SurveyStats.toString()) { inclusive = true }
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
                        Text(
                            text = if (questionario.active == true) "Active" else "",
                            color = colorResource(R.color.black),
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = questionario.id ?: "N/A",
                            color = colorResource(R.color.black),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Statistics (
    viewModel: FirebaseViewModel,
    navController: NavController,
    id: String
){
    val answerList = viewModel.getAnswersByID(id)
    val context = LocalContext.current

    Column (modifier = Modifier.padding(16.dp)){
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.size(30.dp).align(Alignment.End).fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = context.getString(R.string.survey_id) + "  " + id,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(answerList) { answer ->
                when (answer.questionType) {
                    QuestionsType.TRUE_FALSE, QuestionsType.SINGLE_CHOICE -> {
                        QuestionStatsCard(answer, navController = navController)
                    }
                    QuestionsType.MULTIPLE_CHOICE, QuestionsType.MATCHING, QuestionsType.ORDERING, QuestionsType.FILL_BLANK, QuestionsType.ASSOCIATION -> {
                        // QuestionStatsCardWithList(question, navController)
                    }
                    QuestionsType.WORD_INPUT -> TODO()
                    QuestionsType.EMPTY -> TODO()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun QuestionStatsCard(
    question: Question,
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = question.title,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.number_of_answers) + question.respostas?.size,
            fontSize = 15.sp,
            modifier = Modifier.fillMaxWidth()
        )

        val answerCount = mutableMapOf<String, Int>()
        if (question.respostas?.size!! > 0) {
            question.respostas?.forEach { resposta ->
                question.options?.forEach { option ->
                    if (resposta.toString() == option.toString()) {
                        val currentCount = answerCount.getOrDefault(option.toString(), 0)
                        answerCount[option.toString()] = currentCount + 1
                    }
                }
            }
            val answerPercentages = answerCount.map { (option, count) ->
                option to (count.toFloat() / question.respostas?.size!! * 100)
            }
            val colors = listOf(
                colorResource(id = R.color.scarletQuizec),
                colorResource(id = R.color.darkOrangeQuizec),
                colorResource(id = R.color.aureolinQuizec),
                colorResource(id = R.color.greenYellowQuizec),
                colorResource(id = R.color.screamingGreenQuizec),
                colorResource(id = R.color.mintQuizec),
                colorResource(id = R.color.moonstoneQuizec)
            )
            var i = 0
            val graphicsSize = 150f

            Row (modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PieChart(
                    data = answerPercentages, modifier = Modifier
                        .size(graphicsSize.dp)
                        .weight(1f)
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    answerPercentages.forEach { (label, count) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(16.dp).background(colors[i]))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "$label - ${"%.1f".format(count)}%")
                        }
                        i = (i + 1) % colors.size
                    }
                }
            }
            Spacer(modifier = Modifier.padding(16.dp))
            BarChart(
                data = answerPercentages,
                modifier = Modifier.weight(1f).size(graphicsSize.dp)
            )
        }
    }
}

@Composable
fun PieChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    radius: Float = 150f
) {
    val total = data.sumOf { it.second.toDouble() }
    val colors = listOf(
        colorResource(id = R.color.scarletQuizec),
        colorResource(id = R.color.darkOrangeQuizec),
        colorResource(id = R.color.aureolinQuizec),
        colorResource(id = R.color.greenYellowQuizec),
        colorResource(id = R.color.screamingGreenQuizec),
        colorResource(id = R.color.mintQuizec),
        colorResource(id = R.color.moonstoneQuizec)
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(radius.dp * 2)
        ) {
            var startAngle = 0f
            var i = 0

            data.forEach { (_, value) ->
                val sweepAngle = (value / total.toFloat()) * 360f
                drawArc(
                    color = colors[i],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = size.copy(width = radius * 2, height = radius * 2)
                )
                startAngle += sweepAngle
                i = (i + 1) % colors.size
            }
        }
    }
}

@Composable
fun BarChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    barWidth: Float = 50f,
    spacing: Float = 16f,
    chartHeight: Float = 400f
) {
    val colors = listOf(
        colorResource(id = R.color.scarletQuizec),
        colorResource(id = R.color.darkOrangeQuizec),
        colorResource(id = R.color.aureolinQuizec),
        colorResource(id = R.color.greenYellowQuizec),
        colorResource(id = R.color.screamingGreenQuizec),
        colorResource(id = R.color.mintQuizec),
        colorResource(id = R.color.moonstoneQuizec)
    )
    val maxDataValue = data.maxOf { it.second }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(barWidth.dp)
            ) {
                data.forEachIndexed { index, (_, value) ->
                    val barHeight = (value / maxDataValue) * chartHeight
                    val xOffset = index * (barWidth + spacing)

                    drawRect(
                        color = colors[index % colors.size],
                        topLeft = Offset(x = xOffset, y = chartHeight - barHeight),
                        size = Size(width = barWidth, height = barHeight)
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            data.forEachIndexed { index, (label, _) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colors[index % colors.size])
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = label)
                }
            }
        }
    }
}