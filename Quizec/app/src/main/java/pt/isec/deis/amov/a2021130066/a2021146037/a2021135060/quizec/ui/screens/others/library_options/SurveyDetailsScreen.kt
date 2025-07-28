package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun SurveyDetailsScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController,
) {
    var showConfirmationDialog by remember { mutableStateOf(false) }
    val infoQuest by viewModel.questinarioOnWatch.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
            .padding(8.dp)
    )  {
        IconButton(
            onClick = {
                navController.navigate(Screens.Library.toString()) {
                    popUpTo(Screens.Library.toString()) { inclusive = true }
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
                contentDescription = "back",
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(2f)
        )
        {
            Text(
                text = "${infoQuest.titulo}",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Perguntas: ${infoQuest.nPerguntas}"
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            Text(
                text = "${infoQuest.data}",
                fontSize = 15.sp,
            )
            Text(
                text =  "${infoQuest.id}",
                fontSize = 15.sp,
            )
            IconButton(
                onClick = {
                    showConfirmationDialog = true
                },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Button(
                onClick = {
                    navController.navigate(Screens.HostSurvey.toString()) {
                        popUpTo(Screens.HostSurvey.toString()) {inclusive = true }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.greenYellowQuizec),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(width = 150.dp, height = 60.dp)
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp, color = colorResource(R.color.greenYellowQuizec)),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = "HOST",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                onClick = {
                   //TODO
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .size(width = 150.dp, height = 60.dp)
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp, color = colorResource(R.color.black)),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.edit),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
    if (showConfirmationDialog) {
        ConfirmationDialog(
            actionDescription = "Are you sure you want to delete this?\n This action cannot be undone." ,
            onConfirm = {
                showConfirmationDialog = false
                //infoQuest.id?.let { viewModel.removeQuestionarioFromFirestore(it) } //TODO
            },
            onDismiss = { showConfirmationDialog = false }
        )
    }
}


@Composable
fun ConfirmationDialog(
    actionDescription : String,
    onConfirm : () -> Unit,
    onDismiss : () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                Icons.Default.Warning,
                contentDescription = stringResource(R.string.confirmation_dialog)
            )
        },
        title = { Text(stringResource(R.string.confirm_operation)) },
        text = { Text(actionDescription, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)},
        onDismissRequest = onDismiss,
        confirmButton = { Button(onClick = onConfirm) { Text(stringResource(R.string.confirm))} },
        dismissButton = { Button(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } }
    )
}
