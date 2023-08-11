package com.example.assessmentnimbuzz

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.assessmentnimbuzz.data.RouteState
import com.example.assessmentnimbuzz.ui.InputPhotoSizeScreen
import com.example.assessmentnimbuzz.ui.PhotoListingScreen
import com.example.assessmentnimbuzz.ui.theme.AssessmentNimbuzzTheme

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[VMMainActivity::class.java]
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoUris = mutableListOf<Uri>()

                // if multiple images are selected
                if (result.data?.clipData != null) {

                    // Get two first photos only
                    for (i in 0 until 2) {
                        photoUris.add(result.data!!.clipData?.getItemAt(i)!!.uri)
                    }

                } else if (result.data?.data != null) {
                    // if single image is selected
                    photoUris.add(result.data!!.data!!)
                }

                viewModel.preparePhotos(photoUris.toList())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssessmentNimbuzzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    private fun openGalleryForImages() {
        Log.i("TAG", "openGalleryForImages()")

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }


    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = RouteState.INPUT.route
        ) {
            composable(route = RouteState.INPUT.route) {
                InputPhotoSizeScreen(navController)
            }

            composable(
                route = RouteState.LISTING.route
            ) {
                PhotoListingScreen(
                    navController = navController,
                    fetchPhotos = {
                        openGalleryForImages()
                    }
                )
            }
        }
    }
}
