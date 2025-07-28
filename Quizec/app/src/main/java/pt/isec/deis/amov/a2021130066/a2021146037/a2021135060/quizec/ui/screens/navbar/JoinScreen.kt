package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar

import android.location.Location
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations.LocationUtil

@Composable
fun JoinScreen(
    viewModel: FirebaseViewModel,
    quizecViewModel: QuizecViewModel,
    navController: NavHostController,
) {
    val pin = remember { mutableStateOf("")}
    val focusRequester = remember { FocusRequester() }
    val error = remember { mutableStateOf(false) }
    val msgerror = remember { mutableStateOf(" ") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    fun validaPin(): String?{
        if(pin.value.isEmpty()) return context.getString(R.string.pin_is_empty)
        if(pin.value.length < 6) return context.getString(R.string.pin_is_too_small)

        val questionario = viewModel.getQuestionarioByID(pin.value)
        if (questionario.id == null) return context.getString(R.string.incorrect_code_there_s_no_such_survey)
        if(questionario.active == false ) return context.getString(R.string.the_survey_is_not_active_try_later)
        if(viewModel.user.value?.let { questionario.respostas?.contains(it.email) } == true) return context.getString(R.string.you_already_answered_this_survey)

        if (questionario.location != null && questionario.location!!.isNotBlank()) {
            val locationUtil = quizecViewModel.locationHandler as LocationUtil
            var userLocation: Location? = null

            locationUtil.onLocation = { location -> userLocation = location }
            locationUtil.startLocationUpdates()
            Thread.sleep(3000)
            locationUtil.stopLocationUpdates()

            userLocation?.let { loc ->
                val (targetLat, targetLon) = questionario.location!!.split(",").map { it.toDouble() }
                val isNearby = locationUtil.isWithinBounds(
                    loc.latitude,
                    loc.longitude,
                    targetLat,
                    targetLon,
                    maxDistance = 100.0
                )
                if (!isNearby) {
                    return@let context.getString(R.string.you_are_not_near_the_survey_location)
                } else {
                    Log.i("Teste", "Localização do utilizador: $loc")
                }
            } ?: return context.getString(R.string.unable_to_get_location)
        }

        viewModel.user.value?.let { questionario.respostas?.add(it.email) }
        viewModel.atualizaFireBase(questionario)
        return null
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .background(
                colorResource(R.color.moonstoneQuizec)
            )
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = pin.value,
            onValueChange = { newValue ->
                if (newValue.length <= 6)
                    pin.value = newValue
            },
            placeholder = {
                Text(
                    text = "ENTER CODE",
                    color = colorResource(R.color.white),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .background(colorResource(R.color.moonstoneQuizec))
                .width(300.dp)
                .border(
                    width = 0.dp,
                    color = Color.Transparent
                )
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                fontSize = 25.sp,
                lineHeight = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.moonstoneQuizec),
                unfocusedContainerColor = colorResource(R.color.moonstoneQuizec),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = colorResource(R.color.white),
                focusedTextColor =  colorResource(R.color.white),
                unfocusedTextColor =  colorResource(R.color.white),
            )
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Button(
            onClick = { keyboardController?.hide()
                val errorMessage = validaPin()
                if (errorMessage != null) {
                    msgerror.value = errorMessage
                    error.value = true
                } else {
                    error.value = false
                    navController.navigate("${Screens.Survey}/${pin.value}") {
                        popUpTo(Screens.Survey.toString()) { inclusive = true }
                    }
                }
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.black),
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(300.dp)
                .height(90.dp)
                .padding(8.dp)
                .border(
                    BorderStroke(2.dp, color = colorResource(R.color.black)),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            Text(
                text = stringResource(R.string.done),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if(error.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(colorResource(R.color.scarletQuizec)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = msgerror.value,
                    fontSize = 15.sp,
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}