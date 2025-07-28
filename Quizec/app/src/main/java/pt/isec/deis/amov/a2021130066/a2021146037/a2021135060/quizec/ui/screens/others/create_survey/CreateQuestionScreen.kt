package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.create_survey

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.QuestionsType
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Question
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Questionario
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FileUtils
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.AMovServer
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FileUtils.Companion.generateAlphaNumericCode
import java.io.File

@Composable
fun CreateQuestionScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    idQuestionario : String
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val showPopup = remember { mutableStateOf(false) }
    val showMore = remember { mutableStateOf(false) }
    val showSave = remember { mutableStateOf(false) }

    val numQSelected = remember { mutableIntStateOf(0) }
    val lastSize = remember { mutableIntStateOf(0) }
    val numLastSelected = remember { mutableIntStateOf(0) }
    val listQuestions = remember { mutableStateOf<List<Question>>(emptyList()) }
    val questionActive = remember { mutableStateOf( Question(
        title = "",
        questionType = QuestionsType.EMPTY,
        options = null,
        serverPhotoUrl = null,
        uniqueCode = "",
        quemRespondeu = null,
        respostas = null,
        idQuestionario =idQuestionario,
    ))}

    //picture stuff
    val picture = MutableStateFlow("")
    val pictureUri = remember { mutableStateOf("") }
    val serverPictureUri = remember { mutableStateOf<String?>(null) }
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { urinotnullable ->
                if (serverPictureUri.value != null)
                    AMovServer.asyncDeleteFileFromServer(serverPictureUri.value!!.substringAfterLast("/"), serverPictureUri.value!!){ result ->
                        if (result)
                            serverPictureUri.value = null
                        else
                            println("Failed to Delete from server")
                    }
                picture.value = FileUtils.createFileFromUri(context, urinotnullable)
                pictureUri.value = picture.value
                AMovServer.asyncUploadImage(
                    context.contentResolver.openInputStream(urinotnullable)!!,
                    FileUtils.getFileExtension(context, urinotnullable) ?: "jpg"
                ) { url ->
                    serverPictureUri.value = url
                }
            }
        }
    )

    fun cleanQuestion(){
        questionActive.value.title = ""
        questionActive.value.options = null
        questionActive.value.serverPhotoUrl = null
        questionActive.value.uniqueCode =  generateAlphaNumericCode()
        questionActive.value.quemRespondeu = null
        questionActive.value.respostas = null
        questionActive.value.idQuestionario =idQuestionario
    }

    fun updateQuestion(i: Int) {
        if (i in listQuestions.value.indices) {
            val updatedQuestion = questionActive.value.copy()
            listQuestions.value = listQuestions.value.toMutableList().apply {set(i, updatedQuestion)}
        }
    }
    fun removeQuestion(i: Int) {
        if (i in listQuestions.value.indices)
            listQuestions.value = listQuestions.value.filterIndexed { j, _ -> j != i }

    }

    fun newQuestion(type: QuestionsType) {
        lastSize.intValue = listQuestions.value.size
        numLastSelected.intValue = numQSelected.intValue
        showPopup.value = false
        updateQuestion(numLastSelected.intValue - 1)
        cleanQuestion()
        questionActive.value.questionType = type
        val newQuestion = questionActive.value.copy()
        listQuestions.value = listQuestions.value.toMutableList().apply { add(newQuestion) }
        numQSelected.intValue = listQuestions.value.size
    }

    fun duplicateQuestion() {
        val currentQuestion = questionActive.value
        val newQuestion = currentQuestion.copy(uniqueCode = generateAlphaNumericCode())
        listQuestions.value = listQuestions.value.toMutableList().apply { add(newQuestion) }
        numQSelected.intValue = listQuestions.value.size - 1
        questionActive.value = currentQuestion
    }


    fun more(option:String) {
        showMore.value = false
        when (option) {
            "Duplicate" -> {
                duplicateQuestion()
            }
            "Delete" -> {removeQuestion(numQSelected.intValue-1)}
            "Duplicate to other survey" -> {//TODO
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .weight(1f)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if ( questionActive.value.questionType != QuestionsType.EMPTY ) {
                Text(
                    text = stringResource(R.string.unique_code) + questionActive.value.uniqueCode,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(8f)
                        .fillMaxWidth()
                )
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable {
                            showMore.value = true
                        },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.more),
                )
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable {
                            showSave.value = true

                            //Guarda para a base de dados
                            updateQuestion(numQSelected.intValue - 1)
                            listQuestions.value.forEach() { question ->
                                viewModel.addPerguntaToFirestore(question)
                            }
                            val quest = viewModel.getQuestionarioByID(idQuestionario)
                            quest.nPerguntas = listQuestions.value.size
                            viewModel.atualizaFireBase(quest)

                            navController.navigate("${Screens.SurveyQuestions}/$idQuestionario") {
                                popUpTo(Screens.Survey.toString()) { inclusive = true }
                            }
                        },
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.save),
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Box(
            modifier = Modifier
                .weight(3f)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(colorResource(R.color.def))
                .clickable {
                    pickImage.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
        ) {
            if ( questionActive.value.questionType != QuestionsType.EMPTY ) {
                if (serverPictureUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = serverPictureUri.value),
                        contentDescription = stringResource(R.string.question_image),
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.remove_image),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clickable {
                                if (serverPictureUri.value != null)
                                    AMovServer.asyncDeleteFileFromServer(
                                        serverPictureUri.value!!.substringAfterLast(
                                            "/"
                                        ), serverPictureUri.value!!
                                    ) { result ->
                                        if (result)
                                            serverPictureUri.value = null
                                    }
                                pictureUri.value = ""
                            }
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(50)
                            )
                    )
                } else if (pictureUri.value.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = File(pictureUri.value)),
                        contentDescription =  stringResource(R.string.question_image),
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription =  stringResource(R.string.remove_image),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                            .clickable { pictureUri.value = "" }
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(50)
                            )
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_image),
                        )
                        Text(
                            text = stringResource(R.string.tap_to_add_image),
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End)
                .weight(2f)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if ( questionActive.value.questionType != QuestionsType.EMPTY ) {
                TextField(
                    value = questionActive.value.title,
                    onValueChange = { new -> questionActive.value = questionActive.value.copy(title = new)  },
                    placeholder = {
                        Text(
                            text = if(questionActive.value.title == "") stringResource(R.string.enter_question)
                            else questionActive.value.title ,
                            color = colorResource(R.color.black),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 3
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(16.dp)
                        .border(
                            width = 0.dp,
                            color = Color.Transparent
                        ),
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = colorResource(R.color.black),
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                    ),
                    maxLines = 3
                )
            }
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
            when ( questionActive.value.questionType) {
                QuestionsType.TRUE_FALSE -> {
                    val buttonWidth = remember { mutableStateOf(0.dp) }
                    val density = LocalDensity.current
                    val options = listOf(stringResource(R.string.true_false), stringResource(R.string.yes_no))
                    val expanded = remember { mutableStateOf(false) }
                    val selected = remember { mutableStateOf(options[0])}

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = stringResource(R.string.choose_option),
                                modifier = Modifier.padding(end = 8.dp),
                                fontSize = 20.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Button(
                                onClick = { expanded.value = !expanded.value },
                                shape = RoundedCornerShape(5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(R.color.def),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.5f)
                                    .onGloballyPositioned { coordinates ->
                                        buttonWidth.value =
                                            with(density) { coordinates.size.width.toDp() }
                                    }
                            ) {
                                Text(
                                    text = selected.value,
                                    color = colorResource(R.color.black),
                                )
                            }
                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false },
                                modifier = Modifier
                                    .width(buttonWidth.value)
                                    .padding(0.dp)
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            selected.value = option
                                            if(selected.value == options[0])
                                                questionActive.value.options =  arrayListOf(
                                                    context.getString(R.string.trueo),
                                                    context.getString(R.string.falseo)
                                                )
                                            else
                                                questionActive.value.options = arrayListOf(
                                                    context.getString(R.string.no),
                                                    context.getString(R.string.yes)
                                                )
                                            expanded.value = false
                                        },
                                        text = {
                                            Text(
                                                text = option,
                                                color = colorResource(R.color.black),
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                QuestionsType.SINGLE_CHOICE -> {
                    val options = remember { mutableStateListOf<String>() }
                    var newOptionText by remember { mutableStateOf("") }
                    var showTextField by remember { mutableStateOf(false) }
                    var showWarning by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (options.size < 6) {
                            IconButton(
                                onClick = { showTextField = true },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_option),
                                    tint = colorResource(R.color.screamingGreenQuizec)
                                )
                            }
                        } else {
                            showWarning = true
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (showTextField && options.size < 6) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextField(
                                    value = newOptionText,
                                    onValueChange = {
                                        newOptionText = it
                                        questionActive.value.options = arrayListOf(options)
                                    },
                                    placeholder = { Text(stringResource(R.string.enter_new_option)) },
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = colorResource(R.color.def),
                                        unfocusedContainerColor = colorResource(R.color.def),
                                        focusedIndicatorColor = colorResource(R.color.def),
                                        unfocusedIndicatorColor = colorResource(R.color.def),
                                        cursorColor = colorResource(R.color.black),
                                        focusedTextColor = colorResource(R.color.black),
                                        unfocusedTextColor = colorResource(R.color.black),
                                    ),
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = {
                                        if (newOptionText.isNotBlank() && !options.contains(newOptionText.trim())) {
                                            options.add(newOptionText.trim())
                                            newOptionText = ""
                                            showTextField = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.add_option)
                                    )
                                }
                            }
                        }
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 15.sp
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = { options.remove(option) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove option"
                                    )
                                }
                            }
                        }
                        if (showWarning) {
                            Text(
                                text = (stringResource(R.string.you_can_only_have_6_options)),
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                }
                QuestionsType.MULTIPLE_CHOICE -> {
                    val options = remember { mutableStateListOf<String>() }
                    var newOptionText by remember { mutableStateOf("") }
                    var showTextField by remember { mutableStateOf(false) }
                    var showWarning by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (options.size < 6) {
                            IconButton(
                                onClick = { showTextField = true },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_option),
                                    tint = colorResource(R.color.screamingGreenQuizec)
                                )
                            }
                        }else{
                            showWarning = true
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (showTextField && !showWarning) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextField(
                                    value = newOptionText,
                                    onValueChange = {
                                        newOptionText = it
                                        questionActive.value.options = arrayListOf(options)
                                    },
                                    placeholder = { Text(stringResource(R.string.enter_new_option)) },
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = colorResource(R.color.def),
                                        unfocusedContainerColor = colorResource(R.color.def),
                                        focusedIndicatorColor = colorResource(R.color.def),
                                        unfocusedIndicatorColor = colorResource(R.color.def),
                                        cursorColor = colorResource(R.color.black),
                                        focusedTextColor = colorResource(R.color.black),
                                        unfocusedTextColor = colorResource(R.color.black),
                                    ),
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = {
                                        if (newOptionText.isNotBlank() && !options.contains(newOptionText.trim())) {
                                            options.add(newOptionText.trim())
                                            newOptionText = ""
                                            showTextField = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.add_option)
                                    )
                                }
                            }
                        }
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = option,
                                    fontSize = 15.sp
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = { options.remove(option) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove option"
                                    )
                                }
                            }
                        }
                        if (showWarning) {
                            Text(
                                text = (stringResource(R.string.you_can_only_have_6_options)),
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                QuestionsType.MATCHING -> {
                    val firstColumnItems = remember { mutableStateListOf<String>() }
                    val secondColumnItems = remember { mutableStateListOf<String>() }
                    var newItemText by remember { mutableStateOf("") }
                    var currentColumn by remember { mutableStateOf<Int?>(null) }
                    var showDialog by remember { mutableStateOf(false) }
                    var showWarning by remember { mutableStateOf(false) }
                    val combinedItems = mutableListOf<String>()
                    val maxSize = maxOf(firstColumnItems.size, secondColumnItems.size)
                    for (i in 0 until maxSize) {
                        if (i < firstColumnItems.size) combinedItems.add(firstColumnItems[i])
                        if (i < secondColumnItems.size) combinedItems.add(secondColumnItems[i])
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    stringResource(R.string.first_column),
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                firstColumnItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 0.5.dp,
                                                color = colorResource(R.color.screamingGreenQuizec),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(start = 8.dp, end = 8.dp)
                                            .background(Color.White)
                                    ) {
                                        Text(
                                            item,
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                if (firstColumnItems.size < 6) {
                                    IconButton(
                                        onClick = {
                                            currentColumn = 1
                                            showDialog = true
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = stringResource(R.string.add_option),
                                            tint = colorResource(R.color.screamingGreenQuizec)
                                        )
                                    }
                                } else {
                                    showWarning = true
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                            ) {
                                Text(
                                    "Second Column",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                secondColumnItems.forEach { item ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(
                                                width = 0.5.dp,
                                                color = colorResource(R.color.screamingGreenQuizec),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(start = 8.dp, end = 8.dp)
                                            .background(Color.White)
                                    ) {
                                        Text(
                                            item,
                                            modifier = Modifier.align(Alignment.CenterStart),
                                            fontSize = 15.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                if (secondColumnItems.size < 6) {
                                    IconButton(
                                        onClick = {
                                            currentColumn = 2
                                            showDialog = true
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = stringResource(R.string.add_option),
                                            tint = colorResource(R.color.screamingGreenQuizec)
                                        )
                                    }
                                } else {
                                    showWarning = true
                                }
                            }
                        }
                    }
                    if (showDialog && !showWarning) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = {
                                Text(text = "Enter new item")
                            },
                            text = {
                                TextField(
                                    value = newItemText,
                                    onValueChange = {
                                        newItemText = it
                                        questionActive.value.options = arrayListOf(combinedItems)
                                    },
                                    placeholder = { Text("Enter new item") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (newItemText.isNotBlank()) {
                                            if (currentColumn == 1) {
                                                firstColumnItems.add(newItemText.trim())
                                            } else if (currentColumn == 2) {
                                                secondColumnItems.add(newItemText.trim())
                                            }
                                            newItemText = ""
                                            showDialog = false
                                        }
                                    }
                                ) {
                                    Text("Add Item")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                    if (showWarning) {
                        Text(
                            text = (stringResource(R.string.you_can_only_have_6_options)),
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }

                QuestionsType.ORDERING -> {
                    val options = remember { mutableStateListOf<String>() }
                    var newOptionText by remember { mutableStateOf("") }
                    var showTextField by remember { mutableStateOf(false) }
                    var showWarning by remember { mutableStateOf(false)}

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (options.size < 6) {
                            IconButton(
                                onClick = { showTextField = true },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_option),
                                    tint = colorResource(R.color.screamingGreenQuizec)
                                )
                            }
                        } else {
                            showWarning = true
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (showTextField && !showWarning) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .wrapContentHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextField(
                                    value = newOptionText,
                                    onValueChange = { newOptionText = it
                                        questionActive.value.options =  arrayListOf(options )
                                                    },
                                    placeholder = { Text(stringResource(R.string.enter_new_option)) },
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = colorResource(R.color.def),
                                        unfocusedContainerColor = colorResource(R.color.def),
                                        focusedIndicatorColor = colorResource(R.color.def),
                                        unfocusedIndicatorColor = colorResource(R.color.def),
                                        cursorColor = colorResource(R.color.black),
                                        focusedTextColor = colorResource(R.color.black),
                                        unfocusedTextColor = colorResource(R.color.black),
                                    ),
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = {
                                        if (newOptionText.isNotBlank() && !options.contains(newOptionText.trim())) {
                                            options.add(newOptionText.trim())
                                            newOptionText = ""
                                            showTextField = false
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = stringResource(R.string.add_option)
                                    )
                                }
                            }
                        }
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = option,
                                    fontSize = 15.sp
                                )
                                IconButton(
                                    modifier = Modifier.size(20.dp),
                                    onClick = { options.remove(option) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove option"
                                    )
                                }
                            }
                        }
                        if (showWarning) {
                            Text(
                                text = (stringResource(R.string.you_can_only_have_6_options)),
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
                QuestionsType.FILL_BLANK -> {
                    var answer by remember { mutableStateOf(TextFieldValue()) }
                    var showDialog by remember { mutableStateOf(false) }
                    var isSpaceAdded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {
                        TextField(
                            value = answer,
                            onValueChange = { answer = it
                                questionActive.value.options =  arrayListOf(answer)
                                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            placeholder = { Text(stringResource(R.string.enter_question)) }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Add a space")
                        }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = { Text("Confirm Space Addition") },
                                text = { Text("Do you want to add a blank space?") },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            answer = TextFieldValue(answer.text + "_")
                                            isSpaceAdded = true
                                            showDialog = false
                                        }
                                    ) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    Button(
                                        onClick = { showDialog = false }
                                    ) {
                                        Text("No")
                                    }
                                }
                            )
                        }

                        if (isSpaceAdded) {
                            Text(
                                text = "Space added!",
                                color = colorResource(R.color.moonstoneQuizec),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
                QuestionsType.ASSOCIATION -> {
                    val firstColumnItems = remember { mutableStateListOf<String>() }
                    val secondColumnItems = remember { mutableStateListOf<String>() }
                    var newItemText by remember { mutableStateOf("") }
                    var currentColumn by remember { mutableStateOf<Int?>(null) }
                    var showDialog by remember { mutableStateOf(false) }
                    var showImageDialog by remember { mutableStateOf(false) }


                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(16.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                ) {
                                    Text(
                                        stringResource(R.string.first_column),
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    firstColumnItems.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 0.5.dp,
                                                    color = colorResource(R.color.screamingGreenQuizec),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(start = 8.dp, end = 8.dp)
                                                .background(Color.White)
                                        ) {
                                            Text(
                                                item,
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                fontSize = 15.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            currentColumn = 1
                                            showDialog = true
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = stringResource(R.string.add_option),
                                            tint = colorResource(R.color.screamingGreenQuizec)
                                        )
                                    }

                                    if (showImageDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showImageDialog = false },
                                            title = { Text(stringResource(R.string.add_an_image)) },
                                            text = { Text(stringResource(R.string.do_you_want_to_add_an_image)) },
                                            confirmButton = {
                                                Button(
                                                    onClick = {
                                                        //  selectedImage = ImageBitmap.imageResource(id = R.drawable.ic_sample_image) // Replace with actual image resource or picker
                                                        firstColumnItems.add("Image")

                                                        showImageDialog = false
                                                    }
                                                ) {
                                                    Text("Yes")
                                                }
                                            },
                                            dismissButton = {
                                                Button(
                                                    onClick = { showImageDialog = false }
                                                ) {
                                                    Text("No")
                                                }
                                            }
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                ) {
                                    Text(
                                        "Second Column",
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    secondColumnItems.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 0.5.dp,
                                                    color = colorResource(R.color.screamingGreenQuizec),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(start = 8.dp, end = 8.dp)
                                                .background(Color.White)
                                        ) {
                                            Text(
                                                item,
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                fontSize = 15.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            currentColumn = 2
                                            showDialog = true
                                        },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = stringResource(R.string.add_option),
                                            tint = colorResource(R.color.screamingGreenQuizec)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            if (currentColumn != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextField(
                                        value = newItemText,
                                        onValueChange = { newItemText = it },
                                        placeholder = { Text("Enter new item") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = {
                                            if (newItemText.isNotBlank()) {
                                                if (currentColumn == 1) {
                                                    firstColumnItems.add(newItemText.trim())
                                                } else if (currentColumn == 2) {
                                                    secondColumnItems.add(newItemText.trim())
                                                }
                                                newItemText = ""
                                                currentColumn = null
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Add item"
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            /*    selectedImage?.let {
                                    Image(
                                        bitmap = it,
                                        contentDescription = "Added Image",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(top = 16.dp)
                                    )
                                }*/
                        }
                    }
                }


                QuestionsType.WORD_INPUT -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                    ) {}
                }
                QuestionsType.EMPTY -> {
                    Row(modifier = Modifier.fillMaxWidth()) {}
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.4f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            Row(
                modifier = Modifier
                    .weight(1.2f)
                    .horizontalScroll(rememberScrollState())
            ) {
                if (listQuestions.value.isNotEmpty())
                    for (i in 1..listQuestions.value.size) {
                        AddQuestion(
                            questionNumber = i,
                            imgRes = serverPictureUri.value ?: R.drawable.logo_sem_texto,
                            isSelected = numQSelected.intValue == i,
                            onClick = {
                                if(lastSize.intValue < listQuestions.value.size) {
                                    lastSize.intValue = listQuestions.value.size
                                    numLastSelected.intValue = numQSelected.intValue
                                }
                                if(lastSize.intValue == listQuestions.value.size)
                                    numLastSelected.intValue = numQSelected.intValue
                                updateQuestion(numLastSelected.intValue - 1)
                                numQSelected.intValue = i
                                questionActive.value = listQuestions.value[i-1]

                                if (serverPictureUri.value != null) pictureUri.value = ""
                                else pictureUri.value = serverPictureUri.value ?: ""
                            }
                        )
                    }

            }
            Button(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Transparent),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.moonstoneQuizec)
                ),
                onClick = {
                    showPopup.value = true
                    Log.d("Question", "Numero Pergunta: $numQSelected.intValue")
                },
            ) {
                Text(
                    text = "+",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    if (showMore.value) {
        Dialog(
            onDismissRequest = { showMore.value = false },
            properties = DialogProperties( usePlatformDefaultWidth = false ),
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.duplicate),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .clickable {
                                more("Duplicate")
                            },
                        fontSize = 15.sp,
                    )
                    Text(
                        text = stringResource(R.string.delete),
                        modifier = Modifier
                            .clickable {
                                more("Delete")
                            },
                        fontSize = 15.sp,
                    )

                }
            }
        }
    }
    if (showPopup.value) {
        Dialog(
            onDismissRequest = { showPopup.value = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = stringResource(R.string.select_question_type),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        ) {
                            QuestionsType.entries
                                .take(4)
                                .forEach { questionType ->
                                    Text(
                                        text = questionType.displayName,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                newQuestion(questionType)
                                            },
                                        fontSize = 11.sp
                                    )
                                }
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            QuestionsType.entries
                                .filter { it != QuestionsType.EMPTY }
                                .drop(4)
                                .forEach { questionType ->
                                    Text(
                                        text = questionType.displayName,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                newQuestion(questionType)
                                            },
                                        fontSize = 11.sp
                                    )
                                }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AddQuestion(
    questionNumber: Int,
    imgRes: Any,
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
        when (imgRes) {
            is String ->
                Image(
                    painter = rememberAsyncImagePainter(model = imgRes),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .graphicsLayer { alpha = if (isSelected) 0.6f else 1f }
                )

            is Int ->
                Image(
                    painter = painterResource(id = imgRes),
                    contentDescription = "Question Image",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .graphicsLayer { alpha = if (isSelected) 0.6f else 1f }
                )
        }
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