package com.example.assessmentnimbuzz.vm

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assessmentnimbuzz.common.AppConstants
import com.example.assessmentnimbuzz.data.PhotoDto
import com.example.assessmentnimbuzz.data.PhotoState
import com.example.assessmentnimbuzz.data.PhotoType
import com.example.assessmentnimbuzz.sam.PhotoTypeProcessingInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Sujan Rai on 10/08/2023.
 * srai@dimitra.io
 *
 *
 */

class VMMainActivity : ViewModel() {

    private var photoTypes: List<PhotoType> = emptyList()

    private var _photoState = MutableStateFlow<PhotoState>(PhotoState.Empty)
    val photoState = _photoState.asStateFlow()

    private var hasPreviouslyFetchedPhotos = false

    init {
        preparePhotoTypes(AppConstants.PHOTO_FETCH_SYSTEM_COUNT)
    }

    /**
     * Prepares list to track Regular or Traingular image
     *
     * @param size Size of the images to be displayed
     */
    fun preparePhotoTypes(size: Int) {
        viewModelScope.launch {
            photoTypes = PhotoTypeProcessingInterface() {
                val types = mutableListOf<PhotoType>()
                var j = 1
                var k = 1
                for (i in 1..size) {
                    if (i == k) {
                        types.add(PhotoType.TRIANGULAR)
                        j += 1
                        k += j
                    } else
                        types.add(PhotoType.REGULAR)
                }

                types
            }.process(size).toList()
        }

    }

    /**
     * Updates photo in UI
     *
     * @param photoUris Uris of photos selected by user
     */
    fun preparePhotos(photoUris: List<Uri>) {
        Log.d(AppConstants.TAG, "preparePhotos() $hasPreviouslyFetchedPhotos")

        if (photoUris.isEmpty()) {
            if (hasPreviouslyFetchedPhotos)
                return

            _photoState.update {
                PhotoState.Empty
            }

            return
        }

        _photoState.update {
            PhotoState.Success(
                PhotoDto(
                    types = photoTypes,
                    uris = photoUris
                )
            )
        }

        hasPreviouslyFetchedPhotos = true
    }

    /**
     * Sets circular loader in UI
     */
    fun setLoading() {
        if (!hasPreviouslyFetchedPhotos)
            _photoState.update {
                PhotoState.Loading
            }
    }
}