package com.example.assessmentnimbuzz.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.assessmentnimbuzz.R
import com.example.assessmentnimbuzz.data.PermissionState

/**
 * Created by Sujan Rai on 11/08/2023.
 * srai@dimitra.io
 *
 *  Contains composable function to build "Get Photos" button
 */

typealias OnButtonClick = (PermissionState) -> Unit

@Composable
fun GetPhotosButtonBuilder(
    permissionGranted: Boolean,
    onButtonClick: OnButtonClick,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {

        Button(
            onClick = {
                if (permissionGranted) {
                    onButtonClick.invoke(PermissionState.PERMISSION_GRANTED)
                } else
                    onButtonClick.invoke(PermissionState.PERMISSION_REQUEST)
            }
        ) {
            Text(text = stringResource(id = R.string.get_photos))
        }
    }
}