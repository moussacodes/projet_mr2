package com.mr2.dao

import androidx.room.*
import com.mr2.entity.Vocal

@Dao
interface VocalDao {
    @Insert
    fun insert(vocal: Vocal)

    @Delete
    fun delete(vocal: Vocal)

    @Query("SELECT * FROM vocals ORDER BY id DESC")
    fun getAll() : List<Vocal>

    @Query("SELECT * FROM vocals WHERE id = :id")
    fun getById(id: Int) : List<Vocal>

    @Query("SELECT * FROM vocals WHERE title LIKE :search ORDER BY id DESC")
    fun getByTitle(search: String?): List<Vocal>

}