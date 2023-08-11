package com.example.assessmentnimbuzz.data

/**
 * Created by Sujan Rai on 11/08/2023.
 * srai@dimitra.io
 *
 * Sealed class for photo data state
 *
 */
sealed class PhotoState {
    object Loading : PhotoState()

    data class Success(var photoDto: PhotoDto) : PhotoState()

    object Empty : PhotoState()
}


