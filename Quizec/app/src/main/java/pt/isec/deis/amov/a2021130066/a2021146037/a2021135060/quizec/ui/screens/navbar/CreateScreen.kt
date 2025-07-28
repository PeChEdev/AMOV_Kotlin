package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.Questionario
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.AMovServer
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FileUtils
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.FileUtils.Companion.generateAlphaNumericCode
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
) {
    val title = remember { mutableStateOf("") }
    val dataHoje = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)

    val quest by viewModel.newQuestinario.collectAsState()
    val questionarioInfo = remember { mutableStateOf( Questionario(
        id = generateAlphaNumericCode(),
        active = false,
        criadorPor = viewModel.user.value?.email,
        data =  dataHoje,
        imageUrl = null,
        nPerguntas = 0,
        titulo = "",
        respostas = null
    ))}

    val id = questionarioInfo.value.id
    val context = LocalContext.current
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

    Column (modifier = Modifier.background(Color.White)){
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End)
                    .weight(0.5f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.unique_code),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(2f).fillMaxWidth()
                )
                Text(
                    text = "${questionarioInfo.value.id}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(3f).fillMaxWidth()
                )
                Text(
                    text = "${questionarioInfo.value.data}",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(2f).fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = "Title",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            TextField(
                value = title.value,
                onValueChange = { newText -> title.value = newText },
                placeholder = { Text("Enter title", textAlign = TextAlign.Center, fontSize = 15.sp)},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .align(Alignment.CenterHorizontally),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = if (title.value.isNotEmpty()) Color.Transparent else Color.Red,
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .background(colorResource(R.color.def))
                    .background(colorResource(R.color.def))
                    .clickable {
                        //if (pictureUri.value.isEmpty()) {
                        pickImage.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                        //}
                    }
            ) {
                if (serverPictureUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = serverPictureUri.value),
                        contentDescription = "Question Image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Image",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                            .clickable {
                                if (serverPictureUri.value != null)
                                    AMovServer.asyncDeleteFileFromServer(
                                        serverPictureUri.value!!.substringAfterLast(
                                            "/"
                                        ), serverPictureUri.value!!
                                    ) {
                                        result -> if (result) serverPictureUri.value = null
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
                        contentDescription = "Question Image",
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove Image",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                            .clickable {
                                pictureUri.value = ""
                            }
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(50)
                            )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.tap_to_add_image),
                    )
                    Text(
                        text = stringResource(R.string.tap_to_add_image),
                        fontSize = 15.sp,
                        maxLines = 1,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = {
                    if (title.value.isNotEmpty()) {
                        questionarioInfo.value.titulo = title.value
                        questionarioInfo.value.imageUrl = pictureUri.value

                        quest.titulo = questionarioInfo.value.titulo
                        quest.imageUrl = questionarioInfo.value.imageUrl
                        quest.active = questionarioInfo.value.active
                        quest.criadorPor = questionarioInfo.value.criadorPor
                        quest.nPerguntas = questionarioInfo.value.nPerguntas
                        quest.data = questionarioInfo.value.data
                        quest.id = questionarioInfo.value.id
                        quest.respostas = questionarioInfo.value.respostas

                        //viewModel.addQuestionarioToFirestore(quest)

                        navController.navigate("${Screens.CreateSurvey}/$id") {
                            popUpTo(Screens.CreateSurvey.toString()) { inclusive = true }
                        }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (title.value.isEmpty())
                        colorResource(R.color.scarletQuizec)
                    else
                        colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp,
                            color = if (title.value.isEmpty()) colorResource(R.color.scarletQuizec)
                                    else colorResource(R.color.black)
                        ),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = "DONE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}