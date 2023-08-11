package com.example.assessmentnimbuzz

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.assessmentnimbuzz.data.RouteState
import com.example.assessmentnimbuzz.ui.InputPhotoSizeScreen
import com.example.assessmentnimbuzz.ui.PhotoListingScreen
import com.example.assessmentnimbuzz.ui.theme.AssessmentNimbuzzTheme
import com.example.assessmentnimbuzz.vm.VMMainActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AssessmentNimbuzzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val view = LocalView.current
                    if (!view.isInEditMode) {
                        SideEffect {
                            val window = (view.context as Activity).window
                            window.statusBarColor = Color.TRANSPARENT
                            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                        }
                    }

                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val viewModel: VMMainActivity = androidx.lifecycle.viewmodel.compose.viewModel()

    NavHost(
        navController = navController,
        startDestination = RouteState.INPUT.route,
    ) {
        composable(route = RouteState.INPUT.route) {
            InputPhotoSizeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(
            route = RouteState.LISTING.route
        ) {
            PhotoListingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

