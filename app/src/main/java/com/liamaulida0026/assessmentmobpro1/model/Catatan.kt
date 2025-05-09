package com.liamaulida0026.assessmentmobpro1.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catatan")
data class Catatan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judul: String,
    val berat: Double,
    val satuan: String,
    val kategori: String,
    val isDeleted: Boolean = false
)