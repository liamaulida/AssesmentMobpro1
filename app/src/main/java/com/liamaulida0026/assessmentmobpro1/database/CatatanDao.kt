package com.liamaulida0026.assessmentmobpro1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.liamaulida0026.assessmentmobpro1.model.Catatan
import kotlinx.coroutines.flow.Flow

@Dao
interface CatatanDao {

    @Insert
    suspend fun insert(catatan: Catatan)

    @Update
    suspend fun update(catatan: Catatan)

    @Query("SELECT * FROM catatan WHERE isDeleted = 0 ORDER BY judul ASC")
    fun getCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE isDeleted = 1 ORDER BY judul ASC")
    fun getDeletedCatatan(): Flow<List<Catatan>>

    @Query("SELECT * FROM catatan WHERE id = :id")
    suspend fun getCatatanById(id: Long): Catatan?

    @Query("UPDATE catatan SET isDeleted = 1 WHERE id = :id")
    suspend fun moveToRecycleBin(id: Long)

    @Query("UPDATE catatan SET isDeleted = 0 WHERE id = :id")
    suspend fun restoreById(id: Long)

    @Query("DELETE FROM catatan WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM catatan")
    fun getAllCatatan(): LiveData<List<Catatan>>

    @Query("DELETE FROM catatan WHERE isDeleted = 1")
    suspend fun emptyRecycleBin()
}