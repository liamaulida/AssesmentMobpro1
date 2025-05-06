package com.liamaulida0026.assessmentmobpro1.ui.screen

import androidx.lifecycle.ViewModel
import com.liamaulida0026.assessmentmobpro1.model.Catatan

class MainViewModel : ViewModel() {
    val data = listOf(
        Catatan(
            id = 1,
            judul = "Pisang",
            berat = 1.2,
            satuan = "Kilogram",
            kategori = "Buah"
        ),
        Catatan(
            id = 2,
            judul = "Wortel",
            berat = 0.5,
            satuan = "Kilogram",
            kategori = "Sayur"
        ),
        Catatan(
            id = 3,
            judul = "Dada Ayam",
            berat = 1.0,
            satuan = "Kilogram",
            kategori = "Daging"
        ),
        Catatan(
            id = 4,
            judul = "Beras",
            berat = 2.0,
            satuan = "Kilogram",
            kategori = "Lainnya"
        ),
        Catatan(
            id = 5,
            judul = "Apel",
            berat = 1.0,
            satuan = "Kilogram",
            kategori = "Buah"
        ),
        Catatan(
            id = 6,
            judul = "Bayam",
            berat = 0.3,
            satuan = "Kilogram",
            kategori = "Sayur"
        ),
        Catatan(
            id = 7,
            judul = "Mie Kering",
            berat = 2.0,
            satuan = "Kilogram",
            kategori = "Lainnya"
        )
    )

    fun getCatatan(id: Long): Catatan? {
        return data.find { it.id == id }
    }
}