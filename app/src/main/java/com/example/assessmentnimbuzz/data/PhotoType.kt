package com.example.assessmentnimbuzz.data

/**
 * Created by Sujan Rai on 11/08/2023.
 * srai@dimitra.io
 *
 * Sealed class for specifying if a photo is Regular type of Traingular
 */
sealed class PhotoType {
    object REGULAR: PhotoType()

    object TRIANGULAR: PhotoType()
}
