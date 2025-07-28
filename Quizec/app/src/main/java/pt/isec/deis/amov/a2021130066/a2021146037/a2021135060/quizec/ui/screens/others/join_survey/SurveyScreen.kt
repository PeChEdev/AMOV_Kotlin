package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import java.io.File

@Composable
fun Survey(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
    id: String
) {
    val questionarioInfo = viewModel.getQuestionarioByID(id)

    Column(modifier = Modifier.background(colorResource(R.color.white))) {
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
                    text = id,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth()
                )
                Text(
                    text = "${questionarioInfo.data}",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = "${questionarioInfo.titulo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .background(colorResource(R.color.white))
            ) {
                val imagePath = questionarioInfo.imageUrl
                val imageFile = imagePath?.let { File(it) }

                if (imageFile != null) {
                    if (imageFile.exists()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = imageFile),
                            contentDescription = stringResource(R.string.survey_image),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(text = stringResource(R.string.cant_image))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = {
                    navController.navigate("${Screens.SurveyQuestions}/$id") {
                        popUpTo(Screens.Survey.toString()) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp, color = colorResource(R.color.black)),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.start),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
