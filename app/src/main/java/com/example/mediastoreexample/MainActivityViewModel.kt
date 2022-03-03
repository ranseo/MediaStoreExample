package com.example.mediastoreexample

import android.app.Application
import android.content.ContentUris
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mediastoreexample.data.MediaStoreImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.concurrent.TimeUnit

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _images = MutableLiveData<List<MediaStoreImage>>()
    val images: LiveData<List<MediaStoreImage>>
        get() = _images

    fun loadImage() {
        viewModelScope.launch {
            val imageList = queryImages()
            Log.d("ViewModel", "queryImages 호출 완료.")
            _images.postValue(imageList)
        }

    }


    private suspend fun queryImages(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()

        withContext(Dispatchers.IO) {
            Log.d("ViewModel", "withContext 도입.")
            val projection =
                arrayOf(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media._ID,
                )


            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
            } else {
                null
            }

            val selectionArgs = if (selection != null) {
                arrayOf("%Solaroid%")
            } else {
                null
            }

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val query = getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            query?.use { cursor ->
                Log.d("ViewModel", "query 도입")
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val dateModifiedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    Log.d("ViewModel", "while() 도입")
                    val id = cursor.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(cursor.getLong(dateModifiedColumn)))
                    val displayName = cursor.getString(nameColumn)

                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    val image = MediaStoreImage(id, displayName, dateModified, contentUri)
                    images += image
                    Log.d("ViewModel", "images : ${image}")
                }
                Log.d("ViewModel", "빠져나옴")

            }


        }
        Log.d("ViewModel", "queryImage() withContext 완료")
        Log.d("ViewModel", "images size : ${images.size}")
        return images
    }


}