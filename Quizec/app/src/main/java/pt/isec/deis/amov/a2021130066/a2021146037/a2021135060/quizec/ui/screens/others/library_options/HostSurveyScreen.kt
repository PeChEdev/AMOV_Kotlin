package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.QuizecViewModel

@Composable
fun HostSurveyScreen(
    viewModel: FirebaseViewModel,
    quizecViewModel: QuizecViewModel,
    navController: NavHostController,
){
    val buttonWidth = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val time = remember { mutableIntStateOf(0) }
    val infoQuest by viewModel.questinarioOnWatch.collectAsState()
    val options = listOf(stringResource(R.string.yes), stringResource(R.string.no))
    val expandedAccess = remember { mutableStateOf(false) }
    val expandedGeo = remember { mutableStateOf(false) }
    val expandedResult = remember { mutableStateOf(false) }
    val selectedAccess = remember { mutableStateOf(options[0]) }
    val selectedGeo = remember { mutableStateOf(options[0]) }
    val selectedResult = remember { mutableStateOf(options[0]) }

    LaunchedEffect(Unit) {
        quizecViewModel.startLocationUpdates()
    }

    val currentLocation = quizecViewModel.currentLocation.value
    /*LaunchedEffect(currentLocation) {
        if(currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0)
        {
            selectedGeo.value = "Lat: ${currentLocation.latitude}, Long: ${currentLocation.longitude}"
        }
    }*/

    Column (modifier = Modifier.background(Color.White)){
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
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
                    .weight(0.5f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = stringResource(R.string.answering_time_limit),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            TextField(
                value = time.intValue.toString(),
                onValueChange = { newText ->
                    if (newText.isNotEmpty())
                        if (newText.isDigitsOnly())
                            time.intValue = newText.toInt()
                        else
                            time.intValue = 0 },
                placeholder = { Text(text = stringResource(R.string.enter_time_in_minutes), textAlign = TextAlign.Center, fontSize = 15.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .align(Alignment.CenterHorizontally),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.def),
                    unfocusedContainerColor = colorResource(R.color.def),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = if (time.intValue <= 0) Color.Transparent else Color.Red,
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = stringResource(R.string.immediate_access),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
            ) {
                Button(
                    onClick = { expandedAccess.value = !expandedAccess.value },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.def),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .onGloballyPositioned { coordinates ->
                            buttonWidth.value = with(density) { coordinates.size.width.toDp() }
                        }
                ) {
                    Text(
                        text = selectedAccess.value,
                        color = colorResource(R.color.black),
                    )
                }
                DropdownMenu(
                    expanded = expandedAccess.value,
                    onDismissRequest = { expandedAccess.value = false },
                    modifier = Modifier
                        .width(buttonWidth.value)
                        .padding(0.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedAccess.value = option
                                expandedAccess.value = false
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
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = stringResource(R.string.restrict_access_geographically),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
            ) {
                Button(
                    onClick = { expandedGeo.value = !expandedGeo.value },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.def),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .onGloballyPositioned { coordinates ->
                            buttonWidth.value = with(density) {
                                coordinates.size.width.toDp()
                            }
                        }
                ) {
                    Text(
                        text = selectedGeo.value,
                        color = colorResource(R.color.black),
                    )
                }
                DropdownMenu(
                    expanded = expandedGeo.value,
                    onDismissRequest = { expandedGeo.value = false },
                    modifier = Modifier
                        .width(buttonWidth.value)
                        .padding(0.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedGeo.value = option
                                expandedGeo.value = false
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
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = stringResource(R.string.show_results_immediately),
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
            ) {
                Button(
                    onClick = { expandedResult.value = !expandedResult.value },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.def),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.2f)
                        .onGloballyPositioned { coordinates ->
                            buttonWidth.value = with(density) {
                                coordinates.size.width.toDp()
                            }
                        }
                ) {
                    Text(
                        text = selectedResult.value,
                        color = colorResource(R.color.black),
                    )
                }
                DropdownMenu(
                    expanded = expandedResult.value,
                    onDismissRequest = { expandedResult.value = false },
                    modifier = Modifier
                        .width(buttonWidth.value)
                        .padding(0.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedResult.value = option
                                expandedResult.value = false
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
            Spacer(modifier = Modifier.weight(0.8f))
            Button(
                onClick = {
                    //update active survey
                    //activate location
                    //acesso imediato aos resultados
                    //tempo limite qnd passar o tempo desativar
                    //TODO
                    if(selectedAccess.value == expandedAccess.value.toString())
                        infoQuest.active = true
                    if (currentLocation.latitude != 0.0 && currentLocation.longitude != 0.0) {
                        val latitude = currentLocation.latitude
                        val longitude = currentLocation.longitude
                        infoQuest.location = "$latitude,$longitude" }
                    viewModel.atualizaFireBase(infoQuest)
                    viewModel.timetoAdd.value = time.intValue
                    navController.navigate("${Screens.WaitHost}/${infoQuest.id}") {
                        popUpTo(Screens.WaitHost.toString()) {inclusive = true }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (time.intValue <= 0) colorResource(R.color.scarletQuizec)
                    else colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.3f)
                    .border(
                        BorderStroke(
                            2.dp,
                            color = if (time.intValue <= 0) colorResource(R.color.scarletQuizec)
                            else colorResource(R.color.black)
                        ),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.done),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}