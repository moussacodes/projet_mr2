package com.mr2.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

 @Entity(tableName = "vocals")
 @Parcelize
data class Vocal(
    //PrimaryKey annotation to declare primary key with auto increment value
    //ColumnInfo annotation to specify the column's name
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "path") var path: String = "",
    @ColumnInfo(name = "date") var date: String = "",
    @ColumnInfo(name = "length") var length: String = "",
    @ColumnInfo(name = "time") var time: String = "",
    @ColumnInfo(name = "updatedDate") var updatedDate: String = "",
    @ColumnInfo(name = "updatedTime") var updatedTime: String = "",
) : Parcelable
