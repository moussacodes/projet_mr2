package com.mr2.dao

import androidx.room.*
import com.mr2.entity.Note
import com.mr2.entity.Password

@Dao
interface PasswordDao {

    @Insert
    fun insert(password: Password)

    @Update
    fun update(password: Password)

    @Delete
    fun delete(password: Password)

    @Query("SELECT * FROM passwords ORDER BY id DESC")
    fun getAll() : List<Password>

    @Query("SELECT * FROM passwords WHERE id = :id")
    fun getById(id: Int) : Password

    @Query("SELECT * FROM passwords WHERE title LIKE :search ORDER BY id DESC")
    fun getByTitle(search: String?): List<Password>

}