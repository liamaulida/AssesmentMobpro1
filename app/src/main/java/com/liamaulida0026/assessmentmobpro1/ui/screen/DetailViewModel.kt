package com.liamaulida0026.assessmentmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liamaulida0026.assessmentmobpro1.model.Catatan
import com.liamaulida0026.assessmentmobpro1.database.CatatanDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val dao: CatatanDao) : ViewModel() {

    var recentlyDeletedList: Catatan? = null

    suspend fun getCatatan(id: Long): Catatan? {
        return dao.getCatatanById(id)
    }

    fun insert(judul: String, berat: Double, satuan: String, kategori: String) {
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

    fun update(id: Long, judul: String, berat: Double, satuan: String, kategori: String) {
        val catatan = Catatan(
            id = id,
            judul = judul,
            berat = berat,
            satuan = satuan,
            kategori = kategori
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(catatan)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyDeletedList = dao.getCatatanById(id)
            dao.moveToRecycleBin(id)
        }
    }

    fun restoreDeletedCatatan() {
        viewModelScope.launch(Dispatchers.IO) {
            recentlyDeletedList?.let {
                dao.update(it.copy(isDeleted = false))
                recentlyDeletedList = null
            }
        }
    }
}