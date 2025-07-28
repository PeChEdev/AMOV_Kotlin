package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.Screens

@Composable
fun ThanksScreen(
    navController: NavHostController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.mintQuizec))
            .padding(8.dp)
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screens.Join.toString()) {
                    popUpTo(Screens.Join.toString()) { inclusive = true }
                }
            },
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.End)
                .padding(top = 16.dp, end = 16.dp)
                .weight(0.5f)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "back",
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.weight(3.5f))
        Text(
            text = stringResource(R.string.thank_you_for_answering),
            color = colorResource(R.color.white),
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(0.5f))
    }
}