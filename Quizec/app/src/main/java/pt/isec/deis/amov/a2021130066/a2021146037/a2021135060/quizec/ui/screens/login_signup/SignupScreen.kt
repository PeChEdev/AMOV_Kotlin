package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.login_signup

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel

@Composable
fun SignupScreen(
    viewModel: FirebaseViewModel,
    onSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
){
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            SignupPortraitScreen(
                viewModel = viewModel,
                onSuccess = onSuccess,
                onNavigateToLogin = onNavigateToLogin,
                modifier = modifier
            )
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            SignupLandscapeScreen(
                viewModel = viewModel,
                onSuccess = onSuccess,
                onNavigateToLogin = onNavigateToLogin,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPortraitScreen(
    viewModel: FirebaseViewModel,
    onSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
){
    val username = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}
    LaunchedEffect(viewModel.user.value) {
        if (viewModel.user.value != null && viewModel.error.value == null) {
            onSuccess()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = modifier.fillMaxWidth(),
            model = R.drawable.design_login_signup,
            contentDescription = "Quizec Logo"
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Top ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text =stringResource(R.string.text_signupscreen),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.username),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .width(300.dp),
            )
            OutlinedTextField(
                value = username.value,
                onValueChange = {username.value = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .width ( 300.dp)
                    .weight(1f),
                textStyle = TextStyle(fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = stringResource(R.string.password),
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .width(300.dp),
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = {password.value = it},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .width ( 300.dp)
                    .weight(1f),
                textStyle = TextStyle(fontSize = 10.sp)
            )
            Spacer(modifier = Modifier.weight(0.5f))
            Button(
                onClick = {
                    viewModel.createUserWithEmail(username.value, password.value)
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width (320.dp)
                    .weight(1.4f)
                    .padding(8.dp)
                    .border(
                        BorderStroke(2.dp, color = colorResource(R.color.black)),
                        shape = RoundedCornerShape(5.dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.button_signup),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (viewModel.error.value != null) {
                Text(
                    text="Error: ${viewModel.error.value}",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(3f)
                        .padding(16.dp),
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            else
                Spacer(modifier = Modifier.weight(3f))

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text =stringResource(R.string.text_login),
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(1.dp))
                TextButton(
                    onClick = {onNavigateToLogin()},
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(
                        text = stringResource(R.string.button_login),
                        fontSize = 20.sp,
                        color = colorResource(R.color.mintQuizec),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SignupLandscapeScreen(
    viewModel: FirebaseViewModel,
    onSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.text_signupscreen),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.username),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = remember { mutableStateOf("") }.value,
                onValueChange = {},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 10.sp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.password),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = remember { mutableStateOf("") }.value,
                onValueChange = {},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 10.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.createUserWithEmail("", "") },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.black),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_signup),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_login),
                    fontSize = 18.sp,
                    color = colorResource(R.color.mintQuizec),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}