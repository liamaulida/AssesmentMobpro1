package com.liamaulida0026.assessmentmobpro1.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liamaulida0026.assessmentmobpro1.model.Buku
import com.liamaulida0026.assessmentmobpro1.network.BukuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Buku>())
        private set

    var status = MutableStateFlow(BukuApi.ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = BukuApi.ApiStatus.LOADING
            try {
                data.value = BukuApi.service.getBuku(userId)
                status.value = BukuApi.ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = BukuApi.ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, judul: String, penulis: String, review: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BukuApi.service.postBuku(
                    userId,
                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    penulis.toRequestBody("text/plain".toMediaTypeOrNull()),
                    review.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteBuku(idBuku: String, userId: String) {
        viewModelScope.launch {
            try {
                BukuApi.service.deleteBuku(userId, idBuku)
                retrieveData(userId)
            } catch (e: Exception) {
                Log.d("delete", "$idBuku $userId")
                errorMessage.value = "Gagal menghapus data"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }

    fun clearMessage() { errorMessage.value = null }
}