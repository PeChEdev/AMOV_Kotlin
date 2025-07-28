package pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.theme.QuizecAppTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.location.LocationServices
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.login_signup.LoginScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.login_signup.SignupScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.screens.MainScreen
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.FirebaseViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.deis.amov.a2021130066.a2021146037.a2021135060.quizec.utils.locations.LocationUtil

class MainActivity : ComponentActivity() {
    companion object {
        const val LOGIN_SCREEN = "login"
        const val SIGNUP_SCREEN = "signup"
        const val MAIN_SCREEN = "main"
    }

    private val viewModel : FirebaseViewModel by viewModels()
    private val viewModelLoc : QuizecViewModel by viewModels {
        QuizecViewModel.provideFactory(
            LocationUtil(
                LocationServices.getFusedLocationProviderClient(this)
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            QuizecAppTheme {
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination = LOGIN_SCREEN
                    ) {
                        composable(LOGIN_SCREEN) {
                            LoginScreen(viewModel,
                                onSuccess = {
                                    navController.navigate(MAIN_SCREEN) {
                                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                                    }
                                },
                                onNavigateToSignup = {
                                    navController.navigate(SIGNUP_SCREEN)
                                }
                            )
                        }
                        composable(SIGNUP_SCREEN) {
                            SignupScreen(viewModel,
                                onSuccess = {
                                    navController.navigate(MAIN_SCREEN) {
                                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate (LOGIN_SCREEN)
                                }
                            )

                        }
                        composable(MAIN_SCREEN) {
                            MainScreen(
                                viewModel = viewModel,
                                quizecViewModel = viewModelLoc,
                                onSignOut =  {
                                    viewModel.signOut()
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(MAIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        if (checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            askSinglePermissionCamera.launch(android.Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                askSinglePermissionMedia.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askMultiplePermissions.launch(
                arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE )
            )
        }
        verifyLocationPermissions()
    }

    private val askSinglePermissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* TODO */ }
    private val askSinglePermissionMedia = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* TODO */ }
    private val askMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* TODO */ }


    override fun onResume() {
        super.onResume()
        if(viewModelLoc.hasLocationPermission){
            viewModelLoc.startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModelLoc.stopLocationUpdates()
    }

    fun verifyLocationPermissions() {
        viewModelLoc.hasLocationPermission =
            (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        if (!viewModelLoc.hasLocationPermission) {
            askLocationPermissions.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private val askLocationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        viewModelLoc.hasLocationPermission =
            map[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                    map[android.Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (viewModelLoc.hasLocationPermission){
            viewModelLoc.startLocationUpdates()
        }
    }
}