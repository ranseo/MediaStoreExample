package com.example.mediastoreexample.data

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import java.sql.Date

data class MediaStoreImage(
    val id:Long,
    val displayName:String,
    val dateAdded: Date,
    val contentUri: Uri
) {
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<MediaStoreImage>()
        {
            override fun areItemsTheSame(
                oldItem: MediaStoreImage,
                newItem: MediaStoreImage
            ): Boolean = oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: MediaStoreImage,
                newItem: MediaStoreImage
            ): Boolean = oldItem == newItem

        }
    }
}