package com.example.assessmentnimbuzz.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.assessmentnimbuzz.R
import com.example.assessmentnimbuzz.common.AppConstants
import com.example.assessmentnimbuzz.data.*
import com.example.assessmentnimbuzz.ui.components.GetPhotosButtonBuilder
import com.example.assessmentnimbuzz.vm.VMMainActivity


/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 * File contains composable functions used in application
 */

/**
 * Builds input screen
 *
 * @param navController Navigation controller used for routing
 * @param viewModel Viewmodel object
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputPhotoSizeScreen(
    navController: NavController,
    viewModel: VMMainActivity,
) {
    var value by remember {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeActionDoneCallback = {
        keyboardController?.hide()
    }

    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_the_number_contd)
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        imeActionDoneCallback()

                        navigateToListing(value, viewModel, navController)
                    }
                ),
                value = value, onValueChange = {
                    value = it
                }
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                modifier = Modifier
                    .wrapContentWidth(),
                onClick = {
                    navigateToListing(value, viewModel, navController)
                }
            ) {
                Text(text = stringResource(id = R.string.got_it))
            }
        }
    }
}

/**
 * Builds listing screen
 *
 * @param navController Navigation controller used for routing
 * @param viewModel Viewmodel object
 */
@Composable
fun PhotoListingScreen(
    navController: NavController,
    viewModel: VMMainActivity = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

    var permissionGranted by remember {
        mutableStateOf(isPermissionGranted(context))
    }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            viewModel.preparePhotos(uris)
        }
    )

    // Runtime permission launcher
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted_ ->
            Log.i(AppConstants.TAG, "permission: $permissionGranted_")

            permissionGranted = permissionGranted_

            if (permissionGranted)
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
        }

    val photos = viewModel.photoState.collectAsState().value

    Column() {
        when (photos) {
            is PhotoState.Empty ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    EmptyContent()
                }
            is PhotoState.Success ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopCenter)
                ) {
                    PhotoContent(photos.photoDto)
                }
            is PhotoState.Loading ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                ) {
                    LoadingContent()
                }
        }

        GetPhotosButtonBuilder(
            permissionGranted = permissionGranted,
            onButtonClick = { state ->
                if (state == PermissionState.PERMISSION_REQUEST) {
                    permissionLauncher.launch(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else if (state == PermissionState.PERMISSION_GRANTED) {
                    viewModel.setLoading()

                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            })
    }
}


@Composable
private fun LoadingContent(
) {
    CircularProgressIndicator()
}

@Composable
private fun EmptyContent() {

    Text(
        text = stringResource(id = R.string.looks_like_its_contd),
        modifier = Modifier.padding(16.dp),
        textAlign = TextAlign.Center,
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoContent(
    photos: PhotoDto
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 3),
        contentPadding = PaddingValues(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
    ) {
        itemsIndexed(
            photos.types
        ) { index, item ->
            PhotoItem(index, item, photos.uris)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoItem(index: Int, item: PhotoType, uris: List<Uri>) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {

        GlideImage(
            modifier = Modifier.fillMaxSize(),
            model = when (item) {
                is PhotoType.REGULAR -> uris[0]
                is PhotoType.TRIANGULAR -> uris[if (uris.size > 1) 1 else 0]
            },
            contentDescription = stringResource(id = R.string.photo)
        )

        Text(
            modifier = Modifier
                .padding(16.dp)
                .drawBehind {
                    drawCircle(
                        color = Color.Yellow,
                        radius = this.size.maxDimension
                    )
                },
            text = "${index + 1}"
        )
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


private fun navigateToListing(
    value: String,
    viewModel: VMMainActivity,
    navController: NavController
) {
    viewModel.preparePhotoTypes(if (value.isEmpty() || value.contentEquals("0")) AppConstants.PHOTO_FETCH_SYSTEM_COUNT else value.toInt())

    navController.navigate(RouteState.LISTING.route)
}
