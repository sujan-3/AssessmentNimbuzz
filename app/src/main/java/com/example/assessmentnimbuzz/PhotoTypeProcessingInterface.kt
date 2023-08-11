package com.example.assessmentnimbuzz

import com.example.assessmentnimbuzz.data.PhotoType

/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 *
 */
fun interface PhotoTypeProcessingInterface {
    fun process(n: Int): List<PhotoType>
}