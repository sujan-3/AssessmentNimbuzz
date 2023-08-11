package com.example.assessmentnimbuzz.sam

import com.example.assessmentnimbuzz.data.PhotoType

/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 * Interface to create list containing Regular or Traingular image object
 */
fun interface PhotoTypeProcessingInterface {
    fun process(n: Int): List<PhotoType>
}