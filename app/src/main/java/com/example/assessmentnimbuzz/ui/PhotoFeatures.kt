package com.example.assessmentnimbuzz.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.assessmentnimbuzz.VMMainActivity
import com.example.assessmentnimbuzz.data.PhotoType
import com.example.assessmentnimbuzz.data.RouteState
import com.example.assessmentnimbuzz.R


/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 *
 */

typealias FetchPhotos = () -> Unit

@Composable
fun InputPhotoSizeScreen(
    navController: NavController,
    viewModel: VMMainActivity = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var value by remember {
        mutableStateOf("25")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 48.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 32.dp),
            text = stringResource(id = R.string.enter_the_number_contd)
        )

        BasicTextField(value = value, onValueChange = {
            value = it
        })

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 16.dp),
            onClick = {
                viewModel.updateSize(value.toInt())

                navController.navigate(RouteState.LISTING.route)
            }
        ) {
            Text(text = stringResource(id = R.string.got_it))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoListingScreen(
    navController: NavController,
    fetchPhotos: FetchPhotos,
    viewModel: VMMainActivity = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

    var permissionGranted by remember {
        mutableStateOf(isPermissionGranted(context))
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted_ ->
            Log.i("PhotoListingScreen()", "permission: $permissionGranted_")

            permissionGranted = permissionGranted_

            if (permissionGranted)
                fetchPhotos.invoke()
        }


    val photos = viewModel.photoDto

    Log.d("TAG", "size: ${photos.size}")

    Column() {
        LazyColumn(modifier = Modifier.weight(1f)) {
            //  Log.d("TAG", "${photos.photoTypes.isEmpty()}")

            items(photos.size) { index ->

                Text(text = "Hello")

               /* GlideImage(
                    model = photos[index]*//*when (photos.photoTypes[index]) {
                        is PhotoType.REGULAR -> photos.photoUris[0]
                        is PhotoType.TRIANGULAR -> photos.photoUris[1]
                    }*//*,
                    contentDescription = stringResource(id = R.string.photo)
                )*/
            }
        }

        Button(
            onClick = {
                if (permissionGranted)
                    fetchPhotos.invoke()
                else
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            }
        ) {
            Text(text = stringResource(id = R.string.get_photos))
        }

        /* if (permissionGranted) {
             fetchPhotos.invoke()


         } else {
             Column() {
                 Text(
                     modifier = Modifier.padding(vertical = 16.dp),
                     text = stringResource(id = R.string.storage_permission_is_contd)
                 )

                 Button(
                     onClick = {
                         Log.i("PhotoListingScreen()", "permission requested")

                         permissionLauncher.launch(
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                 Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                         )
                     }
                 ) {
                     Text(text = stringResource(id = R.string.request_storage_permission))
                 }
             }
         }*/
    }
}

private fun isPermissionGranted(
    context: Context,
): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}
