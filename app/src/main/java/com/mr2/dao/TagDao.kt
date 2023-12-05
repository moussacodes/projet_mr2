package com.mr2.dao

import androidx.lifecycle.LiveData
import com.mr2.entity.Tag
import androidx.room.*

@Dao
interface TagDao {
    @Insert
    fun insert(tag: Tag)

    @Delete
    fun delete(tag: Tag)

    @Query("SELECT * FROM tags ORDER BY id DESC")
    fun getAll() : List<Tag>


    @Query("SELECT * FROM tags WHERE id = :id")
    fun getById(id: Int) : List<Tag>

    @Query("SELECT * FROM tags WHERE name LIKE :search ORDER BY id DESC")
    fun getByTitle(search: String?): List<Tag>

}