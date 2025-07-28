package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R

@Composable
fun CreditsScreen(
    navController: NavHostController
) {
    Column(modifier = Modifier.background(Color.White)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isec),
                    contentDescription = "ISEC",
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1.5f))
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .weight(0.5f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = stringResource(R.string.informatics_engineering) + "\n" +
                       stringResource(R.string.mobile_applications) + "\n"+
                       "2024/2025",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = stringResource(R.string.project_realized_by) +
                        "\n\nDiogo Rafael Abrantes Oliveira - 2021146037" +
                        "\nLara Filipa da Silva Bizarro - 2021130066" +
                        "\nTomás Alexandre Dias Laranjeira - 2021135060",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}