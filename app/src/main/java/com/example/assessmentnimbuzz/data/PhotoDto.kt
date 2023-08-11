package com.example.assessmentnimbuzz.data

import android.net.Uri

/**
 * Created by Sujan Rai on 11/08/2023.
 * srai@dimitra.io
 *
 * Data class for photo dto
 */
data class PhotoDto(
    val types: List<PhotoType> = listOf(),
    val uris: List<Uri> = listOf()
)
