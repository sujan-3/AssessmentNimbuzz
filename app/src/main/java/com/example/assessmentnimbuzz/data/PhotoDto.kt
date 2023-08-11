package com.example.assessmentnimbuzz.data

import android.net.Uri

data class PhotoDto(
    val photoTypes: List<PhotoType> = listOf(),
    val photoUris: List<Uri> = listOf()
)
