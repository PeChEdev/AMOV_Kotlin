package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun AnswerScreen(
    viewModel: FirebaseViewModel,
    modifier: Modifier = Modifier
) {

    Column( modifier = Modifier.background(Color.White) ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // questionarios.forEach { questionario ->
            item {
                //  AnswerCard(questionario)
                AnswerCard()
                Spacer(modifier = Modifier.height(3.dp))
            }
            //}
        }
    }

}
@Composable
fun AnswerCard(
    //questionario,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "TITULO",
                    fontWeight = FontWeight.Bold,
                    //text = questionario.nome,
                    color = colorResource(R.color.black)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "12/02/2024",
                    //text = "Data: ${questionario.data}",
                    color = colorResource(R.color.black)
                )
            }
        }
    }
}