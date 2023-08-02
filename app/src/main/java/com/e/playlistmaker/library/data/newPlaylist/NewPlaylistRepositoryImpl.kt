package com.e.playlistmaker.library.data.newPlaylist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.e.playlistmaker.App
import com.e.playlistmaker.R
import com.e.playlistmaker.library.domain.newPlaylist.NewPlaylistRepository
import java.io.File
import java.io.FileOutputStream

class NewPlaylistRepositoryImpl : NewPlaylistRepository {

    companion object {
        private const val DIRECTORY_NAME = "Pictures"
    }

    private val filePath = File(
        App.appContext.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE),
        R.string.app_name.toString()
    )

    init {
        if (!filePath.exists()) filePath.mkdirs()
    }

    override suspend fun savePicture(uri: Uri, fileName: String) {
        val file = File(filePath, "$fileName.jpg")
        val inputStream = App.appContext.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun loadPicture(fileName: String): Uri? {
        val file = File(filePath, "$fileName.jpg")
        if (file.exists()) return file.toUri()
        return null
    }
}