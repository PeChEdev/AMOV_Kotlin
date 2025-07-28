package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.R
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.AnswerScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.CreateScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.JoinScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.LibraryScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.Statistics
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.navbar.StatisticsScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.create_survey.CreateQuestionScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey.Survey
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey.SurveyQuestionsScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.join_survey.ThanksScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options.HostSurveyScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options.SurveyDetailsScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.others.library_options.WaitHostScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.QuizecViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: FirebaseViewModel,
    quizecViewModel: QuizecViewModel,
    onSignOut: () -> Unit,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val currentScreen by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors( containerColor = colorResource(R.color.def)),
                title = {
                    Text(text = "Quizec App")
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screens.Credits.toString()) {
                                popUpTo(Screens.Credits.toString()) {inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.info),
                            contentDescription = stringResource(R.string.credits),
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    IconButton(
                        onClick = { onSignOut()}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
               actions = {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Create.toString()) {
                                    popUpTo(Screens.Create.toString()) {inclusive = true }
                                }
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = Screens.Create.toString(),
                                tint =  if (currentScreen?.destination?.route == Screens.Create.toString()) colorResource(R.color.moonstoneQuizec)
                                        else Color.Black
                            )
                        }
                        Text(
                            text = "Create",
                            color = if (currentScreen?.destination?.route == Screens.Create.toString()) colorResource(R.color.moonstoneQuizec)
                                    else Color.Black,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.offset(y = (-5).dp)
                        )
                    }
                   Spacer(modifier = Modifier.weight(1f))
                   Column(
                       horizontalAlignment = Alignment.CenterHorizontally,
                       modifier = Modifier.wrapContentSize()
                   ) {
                       IconButton(
                           onClick = {
                               navController.navigate(Screens.Library.toString()) {
                                   popUpTo(Screens.Library.toString()) {inclusive = true }
                               }
                           },
                           modifier = Modifier.size(30.dp)
                       ) {
                           Icon(
                               painter = painterResource(id = R.drawable.form_icon),
                               contentDescription = Screens.Library.toString(),
                               tint =  if (currentScreen?.destination?.route == Screens.Library.toString()) colorResource(R.color.moonstoneQuizec)
                                       else Color.Black,
                               modifier = Modifier.size(30.dp)
                           )
                       }
                       Text(
                           text = "Library",
                           color = if (currentScreen?.destination?.route == Screens.Library.toString()) colorResource(R.color.moonstoneQuizec)
                                   else Color.Black,
                           fontSize = 10.sp,
                           maxLines = 1,
                           modifier = Modifier.offset(y = (-5).dp)
                       )
                   }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Join.toString()) {
                                    popUpTo(Screens.Join.toString()) { inclusive = true }
                                }
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.logo_sem_texto),
                                contentDescription = Screens.Join.toString(),
                                tint =  if (currentScreen?.destination?.route == Screens.Join.toString()) colorResource(R.color.moonstoneQuizec)
                                        else Color.Unspecified,
                            )
                        }
                        Text(
                            text = stringResource(R.string.join),
                            color = if (currentScreen?.destination?.route == Screens.Join.toString()) colorResource(R.color.moonstoneQuizec)
                                    else Color.Black,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.offset(y = (-5).dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.Answer.toString()) {
                                    popUpTo(Screens.Answer.toString()) { inclusive = true }
                                }
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.answers_icon),
                                contentDescription = Screens.Answer.toString(),
                                tint =  if (currentScreen?.destination?.route == Screens.Answer.toString()) colorResource(R.color.moonstoneQuizec)
                                        else Color.Black,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.answers),
                            color = if (currentScreen?.destination?.route == Screens.Answer.toString()) colorResource(R.color.moonstoneQuizec)
                                    else Color.Black,
                            fontSize = 10.sp,
                            maxLines = 1,
                            modifier = Modifier.offset(y = (-5).dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                       horizontalAlignment = Alignment.CenterHorizontally,
                       modifier = Modifier.wrapContentSize()
                    ) {
                       IconButton(
                           onClick = {
                               navController.navigate(Screens.Statistics.toString()) {
                                   popUpTo(Screens.Statistics.toString()) { inclusive = true }
                               }
                           },
                           modifier = Modifier.size(30.dp)
                       ) {
                           Icon(
                               painter = painterResource(id = R.drawable.statics),
                               contentDescription = Screens.Statistics.toString(),
                               tint =  if (currentScreen?.destination?.route == Screens.Statistics.toString()) colorResource(R.color.moonstoneQuizec)
                                       else Color.Black
                           )
                       }
                       Text(
                           text = "Statistics",
                           color = if (currentScreen?.destination?.route == Screens.Statistics.toString()) colorResource(R.color.moonstoneQuizec)
                                   else Color.Black,
                           fontSize = 10.sp,
                           maxLines = 1,
                           modifier = Modifier.offset(y = (-5).dp)
                       )
                    }
                   Spacer(modifier = Modifier.weight(1f))
                },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Create.toString(),
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Create.toString()) {
                CreateScreen(viewModel, navController = navController)
            }
            composable(Screens.Join.toString()) {
                JoinScreen(viewModel,  quizecViewModel, navController = navController)
            }
            composable(Screens.Library.toString()) {
                LibraryScreen(viewModel,navController = navController)
            }
            composable(Screens.Answer.toString()) {
                AnswerScreen(viewModel)
            }
            composable(Screens.Statistics.toString()) {
                StatisticsScreen(viewModel,navController = navController)
            }
            composable(Screens.ThankYou.toString()) {
                ThanksScreen(navController = navController)
            }
            composable(Screens.DetailsSurvey.toString()) {
                SurveyDetailsScreen(viewModel,navController = navController)
            }
            composable(Screens.Credits.toString()) {
                CreditsScreen(navController = navController)
            }
            composable(Screens.HostSurvey.toString()) {
                HostSurveyScreen(viewModel,quizecViewModel,navController = navController)
            }
            composable("${Screens.SurveyQuestions}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                SurveyQuestionsScreen(viewModel, navController = navController,id)
            }
            composable("${Screens.SurveyStats}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                Statistics(viewModel, navController = navController,id)
            }
            composable("${Screens.Survey}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                Survey(viewModel, navController = navController,id)
            }
            composable("${Screens.WaitHost}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                WaitHostScreen(viewModel, navController = navController,id)
            }
            composable("${Screens.CreateSurvey}/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                CreateQuestionScreen(viewModel, navController, id)
            }
        }
    }
}


