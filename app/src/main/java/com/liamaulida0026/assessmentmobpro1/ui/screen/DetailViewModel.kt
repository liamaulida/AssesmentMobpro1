package com.liamaulida0026.assessmentmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liamaulida0026.assessmentmobpro1.model.Catatan
import com.liamaulida0026.assessmentmobpro1.database.CatatanDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: CatatanDao) : ViewModel() {
    fun insert(judul: String, berat: Double, satuan: String, kategori: String){
        val catatan = Catatan(
            judul = judul,
            berat = berat,
            satuan = satuan,
            kategori = kategori
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(catatan)
        }
    }
    fun getCatatan(id: Long): Catatan? {
        return null
    }
}
