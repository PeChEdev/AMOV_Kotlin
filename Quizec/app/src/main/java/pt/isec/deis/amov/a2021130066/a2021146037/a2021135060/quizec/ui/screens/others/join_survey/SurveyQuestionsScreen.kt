package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.QuestionsType
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Question
import kotlin.random.Random

@Composable
fun SurveyQuestionsScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    id: String
) {
    val questionActive = remember { mutableStateOf( Question(
            title = "",
            questionType = QuestionsType.EMPTY,
            options = null,
            serverPhotoUrl = null,
            uniqueCode = "",
            quemRespondeu = null,
            respostas = null,
            idQuestionario = id,
    ))}
    val selectedOption = remember { mutableStateOf("") }
    val numQSelected = remember { mutableIntStateOf(0) }
    val numLastSelected = remember { mutableIntStateOf(0) }
    val listQuestions = remember { mutableStateOf<List<Question>>(emptyList())}
    val answerList = viewModel.getAnswersByID(id)
    listQuestions.value = answerList

    fun updateQuestion(i: Int) {
        if (i in listQuestions.value.indices) {
            val updatedQuestion = questionActive.value.copy()
            listQuestions.value = listQuestions.value.toMutableList().apply {set(i, updatedQuestion)}
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.unique_code) + questionActive.value.uniqueCode,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(8f).fillMaxWidth()
            )
            Icon(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        listQuestions.value.forEach() { question ->
                            viewModel.addPerguntaToFirestore(question)
                        }
                        navController.navigate(Screens.ThankYou.toString()) {
                            popUpTo(Screens.ThankYou.toString()) {inclusive = true }
                        }
                    },
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(R.string.save),
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .weight(2f)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = questionActive.value.title,
                color = colorResource(R.color.black),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 3
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(5f)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(colorResource(R.color.white))
        ) {
            when (questionActive.value.questionType) {
                QuestionsType.TRUE_FALSE -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.Transparent)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "True",
                                modifier = Modifier.padding(end = 8.dp),
                            )
                            RadioButton(
                                selected = selectedOption.value == "True",
                                onClick = { selectedOption.value = "True" },
                            )
                            Spacer(modifier = Modifier.width(32.dp))
                            Text(
                                text = "False",
                                modifier = Modifier.padding(end = 8.dp),
                            )
                            RadioButton(
                                selected = selectedOption.value == "False",
                                onClick = { selectedOption.value = "False" },
                            )
                        }
                    }
                }
                QuestionsType.SINGLE_CHOICE -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.Transparent)
                    ) {
                        val options = questionActive.value.options ?: emptyList()
                        val (first, second) = options.chunked((options.size + 1) / 2)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column(
                                modifier = Modifier.weight(1f).padding(start = 32.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                first.forEach { option ->
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = selectedOption.value == option,
                                            onClick = { selectedOption.value = option.toString() }
                                        )
                                        Text(
                                            text = option.toString(),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                second.forEach { option ->
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                        RadioButton(
                                            selected = selectedOption.value == option,
                                            onClick = { selectedOption.value = option.toString() }
                                        )
                                        Text(
                                            text = option.toString(),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                QuestionsType.MULTIPLE_CHOICE -> {
                    val selectedOptions = remember { mutableStateListOf<String>() }
                    val options = questionActive.value.options ?: emptyList()
                    val (firstHalf, secondHalf) = options.chunked((options.size + 1) / 2)

                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            firstHalf.forEach { option ->
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Checkbox(
                                        checked = selectedOptions.contains(option),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) selectedOptions.add(option.toString())
                                            else selectedOptions.remove(option)
                                        }
                                    )
                                    Text(text = option.toString(), modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) { secondHalf.forEach { option ->
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Checkbox(
                                        checked = selectedOptions.contains(option),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) selectedOptions.add(option.toString())
                                            else selectedOptions.remove(option)
                                        }
                                    )
                                    Text(text = option.toString(), modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
                QuestionsType.MATCHING -> {
                    val evenOptions = remember { mutableListOf<String>() }
                    val oddOptions = remember { mutableListOf<String>() }

                    questionActive.value.options?.forEachIndexed { index, option ->
                        if (index % 2 == 0)  evenOptions.add(option.toString())
                         else oddOptions.add(option.toString())
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            evenOptions.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .border(1.dp, Color.Black)
                                        .padding(8.dp)
                                ) { Text(text = option) }
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            oddOptions.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .border(1.dp, Color.Black)
                                        .padding(8.dp)
                                ) { Text(text = option) }
                            }
                        }
                    }
                }
                QuestionsType.ORDERING -> {
                    val options = remember { mutableStateListOf<String>().apply {
                    addAll((questionActive.value.options ?: emptyList()) as Collection<String>) }}
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(8.dp).background(Color.Transparent),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        options.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "${index + 1}. $item", modifier = Modifier.weight(1f),)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (index > 0) {
                                                options[index] = options[index - 1].also { options[index - 1] = options[index] }
                                            }
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Move up",)
                                    }
                                    IconButton(
                                        onClick = {
                                            if (index < options.lastIndex) {
                                                options[index] = options[index + 1].also { options[index + 1] = options[index] }
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Move down",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                QuestionsType.FILL_BLANK -> {
                    var answer by remember { mutableStateOf(TextFieldValue()) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        TextField(
                            value = answer,
                            onValueChange = { answer = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text(stringResource(R.string.type_here))}
                        )
                    }
                }

                QuestionsType.ASSOCIATION -> {
                    val evenOptions = remember { mutableListOf<String>() }
                    val oddOptions = remember { mutableListOf<String>() }

                    questionActive.value.options?.forEachIndexed { index, option ->
                        if (index % 2 == 0)  evenOptions.add(option.toString())
                        else oddOptions.add(option.toString())
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            evenOptions.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .border(1.dp, Color.Black)
                                        .padding(8.dp)
                                ) { Text(text = option) }
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        ) {
                            oddOptions.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .border(1.dp, Color.Black)
                                        .padding(8.dp)
                                ) { Text(text = option) }
                            }
                        }
                    }
                }

                QuestionsType.WORD_INPUT -> {
                    var textvalue by remember { mutableStateOf(TextFieldValue()) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        TextField(
                            value = textvalue,
                            onValueChange = { textvalue = it },
                            modifier = Modifier .fillMaxWidth().padding(16.dp),
                            placeholder = { Text(stringResource(R.string.type_here)) }
                        )
                    }
                }

                QuestionsType.EMPTY -> { Row(modifier = Modifier.fillMaxWidth()) {} }
            }
        }
        Spacer(modifier = Modifier.weight(0.4f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .weight(0.7f)
                    .horizontalScroll(rememberScrollState())
            ) {
                if (listQuestions.value .isNotEmpty()) {
                    for (i in listQuestions.value .indices) {
                        AddQuestion(
                            questionNumber = i + 1,
                            isSelected = numQSelected.intValue == i,
                            onClick = {
                                questionActive.value = listQuestions.value [i]
                                numLastSelected.intValue = numQSelected.intValue
                                listQuestions.value[i].respostas?.add(selectedOption as Nothing)
                                updateQuestion (numLastSelected.intValue)
                                numQSelected.intValue = i
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddPairButton(
    onAddPair: () -> Unit,
    isSelected: Boolean
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .padding(6.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) colorResource(R.color.moonstoneQuizec) else Color.Gray,
                shape = RoundedCornerShape(50)
            )
            .background(
                color = if (isSelected) colorResource(R.color.moonstoneQuizec) else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
            .clickable { onAddPair() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Pair",
            tint = if (isSelected) Color.White else colorResource(R.color.black)
        )
    }
}

@Composable
fun AddQuestion(
    questionNumber: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxSize()
            .padding(8.dp)
            .border(
                width = 0.5.dp,
                color = if (isSelected) colorResource(R.color.moonstoneQuizec) else colorResource(R.color.black),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(R.drawable.logo_sem_texto),
            contentDescription = "Question Image",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight()
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .graphicsLayer { alpha = if (isSelected) 0.6f else 1f }
        )
        Text(
            text = "$questionNumber",
            fontSize = 15.sp,
            color = if (isSelected) colorResource(R.color.moonstoneQuizec) else colorResource(R.color.black),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp),
            fontWeight = FontWeight.ExtraBold
        )
    }
}

fun getRandomColor(): Color {
    val random = Random(System.currentTimeMillis())
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
}