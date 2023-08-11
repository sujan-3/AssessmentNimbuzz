package com.example.assessmentnimbuzz

import android.app.Application
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract.Contacts.Photo
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessmentnimbuzz.data.PhotoDto
import com.example.assessmentnimbuzz.data.PhotoType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 *
 */

class VMMainActivity : ViewModel() {

    private lateinit var photoTypes: List<PhotoType>

    private var photoSize: Int = 25

    private val _photoDto = mutableStateListOf<Uri>()
    val photoDto: List<Uri>
        get() = _photoDto

    init {
        preparePhotoTypes()
    }

    private fun preparePhotoTypes() {
        photoTypes = PhotoTypeProcessingInterface() {
            val types = mutableListOf<PhotoType>()
            var j = 1
            var k = 1
            for (i in 1..photoSize) {
                if (i == k) {
                    types.add(PhotoType.TRIANGULAR)
                    j += 1
                    k += j
                } else
                    types.add(PhotoType.REGULAR)
            }

            types
        }.process(photoSize).toList()
    }

    fun updateSize(size: Int) {
        if (size == photoSize) {
            return
        }

        photoSize = size

        preparePhotoTypes()
    }

    fun preparePhotos(photoUris: List<Uri>) {
        if (photoUris.isEmpty()) {
            return
        }

        //  Log.d("TAG", "preparePhotos() photoUris:  ${photoDto.value.photoUris} photoTypes size: ${photoTypes.size}")

        /* _photoDto.value = _photoDto.value.copy(
             photoTypes = _photoDto.value.photoTypes.toMutableList().apply { this.addAll(photoTypes) },
             photoUris = _photoDto.value.photoUris.toMutableList().apply { this.addAll(photoUris) }
         )*/

        /*_photoDto.update {
            PhotoDto(
                photoTypes = photoTypes,
                photoUris = photoUris
            )
        }*/

        /* _photoDto.update {
             it.copy(
                 photoTypes = listOf(PhotoType.REGULAR, PhotoType.REGULAR),
                 photoUris = listOf(photoUris[0])
             )
         }*/
        /*_photoDto.value  =
            PhotoDto(
                photoTypes = listOf(PhotoType.REGULAR, PhotoType.REGULAR),
                photoUris = listOf(photoUris[0])
            )*/


        val items = photoTypes.map {
            if (it == PhotoType.REGULAR)
                photoUris[0]
            else
                photoUris[1]
        }.toMutableStateList()

        Log.d("TAG", "uri: ${photoUris[0]}")

        val item = List(1) { photoUris[0]}.toMutableStateList()

        Log.d("TAG", "items: ${items.size}")

        _photoDto.addAll(item)

        /* _photoDto.update {
             it.copy(
                 photoTypes = it.photoTypes + photoTypes,
                 photoUris = it.photoUris + photoUris
             )
         }*/

        /*Log.d(
            "TAG",
            "preparePhotos() photoUris:  ${photoDto.value.photoUris} photoTypes size: ${photoDto.value.photoTypes.size}"
        )*/

    }
}